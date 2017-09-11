package com.gbq.library.imageselect;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gbq.library.R;
import com.gbq.library.imageselect.utils.ImageSelectUtil;
import com.gbq.library.imageselect.utils.ImageUtil;
import com.gbq.library.imageselect.widget.CutImageView;
import com.gbq.library.toast.ToastUtils;
import com.gbq.library.utils.FileUtils;
import com.gbq.library.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;


/**
 * 类说明：图片处理界面
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class ProcessImageActivity extends Activity {

    Button btnConfirm;
    ImageView btnBack;
    CutImageView imageView;

    public static final int START_CAMERA = 1;
    public static final int OPEN_PHOTO = 2;
    private final int TAKE_PHOTO = 110;

    private int mType = OPEN_PHOTO;
    private boolean mIsCut = false;

    private int mRequestCode;
    private int CODE_FOR_CAMERA_PERMISSION = 0x00000235;
    private int CODE_FOR_WRITE_PERMISSION = 0x00001234; // 读取外置sd卡权限

    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_process_img);

        Intent intent = getIntent();

        mType = intent.getIntExtra("type", OPEN_PHOTO);
        mIsCut = intent.getBooleanExtra("isCut", false);
        mRequestCode = intent.getIntExtra("requestCode", 0);
        if (mType == START_CAMERA) {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);
            if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                imageUri = Uri.parse(getCacheDir().getPath() + File.separator + System.currentTimeMillis() + ".jpg");
                imageUri = Uri.parse("file:///sdcard/" + System.currentTimeMillis() + ".jpg");
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(imageIntent, TAKE_PHOTO);
            } else {
                ActivityCompat.requestPermissions(ProcessImageActivity.this, new String[]{Manifest.permission.CAMERA}, CODE_FOR_CAMERA_PERMISSION);
            }
        } else {
            ImageSelectActivity.openActivity(this, mRequestCode);
        }

        initView();
    }

    private void initView() {
        imageView = (CutImageView) findViewById(R.id.process_img);
        btnConfirm = (Button) findViewById(R.id.commit);
        btnBack = (ImageView) findViewById(R.id.btn_back);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConfirm.setEnabled(false);
                confirm(imageView.clipImage());
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO) {
            if (imageUri != null) {
                String url = ImageUtil.getRealFilePath(this, imageUri);
                if(StringUtils.isNotEmptyString(url)) {
                    int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                        getCameraImage();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION);
                    }
                } else {
                    ToastUtils.ToastMessage(this, "获取图片失败");
                    finish();
                }
            }
        } else if (data != null && requestCode == mRequestCode) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelectUtil.SELECT_RESULT);
            if (mIsCut) {
                Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(images.get(0), 720, 1080);
                if(bitmap != null) {
                    imageView.setBitmapData(bitmap, getIntent().getIntExtra("cutType", CutImageView.RECT_TYPE),
                            getIntent().getIntExtra("width", -1), getIntent().getIntExtra("height", -1));
                } else {
                    ToastUtils.ToastMessage(this, "获取图片失败");
                    finish();
                }
            } else {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(ImageSelectUtil.SELECT_RESULT, images);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            finish();
        }
    }

    private void getCameraImage(){
        if (imageUri != null) {
            String url = ImageUtil.getRealFilePath(this, imageUri);
            if (StringUtils.isNotEmptyString(url)) {
                Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(url, 720, 1080);
                if (bitmap != null) {
                    if (mIsCut) {
                        imageView.setBitmapData(bitmap, getIntent().getIntExtra("cutType", CutImageView.RECT_TYPE),
                                getIntent().getIntExtra("width", -1), getIntent().getIntExtra("height", -1));
                    } else {
                        confirm(bitmap);
                    }
                    deleteTempImage();
                } else {
                    ToastUtils.ToastMessage(this, "获取图片失败");
                    finish();
                }
            } else {
                ToastUtils.ToastMessage(this, "获取图片失败");
                finish();
            }
        } else {
            finish();
        }
    }

    private void deleteTempImage(){
        if (imageUri != null) {
            String url = ImageUtil.getRealFilePath(this, imageUri);
            if (StringUtils.isNotEmptyString(url)) {
                FileUtils.deleteFile(url);
            }
        }
    }

    public void confirm(Bitmap bitmap) {

        String imagePath = null;

        if (bitmap != null) {
            imagePath = ImageUtil.saveImage(bitmap, getCacheDir().getPath() + File.separator + "image_select");
            bitmap.recycle();
            bitmap = null;
        }

        if (StringUtils.isNotEmptyString(imagePath)) {
            ArrayList<String> selectImages = new ArrayList<>();
            selectImages.add(imagePath);
            Intent intent = new Intent();
            intent.putStringArrayListExtra(ImageSelectUtil.SELECT_RESULT, selectImages);
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    public static void openThisActivity(Activity context, int requestCode, int type, boolean isCut, int cutType, int width, int height) {
        Intent intent = new Intent(context, ProcessImageActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("isCut", isCut);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("cutType", cutType);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CODE_FOR_CAMERA_PERMISSION) {
            if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用CAMERA
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PHOTO);
            } else {
                //用户不同意，向用户展示该权限作用
                finish();
            }
        } else if (requestCode == CODE_FOR_WRITE_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCameraImage();
            } else {
                ToastUtils.ToastMessage(this, "获取图片失败");
                finish();
            }
        }
    }

}
