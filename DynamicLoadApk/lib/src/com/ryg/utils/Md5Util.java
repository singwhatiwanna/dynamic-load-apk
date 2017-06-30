package com.ryg.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };

    public static String md5To16(String text) {
        return md5To16(text, "UTF-8");
    }

    public static String md5To32(String text) {
        return md5To32(text, "UTF-8");
    }

    public static String md5To16(String text, String charset) {
        return md5To32(text, charset).substring(8, 24);
    }

    public static String md5To32(String text, String charset) {
        if(text == null || "".equals(text)) {
            return null;
        }
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(msgDigest==null){
            return null;
        }

        try {
            msgDigest.update(text.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] bytes = msgDigest.digest();
        return new String(encodeHex(bytes));
    }

    public static String md5To32(File file) {
        if(file == null || !file.exists()) {
            return null;
        }

        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(msgDigest==null){
            return null;
        }

        byte[] bytes = new byte[8192];
        int byteCount;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            while ((byteCount = inputStream.read(bytes)) > 0) {
                msgDigest.update(bytes, 0, byteCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        byte[] digest = msgDigest.digest();
        return new String(encodeHex(digest));
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }
}
