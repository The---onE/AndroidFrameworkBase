package com.xmx.androidframeworkbase.ShoppingCart;

import android.content.Context;

import com.xmx.androidframeworkbase.Tools.ShoppingCart.BaseCartAdapter;

import java.util.List;

/**
 * Created by The_onE on 2016/8/21.
 */
public class CartAdapter extends BaseCartAdapter<CartItem> {
    public CartAdapter(Context context, List<CartItem> data) {
        super(context, data);
    }
}
