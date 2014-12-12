package com.ryg.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.DLBasePluginFragmentActivity;

public class DLUtils {
    private static final String TAG = "DLUtils";

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            // should be something wrong with parse
            e.printStackTrace();
        }

        return pkgInfo;
    }

    public static Drawable getAppIcon(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        // Workaround for http://code.google.com/p/android/issues/detail?id=9151
        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }

        return pm.getApplicationIcon(appInfo);
    }

    public static CharSequence getAppLabel(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        // Workaround for http://code.google.com/p/android/issues/detail?id=9151
        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }

        return pm.getApplicationLabel(appInfo);
    }

    public static String getProxyViewAction(String className, ClassLoader classLoader) {
        int activityType = getActivityType(className, classLoader);
        return getProxyViewActionByActivityType(activityType);
    }

    public static String getProxyViewAction(Class<?> cls) {
        int activityType = getActivityType(cls);
        return getProxyViewActionByActivityType(activityType);
    }

    private static String getProxyViewActionByActivityType(int activityType) {
        String proxyViewAction = null;

        switch (activityType) {
        case DLConstants.ACTIVITY_TYPE_NORMAL: {
            proxyViewAction = DLConstants.PROXY_ACTIVITY_VIEW_ACTION;
            break;
        }
        case DLConstants.ACTIVITY_TYPE_FRAGMENT: {
            proxyViewAction = DLConstants.PROXY_FRAGMENT_ACTIVITY_VIEW_ACTION;
            break;
        }
        case DLConstants.ACTIVITY_TYPE_ACTIONBAR:
        case DLConstants.ACTIVITY_TYPE_UNKNOWN:
        default:
            break;
        }

        if (proxyViewAction == null) {
            Log.e(TAG, "unsupported activityType:" + activityType);
        }

        return proxyViewAction;
    }

    private static int getActivityType(String className, ClassLoader classLoader) {
        int activityType = DLConstants.ACTIVITY_TYPE_UNKNOWN;

        try {
            Class<?> cls = Class.forName(className, false, classLoader);
            activityType = getActivityType(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return activityType;
    }

    private static int getActivityType(Class<?> cls) {
        int activityType = DLConstants.ACTIVITY_TYPE_UNKNOWN;

        try {
            if (cls.asSubclass(DLBasePluginActivity.class) != null) {
                activityType = DLConstants.ACTIVITY_TYPE_NORMAL;
                return activityType;
            }
        } catch (ClassCastException e) {
            // ignored
        }

        try {
            if (cls.asSubclass(DLBasePluginFragmentActivity.class) != null) {
                activityType = DLConstants.ACTIVITY_TYPE_FRAGMENT;
                return activityType;
            }
        } catch (ClassCastException e) {
            // ignored
        }

        //TODO: handle other activity types, ActionbarActivity,eg.
        return activityType;
    }
  /**
   * 获取CPU名称，根据名称，解析相应平台下的so库
   * @return
   *      ARM、ARMV7、X86、MIPS
   */
  public static String getCpuName() {
    try {
      FileReader fr = new FileReader("/proc/cpuinfo");
      BufferedReader br = new BufferedReader(fr);
      String text = br.readLine();
      String[] array = text.split(":\\s+", 2);
      for (int i = 0; i < array.length; i++) {
      }
      return array[1];
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

    public static void showDialog(Activity activity, String title, String message) {
        new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                .setPositiveButton("确定", null).show();
    }
}
