package com.huimaibao.app.fragment.library.act.focuson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.library.adapter.NewsFragmentPagerAdapter;
import com.huimaibao.app.fragment.library.bean.FocusOnUserEntity;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.banner.BaseIndicator;
import com.youth.xframe.banner.Indicator;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 关注用户
 */
public class FocusOnActivity extends BaseActivity {


    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout mIndicators;

    private ViewPager mViewPager;
    private ImageView button_more_columns;
    private BaseIndicator mIndicatorView;
    private int count=0;

    //关注用户
    private ArrayList<FocusOnUserEntity> userList;


    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private View _needOffsetView;
    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lib_focus_on);
        Intent intent=getIntent();
        if(intent!=null){
            count=intent.getIntExtra("position",0);
        }

        initView();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        _needOffsetView = findViewById(R.id.fake_status_bar);
        _top_layout = findViewById(R.id.top_layout);
        params = _top_layout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = XDensityUtils.getStatusBarHeight();
        _top_layout.setLayoutParams(params);

        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
    }

    /**
     * 初始化layout控件
     */
    private void initView() {
        _needOffsetView = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);

        mHorizontalScrollView = findViewById(R.id.mColumnHorizontalScrollView);
        mIndicators = findViewById(R.id.mRadioGroup_content);
        button_more_columns = findViewById(R.id.button_more_columns);
        mViewPager = findViewById(R.id.mViewPager);
        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mActivity.finish();
                overridePendingTransition(R.anim.slide_in_up_ac, R.anim.slide_out_down_ac);
            }
        });

        getFoucsOnUser();

        //setChangelView();
    }

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        setIndicators();
        initFragment();
    }


    /**
     * 初始化Column栏目项
     */
    private void setIndicators() {
        mIndicators.removeAllViews();
        for (int i = 0; i < userList.size(); i++) {
            if (mIndicatorView == null) {
                Indicator indicator = new Indicator(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (getRatioDimension(20, false),
                                getRatioDimension(20, false));
                layoutParams.setMargins(getRatioDimension(10, true), 0,
                        getRatioDimension(10, true), 0);
                indicator.setLayoutParams(layoutParams);
                mIndicators.addView(indicator);
            } else {

                BaseIndicator baseIndicator = mIndicatorView;
                ViewParent vp = baseIndicator.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(baseIndicator);
                }
                mIndicators.addView(baseIndicator);
                // Log.e("iii", "" + mIndicators.getChildCount());
            }
        }
        if (userList.size() > 0) {
            ((BaseIndicator) mIndicators.getChildAt(0)).setState(true);
        }
    }


    public static int getRatioDimension(int value, boolean isWidth) {
        int widthPixels = XDensityUtils.getScreenWidth();
        int heightPixels = XDensityUtils.getScreenHeight();
        float STANDARD_WIDTH = 1080F;
        float STANDARD_HEIGHT = 1920F;
        if (isWidth) {
            return (int) (value / STANDARD_WIDTH * widthPixels);
        } else {
            return (int) (value / STANDARD_HEIGHT * heightPixels);
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        // columnSelectIndex = tab_postion;
        for (int i = 0; i < mIndicators.getChildCount(); i++) {
            View checkView = mIndicators.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            //显示屏幕宽度
            int screenWidth = XDensityUtils.dp2px(75);
            //计算控件居正中时距离左侧屏幕的距离
            int middleLeftPosition = (screenWidth - k) / 2;
            //正中间位置需要向左偏移的距离
            int offset = l - middleLeftPosition;
            //让水平的滚动视图按照执行的x的偏移量进行移动
            mHorizontalScrollView.smoothScrollTo(offset, 0);
        }
        //判断是否选中
        for (int j = 0; j < mIndicators.getChildCount(); j++) {
            if (j == tab_postion) {
                ((BaseIndicator) mIndicators.getChildAt(j)).setState(true);
            } else {
                ((BaseIndicator) mIndicators.getChildAt(j)).setState(false);
            }
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        int count = userList.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("text", userList.get(i).getFocusOnUserName());
            data.putString("id", userList.get(i).getFocusOnUserId());
            FocusOnFragment focusOnfragment = new FocusOnFragment();
            focusOnfragment.setArguments(data);
            fragments.add(focusOnfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
//		mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mAdapetr);

        mViewPager.setCurrentItem(0);
        selectTab(0);

        //ViewPager滑动Pager监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 设置按钮最后一页显示，其他页面隐藏
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    /**
     * 获取关注用户
     */
    private void getFoucsOnUser() {
        LibLogic.Instance(mActivity).getFocusOnUserApi(false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(data);
                        userList = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            FocusOnUserEntity entity = new FocusOnUserEntity();
                            entity.setFocusOnUserId(array.getJSONObject(i).getString("id"));
                            entity.setFocusOnUserHead(array.getJSONObject(i).getString("avatar"));
                            entity.setFocusOnUserName(array.getJSONObject(i).getString("account"));
                            userList.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setChangelView();
                                mViewPager.setCurrentItem(count);
                                selectTab(count);
                            }
                        });

                    } else {
                        showToast(message);
                    }

                } catch (Exception e) {
                    //XLog.d("e:" + e.toString());
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mActivity.finish();
            overridePendingTransition(R.anim.slide_in_up_ac, R.anim.slide_out_down_ac);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
