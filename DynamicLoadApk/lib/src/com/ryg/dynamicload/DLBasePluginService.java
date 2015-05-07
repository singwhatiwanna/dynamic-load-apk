/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:zhangjie1980(张杰)
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

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.dynamicload.internal.DLServicePlugin;
import com.ryg.utils.DLConstants;
import com.ryg.utils.LOG;

public class DLBasePluginService extends Service implements DLServicePlugin {

    public static final String TAG = "DLBasePluginService";
    private Service mProxyService;
    protected DLPluginPackage mPluginPackage;
    protected Service that = this;
    protected int mFrom = DLConstants.FROM_INTERNAL;
    
    @Override
    public void attach(Service proxyService, DLPluginPackage pluginPackage) {
        LOG.d(TAG, TAG + " attach");
        mProxyService = proxyService;
        mPluginPackage = pluginPackage;
        that = mProxyService;
        mFrom = DLConstants.FROM_EXTERNAL;
    }
    
    protected boolean isInternalCall() {
        return mFrom == DLConstants.FROM_INTERNAL;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        LOG.d(TAG, TAG + " onBind");
        return null;
    }

    @Override
    public void onCreate() {
        LOG.d(TAG, TAG + " onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LOG.d(TAG, TAG + " onStartCommand");
        return 0;
    }

    @Override
    public void onDestroy() {
        LOG.d(TAG, TAG + " onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LOG.d(TAG, TAG + " onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        LOG.d(TAG, TAG + " onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        LOG.d(TAG, TAG + " onTrimMemory");
        
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LOG.d(TAG, TAG + " onUnbind");
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        LOG.d(TAG, TAG + " onRebind");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        LOG.d(TAG, TAG + " onTaskRemoved");
    }

}
