package com.ryg.dynamicload;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import dalvik.system.DexClassLoader;

public class DLClassLoader extends DexClassLoader {

    private static final HashMap<String, DLClassLoader> loaders = new HashMap<String, DLClassLoader>();

    public DLClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }

    /**
     * return a available classloader which belongs to different apk
     */
    public static DLClassLoader getClassLoader(String dexPath, String optimizedDirectory, ClassLoader classLoader) {
        DLClassLoader dLClassLoader = loaders.get(dexPath);
        if (dLClassLoader != null)
            return dLClassLoader;
        dLClassLoader = new DLClassLoader(dexPath, optimizedDirectory, null, classLoader);
        loaders.put(dexPath, dLClassLoader);
        return dLClassLoader;
    }
}
