package com.ryg.dynamicload.internal;

import android.content.Context;

public interface DLContext {
    public void startPluginActivity(Context context, DLIntent intent);
    
    public DLPluginManager getPluginManager();
}
