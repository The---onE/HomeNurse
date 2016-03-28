package com.xmx.homenurse.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmx.homenurse.Tools.BaseFragment;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Measure.Schedule.MeasureScheduleManager;
import com.xmx.homenurse.Measure.Schedule.MeasureSchedule.AddMeasureScheduleActivity;
import com.xmx.homenurse.Measure.BloodPressureActivity;
import com.xmx.homenurse.Measure.HeartRateActivity;
import com.xmx.homenurse.Measure.Schedule.MeasureSchedule;
import com.xmx.homenurse.Measure.Schedule.MeasureScheduleDetailActivity;
import com.xmx.homenurse.Measure.Schedule.MeasureTimerService;
import com.xmx.homenurse.Measure.PulesActivity;
import com.xmx.homenurse.Measure.Schedule.ScheduleAdapter;
import com.xmx.homenurse.Measure.TemperatureActivity;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.Timer;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {
    ScheduleAdapter adapter;
    long version = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RelativeLayout temperature = (RelativeLayout) view.findViewById(R.id.layout_temperature);
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TemperatureActivity.class);
            }
        });

        RelativeLayout pules = (RelativeLayout) view.findViewById(R.id.layout_pules);
        pules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PulesActivity.class);
            }
        });

        RelativeLayout heartRate = (RelativeLayout) view.findViewById(R.id.layout_heart_rate);
        heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HeartRateActivity.class);
            }
        });

        RelativeLayout bloodPressure = (RelativeLayout) view.findViewById(R.id.layout_blood_pressure);
        bloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BloodPressureActivity.class);
            }
        });

        TextView addSchedule = (TextView) view.findViewById(R.id.tv_add_measure_schedule);
        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddMeasureScheduleActivity.class);
            }
        });

        adapter = new ScheduleAdapter(getContext());
        ListView planList = (ListView) view.findViewById(R.id.list_measure_schedule);
        planList.setAdapter(adapter);

        planList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MeasureScheduleDetailActivity.class);
                MeasureSchedule measureSchedule = (MeasureSchedule) adapter.getItem(position);
                intent.putExtra("id", measureSchedule.getId());
                startActivity(intent);
            }
        });

        updateScheduleList();
        Timer timer = new Timer() {
            @Override
            public void timer() {
                updateScheduleList();
            }
        };
        timer.start(Constants.UPDATE_FREQUENCY);

        Intent service = new Intent(getContext(), MeasureTimerService.class);
        getContext().startService(service);

        return view;
    }

    void updateScheduleList() {
        long ver = MeasureScheduleManager.getInstance().updateMeasureSchedules();
        if (ver != version) {
            adapter.changeList();
            version = ver;
        }
    }

}
