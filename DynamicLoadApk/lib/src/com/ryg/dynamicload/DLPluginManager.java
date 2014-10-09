package com.ryg.dynamicload;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.ryg.utils.DLConstants;

import dalvik.system.DexClassLoader;

public class DLPluginManager {
	
	public class PluginNotFoundException extends Exception {

		private static final long serialVersionUID = -1736885553981751955L;

		public PluginNotFoundException(String string) {
			super(string);
		}
		
	}
	
	private final HashMap<String, DLPluginPackage> packageHolder = new HashMap<String, DLPluginPackage>();

	/**
	 * Load a apk. Before start a plugin Activity, we should do this first.
	 * @param context
	 * @param dexPath
	 * @throws PluginNotFoundException
	 */
	public void loadApk(Context context, String dexPath) throws PluginNotFoundException {
		PackageInfo packageInfo = context.getPackageManager().
				getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
		if (packageInfo == null) 
			throw new PluginNotFoundException(dexPath + " not found");
		String packageName = packageInfo.packageName;
		
		DLPluginPackage pluginPackage = packageHolder.get(packageName);
		
		if (pluginPackage == null) {
		    DexClassLoader dexClassLoader = createDexClassLoader(context, dexPath);
		    AssetManager assetManager = createAssetManager(dexPath);
		    Resources resources = createResources(context, assetManager);
		    pluginPackage = new DLPluginPackage(packageName, dexPath, dexClassLoader, assetManager, resources);
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
	
	private Resources createResources(Context context, AssetManager assetManager) {
	    Resources superRes = context.getResources();
	    Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
	    return resources;
	}
	
	public void startActivity(Context context, DLIntent dlIntent) {
		String packageName = dlIntent.getPluginPackage();
		if (packageName == null) throw new NullPointerException("package name is null");
		DLPluginPackage pluginPackage = packageHolder.get(packageName);
		
		if (pluginPackage == null) {
			//TODO plugin not found
		} else {
		    DexClassLoader loader = pluginPackage.getLoader();
			String className = dlIntent.getPluginClass();
			if (className.startsWith(".")) {
				className = packageName + className;
			}
			dlIntent.putExtra(DLConstants.EXTRA_CLASS, className);
			dlIntent.putExtra(DLConstants.EXTRA_PACKAGE, packageName);
			dlIntent.setClass(context, DLProxyActivity.class);
			context.startActivity(dlIntent);
		}
	}
}
