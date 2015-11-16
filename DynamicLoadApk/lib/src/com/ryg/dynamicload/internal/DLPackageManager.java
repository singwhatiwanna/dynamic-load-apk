package com.ryg.dynamicload.internal;

import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;

/**
 * 自定义PackageManager
 * 当包名为插件本身时，从apk路径读取
 */
public class DLPackageManager extends PackageManager {

	PackageManager mProxy;
	DLPluginPackage mDlPackage;

	public DLPackageManager(DLPluginPackage dlPackage, PackageManager pm) {
		mProxy = pm;
		mDlPackage = dlPackage;
	}

	@Override
	public PackageInfo getPackageInfo(String packageName, int flags)
	        throws NameNotFoundException {
		if(packageName.equals(mDlPackage.packageName)){
			PackageInfo pi = getPackageArchiveInfo(mDlPackage.dexPath, flags);
			ApplicationInfo ai = pi.applicationInfo;
			ai.sourceDir = mDlPackage.dexPath;
			ai.publicSourceDir = mDlPackage.dexPath;
			return pi;
		}else{
			return mProxy.getPackageInfo(packageName, flags);
		}
	}

	@Override
	public String[] currentToCanonicalPackageNames(String[] names) {
		return mProxy.currentToCanonicalPackageNames(names);
	}

	@Override
	public String[] canonicalToCurrentPackageNames(String[] names) {
		return mProxy.canonicalToCurrentPackageNames(names);
	}

	@Override
	public Intent getLaunchIntentForPackage(String packageName) {
		return mProxy.getLaunchIntentForPackage(packageName);
	}

	@Override
	public int[] getPackageGids(String packageName)
	        throws NameNotFoundException {
		PackageInfo info = getPackageInfo(packageName, 0);
		return info.gids;
	}

	@Override
	public PermissionInfo getPermissionInfo(String name, int flags)
	        throws NameNotFoundException {
		return mProxy.getPermissionInfo(name, flags);
	}

	@Override
	public List<PermissionInfo> queryPermissionsByGroup(String group, int flags)
	        throws NameNotFoundException {
		return mProxy.queryPermissionsByGroup(group, flags);
	}

	@Override
	public PermissionGroupInfo getPermissionGroupInfo(String name, int flags)
	        throws NameNotFoundException {
		return mProxy.getPermissionGroupInfo(name, flags);
	}

