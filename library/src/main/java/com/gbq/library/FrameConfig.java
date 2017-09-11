package com.gbq.library;

import android.content.Context;
import android.os.Environment;

import com.gbq.library.utils.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 类说明：框架常量
 * Author: Kuzan
 * Date: 2017/6/17 09:52.
 */
public class FrameConfig {
    public static String sdPath = Environment.getExternalStorageDirectory().getPath();
    public static String netUnicode = "UTF-8";
    public static boolean openCrashHandler = false; // 是否开启全局异常处理
    public static String server; // 服务器地址
    public static boolean isPastern;//是否允许开启服务器后门
    public static String server_file; // file服务器地址
    public static String logPath = sdPath; // 日志输出目录，默认sdcard根目录
    public static String cachePath = "responses"; // 缓存路径
    public static int cacheSize = 10 * 1024 * 1024; // http缓存大小，默认10M
    public static String imageCachePath; // 图片缓存路径
    public static int imageCacheSize = 50; // 图片缓存大小，默认50M

    public static String appSign; // app与服务器约定的签名

    public static Context appContext; // 全局上下文
    public static boolean isCache = false;

    /**
     * 用户自定义的一些属性 .
     */
    private static HashMap<String, String> dyamicValue = new HashMap<String, String>();

    private FrameConfig() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 传入系统assets目录下的一个文件全路径.. <br>
     *
     * @param ct       上下文
     * @param filepath 不包含assets的全路径
     * @return 是否解析成功
     */
    public static boolean initSystemConfig(final Context ct, final String filepath) {
        try {
            appContext = ct;
            return initSystemConfig(ct.getResources().getAssets().open(filepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 传入系统raw目录下的一个XML的资源ID,并且对其进行解析. <br>
     *
     * @param ct  上下文
     * @param sid XML的资源ID
     * @return 是否解析成功
     */
    public static boolean initSystemConfig(final Context ct, final int sid) {
        appContext = ct;
        return initSystemConfig(ct.getResources().openRawResource(sid));
    }

    /**
     * 获取自定义值的属性 . <br>
     *
     * @param key KEY
     * @return 值
     */
    public static String getDynamicValue(final String key) {
        return dyamicValue.get(key);
    }

    public static boolean initSystemConfig(final InputStream inputStream) {
        DocumentBuilder builder = null;
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
            final Element root = document.getDocumentElement();
            final NodeList nodes = root.getElementsByTagName("system");
            final NodeList sysNodes = nodes.item(0).getChildNodes();
            final int sysNodeSize = sysNodes.getLength();
            for (int i = 0; i < sysNodeSize; i++) {
                final Node item = sysNodes.item(i);
                if ("netUnicode".endsWith(item.getNodeName())) {
                    netUnicode = item.getFirstChild().getNodeValue();
                } else if ("server".endsWith(item.getNodeName())) {
                    server = item.getFirstChild().getNodeValue();
                } else if ("is_postern".endsWith(item.getNodeName())) {
                    if (!StringUtils.isEmptyString(item.getFirstChild().getNodeValue())) {
                        isPastern = "1".equals(item.getFirstChild().getNodeValue().trim());
                    }
                } else if ("cache_path".endsWith(item.getNodeName())) {
                    cachePath = item.getFirstChild().getNodeValue();
                } else if ("image_cache_path".endsWith(item.getNodeName())) {
                    imageCachePath = item.getFirstChild().getNodeValue();
                } else if ("image_cache_size".endsWith(item.getNodeName())) {
                    try {
                        imageCacheSize = Integer.parseInt(item.getFirstChild().getNodeValue());
                        //配置的图片缓存文件不能大于250M.
                        if (imageCacheSize > 250) {
                            imageCacheSize = 250;
                        }
                    } catch (NumberFormatException e) {
                    }
                } else if ("server_file".endsWith(item.getNodeName())) {
                    server_file = item.getFirstChild().getNodeValue();
                } else if ("app_sign".endsWith(item.getNodeName())) {
                    appSign = item.getFirstChild().getNodeValue();
                } else if ("logPath".endsWith(item.getNodeName())) {
                    logPath = item.getFirstChild().getNodeValue();
                } else if ("debug".endsWith(item.getNodeName())) {
                    if (!StringUtils.isEmptyString(item.getFirstChild().getNodeValue())) {
                        openCrashHandler = "1".equals(item.getFirstChild().getNodeValue().trim());
                    }
                } else if (!StringUtils.isEmptyString(item.getNodeName()) && item.getFirstChild() != null
                        && item.getFirstChild().getNodeValue() != null) {
                    dyamicValue.put(item.getNodeName(), item.getFirstChild().getNodeValue().trim());
                }
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
