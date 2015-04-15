package com.ryg.utils;

import android.os.Debug;
import android.util.Log;

public class LOG {
    
    public static void e(String tag, String log) {
        if (DLConfigs.LOG) {
	        Log.e(tag, log);
        }
    }

    public static void d(String tag, String log) {
        if (DLConfigs.LOG) {
	        Log.d(tag, log);
        }
    }
    
    public static void w(String tag, String log) {
        if (DLConfigs.LOG) {
	        Log.w(tag, log);
        }
    }
    
    public static void i(String tag, String log) {
        if (DLConfigs.LOG) {
	        Log.i(tag, log);
        }
    }
}
