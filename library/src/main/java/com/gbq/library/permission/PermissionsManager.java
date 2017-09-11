package com.gbq.library.permission;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gbq.library.beans.PermissionBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * 类说明：权限管理工具，使用RxJava2实现
 * Author: Kuzan
 * Date: 2017/8/31 9:48.
 */
public class PermissionsManager {
    /**
     * 方法一
     * */
//    private String[] PERMISSIONS = new String[] {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA };
//        PermissionsManager manager = new PermissionsManager(this);
//        manager.request(PERMISSIONS)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(@NonNull Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            // 所有权限都开启aBoolean才为true，否则为false
//                            Timber.e(getString(R.string.toast_permission_open));
//                            //ToastUtils.ToastMessage(MainActivity.this, R.string.toast_permission_open);
//                        } else {
//                            ToastUtils.ToastMessage(MainActivity.this, R.string.toast_permission_refuse);
//                        }
//                    }
//                });

        /**
         * 方法二
         * */
//        manager.requestEach(PERMISSIONS)
//                .subscribe(new Consumer<PermissionBean>() {
//                    @Override
//                    public void accept(@NonNull PermissionBean bean) throws Exception {
//                        if (bean.granted) {
//                            // 用户已经同意该权限
//                        } else if (bean.shouldShowRequestPermissionRationale) {
//                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                        } else {
//                            // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
//                        }
//                    }
//                });

    public static final String TAG = PermissionsManager.class.getSimpleName();
    public static final Object TRIGGER = new Object();

    PermissionsFragment mPermissionsFragment;

    public PermissionsManager(@NonNull Activity activity) {
        mPermissionsFragment = getPermissionsFragment(activity);
    }