	@Override
	public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
		return mProxy.getAllPermissionGroups(flags);
	}

	@Override
	public ApplicationInfo getApplicationInfo(String packageName, int flags)
	        throws NameNotFoundException {
		PackageInfo pi = getPackageInfo(packageName, flags);
		return pi.applicationInfo;
	}

	@Override
	public ActivityInfo getActivityInfo(ComponentName component, int flags)
	        throws NameNotFoundException {
		PackageInfo pi = getPackageInfo(component.getPackageName(), flags);
		ActivityInfo[] infos = pi.activities;
		if(infos != null){
			for (ActivityInfo info : infos) {
				if (info.name.equals(component.getClassName())) {
					return info;
				}
			}
		}
		return null;
	}

	@Override
	public ActivityInfo getReceiverInfo(ComponentName component, int flags)
	        throws NameNotFoundException {
		PackageInfo pi = getPackageInfo(component.getPackageName(), flags);
		ActivityInfo[] infos = pi.activities;
		if(infos != null){
			for (ActivityInfo info : infos) {
				if (info.name.equals(component.getClassName())) {
					return info;
				}
			}
		}
		return null;
	}

	@Override
	public ServiceInfo getServiceInfo(ComponentName component, int flags)
	        throws NameNotFoundException {
		PackageInfo pi = getPackageInfo(component.getPackageName(), flags);
		ServiceInfo[] sis = pi.services;
		if(sis != null){
			for (ServiceInfo si : sis) {
				if (si.name.equals(component.getClassName())) {
					return si;
				}
			}
		}
		return null;
	}

	@Override
	public ProviderInfo getProviderInfo(ComponentName component, int flags)
	        throws NameNotFoundException {
		PackageInfo pi = getPackageInfo(component.getPackageName(), flags);
		ProviderInfo[] sis = pi.providers;
		if(sis != null){
			for (ProviderInfo si : sis) {
				if (si.name.equals(component.getClassName())) {
					return si;
				}
			}
		}
		return null;
	}

	@Override
	public List<PackageInfo> getInstalledPackages(int flags) {
		return mProxy.getInstalledPackages(flags);
	}

	@Override
	public List<PackageInfo> getPackagesHoldingPermissions(
	        String[] permissions, int flags) {
		return mProxy.getPackagesHoldingPermissions(permissions, flags);
	}

	@Override
	public int checkPermission(String permName, String pkgName) {
		if (pkgName.equals(mDlPackage.packageName)) {
			PackageInfo pi = null;
			try {
				pi = getPackageInfo(pkgName, 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if(pi != null){
				String[] pis = pi.requestedPermissions;
				if(pis != null){
					for (String pei : pis) {
						if(pei.equals(permName)){
							return PERMISSION_GRANTED;
						}
					}
				}
			}
			return PERMISSION_DENIED;
		} else {
			return mProxy.checkPermission(permName, pkgName);
		}
	}

	@Override
	public boolean addPermission(PermissionInfo info) {
		return mProxy.addPermission(info);
	}

	@Override
	public boolean addPermissionAsync(PermissionInfo info) {
		return mProxy.addPermissionAsync(info);
	}

	@Override
	public void removePermission(String name) {
		mProxy.removePermission(name);
	}

	@Override
	public int checkSignatures(String pkg1, String pkg2) {
		return mProxy.checkSignatures(pkg1, pkg2);
	}

	@Override
	public int checkSignatures(int uid1, int uid2) {
		return mProxy.checkSignatures(uid1, uid2);
	}

	@Override
	public String[] getPackagesForUid(int uid) {
		return mProxy.getPackagesForUid(uid);
	}

	@Override
	public String getNameForUid(int uid) {
		return mProxy.getNameForUid(uid);
	}

	@Override
	public List<ApplicationInfo> getInstalledApplications(int flags) {
		return mProxy.getInstalledApplications(flags);
	}

	@Override
	public String[] getSystemSharedLibraryNames() {
		return mProxy.getSystemSharedLibraryNames();
	}

	@Override
	public FeatureInfo[] getSystemAvailableFeatures() {
		return mProxy.getSystemAvailableFeatures();
	}

	@Override
	public boolean hasSystemFeature(String name) {
		return mProxy.hasSystemFeature(name);
	}

	@Override
	public ResolveInfo resolveActivity(Intent intent, int flags) {
		return mProxy.resolveActivity(intent, flags);
	}

	@Override
	public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
		return mProxy.queryIntentActivities(intent, flags);
	}

	@Override
	public List<ResolveInfo> queryIntentActivityOptions(ComponentName caller,
	        Intent[] specifics, Intent intent, int flags) {
		return mProxy.queryIntentActivityOptions(caller, specifics, intent, flags);
	}

	@Override
	public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
		return mProxy.queryBroadcastReceivers(intent, flags);
	}

	@Override
	public ResolveInfo resolveService(Intent intent, int flags) {
		return mProxy.resolveService(intent, flags);
	}

	@Override
	public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
		return mProxy.queryIntentServices(intent, flags);
	}

	@Override
	public List<ResolveInfo> queryIntentContentProviders(Intent intent,
	        int flags) {
		return mProxy.queryIntentContentProviders(intent, flags);
	}

	@Override
	public ProviderInfo resolveContentProvider(String name, int flags) {
		return mProxy.resolveContentProvider(name, flags);
	}

	@Override
	public List<ProviderInfo> queryContentProviders(String processName,
	        int uid, int flags) {
		return mProxy.queryContentProviders(processName, uid, flags);
	}

	@Override
	public InstrumentationInfo getInstrumentationInfo(ComponentName className,
	        int flags) throws NameNotFoundException {
		return mProxy.getInstrumentationInfo(className, flags);
	}

	@Override
	public List<InstrumentationInfo> queryInstrumentation(String targetPackage,
	        int flags) {
		return mProxy.queryInstrumentation(targetPackage, flags);
	}

	@Override
	public Drawable getDrawable(String packageName, int resid,
	        ApplicationInfo appInfo) {
		return mProxy.getDrawable(packageName, resid, appInfo);
	}

	@Override
	public Drawable getActivityIcon(ComponentName activityName)
	        throws NameNotFoundException {
		return mProxy.getActivityIcon(activityName);
	}

	@Override
	public Drawable getActivityIcon(Intent intent) throws NameNotFoundException {
		return mProxy.getActivityIcon(intent);
	}

	@Override
	public Drawable getDefaultActivityIcon() {
		return mProxy.getDefaultActivityIcon();
	}

	@Override
	public Drawable getApplicationIcon(ApplicationInfo info) {
		return mProxy.getApplicationIcon(info);
	}

	@Override
	public Drawable getApplicationIcon(String packageName)
	        throws NameNotFoundException {
		return mProxy.getApplicationIcon(packageName);
	}

	@Override
	public Drawable getActivityLogo(ComponentName activityName)
	        throws NameNotFoundException {
		return mProxy.getActivityLogo(activityName);
	}

	@Override
	public Drawable getActivityLogo(Intent intent) throws NameNotFoundException {
		return mProxy.getActivityLogo(intent);
	}

	@Override
	public Drawable getApplicationLogo(ApplicationInfo info) {
		return mProxy.getApplicationLogo(info);
	}

	@Override
	public Drawable getApplicationLogo(String packageName)
	        throws NameNotFoundException {
		return mProxy.getApplicationLogo(packageName);
	}

	@Override
	public CharSequence getText(String packageName, int resid,
	        ApplicationInfo appInfo) {
		return mProxy.getText(packageName, resid, appInfo);
	}

	@Override
	public XmlResourceParser getXml(String packageName, int resid,
	        ApplicationInfo appInfo) {
		return mProxy.getXml(packageName, resid, appInfo);
	}

	@Override
	public CharSequence getApplicationLabel(ApplicationInfo info) {
		return mProxy.getApplicationLabel(info);
	}

	@Override
	public Resources getResourcesForActivity(ComponentName activityName)
	        throws NameNotFoundException {
		return mDlPackage.resources;
	}

	@Override
	public Resources getResourcesForApplication(ApplicationInfo app)
	        throws NameNotFoundException {
		return mDlPackage.resources;
	}

	@Override
	public Resources getResourcesForApplication(String appPackageName)
	        throws NameNotFoundException {
		return mDlPackage.resources;
	}

	@Override
	public void verifyPendingInstall(int id, int verificationCode) {
		mProxy.verifyPendingInstall(id, verificationCode);
	}

	@Override
	public void extendVerificationTimeout(int id,
	        int verificationCodeAtTimeout, long millisecondsToDelay) {
		mProxy.extendVerificationTimeout(id, verificationCodeAtTimeout,
		        millisecondsToDelay);
	}

	@Override
	public void setInstallerPackageName(String targetPackage,
	        String installerPackageName) {
		mProxy.setInstallerPackageName(targetPackage, installerPackageName);
	}

	@Override
	public String getInstallerPackageName(String packageName) {
		return mProxy.getInstallerPackageName(packageName);
	}

	@Override
	@Deprecated
	public void addPackageToPreferred(String packageName) {
		mProxy.addPackageToPreferred(packageName);
	}

	@Override
	@Deprecated
	public void removePackageFromPreferred(String packageName) {
		mProxy.removePackageFromPreferred(packageName);
	}

	@Override
	public List<PackageInfo> getPreferredPackages(int flags) {
		return mProxy.getPreferredPackages(flags);
	}

	@Override
	@Deprecated
	public void addPreferredActivity(IntentFilter filter, int match,
	        ComponentName[] set, ComponentName activity) {
		mProxy.addPreferredActivity(filter, match, set, activity);
	}

	@Override
	public void clearPackagePreferredActivities(String packageName) {
		mProxy.clearPackagePreferredActivities(packageName);
	}

	@Override
	public int getPreferredActivities(List<IntentFilter> outFilters,
	        List<ComponentName> outActivities, String packageName) {
		return mProxy.getPreferredActivities(outFilters, outActivities,
		        packageName);
	}

	@Override
	public void setComponentEnabledSetting(ComponentName componentName,
	        int newState, int flags) {
		mProxy.setComponentEnabledSetting(componentName, newState, flags);
	}

	@Override
	public int getComponentEnabledSetting(ComponentName componentName) {
		return mProxy.getComponentEnabledSetting(componentName);
	}

	@Override
	public void setApplicationEnabledSetting(String packageName, int newState,
	        int flags) {
		mProxy.setApplicationEnabledSetting(packageName, newState, flags);
	}

	@Override
	public int getApplicationEnabledSetting(String packageName) {
		return mProxy.getApplicationEnabledSetting(packageName);
	}

	@Override
	public boolean isSafeMode() {
		return mProxy.isSafeMode();
	}

}
