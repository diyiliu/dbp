package com.tiza.util;

/**
 * Description: Common
 * Author: DIYILIU
 * Update: 2016-03-22 17:01
 */
public class Common {

    public static boolean isEmpty(String str) {

        if (str == null || str.trim().length() < 1) {
            return true;
        }

        return false;
    }

    public static String toHex(int i) {

        return String.format("%02X", i);
    }
}
