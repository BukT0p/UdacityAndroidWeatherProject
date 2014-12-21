package com.dataart.vyakunin.udacityandroidweatherproject.ui;

import android.os.Bundle;

import com.dataart.vyakunin.udacityandroidweatherproject.R;
import com.dataart.vyakunin.udacityandroidweatherproject.ui.fragments.HomeFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity
public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchTo(HomeFragment.newInstance());
    }
}
