package com.gbq.library.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 类说明：app签名生成工具
 * Author: Kuzan
 * Date: 2017/5/26 18:51.
 */
public class AppParamEncryptUtil {
    private static Set<String> strSet = new HashSet<String>();

    public static String signParamStr(String str, String key) {

        if (StringUtils.isEmptyString(str)) {
            return "";
        }
        if (StringUtils.isEmptyString(key)) {
            return EncryptUtil.md5(str);
        }
        return EncryptUtil.md5(str + "@" + key);
    }

    public static boolean decryptParamStr(String str) {

        if (StringUtils.isEmptyString(str)) {
            return false;
        }
        if (str.length() != 24) {
            return false;
        }
        char first = str.charAt(0);
        int firstIndex = getCharIndex(first);
        if (firstIndex == -1 || firstIndex > 23) {
            return false;
        }
        char s = str.charAt(firstIndex);
        int sIndex = getCharIndex(s);
        if (sIndex == -1 || sIndex > 23) {
            return false;
        }
        int sum = first + s;
        int j = firstIndex + 1;
        for (int index = 0; index < 11; index++) {
            int nextIndex = j > 23 ? (j) % 23 : j;
            int nextJndex = nextIndex + 11 > 23 ? (nextIndex + 11) % 23 : nextIndex + 11;
            char c1 = str.charAt(nextIndex);
            char c2 = str.charAt(nextJndex);
            char c3 = charArray[(c1 + sum + index) % 25];
            if (c2 != c3) {
                return false;
            }
            j++;
        }
        return true;
    }

    public static String encryptCharStr() {

        char[] ca = new char[24];
        // index 从1开始
        int index = new Random().nextInt(23) + 1;
        int jndex = new Random().nextInt(24);
        ca[0] = charArray[index];
        ca[index] = charArray[jndex];
        int j = index + 1;
        int sum = ca[0] + ca[index];
        for (int i = 0; i < 11; i++) {
            int next = new Random().nextInt(26);
            int nextIndex = j > 23 ? (j) % 23 : j;
            int nextJndex = nextIndex + 11 > 23 ? (nextIndex + 11) % 23 : nextIndex + 11;
            ca[nextIndex] = charArray[next];
            ca[nextJndex] = charArray[(ca[nextIndex] + sum + i) % 25];
            j++;
        }
        return new String(ca);
    }

    private static int getCharIndex(char c) {

        int charIndex = -1;
        for (int index = 0; index < charArray.length; index++) {
            if (charArray[index] == c) {
                charIndex = index;
                break;
            }
        }
        return charIndex;
    }

    public static void main(String[] args) {

        int a = 0;
        for (int index = 0; index < 1000000; index++) {
            String str = AppParamEncryptUtil.encryptCharStr();
            System.out.println(str);
            boolean r = AppParamEncryptUtil.decryptParamStr(str);
//            AssertUtil.assertTrue(r, "xx");
            System.out.println(signParamStr(str, "exy"));
            if (!strSet.add(str)) {
                a++;
            }
        }
        System.out.println(a);
    }

    private static final char[] charArray = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
}
