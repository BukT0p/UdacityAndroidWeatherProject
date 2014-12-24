package com.dataart.vyakunin.coubplayer.service;

import android.content.Context;

import com.dataart.vyakunin.coubplayer.BuildConfig;
import com.dataart.vyakunin.coubplayer.util.IOUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;


public abstract class RetrofittedCommand extends Command {

    private static final String ENDPOINT = "http://coub.com/api/v2/";
    private static final String RESULT = "Result";
    private final static String UTF8 = "UTF-8";
    private final static String MIME_TYPE = "application/json; charset=UTF-8";

    protected JSONObject getResultObject(final JSONObject json) throws JSONException {
        return json.getJSONObject(RESULT);
    }

    protected JSONArray getResultArray(final JSONObject json) throws JSONException {
        return json.getJSONArray(RESULT);
    }

    protected boolean getBooleanFromResult(Response response) {
        TypedInput body = response.getBody();
        byte[] bodyBytes = ((TypedByteArray) body).getBytes();
        String bodyCharset = MimeUtil.parseCharset(body.mimeType(), UTF8);
        String s = null;
        try {
            s = new String(bodyBytes, bodyCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ("true".equals(s));
    }

    protected String getStringFromResult(Response response) {
        TypedInput body = response.getBody();
        byte[] bodyBytes = ((TypedByteArray) body).getBytes();
        String bodyCharset = MimeUtil.parseCharset(body.mimeType(), UTF8);
        String s = null;
        try {
            s = new String(bodyBytes, bodyCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private static WrappedTargetApi service = null;

    protected interface TargetApi {
        @GET("/categories")
        JSONArray getCategories();

        @GET("/timeline/explore")
        JSONObject getAll(@Query("page") int page, @Query("per_page") int perPage);

        @GET("/timeline/explore/{permalink}")
        JSONObject getAllForCategory(@Query("page") int page, @Query("per_page") int perPage);

        @GET("/search")
        JSONObject getSearchResults(@Query("q") String query, @Query("page") int page, @Query("per_page") int perPage);

        //  TODO:
        //  * Add Authorization
        //  * Add ratings
    }


    protected static class WrappedTargetApi {

        private static final String TOKEN = "Token";
        private final TargetApi api;
        private final Context context;
        private final String endPoint;
        private final OrgJsonConverter converter;

        public WrappedTargetApi(final String endPoint, final TargetApi api, OrgJsonConverter converter, final Context context) {
            this.api = api;
            this.context = context;
            this.endPoint = endPoint;
            this.converter = converter;
        }

        public JSONArray getCategories() {
            return api.getCategories();
        }

        public JSONObject getCoubs(String permalink, int page, int perPage) {
            return permalink.isEmpty() ? api.getAll(page, perPage) : api.getAllForCategory(page, perPage);
        }
    }


    protected WrappedTargetApi getApi() {
        String endPoint = ENDPOINT;

        if (null == service || !endPoint.equals(service.endPoint)) {
            synchronized (RetrofittedCommand.class) {
                if (null == service || !endPoint.equals(service.endPoint)) {
                    OrgJsonConverter converter = new OrgJsonConverter();
                    service = new WrappedTargetApi(
                            endPoint,
                            new RestAdapter.Builder()
                                    .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
                                    .setEndpoint(endPoint)
                                    .setConverter(converter)
                                    .build()
                                    .create(TargetApi.class),
                            converter,
                            context
                    );
                }
            }
        }
        return service;
    }

    public static enum StatusCode {
        UNKNOWN(-1);

        public final int code;

        public static final String EXT_CODE = StatusCode.class.getCanonicalName().concat("ext_status_code");

        private StatusCode(final int code) {
            this.code = code;
        }

        public static StatusCode fromCodeValue(String responseCode) {
            int code = IOUtil.parseInt(responseCode, -1);
            for (StatusCode value : StatusCode.values()) {
                if (value.code == code) {
                    return value;
                }
            }

            return UNKNOWN;
        }

        public int getMessage() {
            return 0; // todo implement messages
        }
    }

    /*
     * Copyright (C) 2013 Random Android Code Snippets
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
    private static class OrgJsonConverter implements Converter {
        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            final String charset;
            final String mimeType = body.mimeType();
            if (null != mimeType) {
                charset = MimeUtil.parseCharset(mimeType, UTF8);
            } else {
                charset = UTF8;
            }
            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(body.in(), charset);
                final StringBuilder s = new StringBuilder(-1 != body.length() ? (int) body.length() : 1024);
                final char[] buf = new char[1024];
                int len;
                while ((-1) != (len = reader.read(buf))) {
                    s.append(buf, 0, len);
                }
                return new JSONTokener(s.toString()).nextValue();
            } catch (JSONException | IOException e) {
                throw new ConversionException(e);
            } finally {
                IOUtil.closeQuietly(reader);
            }
        }

        @Override
        public TypedOutput toBody(Object object) {
            final TypedOutput out;
            try {
                if (object instanceof JSONObject || object instanceof JSONArray) {
                    out = new TypedByteArray(MIME_TYPE, object.toString().getBytes(UTF8));
                } else if (null == object) {
                    out = new TypedByteArray(MIME_TYPE, "null".getBytes(UTF8));
                } else {
                    throw new IllegalArgumentException("Expected JSONObject or JSONArray but had " + object.getClass().getName());
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Should not happen, no " + UTF8, e);
            }
            return out;
        }
    }
}
// EOF
