package com.xmx.androidframeworkbase.ShoppingCart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ShoppingCart.ICartItem;

/**
 * Created by The_onE on 2016/8/21.
 */
public class CartItem implements ICartItem {

    static int itemCount = 0;
    String id;
    String name;
    Drawable image;
    int count = 0;
    int max = 10;
    int min = 0;

    public CartItem(String id, int count) {
        itemCount++;
        this.id = id;
        this.count = count;
    }

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

    public int add(int delta) {
        int re = count + delta;
        if (min <= re && re <= max) {
            count = re;
        }
        return count;
    }

    public int sub(int delta) {
        int re = count - delta;
        if (min <= re && re <= max) {
            count = re;
        }
        return count;
    }

    @Override
    public String getEntry() {
        if (count > 0) {
            return id + "=" + count;
        } else {
            return "";
        }
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

        if (count > 0) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            convertView.setBackgroundColor(Color.GRAY);
        }
        holder.nameView.setText(name + (count > 0 ? " Count: " + count : ""));
        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(1);
                CartAdapter.update();
            }
        });

        return convertView;
    }
}
