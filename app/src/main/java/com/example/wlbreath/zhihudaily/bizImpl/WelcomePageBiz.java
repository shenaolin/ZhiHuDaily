package com.example.wlbreath.zhihudaily.bizImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.bean.WelcomePageBean;
import com.example.wlbreath.zhihudaily.biz.IWelcomePageBiz;
import com.example.wlbreath.zhihudaily.utils.ImageUtils;
import com.example.wlbreath.zhihudaily.utils.JsonUtils;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.google.gson.Gson;

/**
 * Created by wlbreath on 16/4/7.
 */
public class WelcomePageBiz implements IWelcomePageBiz {
    private final String TAG = "WelcomePageBiz";

    private Context context;

    public WelcomePageBiz(Context context) {
        this.context = context;
    }

    public WelcomePageBean getWelcomePageBean() {
        String jsonUrl = getJsonUrl();
        JsonUtils jsonUtils = getJsonUtils();
        ImageUtils imageUtils = getImageUtils();

        WelcomePageJsonBean jsonBean = null;
        if (jsonUtils.isInCache(jsonUrl)) {
            jsonBean = parseJson(jsonUtils.loadJsonFromCache(jsonUrl));
        }

        String title = null;
        Bitmap background = null;

        if (jsonBean != null && jsonBean.getImg() != null &&
                (imageUtils.isImageCachedInCache(jsonBean.getImg())
                        || imageUtils.isImageCachedInDiskCache(jsonBean.getImg()))) {

            title = jsonBean.getText();
            if (imageUtils.isImageCachedInCache(jsonBean.getImg())) {
                background = imageUtils.loadImageFromCache(jsonBean.getImg());

            } else if (imageUtils.isImageCachedInDiskCache(jsonBean.getImg())) {
                background = imageUtils.loadImageFromDiskDiskCache(jsonBean.getImg());
            }
        } else {
            title = context.getResources().getString(R.string.welcome_activity_title);
            background = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_welcome);
        }

        return new WelcomePageBean(title, background);
    }

    @Override
    public void updateCachedWelcomePageInfo() {
        if (!NetworkUtils.isOnline(context)) {
            Log.w(TAG, "offline: can not update welcome page info");
            return;
        }

        getJsonUtils().loadJsonFromNetwork(getJsonUrl(), "GET", new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                String imgUrl = parseJson(json).getImg();
                if (imgUrl != null && !getImageUtils().isImageCachedInDiskCache(imgUrl)) {
                    Log.d(TAG, "load welcome page backgroud from internet");
                    getImageUtils().loadImageFromUrl(imgUrl, "GET", null);
                }
            }

            @Override
            public void fail(Exception e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void timeout() {
            }
        });
    }

    public String getJsonUrl() {
        return context.getResources().getString(R.string.welcome_page_json_url);
    }

    public WelcomePageJsonBean parseJson(String json) {
        return new Gson().fromJson(json, WelcomePageJsonBean.class);
    }

    private JsonUtils getJsonUtils() {
        return JsonUtils.getJsonUtils(context);
    }

    private ImageUtils getImageUtils() {
        return ImageUtils.getImageUtils(context);
    }

    private class WelcomePageJsonBean {
        private String img;
        private String text;

        public WelcomePageJsonBean() {
        }

        public WelcomePageJsonBean(String img, String text) {
            this.img = img;
            this.text = text;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "WelcomePageJsonBean{" +
                    "img='" + img + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }
}
