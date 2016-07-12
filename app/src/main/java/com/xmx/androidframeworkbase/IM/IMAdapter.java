package com.xmx.androidframeworkbase.IM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.Data.BaseEntityAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class IMAdapter extends BaseEntityAdapter<AVIMTextMessage> {

    public IMAdapter(Context context, List<AVIMTextMessage> data) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_im, null);
            holder = new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < mData.size()) {
            AVIMTextMessage m = mData.get(position);
            holder.data.setText(m.getFrom() + " : " + m.getText());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(m.getTimestamp());
            holder.time.setText(timeString);
        } else {
            holder.data.setText("加载失败");
        }

        return convertView;
    }
}