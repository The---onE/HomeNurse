package com.xmx.homenurse.Measure;

import android.os.Bundle;

import com.xmx.homenurse.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.R;

public class BloodPressureActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_blood_pressure);

        setTitle(getString(R.string.blood_pressure));

        BloodPressureChartView bloodPressureChartView = getViewById(R.id.spline_chart);
        bloodPressureChartView.setLabels(3, 20);
        bloodPressureChartView.setDataSet(120, 130, 135, 145, 160, 120, 110,
                80, 70, 65, 75, 90, 75, 60);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
