package com.ryg.dynamicload.internal;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.DLBasePluginFragmentActivity;
import com.ryg.dynamicload.DLProxyActivity;
import com.ryg.dynamicload.DLProxyFragmentActivity;
import com.ryg.utils.DLConstants;

import dalvik.system.DexClassLoader;

public class DLPluginManager {
    
    private static final String TAG = "PluginManager";
    
    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)}
     * 
     * start success
     */
    public static final int START_RESULT_SUCCESS = 0;
    
    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)}
     * 
     * package not found
     */
    public static final int START_RESULT_NO_PKG = 1;
    
    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)}
     * 
     * class not found
     */
    public static final int START_RESULT_NO_CLASS = 2;
    
    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)}
     * 
     * class type error
     */
    public static final int START_RESULT_TYPE_ERROR = 3;
    
    private static DLPluginManager sInstance;
	
    private Context mContext;
    
    private final HashMap<String, DLPluginPackage> packageHolder = new HashMap<String, DLPluginPackage>();
    
	DLPluginManager(Context context) {
	    mContext = context;
	}
	
	/**
	 * 必须使用Application，防止内存泄露
	 * @param application
	 */
	public static void init(Application application) {
	    sInstance = new DLPluginManager(application);
	}
	
	public static DLPluginManager getInstance() {
	    if (sInstance == null) {
	        throw new RuntimeException("You must call DLPluginManager.init() first.");
	    }
	    return sInstance;
	}
	
	/**
	 * Load a apk. Before start a plugin Activity, we should do this first.
	 * @param dexPath
	 * @throws PluginNotFoundException
	 */
	public DLPluginPackage loadApk(String dexPath) {
		PackageInfo packageInfo = mContext.getPackageManager().
				getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
		if (packageInfo == null) 
			return null;
		final String packageName = packageInfo.packageName;
		
		DLPluginPackage pluginPackage = packageHolder.get(packageName);
		
		if (pluginPackage == null) {
		    DexClassLoader dexClassLoader = createDexClassLoader(dexPath);
		    AssetManager assetManager = createAssetManager(dexPath);
		    Resources resources = createResources(assetManager);
		    pluginPackage = new DLPluginPackage(packageName, dexPath, dexClassLoader, assetManager, resources, packageInfo);
		    packageHolder.put(packageName, pluginPackage);
		}
		return pluginPackage;
	}
	
	private DexClassLoader createDexClassLoader(String dexPath) {
	    File dexOutputDir = mContext.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, null, mContext.getClassLoader());
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
	
	private Resources createResources(AssetManager assetManager) {
	    Resources superRes = mContext.getResources();
	    Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
	    return resources;
	}
	
	/**
	 * {@link #startPluginActivityForResult(Activity, DLIntent, int)}
     */
	public int startPluginActivity(Context base, DLIntent dlIntent) {
		return startPluginActivityForResult(base, dlIntent, -1);
	}

	
	/**
	 * 
	 * @param base
	 * @param dlIntent
	 * @param requestCode
	 * @return One of below:
	 * {@link #START_RESULT_SUCCESS}
	 * {@link #START_RESULT_NO_PKG}
	 * {@link #START_RESULT_NO_CLASS}
	 * {@link #START_RESULT_TYPE_ERROR}
	 */
    public int startPluginActivityForResult(Context base, DLIntent dlIntent, int requestCode) {
        String packageName = dlIntent.getPluginPackage();
        if (packageName == null) throw new NullPointerException("package name is null");
        DLPluginPackage pluginPackage = packageHolder.get(packageName);
        
        if (pluginPackage == null) {
            return START_RESULT_NO_PKG;
        } else {
            DexClassLoader loader = pluginPackage.loader;
            String className = dlIntent.getPluginClass();
            className = className == null ? pluginPackage.getDefaultActivity() : className;
            if (className.startsWith(".")) {
                className = packageName + className;
            }
            Class<?> clazz = null;
            try {
                clazz = loader.loadClass(className);
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
            
            dlIntent.putExtra(DLConstants.EXTRA_CLASS, className);
            dlIntent.putExtra(DLConstants.EXTRA_PACKAGE, packageName);
            dlIntent.setClass(mContext, activityClass);
            
            if (base instanceof Activity) {
                ((Activity) base).startActivityForResult(dlIntent, requestCode);
            } else {
                base.startActivity(dlIntent);
            }
            return START_RESULT_SUCCESS;
        }
    }

}
