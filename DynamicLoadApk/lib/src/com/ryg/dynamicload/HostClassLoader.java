package com.ryg.dynamicload;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import dalvik.system.DexClassLoader;

public class HostClassLoader extends DexClassLoader {

    private static final HashMap<String, HostClassLoader> loaders = new HashMap<String, HostClassLoader>();

    public HostClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }

    /**
     * return a available classloader which belongs to different apk
     */
    public static HostClassLoader getClassLoader(String dexPath, Context context, ClassLoader classLoader) {
        HostClassLoader hostClassLoader = loaders.get(dexPath);
        if (hostClassLoader != null)
            return hostClassLoader;
        File dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        hostClassLoader = new HostClassLoader(dexPath, dexOutputPath, null, classLoader);
        loaders.put(dexPath, hostClassLoader);
        return hostClassLoader;
    }
}
