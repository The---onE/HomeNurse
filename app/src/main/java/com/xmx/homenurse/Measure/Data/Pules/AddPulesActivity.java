package com.xmx.homenurse.Measure.Data.Pules;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Tools.Data.Callback.InsertCallback;

import java.util.Date;

public class AddPulesActivity extends BaseTempActivity {

    EditText pulesView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_pules);

        pulesView = getViewById(R.id.pules);
    }

    @Override
    protected void setListener() {
        Button ok = getViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double pules = getEditViewDouble(pulesView);
                if (pules > 0) {
                    Pules entity = new Pules();
                    entity.mPules = pules;
                    Date time = new Date();
                    entity.mTime = time;
                    entity.mYear = time.getYear() + 1900;
                    entity.mMonth = time.getMonth() + 1;
                    entity.mDay = time.getDate();
                    entity.mStatus = 0;

                    PulesManager.getInstance().insertData(entity, new InsertCallback() {
                        @Override
                        public void success(String objectId) {
                            showToast(R.string.save_success);
                            finish();
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
                    showToast("请输入脉搏");
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    double getEditViewDouble(EditText et) {
        if (!et.getText().toString().equals("")) {
            return Double.parseDouble(et.getText().toString());
        } else {
            return -1;
        }
    }
}
