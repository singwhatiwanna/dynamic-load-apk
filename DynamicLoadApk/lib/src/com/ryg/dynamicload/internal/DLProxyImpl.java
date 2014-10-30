package com.ryg.dynamicload.internal;

import java.lang.reflect.Constructor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ryg.dynamicload.DLPlugin;
import com.ryg.utils.DLConstants;

public class DLProxyImpl {

    private static final String TAG = "DLProxyImpl";

    private String mClass;
    private String mPackageName;
    
    private DLPluginPackage mPluginPackage;
    private DLPluginManager mPluginManager;

    private AssetManager mAssetManager;
    private Resources mResources;
    private Theme mTheme;


    private ActivityInfo mActivityInfo;
    
    protected DLPlugin mRemoteActivity;
    
    private Activity mActivity;
    
    public DLProxyImpl(Activity activity) {
        mActivity = activity;
    }
    
    private void initializeActivityInfo() {
        PackageInfo packageInfo = mPluginPackage.packageInfo;
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
            if (mClass == null) {
                mClass = packageInfo.activities[0].name;
            }
            for (ActivityInfo a : packageInfo.activities) {
                if (a.name.equals(mClass)) {
                    mActivityInfo = a;
                }
            }
        }
    }

    private void handleActivityInfo() {
        Log.d(TAG, "handleActivityInfo, theme=" + mActivityInfo.theme);
        if (mActivityInfo.theme > 0) {
            mActivity.setTheme(mActivityInfo.theme);
        }
        Theme superTheme = mActivity.getTheme();
        mTheme = mResources.newTheme();
        mTheme.setTo(superTheme);

        // TODO: handle mActivityInfo.launchMode here.
    }

    public void onCreate(Intent intent) {
        mPackageName = intent.getStringExtra(DLConstants.EXTRA_PACKAGE);
        mClass = intent.getStringExtra(DLConstants.EXTRA_CLASS);
        Log.d(TAG, "mClass=" + mClass + " mPackageName=" + mPackageName);

        mPluginManager = DLPluginManager.getInstance(mActivity);
        mPluginPackage = mPluginManager.getPackage(mPackageName);
        mAssetManager = mPluginPackage.assetManager;
        mResources = mPluginPackage.resources;
        
        initializeActivityInfo();
        handleActivityInfo();
        launchTargetActivity();
    }
    
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void launchTargetActivity() {
        try {
            Class<?> localClass = getClassLoader().loadClass(mClass);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
            mRemoteActivity = (DLPlugin) instance;
            ((DLProxy) mActivity).attach(mRemoteActivity, mPluginManager);
            Log.d(TAG, "instance = " + instance);

            mRemoteActivity.attach(mActivity, mPluginPackage);

            Bundle bundle = new Bundle();
            bundle.putInt(DLConstants.FROM, DLConstants.FROM_EXTERNAL);
            mRemoteActivity.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ClassLoader getClassLoader() {
        return mPluginPackage.loader;
    }

    
    public AssetManager getAssets() {
        return mAssetManager;
    }

    public Resources getResources() {
        return mResources;
    }

    public Theme getTheme() {
        return mTheme;
    }
    
    public DLPlugin getRemoteActivity() {
        return mRemoteActivity;
    }
    
    public interface DLProxy {
        public void attach(DLPlugin remoteActivity, DLPluginManager pluginManager);
    }
}