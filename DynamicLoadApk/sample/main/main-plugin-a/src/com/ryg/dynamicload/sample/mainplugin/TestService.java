package com.ryg.dynamicload.sample.mainplugin;

import android.content.Intent;
import android.util.Log;

import com.ryg.dynamicload.DLBasePluginService;

public class TestService extends DLBasePluginService {

    private static final String TAG = "TestService";
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.e(TAG, "onCreate");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
    
    
}
