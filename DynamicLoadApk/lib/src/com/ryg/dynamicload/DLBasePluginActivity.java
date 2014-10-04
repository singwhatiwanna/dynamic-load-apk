package com.ryg.dynamicload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.ryg.dynamicload.DLPlugin;

/**
 * note: can use that like this.
 * @see {@link DLBasePluginActivity.that} 
 * @author renyugang
 */
public class DLBasePluginActivity extends Activity implements DLPlugin {

    private static final String TAG = "DLBasePluginActivity";

    public static final String FROM = "extra.from";
    public static final int FROM_INTERNAL = 0;
    public static final int FROM_EXTERNAL = 1;
    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";

    public static final String PROXY_VIEW_ACTION = "com.ryg.dynamicload.proxy.VIEW";

    /**
     * 代理activity，可以当作Context来使用，会根据需要来决定是否指向this
     */
    protected Activity mProxyActivity;

    /**
     * 等同于mProxyActivity，可以当作Context来使用，会根据需要来决定是否指向this<br/>
     * 可以当作this来使用
     */
    protected Activity that;
    protected int mFrom = FROM_INTERNAL;
    protected String mDexPath;

    public void setProxy(Activity proxyActivity, String dexPath) {
        Log.d(TAG, "setProxy: proxyActivity= " + proxyActivity + ", dexPath= " + dexPath);
        mProxyActivity = proxyActivity;
        that = mProxyActivity;
        mDexPath = dexPath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFrom = savedInstanceState.getInt(FROM, FROM_INTERNAL);
        }
        if (mFrom == FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
            that = mProxyActivity;
        }

        Log.d(TAG, "onCreate: from= " + (mFrom == FROM_INTERNAL ? "FROM_INTERNAL" : "FROM_EXTERNAL"));
    }

    protected void startActivityByProxy(String className) {
        if (mFrom == FROM_INTERNAL) {
            Intent intent = new Intent();
            intent.setClassName(this, className);
            mProxyActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(PROXY_VIEW_ACTION);
            intent.putExtra(EXTRA_DEX_PATH, mDexPath);
            intent.putExtra(EXTRA_CLASS, className);
            mProxyActivity.startActivity(intent);
        }
    }

    public void startActivityForResultByProxy(String className, int requestCode) {
        if (mFrom == FROM_INTERNAL) {
            Intent intent = new Intent();
            intent.setClassName(this, className);
            mProxyActivity.startActivityForResult(intent, requestCode);
        } else {
            Intent intent = new Intent(PROXY_VIEW_ACTION);
            intent.putExtra(EXTRA_DEX_PATH, mDexPath);
            intent.putExtra(EXTRA_CLASS, className);
            mProxyActivity.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mFrom == FROM_INTERNAL) {
            super.setContentView(view);
        } else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        if (mFrom == FROM_INTERNAL) {
            super.setContentView(view, params);
        } else {
            mProxyActivity.setContentView(view, params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mFrom == FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        if (mFrom == FROM_INTERNAL) {
            super.addContentView(view, params);
        } else {
            mProxyActivity.addContentView(view, params);
        }
    }

    @Override
    public View findViewById(int id) {
        if (mFrom == FROM_INTERNAL) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public Intent getIntent() {
        if (mFrom == FROM_INTERNAL) {
            return super.getIntent();
        } else {
            return mProxyActivity.getIntent();
        }
    }

    @Override
    public Resources getResources() {
        if (mFrom == FROM_INTERNAL) {
            return super.getResources();
        } else {
            return mProxyActivity.getResources();
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if (mFrom == FROM_INTERNAL) {
            return super.getLayoutInflater();
        } else {
            return mProxyActivity.getLayoutInflater();
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (mFrom == FROM_INTERNAL) {
            return super.getSharedPreferences(name, mode);
        } else {
            return mProxyActivity.getSharedPreferences(name, mode);
        }
    }

    @Override
    public Context getApplicationContext() {
        if (mFrom == FROM_INTERNAL) {
            return super.getApplicationContext();
        } else {
            return mProxyActivity.getApplicationContext();
        }
    }

    @Override
    public WindowManager getWindowManager() {
        if (mFrom == FROM_INTERNAL) {
            return super.getWindowManager();
        } else {
            return mProxyActivity.getWindowManager();
        }
    }

    @Override
    public Window getWindow() {
        if (mFrom == FROM_INTERNAL) {
            return super.getWindow();
        } else {
            return mProxyActivity.getWindow();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (mFrom == FROM_INTERNAL) {
            return super.getSystemService(name);
        } else {
            return mProxyActivity.getSystemService(name);
        }
    }

    @Override
    public void finish() {
        if (mFrom == FROM_INTERNAL) {
            super.finish();
        } else {
            mProxyActivity.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFrom == FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        if (mFrom == FROM_INTERNAL) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (mFrom == FROM_INTERNAL) {
            super.onRestart();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mFrom == FROM_INTERNAL) {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFrom == FROM_INTERNAL) {
            super.onSaveInstanceState(outState);
        }
    }

    public void onNewIntent(Intent intent) {
        if (mFrom == FROM_INTERNAL) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onResume() {
        if (mFrom == FROM_INTERNAL) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mFrom == FROM_INTERNAL) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mFrom == FROM_INTERNAL) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mFrom == FROM_INTERNAL) {
            super.onDestroy();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mFrom == FROM_INTERNAL) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mFrom == FROM_INTERNAL) {
            return super.onKeyUp(keyCode, event);
        }
        return false;
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (mFrom == FROM_INTERNAL) {
            super.onWindowAttributesChanged(params);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (mFrom == FROM_INTERNAL) {
            super.onWindowFocusChanged(hasFocus);
        }
    }
}
