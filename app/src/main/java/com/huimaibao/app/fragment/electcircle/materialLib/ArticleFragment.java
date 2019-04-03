package com.huimaibao.app.fragment.electcircle.materialLib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.library.adapter.NewsFragmentPagerAdapter;
import com.youth.xframe.utils.XDensityUtils;

import java.util.ArrayList;

/**
 * 文章
 */
public class ArticleFragment extends BaseFragment {

    private TextView _top_recommend, _top_collection;
    private ImageView _top_line;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;

    //动画图片宽度
    private int bmpW;

    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] label = {"推荐", "收藏"};

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_e_m_l_article, container, false);

        initView(view);

        return view;
    }


    @Override
    protected void initData() {
        fragments.clear();//清空
        for (int i = 0; i < 2; i++) {
            Bundle data = new Bundle();
            data.putString("text", label[i]);
            data.putString("type", "文章");
            ListFragment listFragment = new ListFragment();
            listFragment.setArguments(data);
            fragments.add(listFragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }


    /**
     * 初始化控件
     */
    private void initView(View v) {
        mViewPager = v.findViewById(R.id.article_viewpager);
        _top_recommend = v.findViewById(R.id.article_recommend);
        _top_collection = v.findViewById(R.id.article_collection);
        _top_line = v.findViewById(R.id.article_line);

        //添加点击事件
        _top_recommend.setOnClickListener(new MyOnClickListener(0));
        _top_collection.setOnClickListener(new MyOnClickListener(1));

        InitImageView();
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获取分辨率宽度
        int screenW = dm.widthPixels;

        bmpW = XDensityUtils.dp2px(55);

        //设置动画图片宽度
        //setBmpW(_top_line, bmpW);
        offset = screenW / 4 - bmpW / 2;

        //动画图片偏移量赋值
        position_one = offset + screenW / 2;

        Animation animation = new TranslateAnimation(0, offset, 0, 0);
        animation.setFillAfter(true);// true:图片停在动画结束位置
        animation.setDuration(300);
        _top_line.startAnimation(animation);
    }

    /**
     * 头标点击监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }


    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            setTopCurrentItem(position);

            Animation animation = null;
            switch (position) {
                case 0:
                    animation = new TranslateAnimation(position_one, offset, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
                    break;
            }
            animation.setFillAfter(true);// true:图片停在动画结束位置
            animation.setDuration(300);
            _top_line.startAnimation(animation);

        }
    };

    /**
     * 顶部选择变化
     */
    private void setTopCurrentItem(int position) {
        switch (position) {
            case 0:
                _top_recommend.setTextColor(getResources().getColor(R.color.color040404));
                _top_collection.setTextColor(getResources().getColor(R.color.color666666));
                break;
            case 1:
                _top_recommend.setTextColor(getResources().getColor(R.color.color666666));
                _top_collection.setTextColor(getResources().getColor(R.color.color040404));
                break;
            case 2:
                _top_recommend.setTextColor(getResources().getColor(R.color.color666666));
                _top_collection.setTextColor(getResources().getColor(R.color.color040404));
                break;
        }

    }

}
