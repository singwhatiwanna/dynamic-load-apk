package com.ryg.dynamicload.internal;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.DLBasePluginFragmentActivity;
import com.ryg.dynamicload.DLProxyActivity;
import com.ryg.dynamicload.DLProxyFragmentActivity;
import com.ryg.utils.DLConstants;

import dalvik.system.DexClassLoader;

public class DLPluginManager implements DLContext {
    
    private static final String TAG = "PluginManager";
	
	DLPluginManager() {
	    
	}
	
	private static DLPluginManager sInstance;

	public static synchronized DLPluginManager getInstance() {
	    if (sInstance == null) {
	        sInstance = new DLPluginManager();
	    }
	    return sInstance;
	}
	
	private final HashMap<String, DLPluginPackage> packageHolder = new HashMap<String, DLPluginPackage>();

	/**
	 * Load a apk. Before start a plugin Activity, we should do this first.
	 * @param context
	 * @param dexPath
	 * @throws PluginNotFoundException
	 */
	public void loadApk(Context context, String dexPath) throws PluginException {
		PackageInfo packageInfo = context.getPackageManager().
				getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
		if (packageInfo == null) 
			throw new PluginException(dexPath + " not found");
		final String packageName = packageInfo.packageName;
		
		DLPluginPackage pluginPackage = packageHolder.get(packageName);
		
		if (pluginPackage == null) {
		    DexClassLoader dexClassLoader = createDexClassLoader(context, dexPath);
		    AssetManager assetManager = createAssetManager(dexPath);
		    Resources resources = createResources(context, assetManager);
		    pluginPackage = new DLPluginPackage(packageName, dexPath, dexClassLoader, assetManager, resources, packageInfo);
		    packageHolder.put(packageName, pluginPackage);
		}
	}
	
	private DexClassLoader createDexClassLoader(Context context, String dexPath) {
	    File dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, null, context.getClassLoader());
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
	
	public DLPluginPackage getPackage(String packageName) {
	    return packageHolder.get(packageName);
	}
	
	private Resources createResources(Context context, AssetManager assetManager) {
	    Resources superRes = context.getResources();
	    Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
	    return resources;
	}
	
	@Override
	public void startPluginActivity(Context context, DLIntent dlIntent) {
		String packageName = dlIntent.getPluginPackage();
		if (packageName == null) throw new NullPointerException("package name is null");
		DLPluginPackage pluginPackage = packageHolder.get(packageName);
		
		if (pluginPackage == null) {
		    throw new PluginException("plugin not found , packageName=" + packageName);
			//TODO plugin not found
		} else {
		    DexClassLoader loader = pluginPackage.loader;
			String className = dlIntent.getPluginClass();
			if (className.startsWith(".")) {
				className = packageName + className;
			}
			Class clazz = null;
			try {
			    clazz = loader.loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new PluginException("class not found, className=" + className);
                //TODO class not found
            }
			
			Class<? extends Activity> activityClass = null;
			if (DLBasePluginActivity.class.isAssignableFrom(clazz)) {
			    activityClass = DLProxyActivity.class;
			} else if (DLBasePluginFragmentActivity.class.isAssignableFrom(clazz)) {
			    activityClass = DLProxyFragmentActivity.class;
			} else {
			    throw new PluginException("class type error, className=" 
			            + className + " is type of " 
			            + clazz.getName());
			  //TODO class type error
			}
			
			dlIntent.putExtra(DLConstants.EXTRA_CLASS, className);
			dlIntent.putExtra(DLConstants.EXTRA_PACKAGE, packageName);
			dlIntent.setClass(context, activityClass);
			context.startActivity(dlIntent);
		}
	}

}
