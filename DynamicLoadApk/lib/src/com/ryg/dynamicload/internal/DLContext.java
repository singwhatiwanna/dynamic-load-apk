package com.ryg.dynamicload.internal;

import android.content.Context;

public interface DLContext {
    public void startPluginActivity(Context context, DLIntent intent);
    
    public void loadApk(Context context, String dexPath) throws PluginException;
}
