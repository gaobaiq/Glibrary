package com.gbq.library.imageselect.utils;

import android.app.Activity;

import com.gbq.library.imageselect.ImageSelectActivity;
import com.gbq.library.imageselect.ProcessImageActivity;
import com.gbq.library.imageselect.widget.CutImageView;

/**
 * 类说明：文件选择工具类
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class ImageSelectUtil {

    /**
     * 图片选择的结果
     */
    public static final String SELECT_RESULT = "select_result";

    public static final int RECT_TYPE = CutImageView.RECT_TYPE;
    public static final int CIRCLE_TYPE = CutImageView.CIRCLE_TYPE;

    /**
     * 打开相册，选择图片,可多选，不可剪切。
     *
     * @param activity
     * @param requestCode
     * @param maxSelectCount 选择图片的最大数量
     */
    public static void openPhoto(Activity activity, int requestCode, int maxSelectCount) {
        ImageSelectActivity.openActivity(activity, requestCode, maxSelectCount);
    }

    /**
     * 打开相册，选择图片，单选，可剪切。
     *
     * @param activity
     * @param requestCode
     * @param isCut       是否要对图片进行剪切
     */
    public static void openPhoto(Activity activity, int requestCode, boolean isCut) {
        if (isCut) {
            ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.OPEN_PHOTO, isCut, RECT_TYPE, -1, -1);
        } else {
            ImageSelectActivity.openActivity(activity, requestCode);
        }
    }

    /**
     * 打开相册，选择图片，单选，可剪切。
     *
     * @param activity
     * @param requestCode
     * @param isCut       是否要对图片进行剪切
     * @param cutType     剪切类型，RECT_TYPE（正方形）、CIRCLE_TYPE（圆形）
     */
    public static void openPhoto(Activity activity, int requestCode, boolean isCut, int cutType) {
        if (isCut) {
            ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.OPEN_PHOTO, isCut, cutType, -1, -1);
        } else {
            ImageSelectActivity.openActivity(activity, requestCode);
        }
    }

    /**
     * 打开相册，选择图片，单选，可剪切。
     *
     * @param activity
     * @param requestCode
     * @param isCut       是否要对图片进行剪切
     * @param cutType     剪切类型，RECT_TYPE（正方形）、CIRCLE_TYPE（圆形）
     * @param cutSize     剪切大小，必须大于0，默认屏幕宽高最小值得1/2
     */
    public static void openPhoto(Activity activity, int requestCode, boolean isCut, int cutType, int cutSize) {
        if (isCut) {
            ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.OPEN_PHOTO, isCut, cutType, cutSize, cutSize);
        } else {
            ImageSelectActivity.openActivity(activity, requestCode);
        }
    }

    /**
     * 打开相册，选择图片，单选，矩阵剪切。
     *
     * @param activity
     * @param requestCode
     * @param width       剪切大小，必须大于0，默认屏幕宽高最小值得1/2
     * @param height      剪切大小，必须大于0，默认屏幕宽高最小值得1/2
     */
    public static void openPhoto(Activity activity, int requestCode, int width, int height) {
        ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.OPEN_PHOTO, true, RECT_TYPE, width, height);
    }

    /**
     * 调用相机拍照。
     *
     * @param activity
     * @param requestCode
     * @param isCut       是否要对图片进行剪切
     */
    public static void startCamera(Activity activity, int requestCode, boolean isCut) {
        ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.START_CAMERA, isCut, RECT_TYPE, -1, -1);
    }

    /**
     * 调用相机拍照。
     *
     * @param activity
     * @param requestCode
     * @param isCut       是否要对图片进行剪切
     * @param cutType     剪切类型，RECT_TYPE（正方形）、CIRCLE_TYPE（圆形）
     */
    public static void startCamera(Activity activity, int requestCode, boolean isCut, int cutType) {
        ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.START_CAMERA, isCut, cutType, -1, -1);
    }

    /**
     * 调用相机拍照。
     *
     * @param activity
     * @param requestCode
     * @param isCut       是否要对图片进行剪切
     * @param cutType     剪切类型，RECT_TYPE（正方形）、CIRCLE_TYPE（圆形）
     * @param cutSize     剪切大小，必须大于0，默认屏幕宽高最小值得1/2
     */
    public static void startCamera(Activity activity, int requestCode, boolean isCut, int cutType, int cutSize) {
        ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.START_CAMERA, isCut, cutType, cutSize, cutSize);
    }

    /**
     * 调用相机拍照，矩阵剪切。
     *
     * @param activity
     * @param requestCode
     * @param width       剪切大小，必须大于0，默认屏幕宽高最小值得1/2
     * @param height      剪切大小，必须大于0，默认屏幕宽高最小值得1/2；
     */
    public static void startCamera(Activity activity, int requestCode, int width, int height) {
        ProcessImageActivity.openThisActivity(activity, requestCode, ProcessImageActivity.START_CAMERA, true, RECT_TYPE, width, height);
    }
}
