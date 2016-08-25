package com.xmx.androidframeworkbase.ShoppingCart;

import com.xmx.androidframeworkbase.Tools.ShoppingCart.BaseOrderManager;
import com.xmx.androidframeworkbase.Tools.ShoppingCart.CartList;
import com.xmx.androidframeworkbase.Tools.ShoppingCart.ICartItem;

/**
 * Created by The_onE on 2016/8/25.
 */
public class OrderManager extends BaseOrderManager {
    String mUserId;
    boolean loadFlag = false;

    public OrderManager(String userId) {
        mUserId = userId;
        loadFlag = true;
    }

    public String getOrder(CartList list) {
        if (loadFlag) {
            String order = mUserId + "&";
            for (ICartItem item : list) {
                String entry = item.getEntry();
                if (entry != null && !entry.equals("")) {
                    order += item.getEntry() + "&";
                }
            }
            return order;
        } else {
            return "";
        }
    }
}
