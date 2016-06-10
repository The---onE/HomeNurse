package com.xmx.homenurse.Record;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.Data.Callback.InsertCallback;
import com.xmx.homenurse.User.Callback.AutoLoginCallback;
import com.xmx.homenurse.User.UserManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddRecordActivity extends BaseTempActivity {

    TextView typeTextView;
    TextView timeTextView;
    Date recordDate;
    int recordType;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_record);
        timeTextView = getViewById(R.id.time_tv);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        recordDate = date;
        String time = df.format(date);
        timeTextView.setText(time);
        timeTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        typeTextView = getViewById(R.id.tv_type);
        typeTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        recordType = 0;
    }

    @Override
    protected void setListener() {
        final TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR) + 49);
        pvTime.setTime(new Date());
        pvTime.setCancelable(true);
        pvTime.setCyclic(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Date now = new Date();
                date.setHours(now.getHours());
                date.setMinutes(now.getMinutes());
                date.setSeconds(now.getSeconds());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String time = df.format(date);
                timeTextView.setText(time);
                recordDate = date;
            }
        });
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        final ArrayList<String> types = new ArrayList<>();
        types.add(getString(R.string.record_good));
        types.add(getString(R.string.record_high));
        types.add(getString(R.string.record_highest));
        final OptionsPickerView pvOptions = new OptionsPickerView(this);
        pvOptions.setPicker(types);
        pvOptions.setCancelable(true);
        pvOptions.setTitle(getString(R.string.record_type));
        pvOptions.setCyclic(false);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                String type = types.get(options1);
                typeTextView.setText(type);
                recordType = options1;
            }
        });
        typeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });

        Button ok = getViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleView = getViewById(R.id.title);
                String title = titleView.getText().toString();
                if (title.equals("")) {
                    showToast("请输入诊断结果");
                    return;
                }

                EditText textView = getViewById(R.id.text);
                String text = textView.getText().toString();
                EditText suggestionView = getViewById(R.id.suggestion);
                String suggestion = suggestionView.getText().toString();

                Record record = new Record(title, recordDate, text, suggestion,
                        Constants.STATUS_WAITING, recordType);

                RecordSyncManager.getInstance().insertData(record, new InsertCallback() {
                    @Override
                    public void success(String objectId) {
                        showToast(R.string.save_success);
                        finish();
                    }

                    @Override
                    public void notInit() {
                        showToast(R.string.sync_failure);
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
                        showToast(R.string.not_loggedin);
                    }

                    @Override
                    public void errorChecksum() {
                        showToast(R.string.not_loggedin);
                    }
                });
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
