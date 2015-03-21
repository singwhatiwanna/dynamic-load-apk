package com.ryg.dynamicload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DLBasePluginService extends Service implements DLServicePlugin {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
