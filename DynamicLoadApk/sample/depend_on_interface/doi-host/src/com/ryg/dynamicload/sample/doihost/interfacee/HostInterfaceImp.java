package com.ryg.dynamicload.sample.doihost.interfacee;

import android.content.Context;

import com.ryg.dynamicload.sample.docommon.HostInterface;
import com.ryg.dynamicload.sample.doihost.component.HostComponent;

public class HostInterfaceImp implements HostInterface {

    @Override
    public void hostMethod(Context context) {
        new HostComponent().doSomething(context);
    }
}
