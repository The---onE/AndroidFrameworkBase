package com.xmx.androidframeworkbase.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.ShoppingCart.CartAdapter;
import com.xmx.androidframeworkbase.ShoppingCart.CartItem;
import com.xmx.androidframeworkbase.ShoppingCart.OrderCodeActivity;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;
import com.xmx.androidframeworkbase.Tools.ShoppingCart.CartList;

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
        String order = cartItems.getOrder();
        showToast(order);
        if (order != "") {
            Intent intent = new Intent(getContext(), OrderCodeActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        }
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
