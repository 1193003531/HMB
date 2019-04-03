package com.huimaibao.app.fragment.electcircle.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.electcircle.materialLib.ShareRecordFragment;
import com.huimaibao.app.fragment.library.adapter.NewsFragmentPagerAdapter;
import com.youth.xframe.utils.XDensityUtils;

import java.util.ArrayList;

/**
 * 分享记录
 */
public class ShareRecordActivity extends BaseActivity {

    private String mType = "";


    private TextView _top_yfx, _top_wfx;
    private ImageView _top_line;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;

    //动画图片宽度
    private int bmpW;

    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] nameData = {"已分享", "未分享"};
    private String[] typeData = {"1", "2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_elect_share_record);
        setNeedBackGesture(false);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        setShoweLine(false);

        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragments.clear();//清空
        for (int i = 0; i < 2; i++) {
            Bundle data = new Bundle();
            data.putString("name", nameData[i]);
            data.putString("type", typeData[i]);

            ShareRecordFragment shareRecordFragment = new ShareRecordFragment();
            shareRecordFragment.setArguments(data);
            fragments.add(shareRecordFragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mViewPager = findViewById(R.id.share_record_viewpager);
        _top_yfx = findViewById(R.id.share_record_yfx);
        _top_wfx = findViewById(R.id.share_record_wfx);
        _top_line = findViewById(R.id.share_record_line);

        //添加点击事件
        _top_yfx.setOnClickListener(new MyOnClickListener(0));
        _top_wfx.setOnClickListener(new MyOnClickListener(1));

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

        bmpW = XDensityUtils.dp2px(69);

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
                _top_yfx.setTextColor(getResources().getColor(R.color.color040404));
                _top_wfx.setTextColor(getResources().getColor(R.color.color666666));
                break;
            case 1:
                _top_yfx.setTextColor(getResources().getColor(R.color.color666666));
                _top_wfx.setTextColor(getResources().getColor(R.color.color040404));
                break;
            case 2:
                _top_yfx.setTextColor(getResources().getColor(R.color.color666666));
                _top_wfx.setTextColor(getResources().getColor(R.color.color040404));
                break;
        }

    }

}
