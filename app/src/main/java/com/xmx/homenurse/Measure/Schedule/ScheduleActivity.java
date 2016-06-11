package com.xmx.homenurse.Measure.Schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Tools.Timer;

public class ScheduleActivity extends BaseTempActivity {
    ScheduleAdapter adapter;
    long version = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_schedule);
    }

    @Override
    protected void setListener() {
        TextView addSchedule = getViewById(R.id.tv_add_measure_schedule);
        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddMeasureScheduleActivity.class);
            }
        });

        adapter = new ScheduleAdapter(getBaseContext());
        ListView planList = getViewById(R.id.list_measure_schedule);
        planList.setAdapter(adapter);

        planList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScheduleActivity.this, MeasureScheduleDetailActivity.class);
                MeasureSchedule measureSchedule = (MeasureSchedule) adapter.getItem(position);
                intent.putExtra("id", measureSchedule.mId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        updateScheduleList();
        Timer timer = new Timer() {
            @Override
            public void timer() {
                updateScheduleList();
            }
        };
        timer.start(Constants.UPDATE_FREQUENCY);
    }

    void updateScheduleList() {
        long ver = MeasureScheduleManager.getInstance().updateMeasureSchedules();
        if (ver != version) {
            adapter.changeList();
            version = ver;
        }
    }
}
