package com.xmx.homenurse.Measure;

import android.os.Bundle;
import android.widget.TextView;

import com.xmx.homenurse.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.R;

public class BloodPressureActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_blood_pressure);

        setTitle(getString(R.string.blood_pressure));

        TextView dataView = getViewById(R.id.tv_blood_pressure);
        dataView.setText("120/80");
        TextView dateView = getViewById(R.id.tv_date);
        dateView.setText("2016.3.21 6:00am");

        BloodPressureChartView bloodPressureChartView = getViewById(R.id.spline_chart);
        bloodPressureChartView.setLabels(3, 21);
        bloodPressureChartView.setDataSet(120, 130, 135, 145, 160, 120, 120,
                80, 70, 65, 75, 90, 75, 80);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
