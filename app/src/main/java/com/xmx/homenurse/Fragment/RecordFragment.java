package com.xmx.homenurse.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Data.RecordSQLManager;
import com.xmx.homenurse.Datepicker.DateManager;
import com.xmx.homenurse.Datepicker.bizs.calendars.DPCManager;
import com.xmx.homenurse.Datepicker.bizs.decors.DPDecor;
import com.xmx.homenurse.Datepicker.cons.DPMode;
import com.xmx.homenurse.Datepicker.views.DatePicker;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Record.AddRecordActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends BaseFragment {
    DatePicker picker;
    Map<Long, Integer> dates = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        picker = (DatePicker) view.findViewById(R.id.date_picker);

        final Calendar calendar = Calendar.getInstance();
        picker.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                /*Intent intent = new Intent(getContext(), DateInformationActivity.class);

                String regex = "(.+?)-(\\d+)-(\\d+)"; //格式为yyyy-M-d
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(date);
                int year = 1900;
                int month = 1;
                int day = 1;
                if (matcher.find()) {
                    year = Integer.valueOf(matcher.group(1));
                    month = Integer.valueOf(matcher.group(2));
                    day = Integer.valueOf(matcher.group(3));
                }
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);

                startActivity(intent);*/
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

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        List<String> flag = new ArrayList<>();

        Cursor c = RecordSQLManager.getInstance().selectAllRecord();
        dates.clear();
        if (c.moveToFirst()) {
            do {
                long time = RecordSQLManager.getTime(c);
                Date temp = new Date(time);

                Date date = new Date(0);
                date.setYear(temp.getYear());
                date.setMonth(temp.getMonth());
                date.setDate(temp.getDate());
                long t = date.getTime();
                int type = RecordSQLManager.getType(c);
                if (!dates.containsKey(t)) {
                    dates.put(t, type);
                } else {
                    int old = dates.get(t);
                    if (old < type) {
                        dates.put(t, type);
                    }
                }
            } while (c.moveToNext());
        }

        for (Map.Entry<Long, Integer> entry : dates.entrySet()) {
            long date = entry.getKey();
            Date d = new Date(date);
            String s = DateManager.makeString(d);
            flag.add(s);
        }

        DPCManager.getInstance().setDecorBG(flag);
        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint, String data) {
                int[] day = DateManager.getDate(data);

                Date d = new Date(0);
                d.setYear(day[0] - 1900);
                d.setMonth(day[1] - 1);
                d.setDate(day[2]);
                int type = dates.get(d.getTime());
                switch (type)
                {
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
        });
    }
}
