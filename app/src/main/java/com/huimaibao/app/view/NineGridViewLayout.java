package com.huimaibao.app.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.library.act.ImageShowActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 一般项目就实现NineGridLayout类即可，如果没有特殊需求，不要改动NineGridLayout类
 */
public class NineGridViewLayout extends NineGridLayout {
    protected static final int MAX_W_H_RATIO = 3;
    private Context mContext;

    public NineGridViewLayout(Context context) {
        super(context);
    }

    public NineGridViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {

        //这里是只显示一张图片的情况，显示图片的宽高可以根据实际图片大小自由定制，parentWidth 为该layout的宽度
        ImageLoaderManager.displayImage(mContext, imageView, url, ImageLoaderManager.getPhotoImageOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        // true 代表按照九宫格默认大小显示(此时不要调用setOneImageLayoutParams)；false 代表按照自定义宽高显示。
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        ImageLoaderManager.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderManager.getPhotoImageOption());
    }

    @Override
    protected void onClickImage(int i, String url, ArrayList<String> urlList) {
        // Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(mContext, ImageShowActivity.class);
        intent.putStringArrayListExtra("infos", urlList);
        intent.putExtra("position", i);
        mContext.startActivity(intent);
        // mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


}

