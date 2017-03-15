package com.xmx.androidframeworkbase.core.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.module.cart.CartAdapter;
import com.xmx.androidframeworkbase.module.cart.CartItem;
import com.xmx.androidframeworkbase.module.cart.OrderCodeActivity;
import com.xmx.androidframeworkbase.module.cart.OrderManager;
import com.xmx.androidframeworkbase.base.fragment.xUtilsFragment;
import com.xmx.androidframeworkbase.common.cart.CartList;
import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.common.user.UserConstants;
import com.xmx.androidframeworkbase.common.user.UserManager;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_cart)
public class CartFragment extends xUtilsFragment {

    @ViewInject(R.id.list_cart)
    ListView cartList;

    CartList cartItems = new CartList();
    CartAdapter cartAdapter;

    @Event(value = R.id.btn_order)
    private void onOrderClick(View view) {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(UserData user) {
                String userId = user.objectId;
                OrderManager manager = new OrderManager(userId);
                String order = manager.getOrder(cartItems);
                showToast(order);
                if (!order.equals("")) {
                    Intent intent = new Intent(getContext(), OrderCodeActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
                }
            }

            @Override
            public void error(AVException e) {
                showToast(R.string.network_error);
                ExceptionUtil.normalException(e, getContext());
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                    case UserConstants.CANNOT_CHECK_LOGIN:
                        showToast(R.string.not_loggedin);
                        break;

                    case UserConstants.USERNAME_ERROR:
                        showToast(R.string.username_error);
                        break;

                    case UserConstants.CHECKSUM_ERROR:
                        showToast(R.string.not_loggedin);
                        break;
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        for (int i = 0; i < 100; ++i) {
            CartItem item = new CartItem("" + i, 0);
            item.setName("Item: " + i);
            cartItems.add(item);
        }
        cartAdapter = new CartAdapter(getContext(), cartItems);

        cartList.setAdapter(cartAdapter);
    }
}
