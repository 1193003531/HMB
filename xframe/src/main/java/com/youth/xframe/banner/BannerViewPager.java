package com.youth.xframe.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.xframe.R;

import java.util.List;

/**
 *
 */
public class BannerViewPager extends FrameLayout {

    private static final int MSG_WHAT = 0;
    private int delayMillis;
    private ViewPager mViewPager;
    private ViewPager.PageTransformer mPageTransformer;
    private BannerAdapter mBannerAdapter;
    private int mItemCount;
    private LinearLayout mIndicators;
    private TextView mTitle;
    private boolean mAutoPlay;
    private OnBannerItemClickListener mOnBannerClickListener;
    private BaseIndicator mIndicatorView;
    private ImageLoaderInterface mImageLoader;
    private boolean mHaveTitle;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mAutoPlay) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                this.sendEmptyMessageDelayed(MSG_WHAT, delayMillis);
            }
        }
    };

    public BannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHaveTitle = true;
        mAutoPlay = true;
        mItemCount = 1;
        delayMillis = 5000;
        initView();
        initListener();
        mHandler.sendEmptyMessageDelayed(MSG_WHAT, delayMillis);
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTitleSlogan(position);
                for (int i = 0; i < mIndicators.getChildCount(); i++) {
                    if (i == position % mItemCount) {
                        ((BaseIndicator) mIndicators.getChildAt(i)).setState(true);
                    } else {
                        ((BaseIndicator) mIndicators.getChildAt(i)).setState(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initView() {
        View.inflate(getContext(), R.layout.layout_bannerviewpager, this);
        mViewPager = findViewById(R.id.viewPager);
        mIndicators = findViewById(R.id.bannerIndicators);
        mTitle = findViewById(R.id.bannerTitle);
    }

    public BannerViewPager setData(List<BannerItemBean> data, ImageLoaderInterface imageLoader) {
        mImageLoader = imageLoader;
        mBannerAdapter = new BannerAdapter(this);
        mBannerAdapter.setData(data);
        mItemCount = data.size();
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.setCurrentItem(mItemCount * 1000);
        if (data.size() > 1) {
            setIndicators(data.size());
        }
        setTitleSlogan(0);
        return this;
    }

    private void setTitleSlogan(int i) {
        if (mHaveTitle) {
            if (mTitle.getVisibility() == INVISIBLE) {
                mTitle.setVisibility(VISIBLE);
            }
            String s = mBannerAdapter.getData().get(i % mItemCount).getTitle();
            mTitle.setText(s);
        } else if (mTitle.getVisibility() == VISIBLE) {
            mTitle.setVisibility(INVISIBLE);
        }
    }

    private void setIndicators(int dataCount) {
        mIndicators.removeAllViews();
        for (int i = 0; i < dataCount; i++) {
            if (mIndicatorView == null) {
                Indicator indicator = new Indicator(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (getRatioDimension(getContext(), 20, false),
                                getRatioDimension(getContext(), 20, false));
                layoutParams.setMargins(getRatioDimension(getContext(), 10, true), 0,
                        getRatioDimension(getContext(), 10, true), 25);
                indicator.setLayoutParams(layoutParams);
                mIndicators.addView(indicator);
            } else {
                Log.e("iii", "count" + i);

                BaseIndicator baseIndicator = mIndicatorView;
                ViewParent vp = baseIndicator.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(baseIndicator);
                }
                mIndicators.addView(baseIndicator);
                Log.e("iii", "" + mIndicators.getChildCount());
            }
        }
        Log.e("iii", "" + mIndicators.getChildCount());
        ((BaseIndicator) mIndicators.getChildAt(0)).setState(true);
    }


    public void displayImg(Context context, ImageView imageView, Object s) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageLoader.displayImage(context, s, imageView);
    }

    public interface OnBannerItemClickListener {
        void OnClickLister(View view, int currentItem);
    }

    protected void OnBannerItemClick(View view) {
        if (mOnBannerClickListener != null) {
            mOnBannerClickListener.OnClickLister(view, mViewPager.getCurrentItem() % mItemCount);
        }
    }

    public BannerViewPager setOnBannerItemClickListener(OnBannerItemClickListener onBannerClickListener) {
        mOnBannerClickListener = onBannerClickListener;
        return this;
    }

    public static int getRatioDimension(Context context, int value, boolean isWidth) {
        DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        float STANDARD_WIDTH = 1080F;
        float STANDARD_HEIGHT = 1920F;
        if (isWidth) {
            return (int) (value / STANDARD_WIDTH * widthPixels);
        } else {
            return (int) (value / STANDARD_HEIGHT * heightPixels);
        }
    }

    public boolean isAutoPlay() {
        return mAutoPlay;
    }

    public BannerViewPager setAutoPlay(boolean autoPlay) {
        if (mItemCount > 1) {
            mAutoPlay = autoPlay;
        } else {
            mAutoPlay = false;
        }
        return this;
    }


    protected BaseIndicator getIndicatorView() {
        return mIndicatorView;
    }

    private BannerViewPager setIndicatorView(BaseIndicator indicatorView) {
        mIndicatorView = indicatorView;
        Log.e("ooo", "" + mItemCount);
        mViewPager.setCurrentItem(mItemCount * 1000);
        setIndicators(mItemCount);
        setTitleSlogan(0);
        return this;
    }

    public BannerViewPager setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        mPageTransformer = pageTransformer;
        mViewPager.setPageTransformer(true, mPageTransformer);
        return this;
    }

    public BannerViewPager setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    public boolean isHaveTitle() {
        return mHaveTitle;
    }

    public BannerViewPager setHaveTitle(boolean haveTitle) {
        mHaveTitle = haveTitle;
        setTitleSlogan(mViewPager.getCurrentItem());
        return this;
    }

    private void setIndicatorsGravity(int gravity) {
        mIndicators.setGravity(gravity);
    }

    public BannerViewPager setPointGravity(int gravity) {
        setIndicatorsGravity(gravity);
        return this;
    }


    public BannerViewPager setScaleType(ImageView.ScaleType scaleType) {
        mBannerAdapter.setScaleType(scaleType);
        return this;
    }

    public BannerViewPager setViewPagerHeight(int height) {
        //为ViewPager设置高度
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = height;
        mViewPager.setLayoutParams(params);
        return this;
    }

}
