package com.xmx.homenurse.Measure.Schedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.MainActivity;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.Timer;

public class MeasureTimerService extends Service {
    long version = 0;

    long latestId;
    String latestTitle;
    long latestTime;
    long latestPlanTime;
    int latestRepeat;
    int latestPeriod;

    boolean latestFlag = false;

    @Override
    public void onCreate() {
        super.onCreate();

        getLatestPlan();

        Timer timer = new Timer() {
            @Override
            public void timer() {
                checkTime();
            }
        };
        timer.start(Constants.UPDATE_FREQUENCY);

        setForeground();
    }

    boolean getLatestPlan() {
        MeasureSchedule entity = MeasureScheduleSQLManager.getInstance().getLatestSchedule();

        if (entity != null) {
            latestId = entity.mId;
            latestTime = entity.mTime;
            latestTitle = entity.mTitle;
            latestPlanTime = entity.mPlanTime;
            latestRepeat = entity.mRepeat;
            latestPeriod = entity.mPeriod;

            latestFlag = true;
        } else {
            latestFlag = false;
        }

        return latestFlag;
    }

    boolean checkTime() {
        MeasureScheduleSQLManager sqlManager = MeasureScheduleSQLManager.getInstance();
        if (sqlManager.getVersion() != version) {
            version = sqlManager.getVersion();
            if (!getLatestPlan()) {
                return false;
            }
        }

        if (latestFlag) {
            long now = System.currentTimeMillis();
            if (now > latestTime - Constants.EARLIER_TIME) {
                if (latestRepeat < 0) {
                    showNotification(latestId, latestTitle, now - latestPlanTime);
                } else {
                    showRemindNotification(latestId, latestTitle, now - latestPlanTime);
                }
                sqlManager.completeSchedule(latestId);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    void showRemindNotification(long id, String title, long delay) {
        int notificationId = (title + "|" + id).hashCode();

        Intent intent = new Intent(this, NotificationTempActivity.class);
        intent.putExtra("start", false);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String content = "该 " + title + " 啦";
        if ((delay / 1000 / 60) > 0) {
            content += "， 已经过了" + (delay / 1000 / 60) + "分钟啦！";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("时间到啦")
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                        .setContentText(content);

        if (latestPeriod > 0) {
            int startId = (title + "|" + id + "s").hashCode();
            Intent startIntent = new Intent(this, NotificationTempActivity.class);
            startIntent.putExtra("start", true);
            startIntent.putExtra("id", id);
            startIntent.putExtra("title", title);
            PendingIntent startPending = PendingIntent.getActivity(this, startId,
                    startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            int delayId = (title + "|" + id + "d").hashCode();
            Intent delayIntent = new Intent(this, NotificationTempActivity.class);
            delayIntent.putExtra("start", false);
            delayIntent.putExtra("id", id);
            delayIntent.putExtra("title", title);
            PendingIntent delayPending = PendingIntent.getActivity(this, delayId,
                    delayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.addAction(R.drawable.ic_menu_send, "重算周期", startPending)
                    .addAction(R.drawable.ic_menu_slideshow, "知道啦", delayPending);
        }

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        manager.notify(notificationId, notification);
    }

    void showNotification(long id, String title, long delay) {
        int notificationId = (title + "|" + id).hashCode();

        Intent intent = new Intent(this, NotificationTempActivity.class);
        intent.putExtra("id", id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String content = "该 " + title + " 啦";
        if ((delay / 1000 / 60) > 0) {
            content += "， 已经拖了" + (delay / 1000 / 60) + "分钟啦！";
        }

        int startId = (title + "|" + id + "s").hashCode();
        Intent startIntent = new Intent(this, NotificationTempActivity.class);
        startIntent.putExtra("start", true);
        startIntent.putExtra("id", id);
        startIntent.putExtra("title", title);
        PendingIntent startPending = PendingIntent.getActivity(this, startId,
                startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int delayId = (title + "|" + id + "d").hashCode();
        Intent delayIntent = new Intent(this, NotificationTempActivity.class);
        delayIntent.putExtra("start", false);
        delayIntent.putExtra("id", id);
        delayIntent.putExtra("title", title);
        PendingIntent delayPending = PendingIntent.getActivity(this, delayId,
                delayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("时间到啦")
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                        .setContentText(content)
                        .addAction(R.drawable.ic_menu_send, "开始啦", startPending)
                        .addAction(R.drawable.ic_menu_slideshow, "再等会", delayPending);
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        manager.notify(notificationId, notification);
    }

    void setForeground() {
        int notificationId = -1;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentIntent(contentIntent)
                        .setWhen(0)
                        .setContentText(getString(R.string.scheduling));
        Notification notification = mBuilder.build();
        startForeground(notificationId, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
