package com.dataart.vyakunin.udacityandroidweatherproject.ui;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.dataart.vyakunin.udacityandroidweatherproject.R;
import com.dataart.vyakunin.udacityandroidweatherproject.WeatherApplication;
import com.dataart.vyakunin.udacityandroidweatherproject.service.messaging.ResultReceiverManager;
import com.dataart.vyakunin.udacityandroidweatherproject.ui.fragments.MenuFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.List;

@EActivity
public abstract class BaseActivity extends ActionBarActivity {


    private Fragment currentFragment;

    @ViewById(R.id.progress_overlay)
    View progressOverlay;

    protected List<ResultReceiverManager.ResultListener> getResultListeners() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<ResultReceiverManager.ResultListener> listeners;
        if ((listeners = getResultListeners()) != null && !listeners.isEmpty()) {
            for (ResultReceiverManager.IResultListener listener : listeners) {
                WeatherApplication.getResultReceiverManager(this).addResultListener(listener);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        List<ResultReceiverManager.ResultListener> listeners;
        if ((listeners = getResultListeners()) != null && !listeners.isEmpty()) {
            for (ResultReceiverManager.IResultListener listener : listeners) {
                WeatherApplication.getResultReceiverManager(this).removeResultListener(listener);
            }
        }
    }

    public void switchTo(Fragment fragment) {
        switchTo(fragment, false, null);
    }

    public void switchTo(Fragment fragment, boolean addToBackStack, String ftag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if ((fragmentManager.getBackStackEntryCount() > 0) && (addToBackStack) && (fragmentManager.findFragmentByTag(ftag) != null)) {
            fragmentManager.popBackStack(ftag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();
        if (ftag == null) {
            transaction.replace(R.id.container, fragment);
        } else {
            transaction.replace(R.id.container, fragment, ftag);
        }
        if (addToBackStack) transaction.addToBackStack(ftag);
        transaction.commit();
        currentFragment = fragment;
    }


    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    public void showProgressOverlay(boolean show) {
        progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
