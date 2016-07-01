package com.xmx.androidframeworkbase.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.SQL.SQL;
import com.xmx.androidframeworkbase.SQL.SQLAdapter;
import com.xmx.androidframeworkbase.SQL.SQLEntityManager;
import com.xmx.androidframeworkbase.SQL.SQLManager;
import com.xmx.androidframeworkbase.Tools.FragmentBase.BaseFragment;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SQLFragment extends BaseFragment {

    SQLAdapter sqlAdapter;
    ListView sqlList;
    Button add;
    EditText text;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_sql, container, false);
    }

    @Override
    protected void initView(View view) {
        sqlList = (ListView) view.findViewById(R.id.list_sql);
        sqlAdapter = new SQLAdapter(getContext());
        sqlList.setAdapter(sqlAdapter);

        text = (EditText) view.findViewById(R.id.edit_sql);
        add = (Button) view.findViewById(R.id.btn_sql);
    }

    @Override
    protected void setListener(View view) {
        sqlList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final SQL sql = (SQL) sqlAdapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("要更新该记录吗？");
                builder.setTitle("提示");
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLEntityManager.getInstance().deleteById(sql.mId);
                        SQLManager.getInstance().updateData();
                        sqlAdapter.updateList();
                    }
                });
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLEntityManager.getInstance().updateDate(sql.mId,
                                "Time = " + new Date().getTime());
                        SQLManager.getInstance().updateData();
                        sqlAdapter.updateList();
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
                sqlAdapter.updateList();
            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {
        SQLManager.getInstance().updateData();
        sqlAdapter.updateList();
    }

}
