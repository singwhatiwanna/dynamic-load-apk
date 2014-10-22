package com.ryg.dynamicload.internal;

import android.content.Context;

public class DLContextWrapper implements DLContext {

    private DLContext mBaseContext;
    
    public DLContextWrapper() {
        mBaseContext = DLPluginManager.getInstance();
    }

    @Override
    public void startPluginActivity(Context context, DLIntent intent) {
        mBaseContext.startPluginActivity(context, intent);
    }

    @Override
    public void loadApk(Context context, String dexPath) throws PluginException {
        mBaseContext.loadApk(context, dexPath);
    }

}
