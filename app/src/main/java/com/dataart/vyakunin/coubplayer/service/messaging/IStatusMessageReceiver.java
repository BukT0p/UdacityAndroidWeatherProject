package com.dataart.vyakunin.coubplayer.service.messaging;

import android.os.Bundle;

public interface IStatusMessageReceiver {
    public String getAction();

    public void onReceive(Bundle data);
}
