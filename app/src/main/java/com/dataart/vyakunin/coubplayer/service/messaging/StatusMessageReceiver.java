package com.dataart.vyakunin.coubplayer.service.messaging;

import android.content.Context;
import android.os.Bundle;

public abstract class StatusMessageReceiver implements IStatusMessageReceiver {

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_MESSAGE_TYPE = "message_type";
    public static final String TYPE_SINGLE = "message_type_single";

    private final Context context;

    public StatusMessageReceiver(Context context) {
        this.context = context;
    }

    @Override
    public String getAction() {
        return StatusBroadcastReceiver.ACTION_MESSAGE;
    }

    @Override
    public void onReceive(Bundle data) {

    }

    public abstract void onMessage(String message, int progress);
}
