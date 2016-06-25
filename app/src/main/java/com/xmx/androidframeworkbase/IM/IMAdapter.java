package com.xmx.androidframeworkbase.IM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.androidframeworkbase.Cloud.Cloud;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.IM.Message.TextMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class IMAdapter extends BaseAdapter {
    Context mContext;
    List<TextMessage> mData;

    public IMAdapter(Context context, List<TextMessage> data) {
        mContext = context;
        mData = data;
    }

    public void updateList(List<TextMessage> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        if (i < mData.size()) {
            return mData.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView data;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_im, null);
            holder = new ViewHolder();
            holder.data = (TextView) convertView.findViewById(R.id.item_data);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < mData.size()) {
            TextMessage m = mData.get(position);
            holder.data.setText(m.mFrom + " : " + m.mText);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(m.mTime);
            holder.time.setText(timeString);
        } else {
            holder.data.setText("加载失败");
        }

        return convertView;
    }
}