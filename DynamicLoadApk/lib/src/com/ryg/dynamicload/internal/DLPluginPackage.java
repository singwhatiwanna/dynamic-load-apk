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
 * A plugin apk. Activities in a same apk share a same AssetManager, Resources
 * and DexClassLoader.
 * 
 * @author siyu.song
 */
public class DLPluginPackage {

    public String mPackageName;
    public String mDefaultActivity;
    public DexClassLoader mClassLoader;
    public AssetManager mAssetManager;
    public Resources mResources;
    public PackageInfo mPackageInfo;

    public DLPluginPackage(DexClassLoader loader, Resources resources,
            PackageInfo packageInfo) {
        this.mPackageName = packageInfo.packageName;
        this.mClassLoader = loader;
        this.mAssetManager = resources.getAssets();
        this.mResources = resources;
        this.mPackageInfo = packageInfo;

        mDefaultActivity = parseDefaultActivityName();
    }

    private final String parseDefaultActivityName() {
        if (mPackageInfo.activities != null && mPackageInfo.activities.length > 0) {
            return mPackageInfo.activities[0].name;
        }
        return "";
    }
}
