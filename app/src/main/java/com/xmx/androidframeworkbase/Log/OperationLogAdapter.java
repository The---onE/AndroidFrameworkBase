package com.xmx.androidframeworkbase.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.Data.BaseEntityAdapter;
import com.xmx.androidframeworkbase.Tools.OperationLog.OperationLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class OperationLogAdapter extends BaseEntityAdapter<OperationLog> {

    public OperationLogAdapter(Context context, List<OperationLog> data) {
        super(context, data);
    }

    static class ViewHolder {
        TextView operation;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_operation_log, null);
            holder = new ViewHolder();
            holder.operation = (TextView) convertView.findViewById(R.id.item_operation);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < mData.size()) {
            OperationLog log = mData.get(position);
            holder.operation.setText(log.mOperation);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(log.mTime);
            holder.time.setText(timeString);
        } else {
            holder.operation.setText("加载失败");
        }

        return convertView;
    }
}