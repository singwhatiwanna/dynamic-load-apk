/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:田啸,宋思宇
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ryg.dynamicload.internal;

import com.ryg.utils.DLConstants;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DLIntentService extends IntentService {
    private static final String TAG = "DLIntentService";

    public DLIntentService() {
        super("DLIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            //return;
        }
        String action = intent.getAction();
        Log.i(TAG, "onHandleIntent, action:" + action);
        if (DLConstants.ACTION_LAUNCH_PLUGIN.equals(action)) {
            DLIntent dlIntent = (DLIntent) intent.getParcelableExtra(DLConstants.EXTRA_DLINTENT);
            dlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            DLPluginManager pluginManager = DLPluginManager.getInstance(getApplicationContext());
            pluginManager.loadApk(dlIntent.getDexPath());
            pluginManager.startPluginActivity(getApplicationContext(), dlIntent);
        }
    }

}
