package com.xmx.androidframeworkbase.module.net;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.common.net.HttpGetCallback;
import com.xmx.androidframeworkbase.common.net.HttpManager;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetRequestActivity extends BaseTempActivity {

    EditText addressView;
    TextView responseView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_get_request);

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
                            ExceptionUtil.normalException(e, GetRequestActivity.this);
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
