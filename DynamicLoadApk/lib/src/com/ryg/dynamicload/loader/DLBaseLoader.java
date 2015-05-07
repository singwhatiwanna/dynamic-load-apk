/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:田啸,宋思宇,Mr.Simple
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

package com.ryg.dynamicload.loader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ryg.dynamicload.internal.DLAttachable;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.utils.DLConfigs;
import com.ryg.utils.DLConstants;

import java.lang.reflect.Constructor;

/**
 * 插件加载器基类,负责构建、加载插件组件，并且与代理组件建立关联关系
 * 
 * @author mrsimple
 * @param <P> 组件代理类型,例如DLProxyActivity,DLProxyService等
 * @param <T> Plugin类型，例如{@see DLPlugin},{@see DLServicePlugin}
 */
@SuppressWarnings("unchecked")
public abstract class DLBaseLoader<P extends Context, T> {

    /**
     * 组件类型,例如Service,Activity等
     */
    protected P mProxyComponent;

    /**
     * 插件组件,例如DLBasePluginActivity的子类、DLBasePluginService子类等
     */
    protected T mPlugin;

    // 插件的包名
    protected String mPackageName;
    // 插件的Service的类名
    protected String mPluginClazz;
    // 插件管理器
    protected DLPluginManager mPluginManager;
    // 插件DLPluginPackage
    protected DLPluginPackage mPluginPackage;
    // Log's TAG
    protected final String TAG = this.getClass().getName();

    /**
     * 创建组件,并且调用组件的onCreate方法
     * 
     * @param intent
     */
    public final void onCreate(Intent intent) {
        try {
            // 1、初始化插件包名、类名等属性
            init(intent);
            // 2、反射构造插件对象
            mPlugin = createPlugin(mPluginPackage.classLoader);
            // 3、调用插件代理对象的attach方法，将插件注入到代理对象中
            attachPluginToProxy();
            // 4、调用插件的attach、onCreate方法启动插件
            callOnCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void init(Intent intent) {
        // set the extra's class loader
        intent.setExtrasClassLoader(DLConfigs.sPluginClassloader);
        // 插件的包名
        mPackageName = intent.getStringExtra(DLConstants.EXTRA_PACKAGE);
        // 插件的Service的类名
        mPluginClazz = intent.getStringExtra(DLConstants.EXTRA_CLASS);
        mPluginManager = DLPluginManager.getInstance(mProxyComponent);
        mPluginPackage = mPluginManager.getPackage(mPackageName);
    }

    protected T createPlugin(ClassLoader classLoader)
            throws Exception {
        Log.d(TAG, "clazz=" + mPluginClazz + " packageName=" + mPackageName);
        Class<?> localClass = classLoader.loadClass(mPluginClazz);
        Constructor<?> localConstructor = localClass.getConstructor(new Class[] {});
        return (T) localConstructor.newInstance(new Object[] {});
    }

    /**
     * 调用代理对象的attach
     */
    private void attachPluginToProxy() {
        ((DLAttachable<T>) mProxyComponent).attach(mPlugin, mPluginPackage);
    }

    /**
     * 调用插件的attach和onCreate函数，启动插件
     */
    private void callOnCreate() {
        // 调用插件的attach,将Proxy组件注入到插件对象中
        ((DLAttachable<P>) mPlugin).attach(mProxyComponent, mPluginPackage);
        Bundle bundle = new Bundle();
        bundle.putInt(DLConstants.FROM, DLConstants.FROM_EXTERNAL);
        // 调用插件的onCreate
        callPluginOnCreate(bundle);
    }

    /**
     * 调用插件的onCreate方法
     * 
     * @param bundle 额外的数据
     */
    protected abstract void callPluginOnCreate(Bundle bundle);

}
