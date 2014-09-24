/**
 * 
 * @author Song
 * If you want to override more lifecycle method in plugin Activity that havn't been override 
 * in ProxyActivity, create a Activity like this to replace DLProxyActivity, and let plugin Activity 
 * implements IFullLieftcyclePluginActivity.
 * Don't forget to register this Activity in AndroidManifest.xml
 * 
 *
 */
package com.ryg.dynamicload.sample.dependonhosthost;

import com.ryg.dynamicload.DLProxyActivity;

public class CustomProxyActivity extends DLProxyActivity {
    
    private IFullLieftcyclePluginActivity mCustomRemoteActivity;
    
    @Override
    protected void launchTargetActivity(String className) {
        super.launchTargetActivity(className);
        if (mRemoteActivity instanceof IFullLieftcyclePluginActivity) {
            mCustomRemoteActivity = (IFullLieftcyclePluginActivity) mRemoteActivity;
        }
    }
    

    @Override
    public void finish() {
        if (mCustomRemoteActivity != null) {
            mCustomRemoteActivity.onFinish();
        }
        super.finish();
    }
}
