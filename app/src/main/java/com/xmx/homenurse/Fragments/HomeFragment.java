package com.xmx.homenurse.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmx.homenurse.Measure.Schedule.AddMeasureScheduleActivity;
import com.xmx.homenurse.Measure.Schedule.ScheduleActivity;
import com.xmx.homenurse.Prescription.Prescription;
import com.xmx.homenurse.Prescription.PrescriptionActivity;
import com.xmx.homenurse.Tools.BaseFragment;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Measure.Schedule.MeasureScheduleManager;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(View view) {
        TextView dateView = (TextView) view.findViewById(R.id.tv_date);
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        dateView.setText(df.format(now));
    }

    @Override
    protected void setListener(View view) {
        ImageView schedule = (ImageView) view.findViewById(R.id.btn_schedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ScheduleActivity.class);
            }
        });

        ImageView prescription = (ImageView) view.findViewById(R.id.btn_prescription);
        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PrescriptionActivity.class);
            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {
        Intent service = new Intent(getContext(), MeasureTimerService.class);
        getContext().startService(service);
    }

}
