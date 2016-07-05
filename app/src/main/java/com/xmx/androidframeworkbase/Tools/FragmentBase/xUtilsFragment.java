package com.xmx.androidframeworkbase.Tools.FragmentBase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Created by The_onE on 2016/7/4.
 */
public abstract class xUtilsFragment extends BFragment {

    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        processLogic(savedInstanceState);

        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    protected abstract void processLogic(Bundle savedInstanceState);
}
