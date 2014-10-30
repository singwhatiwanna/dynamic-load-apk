package com.ryg.utils;

public class DLConstants {
    public static final String FROM = "extra.from";
    public static final int FROM_INTERNAL = 0;
    public static final int FROM_EXTERNAL = 1;

    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";
    public static final String EXTRA_PACKAGE = "extra.package";

    public static final int ACTIVITY_TYPE_UNKNOWN = -1;
    public static final int ACTIVITY_TYPE_NORMAL = 1;
    public static final int ACTIVITY_TYPE_FRAGMENT = 2;
    public static final int ACTIVITY_TYPE_ACTIONBAR = 3;

    public static final String PROXY_ACTIVITY_VIEW_ACTION =
            "com.ryg.dynamicload.proxy.activity.VIEW";
    public static final String PROXY_FRAGMENT_ACTIVITY_VIEW_ACTION =
            "com.ryg.dynamicload.proxy.fragmentactivity.VIEW";

    public static final String BRAND_SAMSUNG = "samsung";
}
