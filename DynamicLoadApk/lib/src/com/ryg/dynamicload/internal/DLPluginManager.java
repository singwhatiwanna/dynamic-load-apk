/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:田啸,宋思宇
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

package com.ryg.dynamicload.internal;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.DLBasePluginFragmentActivity;
import com.ryg.dynamicload.DLProxyActivity;
import com.ryg.dynamicload.DLProxyFragmentActivity;
import com.ryg.utils.DLConstants;
import com.ryg.utils.DLUtils;

import dalvik.system.DexClassLoader;

public class DLPluginManager {

    private static final String TAG = "DLPluginManager";

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} start
     * success
     */
    public static final int START_RESULT_SUCCESS = 0;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} package
     * not found
     */
    public static final int START_RESULT_NO_PKG = 1;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} class
     * not found
     */
    public static final int START_RESULT_NO_CLASS = 2;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} class
     * type error
     */
    public static final int START_RESULT_TYPE_ERROR = 3;

    private static volatile DLPluginManager sInstance;
    private Context mContext;
    private final HashMap<String, DLPluginPackage> mPackagesHolder = new HashMap<String, DLPluginPackage>();

    private int mFrom = DLConstants.FROM_INTERNAL;

    private DLPluginManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static DLPluginManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DLPluginManager.class) {
                if (sInstance == null) {
                    sInstance = new DLPluginManager(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Load a apk. Before start a plugin Activity, we should do this first.<br/>
     * NOTE : will only be called by host apk.
     * 
     * @param dexPath
     */
    public DLPluginPackage loadApk(String dexPath) {
        // when loadApk is called by host apk, we assume that plugin is invoked
        // by host.
        mFrom = DLConstants.FROM_EXTERNAL;

        PackageInfo packageInfo = mContext.getPackageManager().
                getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo == null)
            return null;

        final String packageName = packageInfo.packageName;
        synchronized (mPackagesHolder) {
            DLPluginPackage pluginPackage = mPackagesHolder.get(packageName);
            if (pluginPackage == null) {
                Log.d(TAG, "load apk, dexPath=" + dexPath);
                DexClassLoader dexClassLoader = createDexClassLoader(dexPath);
                AssetManager assetManager = createAssetManager(dexPath);
                Resources resources = createResources(assetManager);
                pluginPackage = new DLPluginPackage(packageName, dexPath,
                        dexClassLoader, assetManager, resources, packageInfo);
                mPackagesHolder.put(packageName, pluginPackage);
            }
            return pluginPackage;
        }
    }

    public void reLoadApk(String dexPath, String packageName) {
        synchronized (mPackagesHolder) {
            mPackagesHolder.remove(packageName);
        }
        loadApk(dexPath);
    }

    private DexClassLoader createDexClassLoader(String dexPath) {
        File dexOutputDir = mContext.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, null,
                mContext.getClassLoader());
        return loader;
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public DLPluginPackage peekPackage(String packageName) {
        synchronized (mPackagesHolder) {
            return mPackagesHolder.get(packageName);
        }
    }

    public DLPluginPackage getPackage(String packageName, String dexPath) {
        DLPluginPackage pluginPackage = peekPackage(packageName);
        if (pluginPackage == null) {
            loadApk(dexPath);
            pluginPackage = peekPackage(packageName);
        }
        return pluginPackage;
    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        return resources;
    }

    /**
     * {@link #startPluginActivity(Activity, DLIntent)}<br/>
     * NOTE: activity will run in DL process.
     */
    public void launchPluginActivity(DLIntent dlIntent) {
        Intent service = new Intent(mContext, DLIntentService.class);
        service.setAction(DLConstants.ACTION_LAUNCH_PLUGIN);
        service.setExtrasClassLoader(DLConfig.getConfig().mPluginClassLoader);
        service.putExtra(DLConstants.EXTRA_DLINTENT, dlIntent);
        mContext.startService(service);
    }

    /**
     * {@link #startPluginActivityForResult(Activity, DLIntent, int)}<br/>
     */
    public int startPluginActivity(Context context, DLIntent dlIntent) {
        return startPluginActivityForResult(context, dlIntent, -1);
    }

    /**
     * @param context
     * @param dlIntent
     * @param requestCode
     * @return One of below: {@link #START_RESULT_SUCCESS}
     *         {@link #START_RESULT_NO_PKG} {@link #START_RESULT_NO_CLASS}
     *         {@link #START_RESULT_TYPE_ERROR}
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public int startPluginActivityForResult(Context context, DLIntent dlIntent, int requestCode) {

        // 设置 Extras ClassLoader
        dlIntent.setExtrasClassLoader(DLConfig.getConfig().mPluginClassLoader);

        if (mFrom == DLConstants.FROM_INTERNAL) {
            dlIntent.setClassName(context, dlIntent.getPluginClass());
            performStartActivityForResult(context, dlIntent, requestCode);
            return DLPluginManager.START_RESULT_SUCCESS;
        }

        String packageName = dlIntent.getPluginPackage();
        if (packageName == null) {
            throw new NullPointerException("disallow null packageName.");
        }
        DLPluginPackage pluginPackage = null;
        synchronized (mPackagesHolder) {
            pluginPackage = mPackagesHolder.get(packageName);
        }
        if (pluginPackage == null) {
            return START_RESULT_NO_PKG;
        }

        DexClassLoader classLoader = pluginPackage.classLoader;
        String className = dlIntent.getPluginClass();
        className = (className == null ? pluginPackage.getDefaultActivity() : className);
        if (className.startsWith(".")) {
            className = packageName + className;
        }
        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return START_RESULT_NO_CLASS;
        }

        Class<? extends Activity> activityClass = null;
        if (DLBasePluginActivity.class.isAssignableFrom(clazz)) {
            activityClass = DLProxyActivity.class;
        } else if (DLBasePluginFragmentActivity.class.isAssignableFrom(clazz)) {
            activityClass = DLProxyFragmentActivity.class;
        } else {
            return START_RESULT_TYPE_ERROR;
        }

        Intent intent = new Intent();
        intent.setClass(mContext, activityClass);
        intent.putExtra(DLConstants.EXTRA_CLASS, className);
        intent.putExtra(DLConstants.EXTRA_PACKAGE, packageName);
        intent.putExtra(DLConstants.EXTRA_DEX_PATH, dlIntent.getDexPath());
        intent.setFlags(dlIntent.getFlags());

        // 将dlIntent中的参数设置到intent中
        if (dlIntent.getExtras() != null) {
            // 设置ClassLoader,避免出现通过Intent传递Parcelable参数时出现Class Not Found异常
            // dlIntent.setExtrasClassLoader(dlIntent.getPathClassLoader());
            intent.setExtrasClassLoader(DLConfig.getConfig().mPluginClassLoader);
            intent.putExtras(dlIntent.getExtras());
        }

        Log.d(TAG, "launch " + className);
        performStartActivityForResult(context, intent, requestCode);
        return START_RESULT_SUCCESS;
    }

    private void performStartActivityForResult(Context context, Intent intent, int requestCode) {
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }

    /**
     * verify plugin, return false means verify plugin failed,the plugin can not
     * be loaded.
     * 
     * @param context
     * @param pluginPath
     * @param packageInfo
     * @return true : plugin is verified, false otherwise.
     */
    public boolean verifyPlugin(String pluginPath, PackageInfo packageInfo) {
        DLPluginPackage pluginPackage = getPackage(packageInfo.packageName, pluginPath);
        if (pluginPackage == null) {
            Log.e(TAG, "should never be happened.");
            return false;
        }
        int loadedPluginVersionCode = pluginPackage.packageInfo.versionCode;
        int currentPluginVersionCode = packageInfo.versionCode;
        Log.d(TAG, "plugin versionCode, old:" + loadedPluginVersionCode
                + " new:" + currentPluginVersionCode);
        if (packageInfo.versionCode != loadedPluginVersionCode) {
            Log.d(TAG, "plugin versionCode unmatched, old:"
                    + loadedPluginVersionCode + " new:"
                    + currentPluginVersionCode);
            Log.d(TAG, "kill DL process,restart it then.");
            reLoadApk(pluginPath, pluginPackage.packageName);
            boolean success = DLUtils.killDLProcess(mContext);
            if (!success) {
                // TODO : handle it.
                Log.w(TAG, "DL process is still not dead, wait it to stop.");
                return false;
            }
        }

        return true;
    }

}
