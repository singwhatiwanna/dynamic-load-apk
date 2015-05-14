/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:Sizon(赵海洋)
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
