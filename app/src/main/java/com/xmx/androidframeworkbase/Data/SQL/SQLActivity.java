package com.xmx.androidframeworkbase.Data.SQL;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SQLActivity extends BaseTempActivity {

    SQLAdapter sqlAdapter;
    ListView sqlList;
    Button add;
    EditText text;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sql);

        sqlList = getViewById(R.id.list_sql);
        sqlAdapter = new SQLAdapter(this, new ArrayList<SQL>());
        sqlList.setAdapter(sqlAdapter);

        text = getViewById(R.id.edit_sql);
        add = getViewById(R.id.btn_sql);
    }

    @Override
    protected void setListener() {
        sqlList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final SQL sql = (SQL) sqlAdapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                builder.setMessage("要更新该记录吗？");
                builder.setTitle("提示");
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLEntityManager.getInstance().deleteById(sql.mId);
                        SQLManager.getInstance().updateData();
                        sqlAdapter.updateList(SQLManager.getInstance().getData());
                    }
                });
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLEntityManager.getInstance().updateDate(sql.mId,
                                "Time = " + new Date().getTime());
                        SQLManager.getInstance().updateData();
                        sqlAdapter.updateList(SQLManager.getInstance().getData());
                    }
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = text.getText().toString();
                SQL entity = new SQL();
                entity.mData = data;
                entity.mTime = new Date();
                SQLEntityManager.getInstance().insertData(entity);
                text.setText("");
                showToast(R.string.add_success);
                SQLManager.getInstance().updateData();
                sqlAdapter.updateList(SQLManager.getInstance().getData());
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        SQLManager.getInstance().updateData();
        sqlAdapter.updateList(SQLManager.getInstance().getData());
    }
}
