package com.xmx.homenurse.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xmx.homenurse.Appointment.AddAppointmentActivity;
import com.xmx.homenurse.Data.AppointmentManager;
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
        adapter.changeList();

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
