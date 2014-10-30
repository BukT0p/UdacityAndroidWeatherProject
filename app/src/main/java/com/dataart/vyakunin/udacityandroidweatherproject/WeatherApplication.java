package com.dataart.vyakunin.udacityandroidweatherproject;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.dataart.vyakunin.udacityandroidweatherproject.service.messaging.ResultReceiverManager;


public class WeatherApplication extends Application {
    private ResultReceiverManager resultReceiverManager;

    @Override
    public void onCreate() {
        super.onCreate();
        resultReceiverManager = new ResultReceiverManager(new Handler());
    }

    public static WeatherApplication get(Context context) {
        return (WeatherApplication) context.getApplicationContext();
    }

    public static ResultReceiverManager getResultReceiverManager(Context context) {
        return WeatherApplication.get(context).getResultReceiverManager();
    }

    private ResultReceiverManager getResultReceiverManager() {
        return resultReceiverManager;
    }

}
