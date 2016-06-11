package com.xmx.homenurse.Measure;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.xmx.homenurse.Measure.Data.BloodPressure.BloodPressure;
import com.xmx.homenurse.Measure.Data.BloodPressure.BloodPressureManager;
import com.xmx.homenurse.Measure.Data.HeartRate.HeartRate;
import com.xmx.homenurse.Measure.Data.HeartRate.HeartRateManager;
import com.xmx.homenurse.Measure.Data.Pules.Pules;
import com.xmx.homenurse.Measure.Data.Pules.PulesManager;
import com.xmx.homenurse.Measure.Data.Temperature.Temperature;
import com.xmx.homenurse.Measure.Data.Temperature.TemperatureManager;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Tools.Data.Callback.SelectCallback;

import java.util.List;

public class MeasureActivity extends BaseTempActivity {
    boolean loadFlag = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_measure);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.card_blood_pressure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BloodPressureActivity.class);
            }
        });

        getViewById(R.id.card_heart_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(HeartRateActivity.class);
            }
        });

        getViewById(R.id.card_pules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PulesActivity.class);
            }
        });

        getViewById(R.id.card_temperature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TemperatureActivity.class);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        BloodPressureManager.getInstance().syncFromCloud(null, new SelectCallback<BloodPressure>() {
            @Override
            public void success(List<BloodPressure> bloodPressureList) {
                BloodPressure bloodPressure = BloodPressureManager.getInstance().selectToday();
                if (bloodPressure != null) {
                    TextView dataView = getViewById(R.id.data_blood_pressure);
                    dataView.setText(String.format("%.0f/%.0f",
                            bloodPressure.mPressureHigh, bloodPressure.mPressureLow));
                }
            }

            @Override
            public void notInit() {
                showToast(R.string.failure);
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
        HeartRateManager.getInstance().syncFromCloud(null, new SelectCallback<HeartRate>() {
            @Override
            public void success(List<HeartRate> bloodPressureList) {
                HeartRate heartRate = HeartRateManager.getInstance().selectToday();
                if (heartRate != null) {
                    TextView dataView = getViewById(R.id.data_heart_rate);
                    dataView.setText("" + heartRate.mRate);
                }
            }

            @Override
            public void notInit() {
                showToast(R.string.failure);
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
        PulesManager.getInstance().syncFromCloud(null, new SelectCallback<Pules>() {
            @Override
            public void success(List<Pules> bloodPressureList) {
                Pules pules = PulesManager.getInstance().selectToday();
                if (pules != null) {
                    TextView dataView = getViewById(R.id.data_pules);
                    dataView.setText("" + pules.mPules);
                }
            }

            @Override
            public void notInit() {
                showToast(R.string.failure);
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
        TemperatureManager.getInstance().syncFromCloud(null, new SelectCallback<Temperature>() {
            @Override
            public void success(List<Temperature> bloodPressureList) {
                loadFlag = true;
                Temperature temperature = TemperatureManager.getInstance().selectToday();
                if (temperature != null) {
                    TextView dataView = getViewById(R.id.data_temperature);
                    dataView.setText("" + temperature.mTemperature);
                }
            }

            @Override
            public void notInit() {
                showToast(R.string.failure);
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (loadFlag) {
            BloodPressure bloodPressure = BloodPressureManager.getInstance().selectToday();
            if (bloodPressure != null) {
                TextView dataView = getViewById(R.id.data_blood_pressure);
                dataView.setText(String.format("%.0f/%.0f",
                        bloodPressure.mPressureHigh, bloodPressure.mPressureLow));
            }
            HeartRate heartRate = HeartRateManager.getInstance().selectToday();
            if (heartRate != null) {
                TextView dataView = getViewById(R.id.data_heart_rate);
                dataView.setText("" + heartRate.mRate);
            }
            Pules pules = PulesManager.getInstance().selectToday();
            if (pules != null) {
                TextView dataView = getViewById(R.id.data_pules);
                dataView.setText("" + pules.mPules);
            }
            Temperature temperature = TemperatureManager.getInstance().selectToday();
            if (temperature != null) {
                TextView dataView = getViewById(R.id.data_temperature);
                dataView.setText("" + temperature.mTemperature);
            }
        }
    }
}
