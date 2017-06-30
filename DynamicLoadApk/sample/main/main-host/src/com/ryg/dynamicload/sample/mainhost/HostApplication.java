package com.ryg.dynamicload.sample.mainhost;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by 王韬 on 2016/10/21.
 */

public class HostApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
