package com.ryg.dynamicload;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import dalvik.system.DexClassLoader;

public class DLPluginManager {
	
	public class PluginNotFoundException extends Exception {

		private static final long serialVersionUID = -1736885553981751955L;

		public PluginNotFoundException(String string) {
			super(string);
		}
		
	}
	
	private final HashMap<String, DexClassLoader> loaders = new HashMap<String, DexClassLoader>();

	public void loadApk(Context context, String dexPath) throws PluginNotFoundException {
		PackageInfo packageInfo = context.getPackageManager().
				getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
		if (packageInfo == null) 
			throw new PluginNotFoundException(dexPath + " not found");
		String packageName = packageInfo.packageName;
		
		
		DexClassLoader loader = loaders.get(packageName);
		if (loader == null) {
			File dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE);
	        final String dexOutputPath = dexOutputDir.getAbsolutePath();
			loader = new DexClassLoader(dexPath, dexOutputPath, null, context.getClassLoader());
			loaders.put(packageName, loader);
		}
	}
	
	public DexClassLoader getLoader(String pulginPackageName) {
		DexClassLoader loader = loaders.get(pulginPackageName);
		return loader;
	}
	
	public void startActivity(Context context, DLIntent dlIntent) {
		String packageName = dlIntent.getPluginPackage();
		if (packageName == null) throw new NullPointerException("package name is null");
		DexClassLoader loader = getLoader(packageName);
		if (loader == null) {
			//TODO plugin not found
		} else {
			String className = dlIntent.getPluginClass();
			if (className.startsWith(".")) {
				className = packageName + className;
			}
			dlIntent.putExtra(DLProxyActivity.EXTRA_CLASS, className);
			dlIntent.setClass(context, DLProxyActivity.class);
			context.startActivity(dlIntent);
		}
	}
}
