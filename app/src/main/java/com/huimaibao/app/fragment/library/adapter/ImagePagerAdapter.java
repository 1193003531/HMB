package com.huimaibao.app.fragment.library.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.library.adapter.view.Options;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youth.xframe.utils.imageshow.ImageShowViewPager;
import com.youth.xframe.utils.imageshow.TouchImageView;

import java.util.ArrayList;

/**
 * 图片浏览的PagerAdapter
 */
public class ImagePagerAdapter extends PagerAdapter {
    Activity mActivity;
    ArrayList<String> imgsUrl;
    LayoutInflater inflater = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    //view内控件
    TouchImageView full_image;
    TextView progress_text;
    ProgressBar progress;
    TextView retry;

    public ImagePagerAdapter(Activity activity, ArrayList<String> imgsUrl) {
        this.mActivity = activity;
        this.imgsUrl = imgsUrl;
        inflater = LayoutInflater.from(activity);
        options = Options.getListOptions();
    }

    /**
     * 动态加载数据
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((ImageShowViewPager) container).mCurrentView = ((View) object).findViewById(R.id.full_image);
    }

    @Override
    public int getCount() {
        return imgsUrl == null ? 0 : imgsUrl.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.from(mActivity).inflate(R.layout.fragment_lib_imageshow_item, null);
        full_image = view.findViewById(R.id.full_image);
        progress_text = view.findViewById(R.id.progress_text);
        progress = view.findViewById(R.id.progress);
        retry = view.findViewById(R.id.retry);//加载失败
        //progress_text.setText(String.valueOf(position));
        progress.setVisibility(View.GONE);
        // retry.setVisibility(View.GONE);
        // progress_text.setVisibility(View.GONE);
        // ImageLoaderManager.loadImage(imgsUrl.get(position).trim(),full_image,R.drawable.ic_full_image_failed);
        imageLoader.displayImage(imgsUrl.get(position).trim(), full_image, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub
                progress.setVisibility(View.VISIBLE);
                progress_text.setVisibility(View.VISIBLE);
                full_image.setVisibility(View.VISIBLE);
                retry.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                // TODO Auto-generated method stub
                progress.setVisibility(View.GONE);
                progress_text.setVisibility(View.GONE);
                full_image.setVisibility(View.GONE);
                retry.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progress.setVisibility(View.GONE);
                progress_text.setVisibility(View.GONE);
                full_image.setVisibility(View.VISIBLE);
                retry.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progress.setVisibility(View.GONE);
                progress_text.setVisibility(View.GONE);
                full_image.setVisibility(View.GONE);
                retry.setVisibility(View.VISIBLE);
            }
        });

        full_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
