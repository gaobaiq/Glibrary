package com.gbq.library.permission;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gbq.library.beans.PermissionBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

/**
 * 类说明：权限申请处理
 * Author: Kuzan
 * Date: 2017/8/31 9:53.
 */
public class PermissionsFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_CODE = 42;

    /**包含所有当前权限请求，一旦被批准或拒绝，他们就从中移除*/
    private Map<String, PublishSubject<PermissionBean>> mSubjects = new HashMap<>();
    private boolean mLogging;

    public PermissionsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * 请求权限组
     * */
    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    /**
     * 请求权限返回
     * */
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PERMISSIONS_REQUEST_CODE) return;

        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];

        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }

        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }

    /**
     * 请求权限返回处理
     * */
    public void onRequestPermissionsResult(String permissions[], int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0, size = permissions.length; i < size; i++) {
            log("onRequestPermissionsResult  " + permissions[i]);
            // 找到相应的权限
            PublishSubject<PermissionBean> subject = mSubjects.get(permissions[i]);
            if (subject == null) {
                // 没有对应的权限
                Log.e(PermissionsManager.TAG, "PermissionsManager.onRequestPermissionsResult invoked but didn't find the corresponding permission request.");
                return;
            }
            mSubjects.remove(permissions[i]);
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            subject.onNext(new PermissionBean(permissions[i], granted, shouldShowRequestPermissionRationale[i]));
            subject.onComplete();
        }
    }

    /**
     * 是否已开启权限
     * */
    @TargetApi(Build.VERSION_CODES.M)
    boolean isGranted(String permission) {
        return getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否拒绝该权限
     * */
    @TargetApi(Build.VERSION_CODES.M)
    boolean isRevoked(String permission) {
        return getActivity().getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }

    /**
     * 是否打印日志
     * */
    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    /**
     * 获取权限信息
     * */
    public PublishSubject<PermissionBean> getSubjectByPermission(@NonNull String permission) {
        return mSubjects.get(permission);
    }

    /**
     * 判断是否包含该权限
     * */
    public boolean containsByPermission(@NonNull String permission) {
        return mSubjects.containsKey(permission);
    }

    /**
     * 设定权限
     * */
    public PublishSubject<PermissionBean> setSubjectForPermission(@NonNull String permission, @NonNull PublishSubject<PermissionBean> subject) {
        return mSubjects.put(permission, subject);
    }

    /**
     * 打印日志
     * */
    void log(String message) {
        if (mLogging) {
            Log.d(PermissionsManager.TAG, message);
        }
    }
}
