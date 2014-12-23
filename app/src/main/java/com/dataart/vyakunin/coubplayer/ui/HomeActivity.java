package com.dataart.vyakunin.coubplayer.ui;

import android.os.Bundle;

import com.dataart.vyakunin.coubplayer.ui.fragments.HomeFragment;

import org.androidannotations.annotations.EActivity;

@EActivity
public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchTo(HomeFragment.newInstance());
    }
}
