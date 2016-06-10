package com.xmx.homenurse.User;

import com.xmx.homenurse.Tools.Data.Callback.UpdateCallback;
import com.xmx.homenurse.Tools.Data.SQL.BaseSQLEntityManager;
import com.xmx.homenurse.Tools.Data.Sync.BaseSyncEntityManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by The_onE on 2016/3/26.
 */
public class UserSyncManager extends BaseSyncEntityManager<User> {
    private static UserSyncManager instance;

    public synchronized static UserSyncManager getInstance() {
        if (null == instance) {
            instance = new UserSyncManager();
        }
        return instance;
    }

    private UserSyncManager() {
        setTableName("PatientPersonal");
        setEntityTemplate(new User());
        setUserField("patient");
    }

    public void updateUser(String cloudId, String name, String gender, Date birthday, double height
            , double weight, String idNumber, String phone, String email, String address
            , UpdateCallback callback) {
        if (!checkDatabase()) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("gender", gender);
        map.put("birthday", birthday);
        map.put("height", height);
        map.put("weight", weight);
        map.put("idNumber", idNumber);
        map.put("phone", phone);
        map.put("email", email);
        map.put("address", address);
        updateData(cloudId, map, callback);
    }
}
