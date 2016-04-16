package com.example.wlbreath.zhihudaily.utils;

/**
 * Created by wlbreath on 16/3/10.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wlbreath on 16/3/3.
 */
public class JsonUtils {
    private final String TAG = "JsonUtils";

    private static JsonUtils jsonUtils;
    private final Context context;
    private SharedPreferences sp;

    private JsonUtils(Context context) {
        this.context = context;
        sp = SPHelper.getSp(context);
    }

    public static JsonUtils getJsonUtils(Context context) {
        if (jsonUtils == null) {
            return new JsonUtils(context);
        }
        return jsonUtils;
    }

    public void loadJson(final String urlStr, final String method, final JsonUtils.CallBack callBack) {
        Log.d(TAG, "start load json: " + urlStr);
        String cachedJson = getCacheJson(urlStr);
        // 使用缓存的json数据
        if (cachedJson != null) {
            Log.d(TAG, "load json from cache: " + urlStr);
            if (callBack != null) {
                callBack.success(cachedJson);
            }
            return;
        }

        // load json from internet
        loadJsonFromNetwork(urlStr, method, callBack);
    }

    public void loadJsonFromNetwork(final String urlStr, final String method, final JsonUtils.CallBack callBack) {
        if (!NetworkUtils.isOnline(context)) {
            if(callBack != null){
                callBack.fail(new Exception("loadJsonFromNetwork: offline can not load json from internet: " + urlStr));
            }
            Log.e(TAG, "loadJsonFromNetwork: offline can not load json from internet: " + urlStr);
            return;
        }

        Log.d(TAG, "start load json from internet: " + urlStr);
        NetworkUtils.loadUrl(urlStr, method, new NetworkUtils.CallBack() {
            @Override
            public void success(InputStream inputStream) {
                Log.d(TAG, "load json from internet is success: " + urlStr);

                inputStream = new BufferedInputStream(inputStream);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                try {
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                String json = outputStream.toString();
                saveCacheJson(urlStr, json);

                if (callBack != null) {
                    callBack.success(json);
                }
            }

            @Override
            public void fail(Exception e) {
                Log.e(TAG, e.getMessage());

                if(callBack != null){
                    callBack.fail(e);
                }
            }

            @Override
            public void timeout() {
                if(callBack != null){
                    callBack.fail(new Exception("load josn timeout:" + urlStr));
                }
            }
        });
    }

    public void loadJsonFromCache(String urlStr, JsonUtils.CallBack callBack) {
        String json = getCacheJson(urlStr);

        if (callBack != null) {
            callBack.success(json);

        } else {
            callBack.fail(new Exception("json not cached: " + urlStr));
        }
    }

    public String loadJsonFromCache(String urlStr) {
        return getCacheJson(urlStr);
    }

    public boolean isInCache(String urlStr) {
        return getCacheJson(urlStr) != null;
    }


    public interface CallBack {
        public void success(String json);

        public void fail(Exception e);

        public void timeout();
    }

    private void saveCacheJson(String urlStr, String json) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MD5Encoder.encode(urlStr), json);
        editor.commit();
    }

    private String getCacheJson(String urlStr) {
        return sp.getString(MD5Encoder.encode(urlStr), null);
    }
}
