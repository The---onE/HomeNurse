package com.xmx.homenurse.Measure;

import android.os.Bundle;

import com.xmx.homenurse.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.R;

public class BloodPressureActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_blood_pressure);

        setTitle(getString(R.string.blood_pressure));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
