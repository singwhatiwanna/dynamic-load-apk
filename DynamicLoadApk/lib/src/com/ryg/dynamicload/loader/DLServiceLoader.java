
package com.ryg.dynamicload.loader;

import android.app.Service;
import android.os.Bundle;

import com.ryg.dynamicload.internal.DLServicePlugin;

/**
 * 服务代理类
 * 
 * @author mrsimple
 */
public class DLServiceLoader extends DLBaseLoader<Service, DLServicePlugin> {

    public DLServiceLoader(Service service) {
        mProxyComponent = service;
    }

    @Override
    protected void callPluginOnCreate(Bundle dBundle) {
        mPlugin.onCreate();
    }

}
