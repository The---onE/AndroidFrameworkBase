package com.xmx.androidframeworkbase;

import com.avos.avoscloud.AVOSCloud;
import com.xmx.androidframeworkbase.Tools.Data.DataManager;
import com.xmx.androidframeworkbase.User.UserManager;

/**
 * Created by The_onE on 2016/1/3.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, Constants.APP_ID, Constants.APP_KEY);
        UserManager.getInstance().setContext(this);

        DataManager.getInstance().setContext(this);
    }
}
