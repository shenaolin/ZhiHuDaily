package com.example.wlbreath.zhihudaily.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wlbreath on 16/3/6.
 */
public class SPHelper {
    private static final String SP_NAME = "zhihu_daily_sp";

    private static SharedPreferences sp;

    public static SharedPreferences getSp(Context context){
        if(sp == null){
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }
}
