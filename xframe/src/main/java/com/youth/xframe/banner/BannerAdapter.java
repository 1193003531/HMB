package com.youth.xframe.banner;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 *
 */
public class BannerAdapter extends PagerAdapter {

    private BannerViewPager bannerViewPager;
    private int mItemCount = 1, mItemMax = Integer.MAX_VALUE;
    private List<BannerItemBean> mData;
    private ImageView.ScaleType mScaleType;

    public List<BannerItemBean> getData() {
        return mData;
    }

    public void setData(List<BannerItemBean> data) {
        mData = data;
        if (mData != null && mData.size() != 0) {
            mItemCount = mData.size();
        }

        if (mItemCount == 1) {
            mItemMax = 1;
        } else {
            mItemMax = Integer.MAX_VALUE;
        }
    }

    private ImageView.ScaleType getScaleType() {
        return mScaleType == null ? ImageView.ScaleType.CENTER_CROP : mScaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
    }

    public BannerAdapter(BannerViewPager bannerViewPager) {
        this.bannerViewPager = bannerViewPager;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mItemMax;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((ImageView) object));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(getScaleType());
        bannerViewPager.displayImg(container.getContext(), imageView, mData.get(position % mItemCount).getImg_path());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bannerViewPager.OnBannerItemClick(view);
            }
        });
        container.addView(imageView);

        return imageView;
    }

}
