package com.xmx.homenurse.Measure;

import android.os.Bundle;

import com.xmx.homenurse.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.R;

public class TemperatureActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temperature);

        setTitle(getString(R.string.temperature));

        SplineChartView splineChartView = getViewById(R.id.spline_chart);
        splineChartView.setLabels(3, 20);
        splineChartView.setDataSet(37.1, 38.8, 39.2, 40.7, 37.5, 38.5, 39.5);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
