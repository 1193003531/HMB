package com.huimaibao.app.fragment.home.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.fragment.library.act.ChannelActivity;
import com.huimaibao.app.fragment.library.act.NewsFragment;
import com.huimaibao.app.fragment.library.bean.ChannelItem;
import com.huimaibao.app.fragment.library.bean.ChannelManage;
import com.huimaibao.app.fragment.library.lab.LibViewPager;
import com.huimaibao.app.fragment.library.lab.PageAdapter;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.tablayout.TabLayout;
import com.youth.xframe.pickers.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 文库
 */
public class LibraryActivity extends BaseActivity {


    private TabLayout hd_tl;
    LinearLayout ll_more_columns;
    private LibViewPager mViewPager;
    private List<String> columnsData;

    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data, _no_tv_btn;

    private ImageView _n_iv;

    /**
     * 默认的用户选择频道列表
     */
    public static List<ChannelItem> defaultUserChannels;
    /**
     * 默认的其他频道列表
     */
    public static List<ChannelItem> defaultOtherChannels;
    /**
     * 用户选择的新闻分类列表
     */
    private ArrayList<ChannelItem> userChannelList;//= new ArrayList<ChannelItem>();

    private ArrayList<NewsFragment> fragments = new ArrayList<>();

    /**
     * 请求CODE
     */
    public final static int CHANNELREQUEST = 1;
    /**
     * 调整返回的RESULTCODE
     */
    public final static int CHANNELRESULT = 10;

    /**
     * 广播接收
     */
    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;


    //private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_library);
        initView();
        initData();
    }


    private void initData() {

        broadcastManager = LocalBroadcastManager.getInstance(mActivity);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //收到广播后所作的操作
                mViewPager.setCurrentItem(1);
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);

        //ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).deleteAllChannel();
        //setChangelView();
        getUserLabel(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).getUserChannel().size() == 0) {
//            getUserLabel(true);
//        } else {
//            ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).deleteAllChannel();
//            ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).setChannerData(defaultUserChannels, defaultOtherChannels);
//            setChangelView();
//        }
    }


    /**
     * 初始化layout控件
     */
    private void initView() {

        _no_data = findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);
        _no_image_data = findViewById(R.id.list_no_data_iv);
        _no_tv_data = findViewById(R.id.list_no_data_tv);
        _no_tv_btn = findViewById(R.id.list_no_data_btn);

        _no_tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLabel(true);
            }
        });


//        if (text.equals("关注")) {
//            _no_image_data.setImageResource(R.drawable.blank_pages_14_icon);
//            _no_tv_data.setText(R.string.no_datas_14);
//        } else if (text.equals("粉丝")) {
//            _no_image_data.setImageResource(R.drawable.blank_pages_15_icon);
//            _no_tv_data.setText(R.string.no_datas_15);
//        }else{
//            _no_image_data.setImageResource(R.drawable.blank_pages_16_icon);
//            _no_tv_data.setText(R.string.no_datas_17);
//        }


        _n_iv = findViewById(R.id.list_n_iv);
        hd_tl = findViewById(R.id.lib_tab_ll);

        mViewPager = findViewById(R.id.mViewPager);
        ll_more_columns = findViewById(R.id.ll_more_columns);

        ll_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_channel = new Intent(mActivity, ChannelActivity.class);
                startActivityForResult(intent_channel, CHANNELREQUEST);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        userChannelList = new ArrayList<>();
        defaultUserChannels = new ArrayList<>();
        defaultOtherChannels = new ArrayList<>();

