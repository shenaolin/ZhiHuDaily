package com.example.wlbreath.zhihudaily.utils;

import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wlbreath on 16/3/3.
 */
public class ImageUtils {
    private static final String DIR_NAME = "zhihudaily_cache";

    private static LruCache<String, Bitmap> lruCache;
    private static Context context;
    private static ImageUtils utils;

    private ImageUtils(Context context) {
        this.context = context;

        final int freeSize = (int) (Runtime.getRuntime().freeMemory() / 8);
        final int cacheSize = freeSize / 1024;

        this.lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static ImageUtils getImageUtils(Context context) {
        if (utils == null) {
            utils = new ImageUtils(context);
        }

        return utils;
    }

    public boolean isImageCachedInCache(String imagePath) {
        return loadImageFromCache(imagePath) != null;
    }

    public boolean isImageCachedInDiskCache(String imageParh) {
        File dir = new File(getCachedDir());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, MD5Encoder.encode(imageParh)).exists();
    }

    public void loadImageFromUrl(final String imagePath, final String method, final ImageUtils.Callback callback) {
        if (!NetworkUtils.isOnline(context)) {
            Log.d("zhihudaily", "offline, can not download image: " + imagePath);
            return;
        }

        Log.d("zhihudaily", "start load image: " + imagePath + " from network");
        //从网络中下载对应的图片
        NetworkUtils.loadUrl(imagePath, method, new NetworkUtils.CallBack() {
            @Override
            public void success(InputStream inputStream) {
                Log.d("zhihudaily", "load image: " + imagePath + " from network");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                saveImageToCache(bitmap, imagePath);
                saveImageToDiskCache(bitmap, imagePath);

                if (callback != null) {
                    callback.success(bitmap);
                }
            }

            @Override

            public void fail(Exception e) {
                callback.fail(e);
            }

            @Override
            public void timeout() {
                callback.timeout();
            }
        });
    }

    public void loadImage(final String imagePath, final String method, final ImageUtils.Callback callback) {
        //查看图片是否在内存缓存中
        Bitmap bitmap = loadImageFromCache(imagePath);
        if (bitmap != null && callback != null) {
            Log.d("zhihudaily", "load image: " + imagePath + " from lruCache");
            callback.success(bitmap);
            return;
        }

        //查看图片是否缓存在磁盘中
        if (isImageCachedInDiskCache(imagePath) && callback != null) {
            Log.d("zhihudaily", "load image: " + imagePath + " from disk");
            bitmap = loadImageFromDiskDiskCache(imagePath);
            callback.success(bitmap);
            return;
        }

        loadImageFromUrl(imagePath, method, callback);
    }

    public void saveImageToCache(Bitmap bitmap, String imagePath) {
        lruCache.put(MD5Encoder.encode(imagePath), bitmap);
    }

    public Bitmap loadImageFromCache(String imagePath) {
        return lruCache.get(MD5Encoder.encode(imagePath));
    }

    public void saveImageToDiskCache(Bitmap bitmap, String imagePath) {
        saveBitmap(bitmap, imagePath);
    }

    public Bitmap loadImageFromDiskDiskCache(String imagePath) {
        return getBitmap(imagePath);
    }

    private String getCachedDir() {
        String path = "";

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || Environment.isExternalStorageRemovable()) {
            path = Environment.getExternalStorageDirectory().getPath() + "/" + DIR_NAME;
        } else {
            path = context.getCacheDir().getPath();
        }
        return path;
    }

    private void saveBitmap(Bitmap bitmap, String filename) {
        File dir = new File(getCachedDir());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, MD5Encoder.encode(filename));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("112255", "12222" + e.getMessage());
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getBitmap(String filename) {
        return BitmapFactory.decodeFile(getCachedDir() + "/" + MD5Encoder.encode(filename));
    }

    public interface Callback {
        public void success(Bitmap bitmap);

        public void fail(Exception e);

        public void timeout();
    }
}
