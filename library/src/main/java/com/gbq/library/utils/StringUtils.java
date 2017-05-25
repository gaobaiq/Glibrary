package com.gbq.library.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类说明：字符串工具类
 * Author: Kuzan
 * Date: 2017/5/25 15:12.
 */
public class StringUtils {
    /**
     * 将一个字串的首字母大写.
     *
     * @param s String 源字串
     * @return String 首字母大写后的字串
     */
    public static String toUpperCaseFirstLetter(final String s) {
        if (s == null || s.length() < 1) {
            return "";
        }

        final char[] arrC = s.toCharArray();
        arrC[0] = Character.toUpperCase(arrC[0]);
        return String.copyValueOf(arrC);
    }

    /**
     * 错误字符.
     *
     * @param s
     * @return String
     */
    public static String escapeErrorChar(final String s) {
        String s1 = null;
        s1 = s;
        if (s1 == null) {
            return s1;
        } else {
            s1 = replace(s1, "\\", "\\\\");
            s1 = replace(s1, "\"", "\\\"");
            return s1;
        }
    }

    /**
     * 提取字符串中的汉字  <br>
     *
     * @param str String
     * @return String
     * @author liulongzhenhai 2014年3月5日 下午4:48:20 <br>
     */
    public static String getCHStr(final String str) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) + "").getBytes().length > 1) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 取指定字符串的指定长度子字串.
     *
     * @param str
     * @param str
     * @return
     */
    public static String subStr(final String str, final int len) {
        final String s = valueOf(str);
        String result = "";
        if (s.length() >= len) {
            result = s.substring(0, len);
        } else {
            result = s;
        }
        return result;
    }

    /**
     * 获取两字符之间的字符串.
     *
     * @param str      ---------------原字符串
     * @param startStr ---------------要截字符串的开始字符串
     * @param endStr   ---------------要截字符串的结束字符串
     * @return String
     */
    public static String getMiddleString(final String str, final String startStr, final String endStr) {
        final int start = str.indexOf(startStr);
        final int end = str.indexOf(endStr);
        if (start == -1 || end == -1) {
            return "";
        }
        final String resultStr = str.substring(start + startStr.length(), end);
        return resultStr;
    }

    /**
     * 把字符串转为真假值对象 如果为1 就为真,其他为假. <br>
     *
     * @param str 0,1字符串
     * @return true false
     * @author liulongzhenhai 2012-9-12 下午4:09:22 <br>
     */
    public static boolean getStringBoolean(final String str) {
        if ("1".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 替换.
     *
     * @param s
     * @param s1
     * @param s2
     * @return
     */
    public static String replaceIgnoreCase(final String s, final String s1, final String s2) {
        if (s == null) {
            return null;
        }
        final String s3 = s.toLowerCase();
        final String s4 = s1.toLowerCase();
        int i = 0;
        if ((i = s3.indexOf(s4, i)) >= 0) {
            final char ac[] = s.toCharArray();
            final char ac1[] = s2.toCharArray();
            final int j = s1.length();
            final StringBuffer stringbuffer = new StringBuffer(ac.length);
            stringbuffer.append(ac, 0, i).append(ac1);
            i += j;
            int k;
            for (k = i; (i = s3.indexOf(s4, i)) > 0; k = i) {
                stringbuffer.append(ac, k, i - k).append(ac1);
                i += j;
            }

            stringbuffer.append(ac, k, ac.length - k);
            return stringbuffer.toString();
        } else {
            return s;
        }
    }

    /**
     * formatInputStr。
     *
     * @param s
     * @return
     */
    public static String formatInputStr(final String s) {
        String s1 = s;
        s1 = valueOf(s1);
        s1 = escapeHTMLTags(s1);
        return s1;
    }

    /**
     * HTML标签.
     *
     * @param s
     * @return
     */
    public static String escapeHTMLTags(final String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        final StringBuffer stringbuffer = new StringBuffer(s.length());
        // byte byte0 = 32;
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (c == '<') {
                stringbuffer.append("&lt;");
            } else if (c == '>') {
                stringbuffer.append("&gt;");
            } else {
                stringbuffer.append(c);
            }
        }

        return stringbuffer.toString();
    }

    /**
     * 字符串替换.
     *
     * @param s  搜索字符串
     * @param s1 要查找字符串
     * @param s2 要替换字符串
     * @return
     */
    public static String replace(final String s, final String s1, final String s2) {
        if (s == null) {
            return null;
        }
        int i = 0;
        if ((i = s.indexOf(s1, i)) >= 0) {
            final char ac[] = s.toCharArray();
            final char ac1[] = s2.toCharArray();
            final int j = s1.length();
            final StringBuffer stringbuffer = new StringBuffer(ac.length);
            stringbuffer.append(ac, 0, i).append(ac1);
            i += j;
            int k;
            for (k = i; (i = s.indexOf(s1, i)) > 0; k = i) {
                stringbuffer.append(ac, k, i - k).append(ac1);
                i += j;
            }

            stringbuffer.append(ac, k, ac.length - k);
            return stringbuffer.toString();
        } else {
            return s;
        }
    }

    /**
     * 替换字符串.
     *
     * @param s
     * @param s1
     * @param s2
     * @param ai
     * @return
     */
    public static String replace(final String s, final String s1, final String s2, final int ai[]) {
        if (s == null) {
            return null;
        }
        int i = 0;
        if ((i = s.indexOf(s1, i)) >= 0) {
            int j = 0;
            j++;
            final char ac[] = s.toCharArray();
            final char ac1[] = s2.toCharArray();
            final int k = s1.length();
            final StringBuffer stringbuffer = new StringBuffer(ac.length);
            stringbuffer.append(ac, 0, i).append(ac1);
            i += k;
            int l;
            for (l = i; (i = s.indexOf(s1, i)) > 0; l = i) {
                j++;
                stringbuffer.append(ac, l, i - l).append(ac1);
                i += k;
            }

            stringbuffer.append(ac, l, ac.length - l);
            ai[0] = j;
            return stringbuffer.toString();
        } else {
            return s;
        }
    }

    /**
     * convert Array to ArrayList.
     *
     * @param sz  String[]
     * @param len int
     * @return ArrayList
     */
    public static ArrayList getArrayListFromArray(final String[] sz, final int len) {
        final ArrayList aTmp = new ArrayList();

        if (isNullStr(sz)) {
            for (int i = 0; i < len; i++) {
                aTmp.add("");
            }
        } else {
            for (final String element : sz) {
                aTmp.add(element);
            }
        }
        return aTmp;
    }

    /**
     * Convert to String from object Base200312291148
     *
     * @param o Object
     * @return String
     */
    public static String cStr(final Object o) {
        return o == null ? "" : o.toString();
    }

    /**
     * 判断此字符串是否为空、空字符串，或"null".
     *
     * @param s
     */
    public static boolean isNullStr(final String s) {
        return s == null || s.equals("null") || s.equals("") ? true : false;
    }

    /**
     * 判断此字符串是否为空、空字符串，或"null"
     *
     * @param o
     * @return
     */
    public static final boolean isNullStr(final Object o) {
        return o == null || o.toString().equals("null") || o.toString().equals("") ? true : false;
    }

    /**
     * 如果字符串str为空则转换为str1
     *
     * @param str
     * @param str1
     * @return
     */
    public static String getNullStr(final String str, final String str1) {
        if (isNullStr(str)) {
            return str1;
        } else {
            return str;
        }
    }

    /**
     * null对象转为空字符串 . <br>
     *
     * @param str str
     * @return 字符串
     * @author liulongzhenhai 2013-10-22 上午9:26:51 <br>
     */
    public static String nullToEmpty(final String str) {
        if (isEmptyString(str)) {
            return "";
        }
        return str;
    }

    /**
     * 根据转义列表对字符串进行转义(escape).
     *
     * @param source        待转义的字符串
     * @param escapeCharMap 转义列表
     * @return 转义后的字符串
     */
    public static String escapeCharacter(final String source, final HashMap escapeCharMap) {
        if (source == null || source.length() == 0) {
            return source;
        }
        if (escapeCharMap.size() == 0) {
            return source;
        }
        final StringBuffer sb = new StringBuffer(source.length() + 100);
        final StringCharacterIterator sci = new StringCharacterIterator(source);
        for (char c = sci.first(); c != StringCharacterIterator.DONE; c = sci.next()) {
            String character = String.valueOf(c);
            if (escapeCharMap.containsKey(character)) {
                character = (String) escapeCharMap.get(character);
            }
            sb.append(character);
        }
        return sb.toString();
    }

    /**
     * 此方法将给出的字符串source使用delim划分为单词数组.
     *
     * @param source 需要进行划分的原字符串
     * @param delim  单词的分隔字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组， 如果delim为null则使用逗号作为分隔字符串.
     */
    public static String[] split(final String source, String delim) {
        String[] wordLists;
        if (source == null) {
            wordLists = new String[1];
            wordLists[0] = source;
            return wordLists;
        }
        if (delim == null) {
            delim = ",";
        }
        final StringTokenizer st = new StringTokenizer(source, delim);
        final int total = st.countTokens();
        wordLists = new String[total];
        for (int i = 0; i < total; i++) {
            wordLists[i] = st.nextToken();
        }
        return wordLists;
    }

    /**
     * 此方法将给出的字符串source使用delim划分为单词数组.
     *
     * @param source 需要进行划分的原字符串
     * @param delim  单词的分隔字符
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组.
     */
    public static String[] split(final String source, final char delim) {
        return split(source, String.valueOf(delim));
    }

    /**
     * 此方法将给出的字符串source使用逗号划分为单词数组.
     *
     * @param source 需要进行划分的原字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组.
     */
    public static String[] split(final String source) {
        return split(source, ",");
    }

    /**
     * 将set的所有记录并成一个以 delim 分隔的字符串.
     *
     * @param set
     * @param delim
     * @return
     */
    public static String combine(final Set set, String delim) {
        if (set == null || set.size() == 0) {
            return "";
        }
        if (delim == null) {
            delim = "";
        }
        final StringBuffer sb = new StringBuffer(100);
        for (final Iterator iter = set.iterator(); iter.hasNext(); ) {
            sb.append(iter.next());
            sb.append(delim);
        }
        if (sb.length() >= delim.length()) {
            sb.delete(sb.length() - 1 - delim.length(), sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 将List的所有记录并成一个以 delim 分隔的字符串.
     *
     * @param list  列表数据
     * @param delim 分割符
     * @return String 格式：'A','B','C'
     * @throws
     */
    public static String combine(final List list, String delim) {

        if (list == null || list.size() == 0) {
            return "";
        }

        if (delim == null) {
            delim = ",";
        }

        final StringBuffer sb = new StringBuffer();

        for (final Iterator iter = list.iterator(); iter.hasNext(); ) {
            sb.append(iter.next());
            sb.append(delim);
        }

        String result = "'";

        if (sb.length() >= delim.length()) {

            result += sb.toString().substring(0, sb.toString().length() - 2);
        }

        return result;

    }

    /**
     * 将set的所有记录并成一个以 , 分隔的字符串.
     *
     * @param set
     */
    public static String combine(final Set set) {
        return combine(set, ",");
    }

    /**
     * 将字符串数组合并成一个以 delim 分隔的字符串
     *
     * @param array 字符串数组
     * @param delim 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
     * @return 合并后的字符串
     */
    public static String combineArray(final String[] array, String delim) {
        if (array == null || array.length == 0) {
            return "";
        }
        final int length = array.length - 1;
        if (delim == null) {
            delim = "";
        }
        final StringBuffer result = new StringBuffer(length * 8);
        for (int i = 0; i < length; i++) {
            result.append(array[i]);
            result.append(delim);
        }
        result.append(array[length]);
        return result.toString();
    }

    /**
     * 将字符串数组合并成一个以,号分隔的字符串.
     *
     * @param array 字符串数组
     * @return 合并后的字符串
     */
    public static String combineArray(final String[] array) {
        return combineArray(array, ",");
    }

    /**
     * 将int数组使用指定的分隔符合并成一个字符串.
     *
     * @param array int[] int 数组
     * @param delim String 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
     * @return String 合并后的字符串
     */
    public static String combineArray(final int[] array, String delim) {
        if (array == null || array.length == 0) {
            return "";
        }

        final int length = array.length - 1;
        if (delim == null) {
            delim = "";
        }

        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++) {
            result.append(Integer.toString(array[i]));
            result.append(delim);
        }
        result.append(Integer.toString(array[length]));

        return result.toString();
    }

    /**
     * 将int数组合并成一个以,号分隔的字符串.
     *
     * @param array 字符串数组
     * @return 合并后的字符串
     */
    public static String combineArray(final int[] array) {
        return combineArray(array, ",");
    }

    /**
     * 将字符串List使用指定的分隔符合并成一个字符串.
     *
     * @param list  List
     * @param delim String
     * @return String
     */
    public static String combineList(final List list, final String delim) {
        if (list == null || list.size() == 0) {
            return "";
        } else {
            final StringBuffer result = new StringBuffer();
            for (int i = 0; i < list.size() - 1; i++) {
                result.append(list.get(i));
                result.append(delim);
            }
            result.append(list.get(list.size() - 1));
            return result.toString();
        }
    }

    /**
     * 将字符串List使用 , 合并成一个字符串.
     *
     * @param list List
     * @return String
     */
    public static String combineList(final List list) {
        return combineList(list, ",");
    }

    /**
     * 以指定的字符和长度生成一个该字符的指定长度的字符串.
     *
     * @param c      指定的字符
     * @param length 指定的长度
     * @return 最终生成的字符串
     */
    public static String fillString(final char c, final int length) {
        String ret = "";
        for (int i = 0; i < length; i++) {
            ret += c;
        }
        return ret;
    }

    /**
     * 字符串数组中是否包含指定的字符串.
     *
     * @param strings       字符串数组
     * @param string        字符串
     * @param caseSensitive 是否大小写敏感
     * @return 包含时返回true，否则返回false
     */
    public static final boolean contains(final String[] strings, final String string, final boolean caseSensitive) {
        for (final String string2 : strings) {
            if (caseSensitive == true) {
                if (string2.equals(string)) {
                    return true;
                }
            } else {
                if (string2.equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字符串中是否包含另一个指定的字符串.
     *
     * @param source 源字符串
     * @param target 目标字符串
     * @return boolean 是否包含
     */
    public static boolean contains(final String source, final String target) {
        return source.indexOf(target) != -1;
    }

    /**
     * 字符串数组中是否包含指定的字符串.大小写敏感.
     *
     * @param strings 字符串数组
     * @param string  字符串
     * @return 包含时返回true，否则返回false
     */
    public static boolean contains(final String[] strings, final String string) {
        return contains(strings, string, true);
    }

    /**
     * 不区分大小写判定字符串数组中是否包含指定的字符串.
     *
     * @param strings 字符串数组
     * @param string  字符串
     * @return 包含时返回true，否则返回false
     */
    public static boolean containsIgnoreCase(final String[] strings, final String string) {
        return contains(strings, string, false);
    }

    /**
     * 得到字符串的字节长度.
     *
     * @param source 字符串
     * @return 字符串的字节长度
     */
    public static int getByteLength(final String source) {
        int len = 0;
        for (int i = 0; i < source.length(); i++) {
            final char c = source.charAt(i);
            final int highByte = c >>> 8;
            len += highByte == 0 ? 1 : 2;
        }
        return len;
    }

    /**
     * 判断字符是否为双字节字符，如中文.
     *
     * @param c char
     * @return boolean
     */
    public static boolean isDoubleByte(final char c) {
        return !(c >>> 8 == 0);
    }

    /**
     * 输出固定字节长度的字符串.
     *
     * @param source String
     * @param len    int
     * @param exChar String
     * @param exStr  String
     * @return String
     */
    public static String getSubStr(final String source, final int len, final String exChar, final String exStr) {
        if (source == null || getByteLength(source) <= len) {
            return source;
        }
        final StringBuffer result = new StringBuffer();
        char c = '\u0000';
        int i = 0, j = 0;
        for (; i < len; j++) {
            result.append(c);
            c = source.charAt(j);
            i += isDoubleByte(c) ? 2 : 1;
        }
        /**
         * 到这里i有两种情况：等于len或是len+1，如果是len+1. 说明是双字节，并多出一个字节 这时候就只能append(exChar)，否则就append(c).
         */
        if (i > len) {
            result.append(exChar);
        } else {
            result.append(c);
        }
        result.append(exStr);

        return result.toString();
    }

    public static String getSubStr(final String source, final int len) {
        return getSubStr(source, len, ".", "...");
    }

    /**
     * 判断输入参数是否为null返回一个非null的值.
     *
     * @param s String 判断的值
     * @return String
     */
    public static String valueOf(final String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * 判断输入参数是否为null返回一个非null的值.
     *
     * @param o 判断的值
     */
    public static String valueOf(final Object o) {
        if (o == null) {
            return "";
        }

        return String.valueOf(o).trim();
    }

    /**
     * Valueof.
     *
     * @param @param  i
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: valueOf
     */
    public static String valueOf(final int i) {
        return String.valueOf(i);
    }

    /**
     * 属性添加标识符. 例如：金额 =>$[金额]
     *
     * @param sourceStr 源数据
     * @return 属性字符
     */
    public static String propDrapeOn(final String sourceStr) {
        final StringBuffer sb = new StringBuffer();
        sb.append("$[").append(sourceStr).append("]");
        return sb.toString();
    }

    /**
     * 实体添加标识符. 例如：凭证 =>${凭证}
     *
     * @param sourceStr 源数据
     * @return 属性字符
     */
    public static String entityDrapeOn(final String sourceStr) {
        final StringBuffer sb = new StringBuffer();
        sb.append("${").append(sourceStr).append("}");
        return sb.toString();
    }

    /**
     * 读取控制台的输入参数.
     *
     * @param prompt
     * @return
     */
    public static String readConsoleString(final String prompt) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        try {
            str = br.readLine();
        } catch (final IOException e) {
        }
        return str;
    }

    /**
     * 方法的作用.
     *
     * @param str
     * @return boolean
     * @throws
     */
    public static boolean isEmptyString(final String str) {
        return str == null || str.length() < 1;
    }

    /**
     * 方法的作用.
     *
     * @param str
     * @return boolean
     * @throws
     */
    public static boolean isNotEmptyString(final String str) {
        return str != null && str.length() > 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 方法的作用.
     *
     * @param patterns
     * @param arguments
     * @return String[]
     * @throws
     */
    public static String[] formatStrings(final String[] patterns, final Object[] arguments) {
        final String[] results = new String[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            results[i] = MessageFormat.format(patterns[i], arguments);
        }
        return results;
    }

    /**
     * 方法的作用.
     *
     * @param value
     * @return String
     * @throws
     */
    public static String trim(final String value) {
        if (value == null) {
            return null;
        } else {
            return value.trim();
        }
    }

    // 如 \<data>&abc<data>转换为<data>abc<data>.

    /**
     * 转换XML，将XML字符串去掉转义字符&.
     *
     * @param value 字符串.
     * @return 去掉转义字符&的字符串.
     */
    public static String switchXml(final String value) {
        if (value == null) {
            return null;
        }

        return value.replaceAll("&", "&amp;");
    }

    /**
     * 转换字符串为XML节点值.
     *
     * @param value 字符串.
     * @return XML节点值
     */
    public static String switchStringToXml(final String value) {
        if (value == null) {
            return "";
        }

        return value.replaceAll("<", "").replaceAll(">", "").replaceAll("\'", "&apos;").replaceAll("\"", "&quot;")
                .replaceAll("&", "&amp;");
    }

    /**
     * 按一定长度截取字符串.
     *
     * @param text   文本.
     * @param length 截取长度.
     * @return 截取后的文本.
     */
    public static String cutStringByLength(final String text, final int length) {
        if (text == null) {
            return null;
        }
        if (text.length() > length) {
            return text.substring(0, length);
        } else {
            return text;
        }
    }

    /**
     * 是否为空字符串或NULL. 如str = "    ", str = null 返回true
     *
     * @param str 字符串
     * @return 是否为空字符串
     */
    public static boolean isNullOrEmpty(final String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 是否为空对象，如：null或" ".<br>
     *
     * @param obj Object
     * @return 是否为空对象.
     * @author chenxiangbai 2012-10-23 上午8:42:38 <br>
     */
    public static boolean isNullOrEmpty(final Object obj) {
        return obj == null || obj.toString().trim().length() == 0;
    }

    /**
     * 检查两个字符串是否相等 若均为null会被视为相等 . <br>
     *
     * @param s1
     * @param s2
     * @return 如果相等, 则返回true
     * @author liulongzhenhai 2012-7-5 下午3:45:46 <br>
     */
    public static boolean check2StringEquals(final String s1, final String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 != null && s2 != null) {
            return s1.equals(s2);
        }
        return false;
    }

    /**
     * 检查是否是数字 .
     *
     * @param numberString
     * @return TorF .
     */
    public static Boolean isNumber(final Object numberString) {
        if (numberString == null || numberString.toString().equals("")) {
            return false;
        }
        final Pattern pattern = Pattern.compile("[0-9]*");
        final Matcher isNum = pattern.matcher(numberString.toString());
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 去掉字符串前后的空格. <br>
     *
     * @param str 需要去掉前后空格的字符串
     * @return 去掉前后空格的字符串
     * @author shaorulong 2013年12月3日 下午5:32:02 <br>
     */
    public static String replaceStartAndEndEmptyString(String str) {
        while (str.startsWith(" ")) {
            str = str.substring(1, str.length()).trim();
        }
        while (str.endsWith(" ")) {
            str = str.substring(0, str.length() - 1).trim();
        }
        return str;

    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符. <br>
     *
     * @param str 目标字符串
     * @return 去除空格、回车、换行符、制表符的字符串
     * @author shaorulong 2014年3月23日 下午5:32:02 <br>
     */
    public static String replaceBlank(final String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 正则表达式 判断邮箱格式是否正确
     */
    public static boolean isEmail(String email) {
        String str = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 手机号格式检查 .
     */
    public static boolean isMobileNum(String mobiles) {
        boolean isValid = false;
        String expression = "((^(13|15|17|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = mobiles;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 验证邮政编码
     */
    public static boolean checkPost(String post) {
        if (post.matches("[1-9]\\d{5}(?!\\d)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将double类型的大额金额转换成字符串展示
     *
     * @param dou 金额
     * @return 返回非科学计数法的字符串
     */
    public static String double2Str(final double dou) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 设置保留两位小数
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        // true：设置输出的格式用逗号隔开
        nf.setGroupingUsed(true);
        return nf.format(dou);
    }
}
