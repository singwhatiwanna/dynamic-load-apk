package com.ryg.dynamicload.internal;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;

/**
 * A plugin apk. Activities in a same apk share a same AssetManager, Resources and DexClassLoader.
 * @author siyu.song
 *
 */
public class DLPluginPackage {
    public String packageName;
    public String path;
    
    public DexClassLoader loader;
    
    public AssetManager assetManager;
    
    public Resources resources;
    
    public PackageInfo packageInfo;
    
    private String mDefaultActivity;
    
    public DLPluginPackage(String packageName, String path, DexClassLoader loader, AssetManager assetManager,
            Resources resources, PackageInfo packageInfo) {
        super();
        this.packageName = packageName;
        this.path = path;
        this.loader = loader;
        this.assetManager = assetManager;
        this.resources = resources;
        this.packageInfo = packageInfo;
    }
    
    public String getDefaultActivity() {
        if (packageInfo.activities != null && packageInfo.activities.length > 0) {
            mDefaultActivity = packageInfo.activities[0].name;
        }
        return mDefaultActivity;
    }
}
