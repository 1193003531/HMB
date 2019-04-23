package com.huimaibao.app.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.library.act.ImageShowActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿微信朋友圈图片展示
 */
public class FriendsCircleImageLayout extends ViewGroup {

    private Activity mActivity;
    private int mTotalWidth;
    private int mSingleWidth;

    /**
     * 显示的行数
     */
    private int mColumnCount;
    /**
     * 默认间距
     */
    private final float DEFAULT_SPACING = 2.5f;
    private float mSpacing;
    /**
     * 图片宽高比(当为多张图片的时候为1)
     * 一般在url中会有宽高 可计算
     */
    private float mItemAspectRatio;
    /**
     * 最宽的时候相对可用空间比例（当childCount=1的时候）
     * 当只有一张图片的 最大显示宽度和高度相对于可用宽的的比例
     */
    private final float MAX_WIDTH_PERCENTAGE = 270f / 350;

    private int mItemWidth;
    private int mItemHeight;

    public void init(Activity activity) {
        this.mActivity = activity;
    }

    public FriendsCircleImageLayout(Context context) {
        this(context, null);
    }

    public FriendsCircleImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FriendsCircleImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SPACING,
                context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        final int width = MeasureSpec.getSize(widthMeasureSpec);

        //当只有一张图片的时候 显示一张相对比较大的图片
        //当大于1小于等于4张图片的时候 一排显示两张图片

        //当只有一张图片的时候
        if (count == 1) {
            mColumnCount = 1;
            int mItemMaxWidth = (int) (width * MAX_WIDTH_PERCENTAGE);
            int mItemMaxHeight = mItemMaxWidth;
            if (mItemAspectRatio < 1) {
                mItemHeight = mItemMaxHeight;
                mItemWidth = (int) (mItemHeight * mItemAspectRatio);
            } else {
                mItemWidth = mItemMaxWidth;
                mItemHeight = (int) (mItemMaxWidth / mItemAspectRatio);
            }
        } else {
            if (count == 4) {
                mColumnCount = 2;
            } else {
                mColumnCount = 3;
            }
            mItemWidth = (int) ((width - getPaddingLeft() - getPaddingRight() - 2 * mSpacing) / 3);
            mItemHeight = (int) (mItemWidth / mItemAspectRatio);
        }

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = mItemWidth;
            layoutParams.height = mItemHeight;
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    getDesiredHeight(mItemHeight), MeasureSpec.EXACTLY);
        }

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(
                    getDesiredWidth(mItemWidth), MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        final LayoutParams lp = child.getLayoutParams();
        //获取子控件的宽高约束规则
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private int getDesiredHeight(int mItemHeight) {
        int totalHeight = getPaddingTop() + getPaddingBottom();
        int count = getChildCount();
        if (count > 0) {
            int row = (count - 1) / mColumnCount;
            totalHeight = (int) ((row + 1) * mItemHeight + (row) * mSpacing) + totalHeight;
        }
        return totalHeight;
    }

    private int getDesiredWidth(int mItemWidth) {
        int totalWidth = getPaddingLeft() + getPaddingRight();
        int count = getChildCount();
        if (count > 0) {
            if (count < mColumnCount) {
                totalWidth = (int) (count * mItemWidth + (count - 1) * mSpacing) + totalWidth;
            } else {
                totalWidth = (int) (count * mItemWidth + (count - 1) * mSpacing) + totalWidth;
            }

        }
        return totalWidth;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mTotalWidth = r - l;
        mSingleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
        for (int i = 0; i < getChildCount(); i++) {
            View imageView = getChildAt(i);

            int column = i % mColumnCount;
            int row = i / mColumnCount;
            int left = (int) (getPaddingLeft() + column * (mSpacing + mItemWidth));
            int top = (int) (getPaddingTop() + row * (mSpacing + mItemHeight));

            imageView.layout(left, top, left + mItemWidth, top + mItemHeight);
        }
    }


    /**
     * 显示图片
     */
    public void setImageUrls(List<String> imageUrls) {
        removeAllViews();

        if (XEmptyUtils.isEmpty(imageUrls)) {
            return;
        }

        int size = imageUrls.size();
        if (size == 1) {
            //一般在url中会有宽高 可以算出宽高比
            //测试url固定用的 1000:1376
            mItemAspectRatio = 1000 / 1376f;
            //mItemAspectRatio = ImageLoaderManager.getAspectRatio(imageUrls.get(0).trim());
            //setOneImageSHow(imageUrls);
        } else {
            mItemAspectRatio = 1;
            //setMoreImageSHow(imageUrls);
        }
        setMoreImageSHow(imageUrls);
    }


    public int getItemWidth() {
        return mItemWidth;
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int columnCount) {
        mColumnCount = columnCount;
        invalidate();
    }

    public float getSpacing() {
        return mSpacing;
    }

    public void setSpacing(float spacing) {
        mSpacing = spacing;
        invalidate();
    }

    public float getItemAspectRatio() {
        return mItemAspectRatio;
    }

    public void setItemAspectRatio(float itemAspectRatio) {
        mItemAspectRatio = itemAspectRatio;
    }


    /**
     * 一张图片展示
     */
    private void setOneImageSHow(final List<String> imageUrls) {

        //图片展示区域
        final int parentWidth = XDensityUtils.getScreenWidth() - XDensityUtils.dp2px(84);
        final RatioImageView imageView = new RatioImageView(getContext());
//        LayoutParams params = getLayoutParams();
//        params.height = mItemHeight;
//        setLayoutParams(params);
//        imageView.layout(0, 0, mItemWidth, mItemHeight);

        ImageLoader.getInstance().displayImage(imageUrls.get(0).trim(), imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                //mItemAspectRatio=w/h;
                //int newW;
                //int newH;
                if (h > w * 3) {//h:w = 5:3
                    mItemWidth = parentWidth / 2;
                    mItemHeight = mItemWidth * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    mItemWidth = parentWidth * 2 / 3;
                    mItemHeight = mItemWidth * 2 / 3;
                } else {//newH:h = newW :w
                    mItemWidth = parentWidth / 2;
                    mItemHeight = h * mItemWidth / w;
                }

                mItemAspectRatio = mItemWidth / mItemHeight;
                imageView.setLayoutParams(new LayoutParams(mItemWidth, mItemHeight));
                imageView.layout(0, 0, mItemWidth, mItemHeight);

//                LayoutParams params = getLayoutParams();
//                params.height = mItemHeight;
//                setLayoutParams(params);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                addView(imageView);
                //点击查看大图
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickImage(0, (ArrayList) imageUrls);
                    }
                });
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

