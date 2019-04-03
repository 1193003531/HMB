package com.huimaibao.app.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.huimaibao.app.R;
import com.youth.xframe.utils.XDensityUtils;

/**
 *
 */
public class SlideTabView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    //标签布局容器
    private LinearLayout linearLayout;
    //指示器画笔
    private Paint paint;
    //tab容器
    private List<String> list;
    //text容器
    private List<TextView> textViews;
    //当前位置
    private int currIndex = 0;
    //上一次位置
    private int lastIndex = 0;
    //满屏显示数量
    private float maxCount = 3.5f;
    //偏移百分比
    private float offSet;
    //非选中字体颜色
    private int noCurrColor = R.color.FF747474;
    //选中字体颜色
    private int currColor = R.color.FF274FF3;
    //指示器颜色
    private int offLineColor = R.color.FF274FF3;
    //背景色
    private int background = R.color.white;
    //字体大小
    private int txtSize = 16;
    //viewPager
    private ViewPager viewPager;
    //上下文
    private Context context;

    public SlideTabView(Context context) {
        this(context, null);
    }

    public SlideTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SlideTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //初始化View
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        textViews = new ArrayList<>();

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        setBackgroundResource(background);
        addView(linearLayout);
    }

    //初始化tab数据
    public void initTab(List<String> list, ViewPager viewPager) {
        this.list = list;
        this.viewPager = viewPager;

        setListener();
        addTab();
    }

    //设置监听
    private void setListener() {
        viewPager.setOnPageChangeListener(this);

//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @SuppressWarnings("deprecation")
//            @SuppressLint("NewApi")
//            @Override
//            public void onGlobalLayout() {
//
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                } else {
//                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//
//                currIndex = viewPager.getCurrentItem();
//                scrollToChild(currIndex, 0);
//            }
//        });
    }

    //添加tab 默认选中第一个
    private void addTab() {
        linearLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final TextView textView = new TextView(context);
            if (i == 0) {
                //textView.setTextColor(getResources().getColor(currColor));
                textView.setSelected(true);
            } else {
                //textView.setTextColor(getResources().getColor(noCurrColor));
                textView.setSelected(false);
            }
            layoutParams.setMargins(0, 20, 0, 20);
            layoutParams.width = (int) (((float) getWth()) / maxCount);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(txtSize);
            textView.setText(list.get(i));
            textView.setTextColor(getResources().getColorStateList(R.color.lib_colums_text_color));
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh(finalI);
//                    if (currIndex != finalI) { //点击的不是当前tab
//                        refresh(finalI);
//                        viewPager.setCurrentItem(finalI);
//                    }

                }
            });
            textViews.add(textView);
            linearLayout.addView(textView);
        }
        invalidate();
    }

    private void refresh(int position) {
//        for (int i = 0; i < textViews.size(); i++) {
//            if (i != index) {
//                textViews.get(i).setTextColor(getResources().getColor(noCurrColor));
//            } else {
//                textViews.get(i).setTextColor(getResources().getColor(currColor));
//            }
//        }

        View checkView = linearLayout.getChildAt(position);
        int k = checkView.getMeasuredWidth();
        int l = checkView.getLeft();
        int i2 = l + k / 2 - XDensityUtils.getScreenWidth() / 2;
        smoothScrollTo(i2, 0);


//        for (int i = 0; i < linearLayout.getChildCount(); i++) {
//            View localView = linearLayout.getChildAt(i);
//            if (i != position)
//                localView.setSelected(false);
//            else {
//                localView.setSelected(true);
//                viewPager.setCurrentItem(i);
//            }
//        }

        for (int j = 0; j < linearLayout.getChildCount(); j++) {
            checkView = linearLayout.getChildAt(j);
            boolean ischeck;
            if (j == position) {
                ischeck = true;
                viewPager.setCurrentItem(j);
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = getHeight();

        paint.setColor(getResources().getColor(offLineColor));
        View view = linearLayout.getChildAt(currIndex);

        float lineLeft = view.getLeft();
        float lineRight = view.getRight();

        if (offSet > 0f) {
            View nextTab = linearLayout.getChildAt(currIndex + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (offSet * nextTabLeft + (1f - offSet) * lineLeft);
            lineRight = (offSet * nextTabRight + (1f - offSet) * lineRight);
        }
        canvas.drawRect(lineLeft, h - 5, lineRight, h, paint);
    }


    private void scrollToChild(int position, int offset) {
//        int newScrollX = 0;
//        if (lastIndex > position) {// 左滑
//            newScrollX = linearLayout.getChildAt(position).getLeft() - linearLayout.getChildAt(position).getWidth();
//        } else { //右滑
//            newScrollX = linearLayout.getChildAt(lastIndex).getLeft() - linearLayout.getChildAt(position).getWidth();
//        }
//        Log.e("TAG", "newScrollX:" + newScrollX);
//        smoothScrollTo(newScrollX, 0);
//
        View checkView = linearLayout.getChildAt(position);
        int k = checkView.getMeasuredWidth();
        int l = checkView.getLeft();
        int i2 = l + k / 2 - XDensityUtils.getScreenWidth() / 2;
        smoothScrollTo(i2, 0);

        for (int j = 0; j < linearLayout.getChildCount(); j++) {
            checkView = linearLayout.getChildAt(j);
            boolean ischeck;
            if (j == position) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }

    }

    /***
     * @param position             当向右滑动时，此参数是点击的页面位置，
     *                             滑动完成时，及当前页面位置
     *                             当向左滑动时，此参数是向左滑动页面位置，
     *                             及当前页面位置-1，滑动完成时，及当前页面位置
     * @param positionOffset       页面滑动偏移量百分比
     * @param positionOffsetPixels 页面滑动偏移量
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currIndex = position;
        offSet = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        lastIndex = position;
        refresh(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            Log.e("TAG", "currIndex:" + currIndex);
            if (currIndex != textViews.size() - 1 && currIndex != 0) {
                scrollToChild(currIndex, 0);
            }
        }
    }

    private int getWth() {
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public void setTxtSize(int txtSize) {
        this.txtSize = txtSize;
    }

    public void setOffLineColor(int offLineColor) {
        this.offLineColor = offLineColor;
    }

    public void setCurrColor(int currColor) {
        this.currColor = currColor;
    }

    public void setNoCurrColor(int noCurrColor) {
        this.noCurrColor = noCurrColor;
    }

}

