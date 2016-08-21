package com.xmx.androidframeworkbase.ShoppingCart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ShoppingCart.ICartItem;

/**
 * Created by The_onE on 2016/8/21.
 */
public class CartItem implements ICartItem {

    String id;
    String name;
    Drawable image;
    int count;
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

    @Override
    public View getView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, null);

        return view;
    }
}
