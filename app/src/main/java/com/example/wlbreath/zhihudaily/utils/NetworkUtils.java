package com.example.wlbreath.zhihudaily.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wlbreath on 16/3/3.
 */
public class NetworkUtils {
    private static final int THREAD_POOL_NUMBER = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_NUMBER);

    private NetworkUtils() {
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void loadUrl(final String urlStr,
                               final String method, final NetworkUtils.CallBack callbak) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;

                InputStream in = null;
                try {
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(method);
                    conn.setDoInput(true);
                    conn.connect();

                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);

                    if ((conn.getResponseCode() >= 200
                            && conn.getResponseCode() < 300) || conn.getResponseCode() == 304) {

                        in = conn.getInputStream();

                        if (callbak != null) {
                            callbak.success(in);
                        }

                    } else {
                        String info = "responseCode: " + conn.getResponseCode();
                        info += "\r\n message: " + conn.getResponseMessage();

                        if (callbak != null) {
                            callbak.fail(new Exception(info));
                        }

                    }

                } catch (SocketTimeoutException e) {
                    if (callbak != null) {
                        callbak.fail(e);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    System.out.println("wanglei is cool and houna is cute");
                    if (callbak != null) {
                        callbak.fail(e);
                    }

                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    public interface CallBack {
        public void success(InputStream inputStream);

        public void fail(Exception e);

        public void timeout();
    }
}
