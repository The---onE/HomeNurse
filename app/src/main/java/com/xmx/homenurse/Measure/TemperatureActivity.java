package com.xmx.homenurse.Measure;

import android.os.Bundle;

import com.xmx.homenurse.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.R;

public class TemperatureActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temperature);

        setTitle(getString(R.string.temperature));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
