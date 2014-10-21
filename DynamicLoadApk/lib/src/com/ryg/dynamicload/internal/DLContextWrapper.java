package com.ryg.dynamicload.internal;

import android.content.Context;

public class DLContextWrapper implements DLContext {

    private DLContext mBaseContext;
    
    public DLContextWrapper() {
        mBaseContext = DLContextImpl.createInstance();
    }

    @Override
    public void startPluginActivity(Context context, DLIntent intent) {
        mBaseContext.startPluginActivity(context, intent);
    }

    @Override
    public DLPluginManager getPluginManager() {
        return mBaseContext.getPluginManager();
    }

}
