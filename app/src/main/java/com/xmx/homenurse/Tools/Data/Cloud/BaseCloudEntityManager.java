package com.xmx.homenurse.Tools.Data.Cloud;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.xmx.homenurse.User.Callback.AutoLoginCallback;
import com.xmx.homenurse.User.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class BaseCloudEntityManager<Entity extends ICloudEntity> {

    //子类构造函数中初始化下列变量！
    protected String tableName = null;
    protected Entity entityTemplate = null; //空模版，不需要数据
    protected String userField = null; //用户字段，保存当前登录用户的ObjectId，为空时不保存用户字段

    protected boolean checkDatabase() {
        return tableName != null && entityTemplate != null;
    }

    public void selectByCondition(final Map<String, Object> conditions,
                                  final SelectCallback<Entity> callback) {
        if (!checkDatabase()) {
            callback.notInit();
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                AVQuery<AVObject> query = new AVQuery<>(tableName);
                if (userField != null) {
                    query.whereEqualTo(userField, user.getObjectId());
                }
                if (conditions != null) {
                    for (String key : conditions.keySet()) {
                        query.whereEqualTo(key, conditions.get(key));
                    }
                }
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            List<Entity> entities = new ArrayList<>();
                            for (AVObject object : avObjects) {
                                Entity entity = (Entity) entityTemplate.convertToEntity(object);
                                entities.add(entity);
                            }
                            callback.success(entities);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void notLoggedIn() {
                callback.notLoggedIn();
            }

            @Override
            public void errorNetwork() {
                callback.errorNetwork();
            }

            @Override
            public void errorUsername() {
               callback.errorUsername();
            }

            @Override
            public void errorChecksum() {
                callback.errorChecksum();
            }
        });
    }

    public void insertToCloud(final Entity entity, final InsertCallback callback) {
        if (!checkDatabase()) {
            callback.notInit();
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                final AVObject object = entity.getContent(tableName);
                if (userField != null) {
                    object.put(userField, user.getObjectId());
                }
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            callback.success(object.getObjectId());
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void notLoggedIn() {
                callback.notLoggedIn();
            }

            @Override
            public void errorNetwork() {
                callback.errorNetwork();
            }

            @Override
            public void errorUsername() {
                callback.errorUsername();
            }

            @Override
            public void errorChecksum() {
                callback.errorChecksum();
            }
        });
    }
}