//        mSwipeRefreshLayout = v.findViewById(R.id.list_swipe_value);
//        // 设置下拉进度的主题颜色
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.ff274ff3);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getUserLabel(false);
//            }
//        });


    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }

    }

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    /**
     * 获取Column栏目 数据
     */
    private void initColumnData() {
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).getUserChannel());
    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        if (columnsData != null) {
            columnsData.clear();
        } else {
            columnsData = new ArrayList<>();
        }
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            columnsData.add(userChannelList.get(i).getName());
            LogUtils.debug("columnsData:" + userChannelList.get(i).getName());
        }
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("text", userChannelList.get(i).getName());
            data.putInt("id", userChannelList.get(i).getPId());
            NewsFragment newfragment = new NewsFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        // LibPagerAdapter mAdapetr = new LibPagerAdapter(getChildFragmentManager());
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), columnsData, fragments);

        mViewPager.setAdapter(pageAdapter);
        //mViewPager.setOffscreenPageLimit(0);
        //设置tablayout 切换tab的动画
        hd_tl.setNeedSwitchAnimation(true);
        //设置tablayout 线宽为包裹内容 (与设置tablayout固定线宽 互斥 所以尽量使用一个,包裹内容优先级高于设置指定线宽)
        hd_tl.setIndicatorWidthWrapContent(true);
        //还原成原来的tablayout默认线宽 (与设置tablayout固定线宽和包裹内容 互斥 所以尽量使用一个，在不指定固定线宽和包裹内容情况下为tablayout默认线宽)
        //tabLayout.selectedTabIndicatorWidth = -1
        //关联
        hd_tl.setupWithViewPager(mViewPager);


        //默认选择第二个
        mViewPager.setCurrentItem(1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case CHANNELREQUEST:
                if (resultCode == CHANNELRESULT) {
                    setChangelView();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 获取用户栏目
     */
    private void getUserLabel(boolean isShow) {
        LibLogic.Instance(mActivity).getLibUserLabelApi(isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray arraySet = new JSONArray(json.getJSONObject("data").getString("set"));
                        JSONArray arrayUnset = new JSONArray(json.getJSONObject("data").getString("unset"));
                        defaultUserChannels = new ArrayList<>();
                        defaultOtherChannels = new ArrayList<>();
                        // defaultUserChannels.add(new ChannelItem(1, "关注", 1, 1));
                        defaultUserChannels.add(new ChannelItem(1000, 1, "关注", 1, 1));
                        defaultUserChannels.add(new ChannelItem(2000, 2, "推荐", 2, 1));

                        for (int i = 0; i < arraySet.length(); i++) {
                            ChannelItem item = new ChannelItem();
                            item.setPId(arraySet.getJSONObject(i).getInt("id"));
                            item.setId(i + 1 + defaultUserChannels.size());
                            item.setName(arraySet.getJSONObject(i).getString("title"));
                            item.setOrderId(i + 1 + defaultUserChannels.size());
                            item.setSelected(1);
                            defaultUserChannels.add(item);
                        }


                        for (int i = 0; i < arrayUnset.length(); i++) {
                            ChannelItem item = new ChannelItem();
                            item.setId(i + 1 + defaultUserChannels.size());
                            item.setPId(arrayUnset.getJSONObject(i).getInt("id"));
                            item.setName(arrayUnset.getJSONObject(i).getString("title"));
                            item.setOrderId(i + 1);
                            item.setSelected(0);
                            defaultOtherChannels.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _n_iv.setVisibility(View.GONE);
                                ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).deleteAllChannel();
                                ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).setChannerData(defaultUserChannels, defaultOtherChannels);
                                setChangelView();
                            }
                        });

                    } else {
                        showToast(message);
                        hideRe();
                    }

                } catch (Exception e) {
                    hideRe();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("lib=s=" + error);
                hideRe();
            }
        });
    }


    /***/
    private void hideRe() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _n_iv.setVisibility(View.GONE);
                _no_image_data.setImageResource(R.drawable.blank_pages_4_icon);
                _no_tv_data.setText("很抱歉,加载失败");
                _no_tv_btn.setVisibility(View.VISIBLE);
                //加载完数据设置为不刷新状态，将下拉进度收起来
//                if (mSwipeRefreshLayout.isRefreshing()) {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastManager != null) {
            broadcastManager.unregisterReceiver(mReceiver);
        }
    }

}
