package com.xmx.homenurse.Fragment;

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

import com.xmx.homenurse.Datepicker.DateManager;
import com.xmx.homenurse.Datepicker.bizs.calendars.DPCManager;
import com.xmx.homenurse.Datepicker.bizs.decors.DPDecor;
import com.xmx.homenurse.Datepicker.cons.DPMode;
import com.xmx.homenurse.Datepicker.views.DatePicker;
import com.xmx.homenurse.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {
    DatePicker picker;

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
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        List<String> flag = new ArrayList<>();

        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        String s = "" + year + "-" + month + "-" + (day+2);
        flag.add(s);
        String b = "" + year + "-" + month + "-" + (day+1);
        flag.add(b);

        DPCManager.getInstance().setDecorBG(flag);
        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint, String data) {
                int[] day = DateManager.getDate(data);
                if (day[2] == 23) {
                    paint.setColor(Color.RED);
                } else {
                    paint.setColor(Color.GREEN);
                }
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
            }
        });
    }
}
