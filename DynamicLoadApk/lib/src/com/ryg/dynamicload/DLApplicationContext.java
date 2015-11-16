package com.ryg.dynamicload;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;

/**
 * 插件app的application上下文
 * 主要用在单例中对applicaiton上下文的使用
 */
public class DLApplicationContext extends ContextWrapper {

	DLPluginPackage mPluginPackage;
	
	public DLApplicationContext(Context base, DLPluginPackage pluginPkg) {
	    super(base);
	    mPluginPackage = pluginPkg;
    }

	
	@Override
	public Resources getResources() {
		if(mPluginPackage != null){
			return mPluginPackage.resources;
		}else{
			return super.getResources();
		}
	}
	
	@Override
    public Context getApplicationContext() {
		if(mPluginPackage != null){
			return this;
		}else{
			return super.getApplicationContext();
		}
    }

	@Override
	public String getPackageName() {
		if(mPluginPackage != null){
			return mPluginPackage.packageName;
		}else{
			return super.getPackageName();
		}
	}

	@Override
	public AssetManager getAssets() {
		if(mPluginPackage != null){
			return mPluginPackage.assetManager;
		}else{
			return super.getAssets();
		}
	}

	@Override
	public void startActivity(Intent intent) {
		if(mPluginPackage != null){
			startPluginActivity((DLIntent) intent);
		}else{
			super.startActivity(intent);
		}
	}

	public void startPluginActivity(DLIntent dlIntent) {
		startPluginActivityForResult(dlIntent, -1);
	}

	public void startPluginActivityForResult(DLIntent dlIntent, int requestCode) {
		if (dlIntent.getPluginPackage() == null) {
			dlIntent.setPluginPackage(mPluginPackage.packageName);
		}
		DLPluginManager plMgr = DLPluginManager.getInstance(this);
		plMgr.startPluginActivityForResult(this, dlIntent, requestCode);
	}

}
