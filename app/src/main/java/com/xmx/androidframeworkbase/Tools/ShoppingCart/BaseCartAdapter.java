package com.xmx.androidframeworkbase.Tools.ShoppingCart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.androidframeworkbase.Data.SQL.SQL;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.ShoppingCart.CartItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/8/21.
 */
public abstract class BaseCartAdapter<Item extends ICartItem> extends BaseAdapter {

    protected Context mContext;
    protected List<Item> mData;

    public BaseCartAdapter(Context context, List<Item> data) {
        mContext = context;
        mData = data;
    }

    public void updateList(List<Item> data) {
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
}
