package com.huimaibao.app.fragment.message.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.electcircle.act.ShareRecordActivity;
import com.huimaibao.app.fragment.electcircle.materialLib.ShareRecordFragment;
import com.huimaibao.app.fragment.home.act.SreachActivity;
import com.huimaibao.app.fragment.library.adapter.NewsFragmentPagerAdapter;
import com.huimaibao.app.fragment.message.adapter.MessageAdapter;
import com.huimaibao.app.fragment.message.adapter.MessageDelAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.fragment.MsgFragment;
import com.huimaibao.app.fragment.message.fragment.MsgListFragment;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.fragment.mine.act.CardClipDetailActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends BaseActivity {
    private String mType = "消息";

    private TextView _top_titel_1, _top_titel_2;
    private ImageView _top_sreach;


    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] nameData = {"关注", "粉丝"};
    private String[] typeData = {"1", "2"};
    private String isType = "";


    /**
     * 广播接收
     */
    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_message);

        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }


        initView();


        broadcastManager = LocalBroadcastManager.getInstance(mActivity);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MESSAGE_FOCUS");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //收到广播后所作的操作
                if (isType.equals("关注")) {
                    _top_titel_1.setText("关注(" + XPreferencesUtils.get("card_num", "0") + ")");
                } else {
                    _top_titel_2.setText("粉丝(" + XPreferencesUtils.get("fans_num", "0") + ")");
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);


    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_titel_1 = findViewById(R.id.mine_msg_title_1);
        _top_titel_2 = findViewById(R.id.mine_msg_title_2);
        _top_sreach = findViewById(R.id.mine_msg_sreach);

        mViewPager = findViewById(R.id.msg_viewpager);

        if (mType.equals("消息")) {
            _top_titel_1.setText("提醒");
            _top_titel_2.setText("通知");
            _top_sreach.setVisibility(View.INVISIBLE);
        } else {
            isType = "关注";
            _top_titel_1.setText("关注(" + XPreferencesUtils.get("card_num", "0") + ")");
            _top_titel_2.setText("粉丝");
            _top_sreach.setVisibility(View.VISIBLE);
        }

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

        if (mType.equals("消息")) {
            MsgListFragment msgListFragment = new MsgListFragment();
            MsgFragment msgFragment = new MsgFragment();
            Bundle data = new Bundle();
            data.putString("name", "消息");
            data.putString("type", "-1");
            msgListFragment.setArguments(data);
            fragments.add(msgListFragment);
            fragments.add(msgFragment);
        } else {
            for (int i = 0; i < 2; i++) {
                Bundle data = new Bundle();
                data.putString("name", nameData[i]);
                data.putString("type", typeData[i]);
                MsgListFragment msgListFragment = new MsgListFragment();
                msgListFragment.setArguments(data);
                fragments.add(msgListFragment);
            }
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
            case R.id.mine_msg_sreach:
                startActivity(SreachActivity.class, "搜索");
                break;
            case R.id.list_message_push_ll:
                startActivity(MessageListActivity.class, "推送消息");
                break;
            case R.id.list_message_xmb_ll:
                startActivity(MessageListActivity.class, "小脉宝");
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
                if (mType.equals("关注")) {
                    isType = "关注";
                    _top_titel_1.setText("关注(" + XPreferencesUtils.get("card_num", "0") + ")");
                    _top_titel_2.setText("粉丝");
                }
                _top_titel_1.setTextColor(getResources().getColor(R.color.FF333333));
                _top_titel_2.setTextColor(getResources().getColor(R.color.color666666));
                _top_titel_1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                _top_titel_2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _top_titel_1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.msg_line));
                _top_titel_2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                break;
            case 1:
                if (mType.equals("关注")) {
                    isType = "粉丝";
                    _top_titel_1.setText("关注");
                    _top_titel_2.setText("粉丝(" + XPreferencesUtils.get("fans_num", "0") + ")");
                }
                _top_titel_2.setTextColor(getResources().getColor(R.color.FF333333));
                _top_titel_1.setTextColor(getResources().getColor(R.color.color666666));
                _top_titel_1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
                _top_titel_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//常规
                _top_titel_2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.msg_line));
                _top_titel_1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                break;
        }

    }

    public void setFansNum() {
        if (mType.equals("关注")) {
            if (_top_titel_2 != null) {
                _top_titel_2.setText("粉丝(" + XPreferencesUtils.get("fans_num", "0") + ")");
            }
        }
    }

    public void setFocusNum() {
        if (mType.equals("关注")) {
            if (_top_titel_1 != null) {
                _top_titel_1.setText("关注(" + XPreferencesUtils.get("card_num", "0") + ")");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastManager != null) {
            broadcastManager.unregisterReceiver(mReceiver);
        }

    }

}
