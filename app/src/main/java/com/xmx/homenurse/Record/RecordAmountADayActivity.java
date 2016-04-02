package com.xmx.homenurse.Record;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;

import java.util.ArrayList;
import java.util.List;

public class RecordAmountADayActivity extends BaseTempActivity {
    ListView recordList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_record_amount_a_day);

        int year = getIntent().getIntExtra("year", 1900);
        int month = getIntent().getIntExtra("month", 0);
        int day = getIntent().getIntExtra("day", 0);
        recordList = getViewById(R.id.record_list);

        List<Record> records = new ArrayList<>();
        Cursor c = RecordSQLManager.getInstance().selectRecordByDay(year, month, day);
        if (c.moveToFirst()) {
            do {
                long id = RecordSQLManager.getId(c);
                String title = RecordSQLManager.getTitle(c);
                long time = RecordSQLManager.getTime(c);
                String text = RecordSQLManager.getText(c);
                String suggestion = RecordSQLManager.getSuggestion(c);
                int status = RecordSQLManager.getStatus(c);
                int type = RecordSQLManager.getType(c);
                Record r = new Record(id, title, time, text, suggestion, status, type);
                records.add(r);
            } while (c.moveToNext());
        }
        RecordAdapter adapter = new RecordAdapter(this, records);
        recordList.setAdapter(adapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
