package com.dataart.vyakunin.coubplayer.service.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

public class StatusBroadcastReceiver extends BroadcastReceiver {

    private Multimap<String, IStatusMessageReceiver> messageReceivers;
    private final IntentFilter filter;
    private final Context context;

    public static final String EXTRA_DATA = "EXTRA_DATA";

    public static final String ACTION_MESSAGE = StatusBroadcastReceiver.class.getCanonicalName().concat(".ACTION_MESSAGE");

    public StatusBroadcastReceiver(Context context) {
       messageReceivers = HashMultimap.create();
       filter = new IntentFilter();
       filter.addAction(ACTION_MESSAGE);
       context.registerReceiver(this, filter);
       this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Collection<IStatusMessageReceiver> callbacks = messageReceivers.get(intent.getAction());
        if(callbacks != null && callbacks.size() > 0)
        for(IStatusMessageReceiver c: callbacks) {
            c.onReceive(intent.getBundleExtra(EXTRA_DATA));
        }
    }

    public void addReceiver(IStatusMessageReceiver receiver) {
        messageReceivers.put(receiver.getAction(), receiver);
    }

    public void removeReceiver(IStatusMessageReceiver receiver) {
        messageReceivers.remove(receiver.getAction(), receiver);
    }


    public void destroy() {
        context.unregisterReceiver(this);
    }

    public static StatusBroadcastReceiver newInstance(Context context) {
        return new StatusBroadcastReceiver(context);
    }
}
