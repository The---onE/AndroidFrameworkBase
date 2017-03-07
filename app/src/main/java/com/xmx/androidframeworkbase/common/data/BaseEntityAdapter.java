package com.xmx.androidframeworkbase.common.data;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by The_onE on 2016/7/11.
 */
public abstract class BaseEntityAdapter<Entity> extends BaseAdapter {

    protected Context mContext;
    protected List<Entity> mData;

    public BaseEntityAdapter(Context context, List<Entity> data) {
        mContext = context;
        mData = data;
    }

    public void updateList(List<Entity> data) {
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
}
