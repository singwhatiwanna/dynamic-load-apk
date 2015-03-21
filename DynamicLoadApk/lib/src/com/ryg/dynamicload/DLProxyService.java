package com.ryg.dynamicload;

import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLServiceAttachable;
import com.ryg.dynamicload.internal.DLServiceProxyImpl;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class DLProxyService extends Service implements DLServiceAttachable{
    
    private static final String TAG = "DLProxyService";
    private DLServiceProxyImpl mImpl = new DLServiceProxyImpl(this);
    private DLServicePlugin mRemoteService;
    private DLPluginManager mPluginManager;
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onBind");
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(TAG, TAG + " onCreate");
    } 

//    @Override
//    public void onStart(Intent intent, int startId) {
//        // TODO Auto-generated method stub
//        super.onStart(intent, startId);
//        Log.d(TAG, TAG + " onStart");
//        
//    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onStartCommand");
        mImpl.init(intent);
        super.onStartCommand(intent, flags, startId);
        return mRemoteService.onStartCommand(intent, flags, startId);
//        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, TAG + " onDestroy");
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, TAG + " onConfigurationChanged");
    }
    
    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        Log.d(TAG, TAG + " onLowMemory");
    }
    
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTrimMemory(int level) {
        // TODO Auto-generated method stub
        super.onTrimMemory(level);
        Log.d(TAG, TAG + " onTrimMemory");
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onUnbind");
        return super.onUnbind(intent);
    }
    
    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
        super.onRebind(intent);
        Log.d(TAG, TAG + " onRebind");
    }
    
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, TAG + " onTaskRemoved");
    }

    @Override
    public void attach(DLServicePlugin remoteService, DLPluginManager pluginManager) {
        // TODO Auto-generated method stub
        mRemoteService = remoteService;
        mPluginManager = pluginManager;
    }
}
