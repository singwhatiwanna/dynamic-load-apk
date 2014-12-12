package com.ryg.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * persistence data
 * com.ryg.utils.SharedPreferenceHelper
 * @author yuanzeyao <br/>
 * create at 2014年12月11日 下午5:55:59
 */
public class SharedPreferenceHelper 
{
  private static final String TAG = "SharedPreferenceHelper";
  public static final String PREFERENCE_NAME="plugin_sp";
  
  private static SharedPreferenceHelper instance;
  private SharedPreferences sp=null;
  
  private SharedPreferenceHelper(Context mContext)
  {
    sp=mContext.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, 0);
  }
  
  public static SharedPreferenceHelper getInstance(Context mContext)
  {
    if(instance==null)
    {
      instance=new SharedPreferenceHelper(mContext);
    }
    return instance;
  }
  
  public void setString(String key,String value)
  {
    if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
    {
      return;
    }
    Editor editor=sp.edit();
    editor.putString(key, value);
    editor.commit();
  }
  
  public String getString(String key,String default_value)
  {
    if(TextUtils.isEmpty(key))
    {
      return null;
    }
    return sp.getString(key, default_value);
    
  }
}
