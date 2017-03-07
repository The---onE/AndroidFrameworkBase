package com.xmx.androidframeworkbase.common.cart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by The_onE on 2016/8/21.
 */
public abstract class BaseCartAdapter extends BaseAdapter {

    protected Context mContext;
    protected CartList mData;

    public BaseCartAdapter(Context context, CartList data) {
        mContext = context;
        mData = data;

        EventBus.getDefault().register(this);
    }

    public void updateList(CartList data) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < mData.size()) {
            return mData.get(position).getView(mContext, convertView);
        } else {
            return new View(mContext);
        }
    }

    public static void update() {
        EventBus.getDefault().post(new CartChangeEvent());
    }

    @Subscribe
    public void onEvent(CartChangeEvent event) {
        notifyDataSetChanged();
    }
}
