package com.xmx.androidframeworkbase.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);


        return view;
    }

}
