package com.xmx.androidframeworkbase.Tools.ShoppingCart;

import java.util.ArrayList;

/**
 * Created by The_onE on 2016/8/23.
 */
public abstract class CartList extends ArrayList<ICartItem> {

    public String getOrder() {
        String order = getUserId() + "&";
        for (ICartItem item : this) {
            String entry = item.getEntry();
            if (entry != null && !entry.equals("")) {
                order += item.getEntry() + "&";
            }
        }
        return order;
    }

    public abstract String getUserId();
}
