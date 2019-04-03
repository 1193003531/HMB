package com.huimaibao.app.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.youth.xframe.utils.XPreferencesUtils;

import java.util.ArrayList;

/**
 * 引导页
 */
public class GuideActivity extends Activity {

    private ViewPager mViewPager;
    private int[] mImageIds = new int[]{R.drawable.login_guide_1, R.drawable.login_guide_2, R.drawable.login_guide_3};

    private ArrayList<ImageView> mImageViewList;
    // 放4个小灰点的线性布局
    private LinearLayout linearLayout;
    private ImageView lan_Iv;
    //小点之间的距离
    private int pointWidth;

    //点击
   private TextView _text_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_guide);
        initView();
    }


    protected void initView() {

        lan_Iv = findViewById(R.id.lan_Iv);
        linearLayout = findViewById(R.id.ll);
        _text_btn= findViewById(R.id.guide_btn);
        mViewPager = findViewById(R.id.vp_guide);

        initData();

        mImageViewList = new ArrayList<>();
        //初始化图片和小点，图片的个数和小点的个数要始终一致
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViewList.add(imageView);

            // 根据图片的个数去放置相应数量的小灰点
            ImageView huiImageView = new ImageView(this);
            huiImageView.setImageResource(R.drawable.login_guide_hui_icon);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            huiImageView.setLayoutParams(layoutParams);

            if (i > 0) {
                //给除了第一个小点的其它小点左边距
                layoutParams.leftMargin = 40;
            }

            linearLayout.addView(huiImageView);
        }

        /*我们的代码现在都写在onCreate方法中，onCreate在调用的时候，界面底层的绘制工作还没有完成。所以，如果我们直接在onCreate方法里去
         * 拿距离是拿不到
         * dOnGlobalLayoutListener：在视图树全部都绘制完成的时候调用*/
        lan_Iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //视图全部绘制完成时，计算得到小点之间的距离
                pointWidth = linearLayout.getChildAt(1).getLeft() - linearLayout.getChildAt(0).getLeft();
                System.out.println(pointWidth);
            }
        });
        //绑定适配器
        mViewPager.setAdapter(new MyAdapter());
        //设置图片切换的监听事件
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //让滑倒最后一页显示按钮
                if (position == mImageIds.length - 1) {
                    _text_btn.setVisibility(View.VISIBLE);
                } else {
                    _text_btn.setVisibility(View.GONE);
                }
            }

            @Override
            //在Viewpger的界面不断滑动的时候会触发
            //position：当前滑到了第几页从0开始计数
            public void onPageScrolled(int position, float offset, int arg2) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lan_Iv.getLayoutParams();
                //滑动的百分比乘上小点之间的距离，再加上当前位置乘上距离，即为蓝色小点的左边距
                layoutParams.leftMargin = (int) (pointWidth * offset + position * pointWidth);
                lan_Iv.setLayoutParams(layoutParams);
                System.out.println("当前滑动的百分比   " + offset);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }
        });
        //设置滑动特效（可加可不加）
        //viewPager.setPageTransformer(true, new DepthPageTransformer());

        _text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                XPreferencesUtils.put("isFirstUse", false);
                finish();
            }
        });

    }


    /***/
    private void initData() {

    }

    class MyAdapter extends PagerAdapter {
        @Override
        // 返回ViewPager里面有几页
        public int getCount() {
            // TODO Auto-generated method stub
            return mImageIds.length;
        }

        @Override
        // 用于判断是否有对象生成界面
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            imageView.setImageResource(mImageIds[position]);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

    }
}
