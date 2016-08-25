package com.xmx.androidframeworkbase.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.ShoppingCart.CartAdapter;
import com.xmx.androidframeworkbase.ShoppingCart.CartItem;
import com.xmx.androidframeworkbase.ShoppingCart.OrderCodeActivity;
import com.xmx.androidframeworkbase.ShoppingCart.OrderManager;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;
import com.xmx.androidframeworkbase.Tools.ShoppingCart.CartList;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.UserConstants;
import com.xmx.androidframeworkbase.User.UserManager;

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
    private void onOrderCick(View view) {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                String userId = user.getObjectId();
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
                filterException(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
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
