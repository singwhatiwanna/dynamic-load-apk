/**
 * 
 * @author Song
 * If you want to override more lifecycle method in plugin Activity that havn't been override 
 * in ProxyActivity, create a Activity like this to replace ProxyActivity, and let plugin Activity 
 * implements ICustomRemoteActivity.
 * Don't forget to register this Activity in AndroidManifest.xml
 * 
 *
 */
package com.ryg.dynamicloadhost.sample;

import com.ryg.dynamicloadhost.ProxyActivity;

public class CustomProxyActivity extends ProxyActivity{
    
    private IFullLieftcycleRemoteActivity mCustomRemoteActivity;
    
    @Override
    protected void launchTargetActivity(String className) {
        super.launchTargetActivity(className);
        if (mRemoteActivity instanceof IFullLieftcycleRemoteActivity) {
            mCustomRemoteActivity = (IFullLieftcycleRemoteActivity) mRemoteActivity;
        }
    }
    

    @Override
    public void finish() {
        if (mCustomRemoteActivity != null) {
            mCustomRemoteActivity.finish();
        }
        super.finish();
    }
}
