/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ryg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class SoLibManager {

    private static final String TAG = SoLibManager.class.getSimpleName();

    /**
     * So File executor
     */
    private ExecutorService mSoExecutor = Executors.newCachedThreadPool();
    /**
     * single instance of the SoLoader
     */
    private static SoLibManager sInstance = new SoLibManager();
    /**
     * app's lib dir
     */
    private static String sNativeLibDir = "";

    private SoLibManager() {
    }

    /**
     * @return
     */
    public static SoLibManager getSoLoader() {
        return sInstance;
    }

    /**
     * get cpu name, according cpu type parse relevant so lib
     * 
     * @return ARM、ARMV7、X86、MIPS
     */
    private String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            String[] array = text.split(":\\s+", 2);
            if (array.length >= 2) {
                return array[1];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressLint("DefaultLocale")
    private String getCpuArch(String cpuName) {
        String cpuArchitect = DLConstants.CPU_ARMEABI;
        if (cpuName.toLowerCase().contains("arm")) {
            cpuArchitect = DLConstants.CPU_ARMEABI;
        } else if (cpuName.toLowerCase().contains("x86")) {
            cpuArchitect = DLConstants.CPU_X86;
        } else if (cpuName.toLowerCase().contains("mips")) {
            cpuArchitect = DLConstants.CPU_MIPS;
        }

        return cpuArchitect;
    }

    /**
     * copy so lib to specify directory(/data/data/host_pack_name/pluginlib)
     * 
     * @param dexPath plugin path
     * @param cpuName cpuName CPU_X86,CPU_MIPS,CPU_ARMEABI
     */
    public void copyPluginSoLib(Context context, String dexPath, String nativeLibDir) {
        String cpuName = getCpuName();
        String cpuArchitect = getCpuArch(cpuName);

        sNativeLibDir = nativeLibDir;
        Log.d(TAG, "cpuArchitect: " + cpuArchitect);
        long start = System.currentTimeMillis();
        try {
            ZipFile zip = new ZipFile(dexPath);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                if (ze.isDirectory()) {
                    continue;
                }
                String zipEntryName = ze.getName();
                if (zipEntryName.endsWith(".so") && zipEntryName.contains(cpuArchitect)) {
                    final long lastModify = ze.getTime();
                    if (lastModify == DLConfigs.getSoLastModifiedTime(context, zipEntryName)) {
                        // exist and no change
                        Log.d(TAG, "skip copying, the so lib is exist and not change: "
                                + zipEntryName);
                        continue;
                    }
                    InputStream ins = zip.getInputStream(ze);
                    mSoExecutor.execute(new CopySoTask(context, ins, zipEntryName, lastModify));
                }
            }
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        Log.d(TAG, "### copy so time : " + (end - start) + " ms");
    }

    /**
     * @author mrsimple
     */
    private class CopySoTask implements Runnable {

        private String mSoFileName;
        private InputStream mIns;
        private String mZipEntryName;
        private Context mContext;
        private long mLastModityTime;

        CopySoTask(Context context, InputStream ins, String zipEntryName, long lastModify) {
            mIns = ins;
            mContext = context;
            mZipEntryName = zipEntryName;
            mSoFileName = parseSoFileName(zipEntryName);
            mLastModityTime = lastModify;
        }

        private final String parseSoFileName(String zipEntryName) {
            return zipEntryName.substring(zipEntryName.lastIndexOf("/") + 1);
        }

        private void writeSoFile2LibDir() {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(sNativeLibDir, mSoFileName));
                byte[] buf = new byte[8192];
                int len = -1;

                while ((len = mIns.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }

                Log.e(TAG, "### copy so file : " + mSoFileName);
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DLUtils.closeQuietly(fos);
                DLUtils.closeQuietly(mIns);
            }
        }

        @Override
        public void run() {
            writeSoFile2LibDir();
            DLConfigs.setSoLastModifiedTime(mContext, mZipEntryName, mLastModityTime);
            Log.d(TAG, "copy so lib success: " + mZipEntryName);
        }

    }
}