//        ImageLoader.getInstance().loadImage(imageUrls.get(0).trim(), new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                //mItemAspectRatio = 1000 / 1376f;
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                int w = bitmap.getWidth();
//                int h = bitmap.getHeight();
//                int newW;
//                int newH;
//                if (h > w * 3) {//h:w = 5:3
//                    newW = parentWidth / 2;
//                    newH = newW * 5 / 3;
//                    mItemAspectRatio = newW / newH;
//                } else if (h < w) {//h:w = 2:3
//                    newW = parentWidth * 2 / 3;
//                    newH = newW * 2 / 3;
//                    mItemAspectRatio = newW / newH;
//                } else {//newH:h = newW :w
//                    newW = parentWidth / 2;
//                    newH = h * newW / w;
//                    mItemAspectRatio = newW / newH;
//                }
//                imageView.setLayoutParams(new LayoutParams(newW, newH));
//                imageView.layout(0, 0, newW, newH);
//                // setOneImageLayoutParams(imageView, newW, newH);
//                // imageView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//            }
//        });


    }

    /**
     * 多张图片展示
     */
    private void setMoreImageSHow(final List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            final RatioImageView imageView = new RatioImageView(getContext());
            imageView.setId(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //ImageLoaderManager.getImageLoader(getContext()).displayImage(imageUrls.get(i).trim(), imageView, ImageLoaderManager.getPhotoImageOption());
            ImageLoaderManager.loadImage(imageUrls.get(i).trim(), imageView);
            addView(imageView);
            //点击查看大图
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickImage(imageView.getId(), (ArrayList) imageUrls);
                }
            });
        }
    }

    protected void setOneImageLayoutParams(RatioImageView imageView, int width, int height) {
        imageView.setLayoutParams(new LayoutParams(width, height));
        imageView.layout(0, 0, width, height);

        LayoutParams params = getLayoutParams();
//        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    /**
     * 点击图片放大
     */
    protected void onClickImage(int i, ArrayList<String> urlList) {
        // Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(getContext(), ImageShowActivity.class);
        intent.putStringArrayListExtra("infos", urlList);
        intent.putExtra("position", i);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

