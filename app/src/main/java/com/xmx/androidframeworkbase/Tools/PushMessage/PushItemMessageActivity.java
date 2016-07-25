package com.xmx.androidframeworkbase.Tools.PushMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.AlbumActivity;
import com.xmx.androidframeworkbase.User.UserManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_push_item_message)
public class PushItemMessageActivity extends BaseTempActivity {
    String item;
    List<String> paths = new ArrayList<>();
    int pushImage = 0;

    @ViewInject(R.id.push_btn)
    private Button btnPush;

    @ViewInject(R.id.message_content)
    private EditText content;

    @ViewInject(R.id.message_title)
    private EditText title;

    @ViewInject(R.id.photo_path)
    private TextView path;

    @Override
    protected void initView(Bundle savedInstanceState) {
        item = getIntent().getStringExtra("title");
        setTitle(item);
    }

    @Event(R.id.btn_choose_photo)
    private void onChoosePhotoClick(View view) {
        Intent i = new Intent(this, AlbumActivity.class);
        startActivityForResult(i, Constants.CHOOSE_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_ALBUM && resultCode == Activity.RESULT_OK && data != null) {
            paths = data.getStringArrayListExtra("paths");

            String p = "";
            for (String path : paths) {
                p += path + "\n";
            }
            path.setText(p);
        }
    }

    @Event(R.id.push_btn)
    private void onPushClick(View view) {
        try {
            final JSONObject object = new JSONObject();

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

            final AVQuery pushQuery = AVInstallation.getQuery();
            pushQuery.whereEqualTo("channels", UserManager.getSHA(item));

            btnPush.setText("正在推送…");
            btnPush.setEnabled(false);

            if (paths.size() > 0) {
                for (String path : paths) {
                    String fileName;
                    int end = path.lastIndexOf("/");
                    if (end != -1) {
                        fileName = path.substring(end+1);
                    } else {
                        fileName = "image.png";
                    }
                    final AVFile file = AVFile.withAbsoluteLocalPath(fileName, path);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                String url = file.getUrl();

                                try {
                                    object.put("file_url", url);

                                    AVPush push = new AVPush();
                                    push.setQuery(pushQuery);
                                    push.setPushToAndroid(true);
                                    push.setData(object);
                                    push.sendInBackground(new SendCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                pushImage++;
                                                if (pushImage >= paths.size()
                                                            ) {
                                                    showToast("推送成功");
                                                    finish();
                                                }
                                            } else {
                                                filterException(e);
                                            }
                                        }
                                    });
                                } catch (Exception ex) {
                                    filterException(ex);
                                }
                            } else {
                                filterException(e);
                            }
                        }
                    });
                }
            } else {
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
            }
        } catch (Exception e) {
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
