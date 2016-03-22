package com.xmx.homenurse.Measure;

import android.os.Bundle;
import android.widget.TextView;

import com.xmx.homenurse.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.R;

public class TemperatureActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temperature);

        setTitle(getString(R.string.temperature));

        TextView dataView = getViewById(R.id.tv_temperature);
        dataView.setText("36.5");
        TextView dateView = getViewById(R.id.tv_date);
        dateView.setText("2016.3.20 6:00am");

        TemperatureChartView temperatureChartView = getViewById(R.id.spline_chart);
        temperatureChartView.setLabels(3, 20);
        temperatureChartView.setDataSet(37.1, 38.8, 39.2, 40.7, 37.5, 38.5, 36.5);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
