package com.xmx.androidframeworkbase.Log;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;
import com.xmx.androidframeworkbase.Tools.OperationLog.LogChangeEvent;
import com.xmx.androidframeworkbase.Tools.OperationLog.OperationLog;
import com.xmx.androidframeworkbase.Tools.OperationLog.OperationLogEntityManager;
import com.xmx.androidframeworkbase.Tools.OperationLog.OperationLogManager;

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
