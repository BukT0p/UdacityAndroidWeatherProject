package com.dataart.vyakunin.coubplayer.ui.fragments;

import android.support.v4.app.Fragment;

import com.dataart.vyakunin.coubplayer.R;

import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_menu)
public class MenuFragment extends BaseFragment {
    public static Fragment newInstance() {
        return MenuFragment_.builder().build();

    }
}
