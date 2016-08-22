package com.xmx.androidframeworkbase.ShoppingCart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ShoppingCart.ICartItem;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by The_onE on 2016/8/21.
 */
public class CartItem implements ICartItem {

    String id;
    String name;
    Drawable image;
    int count = 0;
    int max;
    int min;


    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Drawable getImage() {
        return image;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getMin() {
        return min;
    }

    class ItemHolder {
        TextView nameView;
        Button clickView;
    }

    @Override
    public View getView(Context context, View convertView) {
        ItemHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, null);
            holder = new ItemHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.item_name);
            holder.clickView = (Button) convertView.findViewById(R.id.item_click);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        holder.nameView.setText(name + (count > 0 ? " Count: " + count : ""));
        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                EventBus.getDefault().post(new CartChangeEvent());
            }
        });

        return convertView;
    }
}
