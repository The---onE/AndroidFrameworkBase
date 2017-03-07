package com.xmx.androidframeworkbase.module.log;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.common.log.LogChangeEvent;
import com.xmx.androidframeworkbase.common.log.OperationLog;
import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;
import com.xmx.androidframeworkbase.common.log.OperationLogManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OperationLogActivity extends BaseTempActivity {

    OperationLogAdapter operationLogAdapter;
    ListView sqlList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_operation_log);

        sqlList = getViewById(R.id.list_operation_log);
        operationLogAdapter = new OperationLogAdapter(this, new ArrayList<OperationLog>());
        sqlList.setAdapter(operationLogAdapter);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.btn_clear_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OperationLogEntityManager.getInstance().clearDatabase();
                OperationLogManager.getInstance().updateData();
                operationLogAdapter.updateList(OperationLogManager.getInstance().getData());
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        OperationLogManager.getInstance().updateData();
        operationLogAdapter.updateList(OperationLogManager.getInstance().getData());

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(LogChangeEvent event) {
        OperationLogManager.getInstance().updateData();
        operationLogAdapter.updateList(OperationLogManager.getInstance().getData());
    }
}
