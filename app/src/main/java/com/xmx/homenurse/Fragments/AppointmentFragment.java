package com.xmx.homenurse.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.okhttp.Call;
import com.xmx.homenurse.Appointment.AddAppointmentActivity;
import com.xmx.homenurse.Appointment.Appointment;
import com.xmx.homenurse.Tools.BaseFragment;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Appointment.AppointmentManager;
import com.xmx.homenurse.Appointment.AppointmentSQLManager;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Appointment.AppointmentAdapter;
import com.xmx.homenurse.Tools.Callback;
import com.xmx.homenurse.User.Callback.AutoLoginCallback;
import com.xmx.homenurse.User.UserManager;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends BaseFragment {
    long version;
    AppointmentAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        syncFromCloud();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        TextView add = (TextView) view.findViewById(R.id.tv_add_appointment);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddAppointmentActivity.class);
            }
        });

        ListView appointmentList = (ListView) view.findViewById(R.id.list_appointment);
        adapter = new AppointmentAdapter(getContext());
        appointmentList.setAdapter(adapter);
        syncFromCloud();

        appointmentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long id) {
                Appointment appointment = (Appointment) adapter.getItem(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                switch (appointment.getStatus()) {
                    case Constants.STATUS_WAITING:
                        builder.setMessage("要取消预约吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor c = AppointmentSQLManager.getInstance().selectAppointmentById(id);
                                if (c.moveToFirst()) {
                                    syncToCloud(AppointmentSQLManager.getCloudId(c),
                                            Constants.STATUS_CANCELED,
                                            new Callback() {
                                                @Override
                                                public void func() {
                                                    AppointmentSQLManager.getInstance().cancelAppointment(id);
                                                    showToast(R.string.sync_success);
                                                    updateAppointmentList();
                                                }
                                            });
                                }
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        break;
                    case Constants.STATUS_CANCELED:
                        builder.setMessage("要恢复该记录吗？");
                        builder.setTitle("提示");
                        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor c = AppointmentSQLManager.getInstance().selectAppointmentById(id);
                                if (c.moveToFirst()) {
                                    syncToCloud(AppointmentSQLManager.getCloudId(c),
                                            Constants.STATUS_DELETED,
                                            new Callback() {
                                                @Override
                                                public void func() {
                                                    AppointmentSQLManager.getInstance()
                                                            .deleteAppointment(id);
                                                    updateAppointmentList();
                                                }
                                            });
                                }
                            }
                        });
                        builder.setPositiveButton("恢复", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor c = AppointmentSQLManager.getInstance().selectAppointmentById(id);
                                if (c.moveToFirst()) {
                                    syncToCloud(AppointmentSQLManager.getCloudId(c),
                                            Constants.STATUS_WAITING,
                                            new Callback() {
                                                @Override
                                                public void func() {
                                                    AppointmentSQLManager.getInstance()
                                                            .resumeAppointment(id);
                                                    updateAppointmentList();
                                                }
                                            });
                                }
                            }
                        });
                        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        break;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAppointmentList();
    }

    void updateAppointmentList() {
        long ver = AppointmentManager.getInstance().updateAppointments();
        if (ver != version) {
            adapter.changeList();
            version = ver;
        }
    }

    void syncFromCloud() {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>("Appointment");
                query.whereEqualTo("patient", user.getObjectId());
                //query.whereEqualTo("status", Constants.STATUS_WAITING);
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            for (AVObject object : avObjects) {
                                int id = Math.abs(object.getObjectId().hashCode());
                                Cursor c = AppointmentSQLManager.getInstance().selectAppointmentById(id);
                                if (!c.moveToFirst()) {
                                    String cloud = object.getObjectId();
                                    Date date = object.getDate("time");
                                    String symptom = object.getString("symptom");
                                    Date add = object.getDate("addTime");
                                    int type = object.getInt("type");
                                    AppointmentSQLManager.getInstance().insertAppointment(id,
                                            cloud, date, type, symptom, add);
                                }
                                //object.put("status", Constants.STATUS_COMPLETE);
                                //object.saveInBackground();
                            }
                            showToast(R.string.sync_success);
                            updateAppointmentList();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
    }

    void syncToCloud(final String id, final int status, final Callback callback) {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>("Appointment");
                query.getInBackground(id, new GetCallback<AVObject>() {
                    public void done(AVObject post, AVException e) {
                        if (e == null) {
                            post.put("status", status);
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        callback.func();
                                        showToast(R.string.sync_success);
                                    } else {
                                        showToast(R.string.sync_failure);
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            showToast(R.string.sync_failure);
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
    }
}
