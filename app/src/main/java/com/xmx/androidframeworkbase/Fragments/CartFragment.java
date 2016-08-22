package com.xmx.androidframeworkbase.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.ShoppingCart.CartAdapter;
import com.xmx.androidframeworkbase.ShoppingCart.CartChangeEvent;
import com.xmx.androidframeworkbase.ShoppingCart.CartItem;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_cart)
public class CartFragment extends xUtilsFragment {

    @ViewInject(R.id.list_cart)
    ListView cartList;

    List<CartItem> cartItems = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        for (int i=0; i<100; ++i) {
            CartItem item = new CartItem();
            item.setName("Item: " + i);
            cartItems.add(item);
        }
        cartAdapter = new CartAdapter(getContext(), cartItems);

        cartList.setAdapter(cartAdapter);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onEventMainThread(CartChangeEvent event) {
        cartAdapter.notifyDataSetChanged();
    }
}
