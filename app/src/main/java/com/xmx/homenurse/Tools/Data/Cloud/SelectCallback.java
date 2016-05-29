package com.xmx.homenurse.Tools.Data.Cloud;

import java.util.List;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class SelectCallback<Entity extends ICloudEntity> {

    public abstract void success(List<Entity> entities);

    public abstract void notInit();

    public abstract void notLoggedIn();

    public abstract void errorNetwork();

    public abstract void errorUsername();

    public abstract void errorChecksum();
}