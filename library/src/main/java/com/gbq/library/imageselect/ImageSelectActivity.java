package com.gbq.library.imageselect;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbq.library.R;
import com.gbq.library.imageselect.adapter.ImageSelectAdapter;
import com.gbq.library.imageselect.beans.FolderBean;
import com.gbq.library.imageselect.utils.ImageSelectUtil;
import com.gbq.library.imageselect.window.FolderPop;
import com.gbq.library.toast.ToastUtils;
import com.gbq.library.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类说明：图片选择页面
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class ImageSelectActivity extends Activity {

    private RecyclerView imageList;
    private Button btnConfirm;
    private ImageView btnBack;
    private TextView tvFolderName;

    /**
     * 最大的图片选择数
     */
    private static final String MAX_SELECT_COUNT = "max_select_count";

    private ImageSelectAdapter mAdapter;

    private ArrayList<FolderBean> folders;

    private int mMaxCount;
    private int CODE_FOR_WRITE_PERMISSION = 0x00001234; // 读取外置sd卡权限

    private boolean isToSettings = false;
    private FolderPop mPop;

    /**
     * 启动图片选择器
     *
     * @param activity
     * @param requestCode
     * @param maxSelectCount 最大的图片选择数 小与0则不限制数量 等于0单选
     */
    public static void openActivity(Activity activity, int requestCode, int maxSelectCount) {
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra(MAX_SELECT_COUNT, maxSelectCount);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动图片选择器 （不传最大的图片选择数，默认为单选）
     *
     * @param activity
     * @param requestCode
     */
    public static void openActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_image_select);

        initView();

        mMaxCount = getIntent().getIntExtra(MAX_SELECT_COUNT, 0);

        imageList.post(new Runnable() {
            @Override
            public void run() {
                imageList.setLayoutManager(new GridLayoutManager(ImageSelectActivity.this, 3));
                mAdapter = new ImageSelectAdapter(ImageSelectActivity.this, imageList.getWidth() / 3, mMaxCount);
                imageList.setAdapter(mAdapter);
                if (folders != null && !folders.isEmpty()) {
                    setFolder(folders.get(0));
                }

                mAdapter.setOnImageSelectListener(new ImageSelectAdapter.OnImageSelectListener() {
                    @Override
                    public void OnImageSelect(String image, boolean isSelect, int selectCount) {
                        if (mMaxCount == 0) {
                            confirm();
                        } else {
                            setSelectCount(selectCount);
                        }
                    }
                });
            }
        });

        if (mMaxCount == 0) {
            btnConfirm.setVisibility(View.GONE);
        } else {
            setSelectCount(0);
        }

        getImages();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isToSettings) {
            isToSettings = false;
            getImages();
        }
    }

    private void initView() {
        imageList = (RecyclerView) findViewById(R.id.image_list);
        btnConfirm = (Button) findViewById(R.id.commit);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        tvFolderName = (TextView) findViewById(R.id.tv_folder_name);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvFolderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folders != null) {
                    if (mPop == null) {
                        mPop = new FolderPop(ImageSelectActivity.this);
                        mPop.setFolders(folders);
                        mPop.setOnSelectListener(new FolderPop.OnSelectListener() {
                            @Override
                            public void onSelect(FolderBean folder) {
                                setFolder(folder);
                            }
                        });
                    }
                    mPop.showAsDropDown(tvFolderName);
                }
            }
        });
    }

    private void setFolder(FolderBean folder) {
        if (folder != null && mAdapter != null) {
            tvFolderName.setText(folder.getName());
            imageList.scrollToPosition(0);
            mAdapter.refresh(folder.getImages());
        }
    }

    /**
     * 发生没有权限等异常时，显示一个提示dialog.
     */
    private void showExceptionDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_exception, null, false);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view)
                .create();

        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startAppSettings();
                isToSettings = true;
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
            }
        });

        dialog.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void setSelectCount(int count) {
        if (mMaxCount > 0) {
            btnConfirm.setText("确定(" + count + "/" + mMaxCount + ")");
        } else {
            btnConfirm.setText("确定(" + count + ")");
        }
    }

    public void confirm() {

        if (mAdapter == null) {
            return;
        }

        ArrayList<String> selectImages = mAdapter.getSelectImages();
        if (selectImages.isEmpty()) {
            ToastUtils.ToastMessage(this, "请选择图片");
            return;
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra(ImageSelectUtil.SELECT_RESULT, selectImages);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CODE_FOR_WRITE_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGetImageThread();
            } else {
                showExceptionDialog();
            }
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.ToastMessage(this, "没有图片");
            return;
        }
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            startGetImageThread();
        } else {
            ActivityCompat.requestPermissions(ImageSelectActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION);
        }
    }

    private void startGetImageThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.Media._ID},
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED);

//                images.clear();

                ArrayList<String> images = new ArrayList<>();

                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    images.add(path);
                }
                mCursor.close();
                Collections.reverse(images);
                folders = splitFolder(images);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (folders != null && !folders.isEmpty()) {
//                            mAdapter.refresh(images);
                            setFolder(folders.get(0));
                        }
                    }
                });

            }
        }).start();
    }

    private ArrayList<FolderBean> splitFolder(ArrayList<String> images) {
        ArrayList<FolderBean> folders = new ArrayList<>();
        folders.add(new FolderBean("全部", images));

        if (images != null && !images.isEmpty()) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                String path = images.get(i);
                String name = getFolderName(path);
                if (StringUtils.isNotEmptyString(name)) {
                    FolderBean folder = getFolder(name, folders);
                    if (folder != null) {
                        folder.addImage(path);
                    } else {
                        FolderBean newFolder = new FolderBean(name);
                        newFolder.addImage(path);
                        folders.add(newFolder);
                    }
                }
            }
        }
        return folders;
    }

    private String getFolderName(String path) {
        if (StringUtils.isNotEmptyString(path)) {
            String[] strings = path.split(File.separator);
            if (strings.length >= 2) {
                return strings[strings.length - 2];
            }
        }
        return "";
    }

    private FolderBean getFolder(String name, List<FolderBean> folders) {
        if (folders != null && !folders.isEmpty()) {
            int size = folders.size();
            for (int i = 0; i < size; i++) {
                FolderBean folder = folders.get(i);
                if (name.equals(folder.getName())) {
                    return folder;
                }
            }
        }
        return null;
    }
}
