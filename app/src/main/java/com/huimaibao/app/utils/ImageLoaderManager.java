package com.huimaibao.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.huimaibao.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;

/**
 * 下载网络图片
 */
public class ImageLoaderManager {
    public static void loadImage(String url, ImageView imageView, int id) {
        if (XEmptyUtils.isSpace(url)) {
            imageView.setImageResource(id);
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//让图片进行内存缓存
                .cacheOnDisk(true)//让图片进行sdcard缓存
                .showImageForEmptyUri(id)//图片地址有误
                .showImageOnFail(id)//当图片加载出现错误的时候显示的图片
                .showImageOnLoading(id)//图片正在加载的时候显示的图片
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public static void loadImage(String url, ImageView imageView) {
        if (XEmptyUtils.isSpace(url)) {
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//让图片进行内存缓存
                .cacheOnDisk(true)//让图片进行sdcard缓存
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }


    public static ImageLoader getImageLoader(Context context) {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getPhotoImageOption() {
        Integer extra = 1;
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                .extraForDownloader(extra)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    public static void displayImage(Context context, ImageView imageView, String url, DisplayImageOptions options) {
        getImageLoader(context).displayImage(url, imageView, options);
    }

    public static void displayImage(Context context, ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener) {
        getImageLoader(context).displayImage(url, imageView, options, listener);
    }

    //
    static float ratio;

    public static float getAspectRatio(String url) {
        //图片展示区域
        final int parentWidth = XDensityUtils.getScreenWidth() - XDensityUtils.dp2px(84);
        ratio = 1000 / 1376f;
        //这里是只显示一张图片的情况，显示图片的宽高可以根据实际图片大小自由定制，parentWidth 为该layout的宽度
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                float w = bitmap.getWidth();
                float h = bitmap.getHeight();
                float newW;
                float newH;
                if (h > w * 3) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                    ratio = newW / newH;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                    ratio = newW / newH;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                    ratio = newW / newH;
                }
            }


            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        return ratio;
    }

}
