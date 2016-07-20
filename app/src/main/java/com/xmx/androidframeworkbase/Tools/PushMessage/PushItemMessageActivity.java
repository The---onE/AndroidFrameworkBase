package com.xmx.androidframeworkbase.Tools.PushMessage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class PushItemMessageActivity extends BaseTempActivity {
    String item;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_push_item_message);

        item = getIntent().getStringExtra("title");
        setTitle(item);
    }

    @Override
    protected void setListener() {
        Button push = getViewById(R.id.push_btn);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
