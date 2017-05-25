package com.gbq.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.gbq.library.R;

/**
 * 类说明：加载动画
 * Author: Kuzan
 * Date: 2017/5/25 18:00.
 */
public class LoadingDialog extends Dialog {

    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_loading);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        try{
            int dividerID=context.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider=findViewById(dividerID);
            divider.setBackgroundColor(Color.TRANSPARENT);
        }catch(Exception e){
            //上面的代码，是用来去除Holo主题的蓝色线条
            e.printStackTrace();
        }

        setCanceledOnTouchOutside(false);
        ImageView imageView = (ImageView) findViewById(R.id.img_loading);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        animationDrawable.start();
    }
}
