package com.dataart.vyakunin.udacityandroidweatherproject.util;

/*
 * Copyright (C) 2011, 2012 Random Android Code Snippets
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

import android.text.TextUtils;
import android.util.Log;


import java.io.Closeable;

public final class IOUtil {

    private IOUtil() {
    }

    public static void closeQuietly(final Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static void closeQuietly(final Closeable... closeables) {
        for (final Closeable closeable : closeables) {
            closeQuietly(closeable);
        }
    }

    public static int parseInt(String text, int def) {
        if (TextUtils.isEmpty(text))
            return def;
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            Log.e("Error while int parsing", e.getMessage());
        }

        return def;
    }

    public static double parseDouble(String text, double def) {
        if (TextUtils.isEmpty(text))
            return def;
        try {
            return Double.parseDouble(text);
        } catch (Exception ignore) {
        }

        return def;
    }
}
// EOF
