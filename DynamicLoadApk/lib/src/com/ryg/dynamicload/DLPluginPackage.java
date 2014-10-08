package com.ryg.dynamicload;

import android.content.res.AssetManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;

/**
 * A plugin apk. Activities in a same apk share a same AssetManager, Resources and DexClassLoader.
 * @author siyu.song
 *
 */
public class DLPluginPackage {
    private String packageName;
    private String path;
    
    private DexClassLoader loader;
    
    private AssetManager assetManager;
    
    private Resources resources;
    
    public DLPluginPackage(String packageName, String path, DexClassLoader loader, AssetManager assetManager,
            Resources resources) {
        super();
        this.packageName = packageName;
        this.path = path;
        this.loader = loader;
        this.assetManager = assetManager;
        this.resources = resources;
    }

    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public DexClassLoader getLoader() {
        return loader;
    }

    public void setLoader(DexClassLoader loader) {
        this.loader = loader;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }
}
