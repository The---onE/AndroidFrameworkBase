package com.xmx.androidframeworkbase;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.xmx.androidframeworkbase.Tools.Data.DataManager;
import com.xmx.androidframeworkbase.Tools.PushMessage.ReceiveMessageActivity;
import com.xmx.androidframeworkbase.User.UserManager;

import org.xutils.x;

/**
 * Created by The_onE on 2016/1/3.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        AVOSCloud.initialize(this, Constants.APP_ID, Constants.APP_KEY);

        PushService.setDefaultPushCallback(this, ReceiveMessageActivity.class);
        PushService.subscribe(this, "system", ReceiveMessageActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground();

        UserManager.getInstance().setContext(this);

        DataManager.getInstance().setContext(this);
    }
}
