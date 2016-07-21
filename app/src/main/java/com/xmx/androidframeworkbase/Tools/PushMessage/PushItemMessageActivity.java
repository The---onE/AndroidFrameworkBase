package com.xmx.androidframeworkbase.Tools.PushMessage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SendCallback;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;
import com.xmx.androidframeworkbase.User.UserManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_push_item_message)
public class PushItemMessageActivity extends BaseTempActivity {
    String item;

    @ViewInject(R.id.message_content)
    private EditText content;

    @ViewInject(R.id.message_title)
    private EditText title;

    @Override
    protected void initView(Bundle savedInstanceState) {
        item = getIntent().getStringExtra("title");
        setTitle(item);
    }

    @Event(R.id.push_btn)
    private void onPushClick(View view) {
        try {
            JSONObject object = new JSONObject();

            if (!title.getText().toString().equals("")) {
                object.put("alert", title.getText().toString());
            } else {
                showToast("请输入标题");
                return;
            }

            if (!content.getText().toString().equals("")) {
                object.put("content", content.getText().toString());
            }

            object.put("item", item);
            object.put("action", "com.avos.ITEM_MESSAGE");

            AVQuery pushQuery = AVInstallation.getQuery();
            pushQuery.whereEqualTo("channels", UserManager.getSHA(item));
            AVPush push = new AVPush();
            push.setQuery(pushQuery);
            push.setPushToAndroid(true);
            push.setData(object);
            push.sendInBackground(new SendCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showToast("推送成功");
                        finish();
                    } else {
                        filterException(e);
                    }
                }
            });
        } catch (JSONException e) {
            filterException(e);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
