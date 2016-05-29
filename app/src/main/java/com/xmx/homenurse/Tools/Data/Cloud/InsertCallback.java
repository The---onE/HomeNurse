package com.xmx.homenurse.Tools.Data.Cloud;

import java.util.List;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class InsertCallback {

    public abstract void success(String objectId);

    public abstract void notInit();

    public abstract void notLoggedIn();

    public abstract void errorNetwork();

    public abstract void errorUsername();

    public abstract void errorChecksum();
}