    /**
     * 实例化
     * */
    private PermissionsFragment getPermissionsFragment(Activity activity) {
        PermissionsFragment permissionsFragment = findPermissionsFragment(activity);
        boolean isNewInstance = permissionsFragment == null;

        if (isNewInstance) {
            permissionsFragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return permissionsFragment;
    }

    private PermissionsFragment findPermissionsFragment(Activity activity) {
        return (PermissionsFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    /**
     * 打印日志
     * */
    public void setLogging(boolean logging) {
        mPermissionsFragment.setLogging(logging);
    }

    /**
     * 如果所有权限都被授予，则返回true，如果没有，则为false
     * <p>
     * 如果没有请求到一个或多个权限，请调用相关的框架方法来询问用户是否允许权限。
     */
    @SuppressWarnings("WeakerAccess")
    public <T> ObservableTransformer<T, Boolean> ensure(final String... permissions) {
        return new ObservableTransformer<T, Boolean>() {
            @Override
            public ObservableSource<Boolean> apply(Observable<T> o) {
                return request(o, permissions)
                        // Transform Observable<Permission> to Observable<Boolean>
                        .buffer(permissions.length)
                        .flatMap(new Function<List<PermissionBean>, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(List<PermissionBean> permissions) throws Exception {
                                if (permissions.isEmpty()) {
                                    return Observable.empty();
                                }
                                // 如果所有权限都被授予，则返回true
                                for (PermissionBean bean : permissions) {
                                    if (!bean.granted) {
                                        return Observable.just(false);
                                    }
                                }
                                return Observable.just(true);
                            }
                        });
            }
        };
    }

    /**
     * 一个一个权限顺序申请
     * <p>
     * 如果没有请求到一个或多个权限，请调用相关的框架方法来询问用户是否允许权限。
     */
    @SuppressWarnings("WeakerAccess")
    public <T> ObservableTransformer<T, PermissionBean> ensureEach(final String... permissions) {
        return new ObservableTransformer<T, PermissionBean>() {
            @Override
            public ObservableSource<PermissionBean> apply(Observable<T> o) {
                return request(o, permissions);
            }
        };
    }

    /**
     * 在应用程序初始化阶段调用请求权限。
     * 最好在onCreate()中请求
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<Boolean> request(final String... permissions) {
        return Observable.just(TRIGGER).compose(ensure(permissions));
    }

    /**
     * 在应用程序初始化阶段调用请求权限。
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<PermissionBean> requestEach(final String... permissions) {
        return Observable.just(TRIGGER).compose(ensureEach(permissions));
    }

    /**
     * 请求权限
     * */
    private Observable<PermissionBean> request(final Observable<?> trigger, final String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("PermissionsManager.request/requestEach requires at least one input permission");
        }
        return oneOf(trigger, pending(permissions))
                .flatMap(new Function<Object, Observable<PermissionBean>>() {
                    @Override
                    public Observable<PermissionBean> apply(Object o) throws Exception {
                        return requestImplementation(permissions);
                    }
                });
    }

    /**
     * 等待用户去申请的权限
     * */
    private Observable<?> pending(final String... permissions) {
        for (String p : permissions) {
            if (!mPermissionsFragment.containsByPermission(p)) {
                return Observable.empty();
            }
        }
        return Observable.just(TRIGGER);
    }

    /**
     * 请求一个权限
     * */
    private Observable<?> oneOf(Observable<?> trigger, Observable<?> pending) {
        if (trigger == null) {
            return Observable.just(TRIGGER);
        }
        return Observable.merge(trigger, pending);
    }

    /**
     * 请求权限执行
     * */
    @TargetApi(Build.VERSION_CODES.M)
    private Observable<PermissionBean> requestImplementation(final String... permissions) {
        List<Observable<PermissionBean>> list = new ArrayList<>(permissions.length);
        List<String> unrequestedPermissions = new ArrayList<>();

        // 在多重权限的情况下，我们为每一个创建一个可观察到的权限。
        // 最后，观测值组合起来具有独特的响应。
        for (String permission : permissions) {
            mPermissionsFragment.log("Requesting permission " + permission);
            if (isGranted(permission)) {
                // 已经申请到的权限或者android 6.0以下
                // 返回已授予权限的对象。
                list.add(Observable.just(new PermissionBean(permission, true, false)));
                continue;
            }

            if (isRevoked(permission)) {
                // 拒绝权限，返回一个拒绝权限对象。
                list.add(Observable.just(new PermissionBean(permission, false, false)));
                continue;
            }

            PublishSubject<PermissionBean> subject = mPermissionsFragment.getSubjectByPermission(permission);
            // 如果不存在，创建一个新的
            if (subject == null) {
                unrequestedPermissions.add(permission);
                subject = PublishSubject.create();
                mPermissionsFragment.setSubjectForPermission(permission, subject);
            }

            list.add(subject);
        }

        if (!unrequestedPermissions.isEmpty()) {
            String[] unrequestedPermissionsArray = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray);
        }
        return Observable.concat(Observable.fromIterable(list));
    }


    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> shouldShowRequestPermissionRationale(final Activity activity, final String... permissions) {
        if (!isMarshmallow()) {
            return Observable.just(false);
        }
        return Observable.just(shouldShowRequestPermissionRationaleImplementation(activity, permissions));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean shouldShowRequestPermissionRationaleImplementation(final Activity activity, final String... permissions) {
        for (String p : permissions) {
            if (!isGranted(p) && !activity.shouldShowRequestPermissionRationale(p)) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissionsFromFragment(String[] permissions) {
        mPermissionsFragment.log("requestPermissionsFromFragment " + TextUtils.join(", ", permissions));
        mPermissionsFragment.requestPermissions(permissions);
    }

    /**
     * 是否已授予权限
     * 如果已授予权限，则返回true。
     * <p>
     * android 6.0以下，返回true
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isGranted(String permission) {
        return !isMarshmallow() || mPermissionsFragment.isGranted(permission);
    }

    /**
     * 是否拒绝权限
     * 如果拒绝，返回true
     * <p>
     * android 6.0以下，返回false
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isRevoked(String permission) {
        return isMarshmallow() && mPermissionsFragment.isRevoked(permission);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults) {
        mPermissionsFragment.onRequestPermissionsResult(permissions, grantResults, new boolean[permissions.length]);
    }

}
