package com.xmx.homenurse.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xmx.homenurse.Appointment.AddAppointmentActivity;
import com.xmx.homenurse.Appointment.Appointment;
import com.xmx.homenurse.Tools.BaseFragment;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Appointment.AppointmentManager;
import com.xmx.homenurse.Appointment.AppointmentSQLManager;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Appointment.AppointmentAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends BaseFragment {
    long version;
    AppointmentAdapter adapter;

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
        updateAppointmentList();

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
                                AppointmentSQLManager.getInstance().cancelAppointment(id);
                                updateAppointmentList();
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
                        builder.setMessage("要删除该记录吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppointmentSQLManager.getInstance().deleteAppointment(id);
                                updateAppointmentList();
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

}