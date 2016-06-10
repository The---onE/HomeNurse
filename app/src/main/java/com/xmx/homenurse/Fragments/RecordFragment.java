package com.xmx.homenurse.Fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.xmx.homenurse.Record.Record;
import com.xmx.homenurse.Record.RecordAmountADayActivity;
import com.xmx.homenurse.Record.RecordSyncManager;
import com.xmx.homenurse.Tools.BaseFragment;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Record.Datepicker.DateManager;
import com.xmx.homenurse.Record.Datepicker.bizs.calendars.DPCManager;
import com.xmx.homenurse.Record.Datepicker.bizs.decors.DPDecor;
import com.xmx.homenurse.Record.Datepicker.cons.DPMode;
import com.xmx.homenurse.Record.Datepicker.views.DatePicker;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Record.AddRecordActivity;
import com.xmx.homenurse.Tools.Data.Callback.SelectCallback;
import com.xmx.homenurse.User.Callback.AutoLoginCallback;
import com.xmx.homenurse.User.UserManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends BaseFragment {
    DatePicker picker;
    Map<Long, Integer> dates = new HashMap<>();
    long version = 0;

    TextView titleView;
    TextView textView;
    TextView suggestionView;
    TextView timeView;
    CardView card;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecordSyncManager.getInstance().syncFromCloud(null, new SelectCallback<Record>() {
            @Override
            public void success(List<Record> records) {
                showToast(R.string.sync_success);
                updateData();
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
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    protected void initView(View view) {
        picker = (DatePicker) view.findViewById(R.id.date_picker);

        titleView = (TextView) view.findViewById(R.id.tv_title);
        textView = (TextView) view.findViewById(R.id.tv_text);
        suggestionView = (TextView) view.findViewById(R.id.tv_suggestion);
        timeView = (TextView) view.findViewById(R.id.tv_time);
        card = (CardView) view.findViewById(R.id.record_card);
    }

    @Override
    protected void setListener(View view) {
        final Calendar calendar = Calendar.getInstance();
        picker.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                Intent intent = new Intent(getContext(), RecordAmountADayActivity.class);

                int[] d = DateManager.getDate(date);
                int year = d[0];
                int month = d[1];
                int day = d[2];
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);

                startActivity(intent);
            }
        });

        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint, String data) {
                int[] day = DateManager.getDate(data);

                Date d = new Date(0);
                d.setYear(day[0] - 1900);
                d.setMonth(day[1] - 1);
                d.setDate(day[2]);
                if (dates.containsKey(d.getTime())) {
                    int type = dates.get(d.getTime());
                    switch (type) {
                        case Constants.GOOD_TYPE:
                            paint.setColor(Color.GREEN);
                            break;
                        case Constants.HIGH_TYPE:
                            paint.setColor(Color.BLUE);
                            break;
                        case Constants.HIGHEST_TYPE:
                            paint.setColor(Color.RED);
                            break;
                    }
                    canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
                }
            }
        });

        TextView add = (TextView) view.findViewById(R.id.tv_add_record);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {
        updateData();
        refreshCard();
    }

    private void updateData() {
        long ver = RecordSyncManager.getInstance().getSQLManager().getVersion();
        if (ver != version) {
            List<String> flag = new ArrayList<>();

            List<Record> records = RecordSyncManager.getInstance().getSQLManager().selectAll();
            dates.clear();
            for (Record record: records) {
                Date temp = record.mTime;

                Date date = new Date(0);
                date.setYear(temp.getYear());
                date.setMonth(temp.getMonth());
                date.setDate(temp.getDate());
                long t = date.getTime();
                int type = record.mType;
                if (!dates.containsKey(t)) {
                    dates.put(t, type);
                } else {
                    int old = dates.get(t);
                    if (old < type) {
                        dates.put(t, type);
                    }
                }
            }

            for (Map.Entry<Long, Integer> entry : dates.entrySet()) {
                long date = entry.getKey();
                Date d = new Date(date);
                String s = DateManager.makeString(d);
                flag.add(s);
            }

            DPCManager.getInstance().setDecorBG(flag);

            refreshCard();

            version = ver;
        }
    }

    private void refreshCard() {
        Record latest = RecordSyncManager.getInstance().getSQLManager().selectLatest("TIME", false);
        if (latest != null) {
            String title = latest.mTitle;
            String text = latest.mText;
            String suggestion = latest.mSuggestion;
            Date time = latest.mTime;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            titleView.setText(title);
            textView.setText(text);
            suggestionView.setText(suggestion);
            timeView.setText(df.format(time));
            int type = latest.mType;
            int bg = Color.GRAY;
            switch (type) {
                case Constants.GOOD_TYPE:
                    bg = Color.GREEN;
                    break;
                case Constants.HIGH_TYPE:
                    bg = Color.BLUE;
                    break;
                case Constants.HIGHEST_TYPE:
                    bg = Color.RED;
                    break;
            }
            card.setCardBackgroundColor(bg);
        } else {
            titleView.setText(R.string.no_record);
            card.setCardBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateData();
    }
}
