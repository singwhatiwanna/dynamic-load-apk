/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:田啸,宋思宇,Mr.Simple
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

package com.ryg.dynamicload.proxy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.ryg.dynamicload.internal.DLActivityPlugin;
import com.ryg.dynamicload.internal.DLAttachable;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.dynamicload.loader.DLActivityLoader;
import com.ryg.utils.DLConstants;

public class DLActivityProxy extends Activity
        implements DLAttachable<DLActivityPlugin> {

    protected DLActivityPlugin mRemoteActivity;
    private DLActivityLoader impl = new DLActivityLoader(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        impl.onCreate(getIntent());
    }

    @Override
    public void attach(DLActivityPlugin remoteActivity, DLPluginPackage pluginPackage) {
        mRemoteActivity = remoteActivity;
    }

    @Override
    public AssetManager getAssets() {
        return impl.getAssets() == null ? super.getAssets() : impl.getAssets();
    }

    @Override
    public Resources getResources() {
        return impl.getResources() == null ? super.getResources() : impl.getResources();
    }

    @Override
    public Theme getTheme() {
        return impl.getTheme() == null ? super.getTheme() : impl.getTheme();
    }

    @Override
    public ClassLoader getClassLoader() {
        return impl.getClassLoader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRemoteActivity.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        mRemoteActivity.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        mRemoteActivity.onRestart();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        mRemoteActivity.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRemoteActivity.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mRemoteActivity.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mRemoteActivity.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mRemoteActivity.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mRemoteActivity.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mRemoteActivity.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        mRemoteActivity.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return mRemoteActivity.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        return mRemoteActivity.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        mRemoteActivity.onWindowAttributesChanged(params);
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mRemoteActivity.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mRemoteActivity.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mRemoteActivity.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }
    
    //--------add by xionghoumiao----------//
    /**
     * 插件apk直接调用上下文的startActivity即可调起activity
     * @param intent
     */
    @Override
	public void startActivity(Intent intent) {
		if (isStartSuperActi(intent)) {
			super.startActivity(intent);
		} else {
			startPluginActivityForResult(intent, -1);
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if (isStartSuperActi(intent)) {
			super.startActivityForResult(intent, requestCode);
		} else {
			startPluginActivityForResult(intent, requestCode);
		}
	}

	/**
	 * 是否启动真正的Activity
	 *  1.调用系统的界面action不能空 2.或者包含start_acti字段为true
	 * @param intent
	 * @return
	 */
	private boolean isStartSuperActi(Intent intent) {
		boolean isStartSuperActi = (mRemoteActivity == null);
		isStartSuperActi |= intent.getBooleanExtra(
		        DLConstants.INTENT_START_ACTI, false);
		if (intent.hasExtra(DLConstants.INTENT_START_ACTI)) {
			intent.removeExtra(DLConstants.INTENT_START_ACTI);
		}
		isStartSuperActi |= !TextUtils.isEmpty(intent.getAction());
		return isStartSuperActi;
	}

	private int startPluginActivityForResult(Intent intent, int requestCode) {
		DLIntent dlIntent = null;
		if (intent instanceof DLIntent) {
			dlIntent = (DLIntent) intent;
		} else {
			dlIntent = new DLIntent(mRemoteActivity.getPackageName(), intent);
		}
		if (dlIntent.getPluginPackage() == null) {
			dlIntent.setPluginPackage(mRemoteActivity.getPackageName());
		}
		DLPluginManager dlMgr = DLPluginManager.getInstance(this);
		return dlMgr.startPluginActivityForResult(this, dlIntent,
                requestCode);
	}

	public void onClick(View view) {
		if (mRemoteActivity != null) {
			mRemoteActivity.onClick(view);
			return;
		}
	}

}
