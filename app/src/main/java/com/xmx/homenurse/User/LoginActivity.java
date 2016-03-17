package com.xmx.homenurse.User;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                DataManager.getInstance().login();
                startActivity(MainActivity.class);
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
