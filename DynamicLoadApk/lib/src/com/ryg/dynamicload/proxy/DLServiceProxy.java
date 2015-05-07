
package com.ryg.dynamicload.proxy;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.ryg.dynamicload.internal.DLAttachable;
import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.dynamicload.internal.DLServicePlugin;
import com.ryg.dynamicload.loader.DLServiceLoader;

public class DLServiceProxy extends Service implements DLAttachable<DLServicePlugin> {

    private static final String TAG = "DLProxyService";
    private DLServiceLoader mImpl = new DLServiceLoader(this);
    private DLServicePlugin mRemoteService;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, TAG + " onBind");
        // 判断是否存在插件Service，如果存在，则不进行Service插件的构造工作
        if (mRemoteService == null) {
            mImpl.onCreate(intent);
        }
        return mRemoteService.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, TAG + " onCreate");
    }

    // @Override
    // public void onStart(Intent intent, int startId) {
    // // TODO Auto-generated method stub
    // super.onStart(intent, startId);
    // Log.d(TAG, TAG + " onStart");
    //
    // }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, TAG + " onStartCommand " + this.toString());
        // 判断是否存在插件Service，如果存在，则不进行Service插件的构造工作
        if (mRemoteService == null) {
            mImpl.onCreate(intent);
        }
        super.onStartCommand(intent, flags, startId);
        return mRemoteService.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mRemoteService.onDestroy();
        super.onDestroy();
        Log.d(TAG, TAG + " onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mRemoteService.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, TAG + " onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        mRemoteService.onLowMemory();
        super.onLowMemory();
        Log.d(TAG, TAG + " onLowMemory");
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTrimMemory(int level) {
        mRemoteService.onTrimMemory(level);
        super.onTrimMemory(level);
        Log.d(TAG, TAG + " onTrimMemory");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, TAG + " onUnbind");
        super.onUnbind(intent);
        return mRemoteService.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        mRemoteService.onRebind(intent);
        super.onRebind(intent);
        Log.d(TAG, TAG + " onRebind");
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mRemoteService.onTaskRemoved(rootIntent);
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, TAG + " onTaskRemoved");
    }

    @Override
    public void attach(DLServicePlugin remoteService, DLPluginPackage pluginPackage) {
        mRemoteService = remoteService;
    }
}
