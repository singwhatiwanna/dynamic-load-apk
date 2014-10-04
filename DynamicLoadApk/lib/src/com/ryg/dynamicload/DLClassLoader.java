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
    public static DLClassLoader getClassLoader(String dexPath, Context context, ClassLoader classLoader) {
        DLClassLoader dLClassLoader = loaders.get(dexPath);
        if (dLClassLoader != null)
            return dLClassLoader;
        File dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        dLClassLoader = new DLClassLoader(dexPath, dexOutputPath, null, classLoader);
        loaders.put(dexPath, dLClassLoader);
        return dLClassLoader;
    }
}
