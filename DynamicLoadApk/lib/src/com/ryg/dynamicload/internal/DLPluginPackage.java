package com.ryg.dynamicload.internal;

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
    
    public DLPluginPackage(String packageName, String path, DexClassLoader loader, AssetManager assetManager,
            Resources resources) {
        super();
        this.packageName = packageName;
        this.path = path;
        this.loader = loader;
        this.assetManager = assetManager;
        this.resources = resources;
    }
}
