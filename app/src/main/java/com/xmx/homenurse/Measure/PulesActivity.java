package com.xmx.homenurse.Measure;

import android.os.Bundle;
import android.widget.TextView;

import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Measure.ChartView.PulesChartView;
import com.xmx.homenurse.R;

public class PulesActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pules);

        setTitle(getString(R.string.pules));

        TextView dataView = getViewById(R.id.tv_pules);
        dataView.setText("85");
        TextView dateView = getViewById(R.id.tv_date);
        dateView.setText("2016.3.22 6:00am");

        PulesChartView pulesChartView = getViewById(R.id.spline_chart);
        pulesChartView.setLabels(3, 22);
        pulesChartView.setDataSet(80, 90, 105, 85, 70, 75, 85);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
