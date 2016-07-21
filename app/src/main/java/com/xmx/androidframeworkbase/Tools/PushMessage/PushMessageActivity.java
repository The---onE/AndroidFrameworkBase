package com.xmx.androidframeworkbase.Tools.PushMessage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.SendCallback;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_push_message)
public class PushMessageActivity extends BaseTempActivity {
    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Event(R.id.push_btn)
    private void onPushClick(View view) {
        try {
            AVPush push = new AVPush();
            JSONObject object = new JSONObject();

            EditText title = getViewById(R.id.message_title);
            if (!title.getText().toString().equals("")) {
                object.put("alert", title.getText().toString());
            } else {
                showToast("请输入标题");
                return;
            }

            EditText content = getViewById(R.id.message_content);
            if (!content.getText().toString().equals("")) {
                object.put("content", content.getText().toString());
            }

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
