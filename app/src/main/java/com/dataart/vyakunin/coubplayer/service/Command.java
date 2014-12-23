package com.dataart.vyakunin.coubplayer.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import com.dataart.vyakunin.coubplayer.service.messaging.ResultReceiverManager;
import com.dataart.vyakunin.coubplayer.BuildConfig;
import com.dataart.vyakunin.coubplayer.service.messaging.StatusBroadcastReceiver;
import com.dataart.vyakunin.coubplayer.service.messaging.StatusMessageReceiver;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Command implements Runnable, Parcelable {

    protected final static String TAG = Command.class.getCanonicalName();

    protected Command() {
        if (BuildConfig.DEBUG) {
            startGuard = new Exception();
        }
    }

    private Exception startGuard = null;

    protected Context context;
    protected ResultReceiver receiver;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        if (BuildConfig.DEBUG && null != startGuard) {
            Log.w(TAG, "Finalizing not yet started command " + this
                    + " Forget to start() it?", startGuard);
        }
        super.finalize();
    }

    public final void execute(final Context context, final ResultReceiver receiver) {
        this.startGuard = null;
        this.context = context;
        this.receiver = receiver;
        try {
            run();
        } catch (Exception e) {
            Log.e("command exception", e.getMessage());
            sendError(RetrofittedCommand.StatusCode.UNKNOWN);
        } finally {
            this.context = null;
            this.receiver = null;
        }
    }

    protected void sendOk(final Bundle resultData) {
        sendResult(Command.getCode(this.getClass()), resultData);
    }

    protected void sendError(final Bundle resultData) {
        sendResult(Command.getCode(this.getClass()) | ResultReceiverManager.FLAG_EXCEPTION, resultData);
    }

    protected void sendError(final RetrofittedCommand.StatusCode code) {
        Bundle data = new Bundle();
        data.putString(RetrofittedCommand.StatusCode.EXT_CODE, code.toString());
        sendResult(Command.getCode(this.getClass()) | ResultReceiverManager.FLAG_EXCEPTION, data);
    }

    protected void sendResult(final int resultCode, final Bundle resultData) {
        if (null != receiver) {
            receiver.send(resultCode, resultData);
        }
    }

    protected void sendMessageBroadcast(Bundle message) {
        Intent intent = new Intent(StatusBroadcastReceiver.ACTION_MESSAGE);
        intent.putExtra(StatusBroadcastReceiver.EXTRA_DATA, message);
        context.sendBroadcast(intent);
    }

    protected void sendStatusMessage(int messageCode) {
        Bundle bundle = new Bundle();
        bundle.putString(StatusMessageReceiver.EXTRA_MESSAGE_TYPE, StatusMessageReceiver.TYPE_SINGLE);
        bundle.putInt(StatusMessageReceiver.EXTRA_MESSAGE, messageCode);
        sendMessageBroadcast(bundle);
    }

    public final void start(final Context context) {
        CommandService.start(context, this);
        startGuard = null;
    }

    private static final HashMap<Class, Integer> commandsCodes = new HashMap<>();
    private static final AtomicInteger codeGenerator = new AtomicInteger();

    public static synchronized int getCode(Class<? extends Command> clazz) {
        Integer code = commandsCodes.get(clazz);
        if (code == null) {
            code = codeGenerator.incrementAndGet();
            commandsCodes.put(clazz, code);
        }
        return code;
    }
}
