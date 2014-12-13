package com.ryg.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * persistence data
 * 
 * @author yuanzeyao <br/>
 *         create at 2014年12月11日 下午5:55:59
 */
public class DLConfigs {
    public static final String PREFERENCE_NAME = "dynamic_load_configs";

    private static final String KEY_DEFAULT = "key_default";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setSoLastModifiedTime(Context cxt, String soName, long time) {
        SharedPreferences prefs = cxt.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        prefs.edit().putLong(soName, time).apply();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static long getSoLastModifiedTime(Context cxt, String soName) {
        SharedPreferences prefs = cxt.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        return prefs.getLong(soName, 0);
    }

}
