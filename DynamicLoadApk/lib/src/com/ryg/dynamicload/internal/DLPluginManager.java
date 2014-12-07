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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import com.ryg.utils.SharedPreferenceHelper;

import dalvik.system.DexClassLoader;

public class DLPluginManager {

    private static final String TAG = "DLPluginManager";

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} start success
     */
    public static final int START_RESULT_SUCCESS = 0;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} package not found
     */
    public static final int START_RESULT_NO_PKG = 1;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} class not found
     */
    public static final int START_RESULT_NO_CLASS = 2;

    /**
     * return value of {@link #startPluginActivity(Activity, DLIntent)} class type error
     */
    public static final int START_RESULT_TYPE_ERROR = 3;


    private static DLPluginManager sInstance;
    private Context mContext;
    private final HashMap<String, DLPluginPackage> mPackagesHolder = new HashMap<String, DLPluginPackage>();

    private int mFrom = DLConstants.FROM_INTERNAL;
    
    private String mLibDir=null;

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
     * @param dexPath
     */
    public DLPluginPackage loadApk(String dexPath) {
        // when loadApk is called by host apk, we assume that plugin is invoked by host.
        return loadApk(dexPath,false);
    }
    
    /**
     * @param dexPath
     *        plugin path
     * @param hassolib
     *        whether exist so lib in plugin
     * @return
     */
    public DLPluginPackage loadApk(final String dexPath,boolean hassolib)
    {
      mFrom = DLConstants.FROM_EXTERNAL;

      PackageInfo packageInfo = mContext.getPackageManager().
              getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
      if (packageInfo == null)
          return null;

      final String packageName = packageInfo.packageName;
      DLPluginPackage pluginPackage = mPackagesHolder.get(packageName);
      if (pluginPackage == null) {
          DexClassLoader dexClassLoader = createDexClassLoader(dexPath);
          AssetManager assetManager = createAssetManager(dexPath);
          Resources resources = createResources(assetManager);
          pluginPackage = new DLPluginPackage(packageName, dexPath, dexClassLoader, assetManager,
                  resources, packageInfo);
          mPackagesHolder.put(packageName, pluginPackage);
      }
      
      if(hassolib)
      {
        mLibDir=mContext.getDir("pluginlib",Context.MODE_PRIVATE).toString();
        new Thread()
        {
          public void run() {
            copyPluginSoLib(dexPath, DLUtils.getCpuName());
          };
        }.start();
      }
      return pluginPackage;
    }
    
    
    /**
     * copy so lib to  specify  directory(/data/data/host_pack_name/pluginlib)
     * @param dexPath
     *    plugin path
     * @param cpuName
     *    cpuName CPU_X86,CPU_MIPS,CPU_ARMEABI
     */
    private void copyPluginSoLib(String dexPath,String cpuName)
    {
      String parseFileName=DLConstants.CPU_ARMEABI;
      if(cpuName.toLowerCase().contains("arm"))
      {
        parseFileName=DLConstants.CPU_ARMEABI;
      }else if(cpuName.toLowerCase().contains("x86"))
      {
        parseFileName=DLConstants.CPU_X86;
      }else if(cpuName.toLowerCase().contains("mips"))
      {
        parseFileName=DLConstants.CPU_MIPS;
      }
      Log.d(TAG,"parseFileName---->"+parseFileName);
      InputStream ins=null;
      FileOutputStream fos=null;
      try {
        ZipFile zip = new ZipFile(dexPath);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        while (entries.hasMoreElements()) {
          ZipEntry ze = (ZipEntry) entries.nextElement();
          if(ze.getName().endsWith(".so") && ze.getName().contains(parseFileName))
          {
            String lastModify=String.valueOf(ze.getTime());
            if(lastModify.equals(SharedPreferenceHelper.getInstance(mContext).getString(ze.getName(), "")))
            {
              //exist and no change
              Log.d(TAG, "the so lib is exist and not change!!!!");
              return;
            }
            String libName=ze.getName().substring(ze.getName().lastIndexOf("/")+1);
            ins = zip.getInputStream(ze);
            fos = new FileOutputStream(new File(mLibDir, libName));
            
            byte[] buf = new byte[2048];
            int len = -1;

            while ((len = ins.read(buf)) != -1) {
              fos.write(buf, 0, len);
            }
            ins.close();
            fos.close();
            SharedPreferenceHelper.getInstance(mContext).setString(ze.getName(),lastModify);
            Log.d(TAG, ze.getName()+" copy success");
            break;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private DexClassLoader createDexClassLoader(String dexPath) {
        File dexOutputDir = mContext.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        //设置ClassLoader的本地so路径
        if(mLibDir==null)
        {
          mLibDir=mContext.getDir("pluginlib", Context.MODE_PRIVATE).toString();
        }
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, mFrom==DLConstants.FROM_EXTERNAL ? mLibDir : null, mContext.getClassLoader());
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
        return mPackagesHolder.get(packageName);
    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        return resources;
    }

    /**
     * {@link #startPluginActivityForResult(Activity, DLIntent, int)}
     */
    public int startPluginActivity(Context context, DLIntent dlIntent) {
        return startPluginActivityForResult(context, dlIntent, -1);
    }

    /**
     * @param context
     * @param dlIntent
     * @param requestCode
     * @return One of below: {@link #START_RESULT_SUCCESS} {@link #START_RESULT_NO_PKG}
     *         {@link #START_RESULT_NO_CLASS} {@link #START_RESULT_TYPE_ERROR}
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public int startPluginActivityForResult(Context context, DLIntent dlIntent, int requestCode) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            dlIntent.setClassName(context, dlIntent.getPluginClass());
            performStartActivityForResult(context, dlIntent, requestCode);
            return DLPluginManager.START_RESULT_SUCCESS;
        }

        String packageName = dlIntent.getPluginPackage();
        if (packageName == null) {
            throw new NullPointerException("disallow null packageName.");
        }
        DLPluginPackage pluginPackage = mPackagesHolder.get(packageName);
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
          clazz=Class.forName(className, true, classLoader);
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
        performStartActivityForResult(context, dlIntent, requestCode);
        return START_RESULT_SUCCESS;
    }

    private void performStartActivityForResult(Context context, DLIntent dlIntent, int requestCode) {
        Log.d(TAG, "launch " + dlIntent.getPluginClass());
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(dlIntent, requestCode);
        } else {
            context.startActivity(dlIntent);
        }
    }

}
