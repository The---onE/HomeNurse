package com.xmx.homenurse.Measure;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.xmx.homenurse.Data.MeasureSQLManager;
import com.xmx.homenurse.R;

public class NotificationTempActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_temp);

        boolean flag = getIntent().getBooleanExtra("start", false);
        int id = getIntent().getIntExtra("id", -1);
        String title = getIntent().getStringExtra("title");
        if (flag) {
            MeasureSQLManager.getInstance().completeSchedule(id);
        }

        int notificationId = (title + "|" + id).hashCode();

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        finish();
    }
}
