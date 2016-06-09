package com.xmx.homenurse.Fragments;

import android.content.DialogInterface;
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
import com.xmx.homenurse.Appointment.AddAppointmentActivity;
import com.xmx.homenurse.Appointment.Appointment;
import com.xmx.homenurse.Appointment.AppointmentSyncManager;
import com.xmx.homenurse.Tools.BaseFragment;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Appointment.AppointmentManager;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Appointment.AppointmentAdapter;
import com.xmx.homenurse.Tools.Data.Callback.SelectCallback;
import com.xmx.homenurse.Tools.Data.Callback.UpdateCallback;

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

        //AppointmentSyncManager.getInstance().getSQLManager().clearDatabase();
        AppointmentSyncManager.getInstance().syncFromCloud(null, new SelectCallback<Appointment>() {
            @Override
            public void success(List<Appointment> appointments) {
                for (Appointment appointment : appointments) {
                    int status = AppointmentSyncManager.getInstance().getSQLManager()
                            .selectByCloudId(appointment.mCloudId).mStatus;
                    if (status != appointment.mStatus) {
                        AppointmentSyncManager.getInstance().getSQLManager()
                                .updateDate(appointment.mCloudId,
                                "STATUS = " + appointment.mStatus);
                    }
                }
                showToast(R.string.sync_success);
                updateAppointmentList();
            }

            @Override
            public void notInit() {
                showToast(R.string.failure);
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
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
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setListener(View view) {
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
        appointmentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long id) {
                final Appointment appointment = (Appointment) adapter.getItem(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                switch (appointment.mStatus) {
                    case Constants.STATUS_WAITING:
                        builder.setMessage("要取消预约吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppointmentSyncManager.getInstance()
                                        .cancelAppointment(appointment.mCloudId, new UpdateCallback() {
                                            @Override
                                            public void success() {
                                                showToast(R.string.sync_success);
                                                updateAppointmentList();
                                            }

                                            @Override
                                            public void notInit() {
                                                showToast(R.string.failure);
                                            }

                                            @Override
                                            public void syncError(AVException e) {
                                                showToast(R.string.sync_failure);
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
                                                showToast(R.string.username_error);
                                            }

                                            @Override
                                            public void errorChecksum() {
                                                showToast(R.string.not_loggedin);
                                            }
                                        });
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
                                AppointmentSyncManager.getInstance()
                                        .deleteAppointment(appointment.mCloudId, new UpdateCallback() {
                                            @Override
                                            public void success() {
                                                showToast(R.string.sync_success);
                                                updateAppointmentList();
                                            }

                                            @Override
                                            public void notInit() {
                                                showToast(R.string.failure);
                                            }

                                            @Override
                                            public void syncError(AVException e) {
                                                showToast(R.string.sync_failure);
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
                                                showToast(R.string.username_error);
                                            }

                                            @Override
                                            public void errorChecksum() {
                                                showToast(R.string.not_loggedin);
                                            }
                                        });
                            }
                        });
                        builder.setPositiveButton("恢复", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppointmentSyncManager.getInstance()
                                        .resumeAppointment(appointment.mCloudId, new UpdateCallback() {
                                            @Override
                                            public void success() {
                                                showToast(R.string.sync_success);
                                                updateAppointmentList();
                                            }

                                            @Override
                                            public void notInit() {
                                                showToast(R.string.failure);
                                            }

                                            @Override
                                            public void syncError(AVException e) {
                                                showToast(R.string.sync_failure);
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
                                                showToast(R.string.username_error);
                                            }

                                            @Override
                                            public void errorChecksum() {
                                                showToast(R.string.not_loggedin);
                                            }
                                        });
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
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {

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

    /*void syncFromCloud() {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>("Appointment");
                query.whereEqualTo("patient", AVObject.createWithoutData("PatientsData", user.getObjectId()));
                //query.whereEqualTo("status", Constants.STATUS_WAITING);
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            for (AVObject object : avObjects) {
                                long id = Math.abs(object.getObjectId().hashCode());
                                Appointment c = AppointmentSQLManager.getInstance()
                                        .selectByCloudId(object.getObjectId());
                                if (c == null) {
                                    String cloud = object.getObjectId();
                                    Date date = new Date(object.getLong("time"));
                                    String symptom = object.getString("symptom");
                                    Date add = new Date(object.getLong("addTime"));
                                    int type = object.getInt("type");
                                    Appointment appointment = new Appointment(id,
                                            cloud, date, type, symptom, add);
                                    AppointmentSQLManager.getInstance().insertData(appointment);
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
    }*/


}
