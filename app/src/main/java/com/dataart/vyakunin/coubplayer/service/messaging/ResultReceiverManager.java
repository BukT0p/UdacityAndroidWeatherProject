package com.dataart.vyakunin.coubplayer.service.messaging;


/*
 * Copyright (C) 2011,2013 Random Android Code Snippets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ResultReceiverManager extends ResultReceiver {

    /*public final static int MASK_FLAGS = 0x0000000F;
    private final static int MASK_CODE = ~MASK_FLAGS;*/

    private final static int QUEUE_MAX_SIZE = 4;

    public final static int FLAG_DEFERRED = 0x00000001;
    public final static int FLAG_EXCEPTION = 0x10000000;

    public static interface IResultListener {
        /**
         * Process result message, return <code>true</code> if message was handled, return
         * <code>false</code> otherwise. Note: when <code>false</code> returned message will
         * be placed in the internal queue.
         *
         * @param resultCode
         * @param resultData
         * @return
         */
        boolean onResult(int resultCode, Bundle resultData);

        boolean onError(int resultCode, Bundle resultData);
    }

    public static abstract class ResultListener implements IResultListener {

        private final Set<Integer> SUPPORT_CODES;

        public ResultListener(int... codes) {
            SUPPORT_CODES = new HashSet<>();
            for(int c : codes) {
                SUPPORT_CODES.add(c);
            }
        }

        @Override
        public boolean onResult(int resultCode, Bundle resultData) {
            if (SUPPORT_CODES.contains(resultCode)){
                onSuccess(resultData);
                return true;
            }
            return false;
        }

        @Override
        public boolean onError(int resultCode, Bundle resultData) {
            if (SUPPORT_CODES.contains(resultCode)){
                onError(resultData);
                return true;
            }
            return false;
        }

        protected abstract void onSuccess(Bundle resultData);

        protected abstract void onError(Bundle resultData);
    }

    public final static ResultListener[] EMPTY = new ResultListener[0];

    private final CopyOnWriteArraySet<IResultListener> listeners = new CopyOnWriteArraySet<IResultListener>();
    private final LinkedList<ResultEntry> queue = new LinkedList<ResultEntry>();

    private final static class ResultEntry {
        public final int resultCode;

        public final Bundle resultData;

        private ResultEntry(final int resultCode, final Bundle resultData) {
            this.resultCode = resultCode;
            this.resultData = resultData;
        }

    }

    public ResultReceiverManager(Handler handler) {
        super(handler);
    }

    public void clear() {
        listeners.clear();
        queue.clear();
    }

    public boolean addResultListener(final IResultListener listener) {
        final boolean status = listeners.add(listener);
        if (queue.size() > 0) {
            deliverDeferred();
        }
        return status;
    }

    public boolean removeResultListener(final IResultListener listener) {
        return listeners.remove(listener);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            final boolean deferred = false;//(resultCode & FLAG_DEFERRED) > 0;
            if (listeners.size() > 0) {
                for (final IResultListener listener : listeners) {
                    if ((resultCode & FLAG_EXCEPTION) > 0) {
                        if (!listener.onError(resultCode ^ FLAG_EXCEPTION, resultData) && deferred) {
                            defer(resultCode, resultData);
                        }
                    } else if (!listener.onResult(resultCode, resultData) && deferred) {
                        defer(resultCode, resultData);
                    }
                }
            } else if (deferred) {
                defer(resultCode, resultData);
            }
        } else {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StringBuilder builder = new StringBuilder();
            for (StackTraceElement el : stack) {
                builder.append(el.toString()).append("\n");
            }
        }

    }

    private void defer(final int resultCode, final Bundle resultData) {
        queue.add(new ResultEntry(resultCode, resultData));
        while (queue.size() > QUEUE_MAX_SIZE) {
            queue.removeFirst();
        }
    }

    private void deliverDeferred() {
        ResultEntry entry;
        while (queue.size() > 0) {
            entry = queue.removeFirst();
            for (final IResultListener listener : listeners) {
                if ((entry.resultCode & FLAG_EXCEPTION) > 0) {
                    if (listener.onError(entry.resultCode ^ FLAG_EXCEPTION, entry.resultData)) {
                        queue.add(entry);
                    }
                } else if (!listener.onResult(entry.resultCode, entry.resultData)) {
                    queue.add(entry);
                }
            }
        }
    }
}
// EOF
