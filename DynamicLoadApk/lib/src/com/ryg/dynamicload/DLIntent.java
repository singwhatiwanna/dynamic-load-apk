package com.ryg.dynamicload;

import android.content.Intent;

public class DLIntent extends Intent {
	private String pluginPackage;
	
	private String pluginClass;

	public String getPluginPackage() {
		return pluginPackage;
	}

	public void setPluginPackage(String pluginPackage) {
		this.pluginPackage = pluginPackage;
	}

	public String getPluginClass() {
		return pluginClass;
	}

	public void setPluginClass(String pluginClass) {
		this.pluginClass = pluginClass;
	}

	
}
