/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ryg.dynamicload.proxy;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Looper;
import android.view.LayoutInflater;

/**
 * Context代理类,代理DLBasePulginActivity和DLBasePluginFragmentActivity关于Context的方法方法,
 * 避免在这两个类中重复判断mFrom字段进行函数调用,消除重复代码.
 */
public final class DLContextProxy {

    Context mBaseContext;

    public DLContextProxy(Activity proxyActivity) {
        mBaseContext = proxyActivity.getBaseContext();
    }

    public AssetManager getAssets() {
        return mBaseContext.getAssets();
    }

    public Resources getResources() {
        return mBaseContext.getResources();
    }

    public PackageManager getPackageManager() {
        return mBaseContext.getPackageManager();
    }

    public ContentResolver getContentResolver() {
        return mBaseContext.getContentResolver();
    }

    public Looper getMainLooper() {
        return mBaseContext.getMainLooper();
    }

    public Context getApplicationContext() {
        return mBaseContext.getApplicationContext();
    }

    public void setTheme(int resid) {
        mBaseContext.setTheme(resid);
    }

    public Theme getTheme() {
        return mBaseContext.getTheme();
    }

    public ClassLoader getClassLoader() {
        return mBaseContext.getClassLoader();
    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        return mBaseContext.getSharedPreferences(name, mode);
    }

    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) mBaseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

}
