package com.ryg.dynamicloadhost;

import java.io.File;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import dalvik.system.DexClassLoader;

public class HostClassLoader extends DexClassLoader {

    static final HashMap<String, HostClassLoader> loaders = new HashMap<String, HostClassLoader>();

    public HostClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }
//    test
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @SuppressLint("NewApi")
//    @Override
//    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
//        Class<?> clazz = findLoadedClass(className);
//        if (clazz != null)
//            return clazz;
//        try {
//            clazz = getParent().loadClass(className);
//        } catch (ClassNotFoundException e) {
//            throw new ClassNotFoundException(className);
//        }
//        return clazz;
//    }

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
