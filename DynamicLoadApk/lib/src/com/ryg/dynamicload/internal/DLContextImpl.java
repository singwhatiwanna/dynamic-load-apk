package com.ryg.dynamicload.internal;

import android.content.Context;

public class DLContextImpl implements DLContext {
    
    private static DLContextImpl sInstance;
    
    static synchronized DLContextImpl createInstance() {
        if (sInstance == null) {
            sInstance = new DLContextImpl();
        }
        return sInstance;
    }
    
    private DLPluginManager mPluginManager;
    
    private DLContextImpl () {
        mPluginManager = new DLPluginManager();
    }
    
    @Override
    public void startPluginActivity(Context context, DLIntent intent) {
        
    }

    @Override
    public DLPluginManager getPluginManager() {
        return mPluginManager;
    }

}
