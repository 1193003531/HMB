package com.huimaibao.app.fragment.electcircle.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.electcircle.materialLib.ArticleFragment;
import com.huimaibao.app.fragment.electcircle.materialLib.CardFragment;
import com.huimaibao.app.fragment.electcircle.materialLib.WebFragment;
import com.huimaibao.app.fragment.library.adapter.NewsFragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 素材库
 */
public class MaterialLibActivity extends BaseActivity {

    private TextView _top_article, _top_web, _top_card;


    private ArticleFragment articleFragment;
    private WebFragment webFragment;
    private CardFragment cardFragment;

    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_elect_material_lib);
        //setNeedBackGesture(true);


        initView();
        initData();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_article = findViewById(R.id.material_article);
        _top_web = findViewById(R.id.material_web);
        _top_card = findViewById(R.id.material_card);
        mViewPager = findViewById(R.id.material_viewpager);

        _top_article.setOnClickListener(new MyOnClickListener(0));
        _top_web.setOnClickListener(new MyOnClickListener(1));
        _top_card.setOnClickListener(new MyOnClickListener(2));

    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragments.clear();//清空
        articleFragment = new ArticleFragment();
        webFragment = new WebFragment();
        cardFragment = new CardFragment();

        fragments.add(articleFragment);
        fragments.add(webFragment);
        fragments.add(cardFragment);
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
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
        }
    };

    /**
     * 顶部选择变化
     */
    private void setTopCurrentItem(int position) {
        switch (position) {
            case 0:
                _top_article.setTextColor(getResources().getColor(R.color.ff274ff3));
                _top_web.setTextColor(getResources().getColor(R.color.color333333));
                _top_card.setTextColor(getResources().getColor(R.color.color333333));
                _top_article.setTextAppearance(mActivity, R.style.text_bold);
                _top_web.setTextAppearance(mActivity, R.style.text_normal);
                _top_card.setTextAppearance(mActivity, R.style.text_normal);
                break;
            case 1:
                _top_article.setTextColor(getResources().getColor(R.color.color333333));
                _top_web.setTextColor(getResources().getColor(R.color.ff274ff3));
                _top_card.setTextColor(getResources().getColor(R.color.color333333));
                _top_article.setTextAppearance(mActivity, R.style.text_normal);
                _top_web.setTextAppearance(mActivity, R.style.text_bold);
                _top_card.setTextAppearance(mActivity, R.style.text_normal);
                break;
            case 2:
                _top_article.setTextColor(getResources().getColor(R.color.color333333));
                _top_web.setTextColor(getResources().getColor(R.color.color333333));
                _top_card.setTextColor(getResources().getColor(R.color.ff274ff3));
                _top_article.setTextAppearance(mActivity, R.style.text_normal);
                _top_web.setTextAppearance(mActivity, R.style.text_normal);
                _top_card.setTextAppearance(mActivity, R.style.text_bold);
                break;
        }

    }

    /**
     * 返回
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

}
