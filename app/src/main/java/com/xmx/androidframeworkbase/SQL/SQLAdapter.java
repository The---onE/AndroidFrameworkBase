package com.xmx.androidframeworkbase.SQL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.Data.BaseEntityAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SQLAdapter extends BaseEntityAdapter<SQL> {

    public SQLAdapter(Context context, List<SQL> data) {
        super(context, data);
    }

    static class ViewHolder {
        TextView data;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sql, null);
            holder = new ViewHolder();
            holder.data = (TextView) convertView.findViewById(R.id.item_data);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < mData.size()) {
            SQL sql = mData.get(position);
            holder.data.setText(sql.mData);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(sql.mTime);
            holder.time.setText(timeString);
        } else {
            holder.data.setText("加载失败");
        }

        return convertView;
    }
}