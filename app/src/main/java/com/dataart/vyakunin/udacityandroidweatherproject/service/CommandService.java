package com.dataart.vyakunin.udacityandroidweatherproject.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import com.dataart.vyakunin.udacityandroidweatherproject.WeatherApplication;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vyakunin on 10/30/2014.
 */
public class CommandService extends Service {
    private final static String TAG = CommandService.class.getSimpleName();

    private final static String NAMESPACE = CommandService.class.getName();

    private final static String EXTRA_COMMAND = NAMESPACE.concat(".EXTRA_COMMAND");
    private final static String EXTRA_RECEIVER = NAMESPACE.concat(".EXTRA_RECEIVER");

    /* package */ final static void start(final Context context, final Command command) {
        context.startService(
                new Intent(context, CommandService.class)
                        .putExtra(EXTRA_COMMAND, command)
                        .putExtra(EXTRA_RECEIVER,
                                WeatherApplication.getResultReceiverManager(
                                        context.getApplicationContext()))
        );
    }

    private final ConcurrentLinkedQueue<Integer> startIdQueue =
            new ConcurrentLinkedQueue<Integer>();

    private final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            4, 6, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private final SerialExecutor SERIAL_EXECUTOR = new SerialExecutor();

    private class SerialExecutor implements Executor {
        private final LinkedList<Runnable> tasks = new LinkedList<Runnable>();
        private Runnable activeTask;

        @Override
        public synchronized void execute(final Runnable r) {
            tasks.offer(new Runnable() {
                @Override
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (activeTask == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            activeTask = tasks.poll();
            if (activeTask != null) {
                THREAD_POOL_EXECUTOR.execute(activeTask);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        THREAD_POOL_EXECUTOR.shutdownNow();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        startIdQueue.add(startId);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    handleIntent(intent);
                } finally {
                    stopSelf(startIdQueue.remove());
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(r);
        return START_NOT_STICKY;
    }

    protected void handleIntent(final Intent intent) {
        final Command command = intent.getParcelableExtra(EXTRA_COMMAND);
        if (null != command) {
            try {
                command.execute(this,
                        intent.<ResultReceiver>getParcelableExtra(EXTRA_RECEIVER));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
