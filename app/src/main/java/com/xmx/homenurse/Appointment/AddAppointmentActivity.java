package com.xmx.homenurse.Appointment;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.R;
import com.xmx.homenurse.User.Callback.AutoLoginCallback;
import com.xmx.homenurse.User.UserManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAppointmentActivity extends BaseTempActivity {
    TextView timeView;
    TextView typeView;
    CheckBox otherCheck;
    EditText otherView;

    Date appointmentTime;
    ArrayList<String> types;
    int appointmentType;

    ArrayList<String> symptoms;
    ArrayList<CheckBox> symptomBoxes;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_appointment);

        timeView = getViewById(R.id.tv_time);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date();
        appointmentTime = date;
        String time = df.format(date);
        timeView.setText(time);
        timeView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        appointmentType = 0;
        types = new ArrayList<>();
        types.add(Constants.APPOINTMENT_TYPE[0]);
        types.add(Constants.APPOINTMENT_TYPE[1]);
        typeView = getViewById(R.id.tv_type);
        typeView.setText(types.get(appointmentType));
        typeView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        otherView = getViewById(R.id.et_symptom);
        otherView.setEnabled(false);

        symptoms = new ArrayList<>();
        symptoms.add("头晕");
        symptoms.add("头痛");
        symptoms.add("恶心");
        symptoms.add("心悸");
        symptoms.add("困倦");
        symptoms.add("视力模糊");
        symptoms.add("耳鸣");
        symptoms.add("胸闷气短");
        symptoms.add("心绞痛");
        symptomBoxes = new ArrayList<>();
        LinearLayout symptom = getViewById(R.id.layout_symptom);
        for (String s : symptoms) {
            CheckBox c = new CheckBox(this);
            c.setText(s);
            symptomBoxes.add(c);
            symptom.addView(c);
        }
    }

    @Override
    protected void setListener() {
        final TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR) + 49);
        pvTime.setTime(new Date());
        pvTime.setCancelable(true);
        pvTime.setCyclic(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time = df.format(date);
                timeView.setText(time);
                appointmentTime = date;
            }
        });
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        final OptionsPickerView pvOptions = new OptionsPickerView(this);
        pvOptions.setPicker(types);
        pvOptions.setCancelable(true);
        pvOptions.setTitle(getString(R.string.appointment_type));
        pvOptions.setCyclic(false);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                String type = types.get(options1);
                typeView.setText(type);
                appointmentType = options1;
            }
        });
        typeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });

        otherCheck = getViewById(R.id.cb_other);
        otherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    otherView.setEnabled(true);
                } else {
                    otherView.setEnabled(false);
                }
            }
        });

        Button ok = getViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();
                boolean flag = false;
                String symptom = "";
                for (CheckBox c : symptomBoxes) {
                    if (c.isChecked()) {
                        symptom += c.getText().toString() + " ";
                        flag = true;
                    }
                }
                if (otherCheck.isChecked()) {
                    String s = otherView.getText().toString();
                    if (!s.equals("")) {
                        symptom += s + " ";
                        flag = true;
                    } else {
                        showToast(R.string.appointment_error);
                        return;
                    }
                }
                if (!flag) {
                    showToast(R.string.appointment_error);
                    return;
                }
                AppointmentSQLManager.getInstance()
                        .insertAppointment(appointmentTime, appointmentType, symptom, now);
                pushToCloud(appointmentTime, appointmentType, symptom, now);
                showToast(R.string.add_success);
                finish();
            }
        });
    }

    private void pushToCloud(Date date, int type, String symptom, Date add) {
        final AVObject post = new AVObject("Appointment");
        post.put("time", date.getTime());
        post.put("type", type);
        post.put("symptom", symptom);
        post.put("status", Constants.STATUS_WAITING);
        post.put("addTime", add.getTime());

        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                post.put("patient", user.getObjectId());
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            showToast(R.string.save_success);
                            finish();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
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

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
