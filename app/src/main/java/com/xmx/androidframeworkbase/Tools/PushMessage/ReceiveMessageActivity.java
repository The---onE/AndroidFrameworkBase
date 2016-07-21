package com.xmx.androidframeworkbase.Tools.PushMessage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_receive_message)
public class ReceiveMessageActivity extends BaseTempActivity {
    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        try {
            String data = getIntent().getStringExtra("com.avos.avoscloud.Data");
            JSONObject json = new JSONObject(data);

            try {
                String alert = json.getString("alert");
                if (alert != null) {
                    setTitle(alert);
                } else {
                    setTitle(R.string.app_name);
                }
            } catch (JSONException e) {
                setTitle(R.string.app_name);
            }

            try {
                String content = json.getString("content");
                TextView contentView = getViewById(R.id.message_content);
                if (content != null) {
                    contentView.setText(content);
                } else {
                    contentView.setText("");
                }
            } catch (JSONException e) {
                TextView contentView = getViewById(R.id.message_content);
                contentView.setText("");
            }

            try {
                EditText urlView = getViewById(R.id.file_url);
                String url = json.getString("file_url");
                if (url != null) {
                    urlView.setText(url);
                } else {
                    urlView.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                EditText urlView = getViewById(R.id.file_url);
                urlView.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
