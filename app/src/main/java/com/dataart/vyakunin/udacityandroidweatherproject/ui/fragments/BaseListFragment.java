package com.dataart.vyakunin.udacityandroidweatherproject.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.inputmethod.InputMethodManager;

import com.dataart.vyakunin.udacityandroidweatherproject.WeatherApplication;
import com.dataart.vyakunin.udacityandroidweatherproject.service.messaging.ResultReceiverManager;
import com.dataart.vyakunin.udacityandroidweatherproject.ui.BaseActivity;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.util.Collections;
import java.util.List;

@EFragment
public abstract class BaseListFragment extends ListFragment {

    private boolean progressVisible;

    @Override
    public void onResume() {
        super.onResume();
        List<ResultReceiverManager.ResultListener> listeners;
        if ((listeners = getResultListeners()) != null && !listeners.isEmpty()) {
            for (ResultReceiverManager.IResultListener listener : listeners) {
                WeatherApplication.getResultReceiverManager(getActivity()).addResultListener(listener);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        List<ResultReceiverManager.ResultListener> listeners;
        if ((listeners = getResultListeners()) != null && !listeners.isEmpty()) {
            for (ResultReceiverManager.IResultListener listener : listeners) {
                WeatherApplication.getResultReceiverManager(getActivity()).removeResultListener(listener);
            }
        }
    }

    @UiThread(delay = 10L)
    protected void hideKeyboard() {
        try {
            Activity activity = getActivity();
            if (activity != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
            }
        } catch (NullPointerException e) { // should not happen
            e.printStackTrace();
        }
    }

    protected List<ResultReceiverManager.ResultListener> getResultListeners() {
        return Collections.EMPTY_LIST;
    }

    public void showProgressOverlay(boolean visibility) {
        progressVisible = visibility;
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showProgressOverlay(visibility);
        }
    }

    public void switchTo(Activity activity, Fragment fragment) {
        ((BaseActivity) activity).switchTo(fragment);
    }

    public void switchTo(Activity activity, Fragment fragment, boolean addToBackStack, String ftag) {
        ((BaseActivity) activity).switchTo(fragment, addToBackStack, ftag);
    }
}
