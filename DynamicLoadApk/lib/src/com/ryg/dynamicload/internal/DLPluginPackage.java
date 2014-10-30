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

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;

/**
 * A plugin apk. Activities in a same apk share a same AssetManager, Resources and DexClassLoader.
 * 
 * @author siyu.song
 */
public class DLPluginPackage {

    public String packageName;
    private String mDefaultActivity;
    public String path;

    public DexClassLoader classLoader;
    public AssetManager assetManager;
    public Resources resources;
    public PackageInfo packageInfo;

    public DLPluginPackage(String packageName, String path, DexClassLoader loader, AssetManager assetManager,
            Resources resources, PackageInfo packageInfo) {
        this.packageName = packageName;
        this.path = path;
        this.classLoader = loader;
        this.assetManager = assetManager;
        this.resources = resources;
        this.packageInfo = packageInfo;
    }

    public String getDefaultActivity() {
        if (packageInfo.activities != null && packageInfo.activities.length > 0) {
            mDefaultActivity = packageInfo.activities[0].name;
        }
        return mDefaultActivity;
    }
}
