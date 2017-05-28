package com.xmx.androidframeworkbase.module.net;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;
import com.xmx.androidframeworkbase.utils.JSONUtil;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class JsonActivity extends BaseTempActivity {

    EditText jsonView;
    TextView resultView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_json);

        jsonView = getViewById(R.id.edit_json);
        resultView = getViewById(R.id.text_result);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.btn_parse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = jsonView.getText().toString().trim();
                try {
                    if (json.startsWith("{")) {
                        Map<String, Object> map = JSONUtil.parseObject(json);
                        resultView.setText(JSONUtil.formatJSONObject(map, " : ", "|----"));
                    } else if (json.startsWith("[")) {
                        List<Object> list = JSONUtil.parseArray(json);
                        resultView.setText(JSONUtil.formatJSONArray(list, " : ", "|----"));
                    } else {
                        showToast("格式不正确");
                    }
                } catch (Exception e) {
                    ExceptionUtil.normalException(e, JsonActivity.this);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
