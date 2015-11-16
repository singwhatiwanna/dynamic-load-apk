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

package com.ryg.dynamicload;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.ryg.dynamicload.internal.DLActivityPlugin;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.utils.DLConstants;

public class DLBasePluginFragmentActivity extends FragmentActivity implements DLActivityPlugin {

    private static final String TAG = "DLBasePluginFragmentActivity";

    /**
     * 代理FragmentActivity，可以当作Context来使用，会根据需要来决定是否指向this
     */
    protected FragmentActivity mProxyActivity;

    /**
     * 等同于mProxyActivity，可以当作Context来使用，会根据需要来决定是否指向this<br/>
     * 可以当作this来使用
     */
    protected FragmentActivity that;
    protected int mFrom = DLConstants.FROM_INTERNAL;
    protected DLPluginManager mPluginManager;
    protected DLPluginPackage mPluginPackage;

    @Override
    public void attach(Activity proxyActivity, DLPluginPackage pluginPackage) {
        Log.d(TAG, "attach: proxyActivity= " + proxyActivity);
        mProxyActivity = (FragmentActivity) proxyActivity;
        that = mProxyActivity;
        mPluginPackage = pluginPackage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFrom = savedInstanceState.getInt(DLConstants.FROM, DLConstants.FROM_INTERNAL);
        }
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
            that = mProxyActivity;
        }

        mPluginManager = DLPluginManager.getInstance(that);
        Log.d(TAG, "onCreate: from= "
                + (mFrom == DLConstants.FROM_INTERNAL ? "DLConstants.FROM_INTERNAL"
                        : "FROM_EXTERNAL"));
    }

    @Override
    public void setContentView(View view) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.setContentView(view);
        } else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.setContentView(view, params);
        } else {
            mProxyActivity.setContentView(view, params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.addContentView(view, params);
        } else {
            mProxyActivity.addContentView(view, params);
        }
    }

    @Override
    public View findViewById(int id) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public Intent getIntent() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getIntent();
        } else {
            return mProxyActivity.getIntent();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getClassLoader();
        } else {
            return mProxyActivity.getClassLoader();
        }
    }

    @Override
    public Resources getResources() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getResources();
        } else {
            return mProxyActivity.getResources();
        }
    }

    @Override
    public String getPackageName() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getPackageName();
        } else {
            return mPluginPackage.packageName;
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getLayoutInflater();
        } else {
            return mProxyActivity.getLayoutInflater();
        }
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getMenuInflater();
        } else {
            return mProxyActivity.getMenuInflater();
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getSharedPreferences(name, mode);
        } else {
            return mProxyActivity.getSharedPreferences(name, mode);
        }
    }

    @Override
    public Context getApplicationContext() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getApplicationContext();
        } else {
            return mProxyActivity.getApplicationContext();
        }
    }

    @Override
    public WindowManager getWindowManager() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getWindowManager();
        } else {
            return mProxyActivity.getWindowManager();
        }
    }

    @Override
    public Window getWindow() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getWindow();
        } else {
            return mProxyActivity.getWindow();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.getSystemService(name);
        } else {
            return mProxyActivity.getSystemService(name);
        }
    }

    @Override
    public void finish() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.finish();
        } else {
            mProxyActivity.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onRestart();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onSaveInstanceState(outState);
        }
    }

    public void onNewIntent(Intent intent) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onResume() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onDestroy();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.onKeyUp(keyCode, event);
        }
        return false;
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onWindowAttributesChanged(params);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            super.onWindowFocusChanged(hasFocus);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return super.onCreateOptionsMenu(menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFrom == DLConstants.FROM_INTERNAL) {
            return onOptionsItemSelected(item);
        }
        return false;
    }

 // ------------------------------------------------------------------------
 	// methods override from FragmentActivity
 	// ------------------------------------------------------------------------

 	@Override
 	public FragmentManager getSupportFragmentManager() {
 		if(mFrom == DLConstants.FROM_INTERNAL){
 			return super.getSupportFragmentManager();
 		}else{
 			return mProxyActivity.getSupportFragmentManager();
 		}
 	}

 	@Override
 	public LoaderManager getSupportLoaderManager() {
 		if(mFrom == DLConstants.FROM_INTERNAL){
 			return super.getSupportLoaderManager();
 		}else{
 			return mProxyActivity.getSupportLoaderManager();
 		}
 	}

 	@Override
 	public int checkPermission(String permission, int pid, int uid) {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			return super.checkPermission(permission, pid, uid);
 		} else {
 			return mProxyActivity.checkPermission(permission, pid, uid);
 		}
 	}

 	public boolean isFinishing() {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			return super.isFinishing();
 		} else {
 			return mProxyActivity.isFinishing();
 		}
 	}

 	@Override
 	public void sendBroadcast(Intent intent) {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			super.sendBroadcast(intent);
 		} else {
 			mProxyActivity.sendBroadcast(intent);
 		}
 	}

    /**
     * 插件apk直接调用上下文的startActivity即可调起activity
     * @param intent
     */
 	@Override
 	public void startActivity(Intent intent) {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			super.startActivity(intent);
 		} else {
 			mProxyActivity.startActivity(intent);
 		}
 	}
 	
 	@Override
 	public void startActivityForResult(Intent intent, int requestCode) {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			super.startActivityForResult(intent, requestCode);
 		} else {
 			mProxyActivity.startActivityForResult(intent, requestCode);
 		}
 	}
 	
 	@Override
 	public Intent registerReceiver(BroadcastReceiver receiver,
 	        IntentFilter filter) {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			return super.registerReceiver(receiver, filter);
 		} else {
 			return mProxyActivity.registerReceiver(receiver, filter);
 		}
 	}

 	@Override
 	public void unregisterReceiver(BroadcastReceiver receiver) {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			super.unregisterReceiver(receiver);
 		} else {
 			mProxyActivity.unregisterReceiver(receiver);
 		}
 	}

 	public ApplicationInfo getApplicationInfo() {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			return super.getApplicationInfo();
 		} else {
 			return mProxyActivity.getApplicationInfo();
 		}
 	}

 	@Override
 	public PackageManager getPackageManager() {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			return super.getPackageManager();
 		} else {
 			return mProxyActivity.getPackageManager();
 		}
 	}

 	@Override
 	public void setTheme(int theme) {
 		if (mFrom == DLConstants.FROM_INTERNAL) {
 			super.setTheme(theme);
 		} else {
 			mProxyActivity.setTheme(theme);
 		}
 	}

 	@Override
 	public ContentResolver getContentResolver() {
 		if(mFrom == DLConstants.FROM_INTERNAL){
 			return super.getContentResolver();
 		}else{
 			return mProxyActivity.getContentResolver();
 		}
 	}
 	
 	@Override
    public void onClick(View view) {
 		
     }
 	
 	@Override
 	public AssetManager getAssets() {
 		if(mFrom == DLConstants.FROM_INTERNAL){
 			return super.getAssets();
 		}else{
 			return mProxyActivity.getAssets();
 		}
 	}
 	
 	@Override
 	public View getCurrentFocus(){
 		if(mFrom == DLConstants.FROM_INTERNAL){
 			return super.getCurrentFocus();
 		}else{
 			return mProxyActivity.getCurrentFocus();
 		}
 	}

}
