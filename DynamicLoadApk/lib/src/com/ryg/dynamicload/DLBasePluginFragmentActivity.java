/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@qq.com>
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
package com.ryg.dynamicload;

import com.ryg.utils.DLConstants;
import com.ryg.utils.DLUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class DLBasePluginFragmentActivity extends FragmentActivity implements DLPlugin {

    private static final String TAG = "DLBasePluginFragmentActivity";

    /**
     * 代理FragmentActivity，可以当作Context来使用，会根据需要来决定是否指向this
     */
    protected FragmentActivity mProxyActivity;

    /**
     * 等同于mProxyActivity，可以当作Context来使用，会根据需要来决定是否指向this<br/>
     * 可以当作this来使用
     */
    protected FragmentActivity that;
    protected int mFrom = DLConstants.FROM_INTERNAL;
    protected String mDexPath;

    public void setProxy(Activity proxyActivity, String dexPath) {
        Log.d(TAG, "setProxy: proxyActivity= " + proxyActivity + ", dexPath= " + dexPath);
        mProxyActivity = (FragmentActivity)proxyActivity;
        that = mProxyActivity;
        mDexPath = dexPath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFrom = savedInstanceState.getInt(DLConstants.FROM, DLConstants.FROM_INTERNAL);
        }
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
            that = mProxyActivity;
        }

        Log.d(TAG, "onCreate: from= " + (mFrom == DLConstants.FROM_INTERNAL ? "DLConstants.FROM_INTERNAL" : "FROM_EXTERNAL"));
    }

    protected void startActivityByProxy(Class<?> cls) {
        startActivityByProxy(cls.getName());
    }

    protected void startActivityForResultByProxy(Class<?> cls, int requestCode) {
        startActivityForResultByProxy(cls.getName(), requestCode);
    }

    protected void startActivityByProxy(String className) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            Intent intent = new Intent();
            intent.setClassName(this, className);
            mProxyActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(DLUtils.getProxyViewAction(className, getClassLoader()));
            intent.putExtra(DLConstants.EXTRA_DEX_PATH, mDexPath);
            intent.putExtra(DLConstants.EXTRA_CLASS, className);
            mProxyActivity.startActivity(intent);
        }
    }

    public void startActivityForResultByProxy(String className, int requestCode) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            Intent intent = new Intent();
            intent.setClassName(this, className);
            mProxyActivity.startActivityForResult(intent, requestCode);
        } else {
            Intent intent = new Intent(DLUtils.getProxyViewAction(className, getClassLoader()));
            intent.putExtra(DLConstants.EXTRA_DEX_PATH, mDexPath);
            intent.putExtra(DLConstants.EXTRA_CLASS, className);
            mProxyActivity.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.setContentView(view);
        } else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.setContentView(view, params);
        } else {
            mProxyActivity.setContentView(view, params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.addContentView(view, params);
        } else {
            mProxyActivity.addContentView(view, params);
        }
    }

    @Override
    public View findViewById(int id) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public Intent getIntent() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getIntent();
        } else {
            return mProxyActivity.getIntent();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getClassLoader();
        } else {
            return mProxyActivity.getClassLoader();
        }
    }

    @Override
    public Resources getResources() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getResources();
        } else {
            return mProxyActivity.getResources();
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getLayoutInflater();
        } else {
            return mProxyActivity.getLayoutInflater();
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getSharedPreferences(name, mode);
        } else {
            return mProxyActivity.getSharedPreferences(name, mode);
        }
    }

    @Override
    public Context getApplicationContext() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getApplicationContext();
        } else {
            return mProxyActivity.getApplicationContext();
        }
    }

    @Override
    public WindowManager getWindowManager() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getWindowManager();
        } else {
            return mProxyActivity.getWindowManager();
        }
    }

    @Override
    public Window getWindow() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getWindow();
        } else {
            return mProxyActivity.getWindow();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getSystemService(name);
        } else {
            return mProxyActivity.getSystemService(name);
        }
    }

    @Override
    public void finish() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.finish();
        } else {
            mProxyActivity.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onRestart();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onSaveInstanceState(outState);
        }
    }

    public void onNewIntent(Intent intent) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onResume() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onDestroy();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.onKeyUp(keyCode, event);
        }
        return false;
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onWindowAttributesChanged(params);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onWindowFocusChanged(hasFocus);
        }
    }

    // ------------------------------------------------------------------------
    // methods override from FragmentActivity
    // ------------------------------------------------------------------------

    @Override
    public FragmentManager getSupportFragmentManager() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getSupportFragmentManager();
        }
        return mProxyActivity.getSupportFragmentManager();
    }

    @Override
    public LoaderManager getSupportLoaderManager() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getSupportLoaderManager();
        }
        return mProxyActivity.getSupportLoaderManager();
    }

}
