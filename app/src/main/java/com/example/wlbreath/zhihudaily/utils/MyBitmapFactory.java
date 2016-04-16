package com.example.wlbreath.zhihudaily.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wlbreath on 16/3/3.
 */
public class MyBitmapFactory {
    private MyBitmapFactory(){
    }

    public static Bitmap decodeResource(Resources res, int id, int targetWidth, int targetHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,id, options);

        int height = options.outHeight;
        int width = options.outWidth;

        options.inSampleSize = sampleSize(width, height, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, id, options);
    }

    public static Bitmap decodeFile(String filename, int targetWidth, int targetHeight ){
        Bitmap bitmap = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(filename);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in);

            int height = options.outHeight;
            int width = options.outWidth;

            options.inSampleSize = sampleSize(width, height, targetWidth, targetHeight);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    private static int sampleSize(int width, int height, int targetWidth, int targetHeight){
        int inSampleSize = 1;

        if (height > targetWidth || width > targetHeight) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > targetHeight
                    && (halfWidth / inSampleSize) > targetWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
