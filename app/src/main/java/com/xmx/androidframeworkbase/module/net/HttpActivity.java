package com.xmx.androidframeworkbase.module.net;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.common.data.callback.DelCallback;
import com.xmx.androidframeworkbase.common.data.callback.InsertCallback;
import com.xmx.androidframeworkbase.common.data.callback.SelectCallback;
import com.xmx.androidframeworkbase.common.data.callback.UpdateCallback;
import com.xmx.androidframeworkbase.common.net.HttpGetCallback;
import com.xmx.androidframeworkbase.common.net.HttpManager;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.module.data.cloud.Cloud;
import com.xmx.androidframeworkbase.module.data.cloud.CloudAdapter;
import com.xmx.androidframeworkbase.module.data.cloud.CloudEntityManager;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HttpActivity extends BaseTempActivity {

    EditText addressView;
    TextView responseView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http);

        addressView = getViewById(R.id.edit_address);
        responseView = getViewById(R.id.text_response);
        responseView.setTextIsSelectable(true);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.btn_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressView.getText().toString();
                if (!address.equals("")) {
                    if (!address.startsWith("http://")) {
                        address = "http://" + address;
                    }
                    HttpManager.getInstance().get(address, null, new HttpGetCallback() {
                        @Override
                        public void success(String result) {
                            responseView.setText(result);
                        }

                        @Override
                        public void fail(Exception e) {
                            ExceptionUtil.normalException(e, HttpActivity.this);
                        }
                    });
                } else {
                    showToast("地址不能为空");
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
