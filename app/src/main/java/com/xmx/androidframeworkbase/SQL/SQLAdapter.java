package com.xmx.androidframeworkbase.SQL;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SQLAdapter extends BaseAdapter {
    Context mContext;

    public SQLAdapter(Context context) {
        mContext = context;
    }

    public void updateList() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return SQLManager.getInstance().getData().size();
    }

    @Override
    public Object getItem(int i) {
        if (i < SQLManager.getInstance().getData().size()) {
            return SQLManager.getInstance().getData().get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        if (i < SQLManager.getInstance().getData().size()) {
            return SQLManager.getInstance().getData().get(i).mId;
        } else {
            return i;
        }
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

        List<SQL> sqlList = SQLManager.getInstance().getData();
        if (position < sqlList.size()) {
            SQL sql = sqlList.get(position);
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