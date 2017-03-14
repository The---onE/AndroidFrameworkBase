package com.xmx.androidframeworkbase.module.message;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.common.choosephoto.AlbumActivity;
import com.xmx.androidframeworkbase.common.user.UserManager;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_push_item_message)
public class PushItemMessageActivity extends BaseTempActivity {
    String item;
    String takePhotoPath;
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

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                showToast("创建目录失败");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Event(R.id.btn_take_photo)
    private void onTakePhotoClick(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutputMediaFileUri();
        takePhotoPath = fileUri.getPath();
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(i, Constants.TAKE_PHOTO);
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
        } else if (requestCode == Constants.TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            paths = new ArrayList<>();
            paths.add(takePhotoPath);

            path.setText(takePhotoPath);
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
                        fileName = path.substring(end + 1);
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
                                                ExceptionUtil.normalException(e, getBaseContext());

                                                btnPush.setText("推送");
                                                btnPush.setEnabled(true);
                                            }
                                        }
                                    });
                                } catch (Exception ex) {
                                    ExceptionUtil.normalException(ex, getBaseContext());

                                    btnPush.setText("推送");
                                    btnPush.setEnabled(true);
                                }
                            } else {
                                ExceptionUtil.normalException(e, getBaseContext());

                                btnPush.setText("推送");
                                btnPush.setEnabled(true);
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
                            ExceptionUtil.normalException(e, getBaseContext());

                            btnPush.setText("推送");
                            btnPush.setEnabled(true);
                        }
                    }
                });
            }
        } catch (Exception e) {
            ExceptionUtil.normalException(e, getBaseContext());

            btnPush.setText("推送");
            btnPush.setEnabled(true);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
