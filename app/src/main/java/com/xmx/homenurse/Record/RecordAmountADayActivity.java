package com.xmx.homenurse.Record;

import android.os.Bundle;
import android.widget.ListView;

import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;

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

        List<Record> records = RecordSyncManager.getInstance().selectByDay(year, month, day);
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
