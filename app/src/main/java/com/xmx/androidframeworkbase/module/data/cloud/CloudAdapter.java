package com.xmx.androidframeworkbase.module.data.cloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.common.data.BaseEntityAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class CloudAdapter extends BaseEntityAdapter<Cloud> {

    public CloudAdapter(Context context, List<Cloud> data) {
        super(context, data);
    }

    static class ViewHolder {
        @ViewInject(R.id.item_data)
        TextView data;

        @ViewInject(R.id.item_time)
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cloud, null);
            holder = new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < mData.size()) {
            Cloud sql = mData.get(position);
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