package com.ryg.dynamicload.internal;

import android.app.Activity;


public interface DLContext {
    public void startPluginActivity(Activity base, DLIntent dlIntent);
    
    public void loadApk(String dexPath) throws PluginException;
    
    public void startPluginActivityForResult(Activity base, DLIntent dlIntent, int requestCode);
}
