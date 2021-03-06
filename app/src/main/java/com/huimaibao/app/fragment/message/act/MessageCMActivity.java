package com.huimaibao.app.fragment.message.act;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.library.adapter.NewsFragmentPagerAdapter;
import com.huimaibao.app.fragment.message.fragment.CMListFragment;
import com.huimaibao.app.fragment.mine.act.MemberActivity;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.NoScrollViewPager;

import java.util.ArrayList;

/**
 * 客户管理
 */
public class MessageCMActivity extends BaseActivity {
    private String mType = "";

    private TextView _top_titel_1, _top_titel_2;


    private NoScrollViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] nameData = {"人气", "对我感兴趣"};
    private String[] typeData = {"1", "2"};

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_message_customer_m);
        mDialogUtils = new DialogUtils(mActivity);

//        Intent intent = getIntent();
//        if (intent != null) {
//            mType = intent.getStringExtra("vType");
//        }


        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_titel_1 = findViewById(R.id.mine_msg_title_1);
        _top_titel_2 = findViewById(R.id.mine_msg_title_2);

        mViewPager = findViewById(R.id.msg_viewpager);
        mViewPager.setNoScroll(true);

        //添加点击事件
        _top_titel_1.setOnClickListener(new MyOnClickListener(0));
        _top_titel_2.setOnClickListener(new MyOnClickListener(1));


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
            CMListFragment cmListFragment = new CMListFragment();
            cmListFragment.setArguments(data);
            fragments.add(cmListFragment);
        }


        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
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
            if (index == 1) {
                if (XPreferencesUtils.get("vip_level", "0").equals("0")) {
                    mDialogUtils.showNoTitleDialog("开通会员才能看\"精准客户\"", "取消", "开通", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(MemberActivity.class, "会员中心");
                            mDialogUtils.dismissDialog();
                        }
                    });
                } else {
                    mViewPager.setCurrentItem(index, false);
                }
            } else {
                mViewPager.setCurrentItem(index, false);
            }

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
            mViewPager.setCurrentItem(position, false);
            setTopCurrentItem(position);
        }
    };

    /**
     * 顶部选择变化
     */
    private void setTopCurrentItem(int position) {
        switch (position) {
            case 0:
                _top_titel_1.setTextColor(getResources().getColor(R.color.FF333333));
                _top_titel_2.setTextColor(getResources().getColor(R.color.color666666));
                _top_titel_1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                _top_titel_2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _top_titel_1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.msg_line));
                _top_titel_2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                break;
            case 1:
                _top_titel_2.setTextColor(getResources().getColor(R.color.FF333333));
                _top_titel_1.setTextColor(getResources().getColor(R.color.color666666));
                _top_titel_1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
                _top_titel_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//常规
                _top_titel_2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.msg_line));
                _top_titel_1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                break;
        }

    }


}
