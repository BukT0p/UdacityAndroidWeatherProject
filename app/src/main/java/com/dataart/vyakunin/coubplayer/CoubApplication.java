package com.dataart.vyakunin.coubplayer;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.dataart.vyakunin.coubplayer.service.messaging.ResultReceiverManager;

public class CoubApplication extends Application {
    private ResultReceiverManager resultReceiverManager;

    @Override
    public void onCreate() {
        super.onCreate();
        resultReceiverManager = new ResultReceiverManager(new Handler());
    }

    public static CoubApplication get(Context context) {
        return (CoubApplication) context.getApplicationContext();
    }

    public static ResultReceiverManager getResultReceiverManager(Context context) {
        return CoubApplication.get(context).getResultReceiverManager();
    }

    private ResultReceiverManager getResultReceiverManager() {
        return resultReceiverManager;
    }

}
