package com.ryg.dynamicload;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.ryg.utils.DLConstants;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;

public class DLProxyActivity extends Activity {

    private static final String TAG = "DLProxyActivity";

    private String mClass;
    private String mDexPath;

    private AssetManager mAssetManager;
    private Resources mResources;
    private Theme mTheme;
    private ClassLoader mLocalClassLoader;

    protected DLPlugin mRemoteActivity;

    protected void loadResources() {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, mDexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDexPath = getIntent().getStringExtra(DLConstants.EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(DLConstants.EXTRA_CLASS);

        Log.d(TAG, "mClass=" + mClass + " mDexPath=" + mDexPath);
        loadResources();
        if (mClass == null) {
            launchTargetActivity();
        } else {
            launchTargetActivity(mClass);
        }
    }

    protected void launchTargetActivity() {
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(mDexPath, 1);
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
            String activityName = packageInfo.activities[0].name;
            mClass = activityName;
            launchTargetActivity(mClass);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void launchTargetActivity(final String className) {
        Log.d(TAG, "start launchTargetActivity, className=" + className);
        if (mLocalClassLoader == null) {
            mLocalClassLoader = DLClassLoader.getClassLoader(mDexPath, this.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(), getClassLoader());
        }
        try {
            Class<?> localClass = mLocalClassLoader.loadClass(className);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
            setRemoteActivity(instance);
            Log.d(TAG, "instance = " + instance);

            mRemoteActivity.setProxy(this, mDexPath);

            Bundle bundle = new Bundle();
            bundle.putInt(DLConstants.FROM, DLConstants.FROM_EXTERNAL);
            mRemoteActivity.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setRemoteActivity(Object activity) {
        mRemoteActivity = (DLPlugin) activity;
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    @Override
    public ClassLoader getClassLoader() {
        ClassLoader classLoader = new ClassLoader(super.getClassLoader()) {
            @Override
            public Class<?> loadClass(String className) throws ClassNotFoundException {
                Class<?> clazz = null;
                clazz = mLocalClassLoader.loadClass(className);
                Log.d(TAG, "load class:" + className);
                if (clazz == null) {
                    clazz = getParent().loadClass(className);
                }
                // still not found
                if (clazz == null) {
                    throw new ClassNotFoundException(className);
                }

                return clazz;
            }
        };

        return classLoader;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRemoteActivity.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        mRemoteActivity.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        mRemoteActivity.onRestart();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        mRemoteActivity.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRemoteActivity.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mRemoteActivity.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mRemoteActivity.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mRemoteActivity.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mRemoteActivity.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mRemoteActivity.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        mRemoteActivity.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return mRemoteActivity.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        return mRemoteActivity.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        mRemoteActivity.onWindowAttributesChanged(params);
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mRemoteActivity.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

}
