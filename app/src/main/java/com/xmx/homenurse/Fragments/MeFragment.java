package com.xmx.homenurse.Fragments;

import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.xmx.homenurse.Tools.BaseFragment;
import com.xmx.homenurse.User.Callback.AutoLoginCallback;
import com.xmx.homenurse.User.UserManager;
import com.xmx.homenurse.User.UserSQLManager;
import com.xmx.homenurse.R;
import com.xmx.homenurse.User.LoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {
    EditText nameView;
    TextView genderView;
    TextView birthdayView;
    EditText heightView;
    EditText weightView;
    EditText idNumberView;
    EditText phoneView;
    EditText emailView;
    EditText addressView;

    Button edit;

    boolean editFlag = false;

    Date birthday;
    String gender;
    ArrayList<String> genders;

    OptionsPickerView pvOptions;
    TimePickerView pvTime;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    protected void initView(View view) {
        nameView = (EditText) view.findViewById(R.id.name);
        genderView = (TextView) view.findViewById(R.id.gender);
        birthdayView = (TextView) view.findViewById(R.id.birthday);
        heightView = (EditText) view.findViewById(R.id.height);
        weightView = (EditText) view.findViewById(R.id.weight);
        idNumberView = (EditText) view.findViewById(R.id.id_number);
        phoneView = (EditText) view.findViewById(R.id.phone);
        emailView = (EditText) view.findViewById(R.id.email);
        addressView = (EditText) view.findViewById(R.id.address);

        nameView.setEnabled(false);
        genderView.setEnabled(false);
        birthdayView.setEnabled(false);
        heightView.setEnabled(false);
        weightView.setEnabled(false);
        idNumberView.setEnabled(false);
        phoneView.setEnabled(false);
        emailView.setEnabled(false);
        addressView.setEnabled(false);

        birthdayView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        genderView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        birthday = new Date(0);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(0);
        birthdayView.setText(df.format(date));

        gender = getString(R.string.male);
        genders = new ArrayList<>();
        genders.add(getString(R.string.male));
        genders.add(getString(R.string.female));
        genderView.setText(genders.get(0));
    }

    @Override
    protected void setListener(View view) {
        pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR) + 49);
        pvTime.setTime(new Date());
        pvTime.setCancelable(true);
        pvTime.setCyclic(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String time = df.format(date);
                birthdayView.setText(time);
                birthday = date;
            }
        });
        birthdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        pvOptions = new OptionsPickerView(getContext());
        pvOptions.setPicker(genders);
        pvOptions.setCancelable(true);
        pvOptions.setTitle(getString(R.string.gender));
        pvOptions.setCyclic(false);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                String type = genders.get(options1);
                genderView.setText(type);
                gender = genders.get(options1);
            }
        });
        genderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });

        edit = (Button) view.findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editFlag) {
                    nameView.setEnabled(true);
                    genderView.setEnabled(true);
                    birthdayView.setEnabled(true);
                    heightView.setEnabled(true);
                    weightView.setEnabled(true);
                    idNumberView.setEnabled(true);
                    phoneView.setEnabled(true);
                    emailView.setEnabled(true);
                    addressView.setEnabled(true);
                    edit.setText(R.string.save);
                    editFlag = true;
                } else {
                    save();
                }
            }
        });

        Button logout = (Button) view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                long id = UserManager.getId(user);
                if (id > 0) {
                    Cursor c = UserSQLManager.getInstance().getUserById(id);
                    if (c.moveToFirst()) {
                        nameView.setText(UserSQLManager.getName(c));
                        genderView.setText(UserSQLManager.getGender(c));
                        for (int i = 0; i < genders.size(); ++i) {
                            String s = genders.get(i);
                            if (s.equals(UserSQLManager.getGender(c))) {
                                pvOptions.setSelectOptions(i);
                                break;
                            }
                        }

                        long time = UserSQLManager.getBirthday(c);
                        Date d = new Date(time);
                        pvTime.setTime(d);
                        birthday = d;
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        birthdayView.setText(df.format(d));

                        heightView.setText("" + UserSQLManager.getHeight(c));
                        weightView.setText("" + UserSQLManager.getWeight(c));
                        idNumberView.setText(UserSQLManager.getIdNumber(c));
                        phoneView.setText(UserSQLManager.getPhont(c));
                        emailView.setText(UserSQLManager.getEmail(c));
                        addressView.setText(UserSQLManager.getAddress(c));
                    } else {
                        UserSQLManager.getInstance().insertUser(id, "", genders.get(0),
                                new Date(0), 0, 0, "", "", "", "");
                    }
                }
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
                UserManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.not_loggedin);
                UserManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
                UserManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }
        });
    }

    float getEditViewFloat(EditText et) {
        if (!et.getText().toString().equals("")) {
            return Float.parseFloat(et.getText().toString());
        } else {
            return 0;
        }
    }

    boolean save() {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                String name = "'" + nameView.getText().toString() + "'";
                String gen = "'" + gender + "'";
                float height = getEditViewFloat(heightView);
                float weight = getEditViewFloat(weightView);

                String idN = idNumberView.getText().toString();
                String regex = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(idN);
                if (!matcher.find()) {
                    showToast(R.string.error_id);
                    return;
                }
                String idNumber = "'" + idN + "'";
                String phone = "'" + phoneView.getText().toString() + "'";
                String email = "'" + emailView.getText().toString() + "'";
                String address = "'" + addressView.getText().toString() + "'";

                long id = UserManager.getId(user);
                if (id > 0) {
                    UserSQLManager.getInstance().updateUser(id, name, gen, birthday, height,
                            weight, idNumber, phone, email, address);
                } else {
                    UserSQLManager.getInstance().insertUser(id, name, gen, birthday,
                            height, weight, idNumber, phone, email, address);
                }
                syncToCloud(name, gen, birthday, height, weight, idNumber, phone, email, address);
                showToast(R.string.save_success);
                nameView.setEnabled(false);
                genderView.setEnabled(false);
                birthdayView.setEnabled(false);
                heightView.setEnabled(false);
                weightView.setEnabled(false);
                idNumberView.setEnabled(false);
                phoneView.setEnabled(false);
                emailView.setEnabled(false);
                addressView.setEnabled(false);
                edit.setText(R.string.edit);
                editFlag = false;
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
                UserManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.not_loggedin);
                UserManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
                UserManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }
        });

        return true;
    }

    public void syncToCloud(final String name, final String gender, final Date birthday,
                            final float height, final float weight,
                            final String idNumber, final String phone, final String email,
                            final String address) {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>("PatientsPersonal");
                query.whereEqualTo("username", user.getString("username"));
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        AVObject post;
                        if (list.size() > 0) {
                            post = list.get(0);
                        } else {
                            post = new AVObject("PatientsPersonal");
                        }
                        post.put("username", user.getString("username"));
                        post.put("name", name);
                        post.put("gender", gender);
                        post.put("birthday", birthday);
                        post.put("height", height);
                        post.put("weight", weight);
                        post.put("idNumber", idNumber);
                        post.put("phone", phone);
                        post.put("email", email);
                        post.put("address", address);
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    showToast(R.string.add_success);
                                } else {
                                    showToast(R.string.sync_success);
                                    e.printStackTrace();
                                }
                            }
                        });
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
}
