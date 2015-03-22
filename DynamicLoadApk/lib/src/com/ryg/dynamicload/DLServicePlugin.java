package com.ryg.dynamicload;


import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;

import com.ryg.dynamicload.internal.DLPluginPackage;

public interface DLServicePlugin {

    public void onCreate(); 

    public void onStart(Intent intent, int startId); 
    
    public int onStartCommand(Intent intent, int flags, int startId);
    
    public void onDestroy();
    
    public void onConfigurationChanged(Configuration newConfig); 
    
    public void onLowMemory();
    
    public void onTrimMemory(int level);
    
    public boolean onUnbind(Intent intent);
    
    public void onRebind(Intent intent);
    
    public void onTaskRemoved(Intent rootIntent); 
    
    public void attach(Service proxyService, DLPluginPackage pluginPackage);
}
