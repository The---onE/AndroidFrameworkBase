package com.xmx.androidframeworkbase.common.cart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by The_onE on 2016/8/21.
 */
public interface ICartItem {
    String getID();
    String getName();
    Drawable getImage();
    int getCount();

    String getEntry();
    View getView(Context context, View convertView);
}
