package com.xmx.homenurse.User;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.xmx.homenurse.ActivityBase.BaseActivity;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Data.DataManager;
import com.xmx.homenurse.MainActivity;
import com.xmx.homenurse.R;

public class LoginActivity extends BaseActivity {
    private long mExitTime = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setListener() {
        Button login = getViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView usernameView = getViewById(R.id.tv_username);
                String username = usernameView.getText().toString();
                EditText passwordView = getViewById(R.id.tv_password);
                String password = passwordView.getText().toString();
                if (username.equals("")) {
                    showToast(R.string.username_empty);
                } else if (password.equals("")) {
                    showToast(R.string.password_empty);
                } else {
                    DataManager.getInstance().login();
                    startActivity(MainActivity.class);
                    finish();
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
            showToast(R.string.confirm_exit);
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
