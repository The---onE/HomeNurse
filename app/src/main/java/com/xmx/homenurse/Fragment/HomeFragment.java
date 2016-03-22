package com.xmx.homenurse.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmx.homenurse.Data.MeasureSQLManager;
import com.xmx.homenurse.Data.MeasureScheduleManager;
import com.xmx.homenurse.Measure.AddMeasureScheduleActivity;
import com.xmx.homenurse.Measure.BloodPressureActivity;
import com.xmx.homenurse.Measure.HeartRateActivity;
import com.xmx.homenurse.Measure.MeasureSchedule;
import com.xmx.homenurse.Measure.PulesActivity;
import com.xmx.homenurse.Measure.ScheduleAdapter;
import com.xmx.homenurse.Measure.TemperatureActivity;
import com.xmx.homenurse.R;

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
                /*Intent intent = new Intent(getBaseContext(), InformationActivity.class);
                Plan plan = (Plan) adapter.getItem(position);
                intent.putExtra("id", plan.getId());
                startActivity(intent);*/
                MeasureSchedule measureSchedule = (MeasureSchedule) adapter.getItem(position);
                MeasureSQLManager.getInstance().cancelSchedule(measureSchedule.getId());
                updateScheduleList();
            }
        });

        updateScheduleList();

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
