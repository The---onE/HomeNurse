package com.xmx.homenurse.Measure;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.xmx.homenurse.Measure.Data.HeartRate.AddHeartRateActivity;
import com.xmx.homenurse.Measure.Data.HeartRate.HeartRate;
import com.xmx.homenurse.Measure.Data.HeartRate.HeartRateManager;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Measure.ChartView.HeartRateChartView;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.Data.Callback.SelectCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HeartRateActivity extends BaseTempActivity {
    TextView dataView;
    boolean loadFlag = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_heart_rate);

        setTitle(getString(R.string.heart_rate));
        dataView = getViewById(R.id.tv_heart_rate);
        dataView.setText("同步中…");
    }

    @Override
    protected void setListener() {
        Button add = getViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddHeartRateActivity.class);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        HeartRateManager.getInstance().syncFromCloud(null, new SelectCallback<HeartRate>() {
            @Override
            public void success(List<HeartRate> heartRates) {
                loadFlag = true;
                HeartRate heartRate = HeartRateManager.getInstance()
                        .getSQLManager().selectLatest("Time", false);

                if (heartRate != null) {
                    TextView dataView = getViewById(R.id.tv_heart_rate);
                    dataView.setText("" + heartRate.mRate);
                    TextView dateView = getViewById(R.id.tv_date);
                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
                    dateView.setText(df.format(heartRate.mTime));
                } else {
                    dataView.setText("还未测量");
                }

                HeartRateChartView heartRateChartView = getViewById(R.id.spline_chart);
                Date now = new Date();
                heartRateChartView.setLabels(now.getMonth() + 1, now.getDate());
                double[] a = new double[7];
                for (int i = 0; i < 7; ++i) {
                    int year = now.getYear() + 1900;
                    int month = now.getMonth() + 1;
                    int day = now.getDate();
                    HeartRate rate = HeartRateManager.getInstance().selectByDate(year, month, day);
                    if (rate != null) {
                        a[i] = rate.mRate;
                    } else {
                        a[i] = -1;
                    }
                    now.setDate(now.getDate() - 1);
                }
                heartRateChartView.setDataSet(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);
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
            HeartRate heartRate = HeartRateManager.getInstance()
                    .getSQLManager().selectLatest("Time", false);

            if (heartRate != null) {
                TextView dataView = getViewById(R.id.tv_heart_rate);
                dataView.setText("" + heartRate.mRate);
                TextView dateView = getViewById(R.id.tv_date);
                DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
                dateView.setText(df.format(heartRate.mTime));
            } else {
                dataView.setText("还未测量");
            }

            HeartRateChartView heartRateChartView = getViewById(R.id.spline_chart);
            Date now = new Date();
            heartRateChartView.setLabels(now.getMonth() + 1, now.getDate());
            double[] a = new double[7];
            for (int i = 0; i < 7; ++i) {
                int year = now.getYear() + 1900;
                int month = now.getMonth() + 1;
                int day = now.getDate();
                HeartRate rate = HeartRateManager.getInstance().selectByDate(year, month, day);
                if (rate != null) {
                    a[i] = rate.mRate;
                } else {
                    a[i] = -1;
                }
                now.setDate(now.getDate() - 1);
            }
            heartRateChartView.setDataSet(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);
        }
    }
}
