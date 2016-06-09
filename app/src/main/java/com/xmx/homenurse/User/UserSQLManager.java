package com.xmx.homenurse.User;

import com.xmx.homenurse.Tools.Data.SQL.BaseSQLEntityManager;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/26.
 */
public class UserSQLManager extends BaseSQLEntityManager<User> {
    private static UserSQLManager instance;

    public synchronized static UserSQLManager getInstance() {
        if (null == instance) {
            instance = new UserSQLManager();
        }
        return instance;
    }

    private UserSQLManager() {
        tableName = "USER";
        entityTemplate = new User();
        openDatabase();
    }

    public void updateUser(long id, String name, String gender, Date birthday, double height
            , double weight, String idNumber, String phone, String email, String address) {
        if (!checkDatabase()) {
            return;
        }
        updateDate(id,
                "NAME = " + name,
                "GENDER = " + gender,
                "BIRTHDAY = " + birthday.getTime(),
                "HEIGHT = " + height,
                "WEIGHT = " + weight,
                "ID_NUMBER = " + idNumber,
                "PHONE = "+ phone,
                "EMAIL = " + email,
                "ADDRESS = " + address);
    }

    public User getUser() {
        if (!checkDatabase()) {
            return null;
        }
        return selectLatest("ID", false);
    }
}
