package com.ryg.dynamicload;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public class DLBasePluginService extends Service implements DLServicePlugin {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTrimMemory(int level) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        
    }

}
