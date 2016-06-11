package com.xmx.homenurse.Measure;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.xmx.homenurse.Measure.Data.BloodPressure.AddBloodPressureActivity;
import com.xmx.homenurse.Measure.Data.BloodPressure.BloodPressure;
import com.xmx.homenurse.Measure.Data.BloodPressure.BloodPressureManager;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Measure.ChartView.BloodPressureChartView;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.Data.Callback.SelectCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BloodPressureActivity extends BaseTempActivity {
    TextView dataView;
    boolean loadFlag = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_blood_pressure);

        setTitle(getString(R.string.blood_pressure));
        dataView = getViewById(R.id.tv_blood_pressure);
        dataView.setText("同步中…");
    }

    @Override
    protected void setListener() {
        Button add = getViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddBloodPressureActivity.class);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        BloodPressureManager.getInstance().syncFromCloud(null, new SelectCallback<BloodPressure>() {
            @Override
            public void success(List<BloodPressure> bloodPressureList) {
                loadFlag = true;
                BloodPressure bloodPressure = BloodPressureManager.getInstance()
                        .getSQLManager().selectLatest("Time", false);

                if (bloodPressure != null) {
                    TextView dataView = getViewById(R.id.tv_blood_pressure);
                    dataView.setText(String.format("%.0f/%.0f",
                            bloodPressure.mPressureHigh, bloodPressure.mPressureLow));
                    TextView dateView = getViewById(R.id.tv_date);
                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
                    dateView.setText(df.format(bloodPressure.mTime));
                } else {
                    dataView.setText("还未测量");
                }

                BloodPressureChartView bloodPressureChartView = getViewById(R.id.spline_chart);
                Date now = new Date();
                bloodPressureChartView.setLabels(now.getMonth() + 1, now.getDate());
                double[] a = new double[7];
                double[] b = new double[7];
                for (int i = 0; i < 7; ++i) {
                    int year = now.getYear() + 1900;
                    int month = now.getMonth() + 1;
                    int day = now.getDate();
                    BloodPressure pressure = BloodPressureManager.getInstance().selectByDate(year, month, day);
                    if (pressure != null) {
                        a[i] = pressure.mPressureHigh;
                        b[i] = pressure.mPressureLow;
                    } else {
                        a[i] = -1;
                        b[i] = -1;
                    }
                    now.setDate(now.getDate() - 1);
                }
                bloodPressureChartView
                        .setDataSet(a[0], a[1], a[2], a[3], a[4], a[5], a[6],
                                b[0], b[1], b[2], b[3], b[4], b[5], b[6]);
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
            BloodPressure bloodPressure = BloodPressureManager.getInstance()
                    .getSQLManager().selectLatest("Time", false);

            if (bloodPressure != null) {
                TextView dataView = getViewById(R.id.tv_blood_pressure);
                dataView.setText(String.format("%.0f/%.0f",
                        bloodPressure.mPressureHigh, bloodPressure.mPressureLow));
                TextView dateView = getViewById(R.id.tv_date);
                DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
                dateView.setText(df.format(bloodPressure.mTime));
            } else {
                dataView.setText("还未测量");
            }

            BloodPressureChartView bloodPressureChartView = getViewById(R.id.spline_chart);
            Date now = new Date();
            bloodPressureChartView.setLabels(now.getMonth() + 1, now.getDate());
            double[] a = new double[7];
            double[] b = new double[7];
            for (int i = 0; i < 7; ++i) {
                int year = now.getYear() + 1900;
                int month = now.getMonth() + 1;
                int day = now.getDate();
                BloodPressure pressure = BloodPressureManager.getInstance().selectByDate(year, month, day);
                if (pressure != null) {
                    a[i] = pressure.mPressureHigh;
                    b[i] = pressure.mPressureLow;
                } else {
                    a[i] = -1;
                    b[i] = -1;
                }
                now.setDate(now.getDate() - 1);
            }
            bloodPressureChartView
                    .setDataSet(a[0], a[1], a[2], a[3], a[4], a[5], a[6],
                            b[0], b[1], b[2], b[3], b[4], b[5], b[6]);
        }
    }
}
