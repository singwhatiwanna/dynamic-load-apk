package com.ryg.dynamicload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;

import com.ryg.utils.DLUtils;

public class DLActivities {

	public static Map<String,Integer> Acts = new HashMap<String,Integer>();

	public static void initData(Context context) {
		String pluginFolder = Environment.getExternalStorageDirectory()
				+ "/DynamicLoadHost";
		File file = new File(pluginFolder);
		File[] plugins = file.listFiles();

		for (File plugin : plugins) {
			PackageInfo packageInfo = DLUtils.getPackageInfo(context, plugin.getAbsolutePath());
			if (packageInfo.activities != null
					&& packageInfo.activities.length > 0) {
				for (int i = 0; i < packageInfo.activities.length; i++) {
					Acts.put(packageInfo.activities[i].name,packageInfo.activities[i].launchMode );
				}
			}
		}
	}
}
