
package com.ryg.dynamicload.proxy;

import android.app.Service;
import android.os.Bundle;

import com.ryg.dynamicload.DLServicePlugin;

/**
 * 服务代理类
 * 
 * @author mrsimple
 */
public class DLServiceProxy extends DLBaseProxy<Service, DLServicePlugin> {

    public DLServiceProxy(Service service) {
        mProxyComponent = service;
    }

    @Override
    protected void callPluginOnCreate(Bundle dBundle) {
        mPlugin.onCreate();
    }

}
