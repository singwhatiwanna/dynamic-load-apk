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

    public static final String CPU_ARMEABI = "armeabi";
    public static final String CPU_X86 = "x86";
    public static final String CPU_MIPS = "mips";

    public static final String PREFERENCE_NAME = "dynamic_load_configs";
    
    /**
     * zhangjie1980
     * Intent Extra keys
     */
    public final static String INTENT_PLUGIN_PACKAGE = "dl_plugin_package";
    public final static String INTENT_PLUGIN_CLASS = "dl_plugin_class";

}
