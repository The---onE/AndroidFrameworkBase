package com.xmx.androidframeworkbase.Tools.PushMessage;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_receive_message)
public class ReceiveMessageActivity extends BaseTempActivity {

    @ViewInject(R.id.image_layout)
    LinearLayout image;

    @ViewInject(R.id.message_content)
    private TextView contentView;

    @ViewInject(R.id.file_url)
    private EditText urlView;

    @ViewInject(R.id.fresco_image)
    private SimpleDraweeView imageView;

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

                if (content != null) {
                    contentView.setText(content);
                } else {
                    contentView.setText("");
                }
            } catch (JSONException e) {
                contentView.setText("");
            }

            try {
                String url = json.getString("file_url");
                if (url != null) {
                    urlView.setText(url);

//                    SimpleDraweeView imageView = new SimpleDraweeView(this);
//                    LinearLayout.LayoutParams params =
//                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT);
//                    imageView.setLayoutParams(params);
//                    imageView.setAspectRatio(1);

                    GenericDraweeHierarchyBuilder builder =
                            new GenericDraweeHierarchyBuilder(getResources());
                    GenericDraweeHierarchy hierarchy = builder
                            .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                            .build();
                    hierarchy.setPlaceholderImage(R.mipmap.ic_launcher);
                    imageView.setHierarchy(hierarchy);

                    Uri uri = Uri.parse(url);
                    imageView.setImageURI(uri);

//                    layout.addView(imageView);

                } else {
                    image.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                image.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
