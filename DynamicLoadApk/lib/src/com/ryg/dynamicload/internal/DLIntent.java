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

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class DLIntent extends Intent {

    private String mDexPath;
    private String mPluginPackage;
    private String mPluginClass;

    public DLIntent() {
        super();
    }

    public DLIntent(String pluginPackage) {
        super();
        this.mPluginPackage = pluginPackage;
    }

    public DLIntent(String pluginPackage, String pluginClass) {
        super();
        this.mPluginPackage = pluginPackage;
        this.mPluginClass = pluginClass;
    }

    public DLIntent(String pluginPackage, Class<?> clazz) {
        super();
        this.mPluginPackage = pluginPackage;
        this.mPluginClass = clazz.getName();
    }

    public String getPluginPackage() {
        return mPluginPackage;
    }

    public void setPluginPackage(String pluginPackage) {
        this.mPluginPackage = pluginPackage;
    }

    public String getPluginClass() {
        return mPluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.mPluginClass = pluginClass;
    }

    public void setPluginClass(Class<?> clazz) {
        this.mPluginClass = clazz.getName();
    }

    public void setDexPath(String dexPath) {
        mDexPath = dexPath;
    }

    public String getDexPath() {
        return mDexPath;
    }

    @Override
    public Intent putExtra(String name, Parcelable value) {
        ClassLoader pluginLoader = value.getClass().getClassLoader();
        if (getExtras() == null) {
            putExtra("dl-verson", DLConfig.getConfig().DL_VERSION);
        }
        DLConfig.getConfig().mPluginClassLoader = pluginLoader;
        getExtras().setClassLoader(pluginLoader);
        return super.putExtra(name, value);
    }

    public static final Parcelable.Creator<DLIntent> CREATOR = new Parcelable.Creator<DLIntent>() {

        public DLIntent createFromParcel(Parcel in) {
            return new DLIntent(in);
        }

        public DLIntent[] newArray(int size) {
            return new DLIntent[size];
        }

    };

    private DLIntent(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDexPath);
        dest.writeString(mPluginPackage);
        dest.writeString(mPluginClass);
        super.writeToParcel(dest, flags);
    }

    public void readFromParcel(Parcel in) {
        mDexPath = in.readString();
        mPluginPackage = in.readString();
        mPluginClass = in.readString();
        super.readFromParcel(in);
    }

    @Override
    public String toString() {
        return "DLIntent [mDexPath=" + mDexPath + ", mPluginPackage=" + mPluginPackage
                + ", mPluginClass=" + mPluginClass + ", hashcode : " + this.hashCode() + " ]";
    }

}
