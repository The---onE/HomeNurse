package com.xmx.homenurse;

import com.xmx.homenurse.Data.DataManager;

/**
 * Created by The_onE on 2016/1/3.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DataManager.getInstance().setContext(this);
    }
}
