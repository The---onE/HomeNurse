package com.xmx.homenurse.Measure;

import android.os.Bundle;
import android.widget.TextView;

import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Measure.ChartView.HeartRateChartView;
import com.xmx.homenurse.R;

public class HeartRateActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_heart_rate);

        setTitle(getString(R.string.heart_rate));

        TextView dataView = getViewById(R.id.tv_heart_rate);
        dataView.setText("75");
        TextView dateView = getViewById(R.id.tv_date);
        dateView.setText("2016.3.19 6:00am");

        HeartRateChartView heartRateChartView = getViewById(R.id.spline_chart);
        heartRateChartView.setLabels(3, 19);
        heartRateChartView.setDataSet(80, 90, 105, 85, 70, 75, 75);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
