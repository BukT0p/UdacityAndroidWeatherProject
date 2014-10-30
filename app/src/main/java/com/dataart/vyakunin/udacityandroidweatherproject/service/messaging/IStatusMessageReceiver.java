package com.dataart.vyakunin.udacityandroidweatherproject.service.messaging;

import android.os.Bundle;

public interface IStatusMessageReceiver {
    public String getAction();

    public void onReceive(Bundle data);
}
