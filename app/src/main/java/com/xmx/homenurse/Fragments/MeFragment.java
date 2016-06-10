package com.xmx.homenurse.Fragments;

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
import com.xmx.homenurse.Tools.Data.Callback.InsertCallback;
import com.xmx.homenurse.Tools.Data.Callback.SelectCallback;
import com.xmx.homenurse.Tools.Data.Callback.UpdateCallback;
import com.xmx.homenurse.User.Callback.AutoLoginCallback;
import com.xmx.homenurse.User.User;
import com.xmx.homenurse.User.UserManager;
import com.xmx.homenurse.R;
import com.xmx.homenurse.User.LoginActivity;
import com.xmx.homenurse.User.UserSyncManager;

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
            public void success(final AVObject user) {
                UserSyncManager.getInstance().syncFromCloud(null, new SelectCallback<User>() {
                    @Override
                    public void success(List<User> users) {
                        User u;
                        if (users.size() > 0) {
                            u = users.get(0);
                            nameView.setText(u.mName);
                            genderView.setText(u.mGender);
                            for (int i = 0; i < genders.size(); ++i) {
                                String s = genders.get(i);
                                if (s.equals(u.mGender)) {
                                    pvOptions.setSelectOptions(i);
                                    break;
                                }
                            }

                            Date d = u.mBirthday;
                            pvTime.setTime(d);
                            birthday = d;
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            birthdayView.setText(df.format(d));

                            heightView.setText("" + u.mHeight);
                            weightView.setText("" + u.mWeight);
                            idNumberView.setText(u.mIdNumber);
                            phoneView.setText(u.mPhone);
                            emailView.setText(u.mEmail);
                            addressView.setText(u.mAddress);
                        } else {
                            u = new User("", genders.get(0),
                                    new Date(0), 0, 0, "", "", "", "");
                            UserSyncManager.getInstance().insertData(u, new InsertCallback() {
                                @Override
                                public void success(String objectId) {

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

    double getEditViewDouble(EditText et) {
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
                final String name = nameView.getText().toString();
                final double height = getEditViewDouble(heightView);
                final double weight = getEditViewDouble(weightView);

                final String idNumber = idNumberView.getText().toString();
                if (!idNumber.equals("")) {
                    String regex = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(idNumber);
                    if (!matcher.find()) {
                        showToast(R.string.error_id);
                        return;
                    }
                }
                final String phone = phoneView.getText().toString();
                final String email = emailView.getText().toString();
                final String address = addressView.getText().toString();

                UserSyncManager.getInstance().syncFromCloud(null, new SelectCallback<User>() {
                    @Override
                    public void success(List<User> users) {
                        User u;
                        if (users.size() > 0) {
                            u = users.get(0);
                            UserSyncManager.getInstance().updateUser(u.mCloudId,
                                    name, gender, birthday, height,
                                    weight, idNumber, phone, email, address, new UpdateCallback() {
                                        @Override
                                        public void success() {
                                            showToast(R.string.save_success);
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
                        } else {
                            u = new User(name, gender, birthday,
                                    height, weight, idNumber, phone, email, address);
                            UserSyncManager.getInstance().insertData(u, new InsertCallback() {
                                @Override
                                public void success(String objectId) {
                                    showToast(R.string.save_success);
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
}
