package com.huimaibao.app.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.library.act.ChannelActivity;
import com.huimaibao.app.fragment.library.act.NewsFragment;
import com.huimaibao.app.fragment.library.bean.ChannelItem;
import com.huimaibao.app.fragment.library.bean.ChannelManage;
import com.huimaibao.app.fragment.library.lab.LibViewPager;
import com.huimaibao.app.fragment.library.lab.PageAdapter;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.tablayout.TabLayout;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 文库
 */
public class LibraryFragment extends BaseFragment {

    private Activity mActivity = this.getActivity();

    private TabLayout hd_tl;
    LinearLayout ll_more_columns;
    private LibViewPager mViewPager;
    private List<String> columnsData;

    private ImageView _no_data_iv;

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
    private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();

    private ArrayList<NewsFragment> fragments = new ArrayList<>();

    /**
     * 请求CODE
     */
    public final static int CHANNELREQUEST = 1;
    /**
     * 调整返回的RESULTCODE
     */
    public final static int CHANNELRESULT = 10;

    //private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        mActivity = this.getActivity();
        initView(view);
        return view;
    }

    @Override
    protected void initData() {
        //setChangelView();
        getUserLabel(true);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
                if (ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).getUserChannel().size() == 0) {
                    getUserLabel(true);
                }else{
                    ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).deleteAllChannel();
                    ChannelManage.getManage(BaseApplication.getApp().getSQLHelper()).setChannerData(defaultUserChannels, defaultOtherChannels);
                    setChangelView();
                }
        }
    }

    /**
     * 初始化layout控件
     */
    private void initView(View v) {

        _no_data_iv = v.findViewById(R.id.list_no_data_iv);

        hd_tl = v.findViewById(R.id.lib_tab_ll);

        mViewPager = v.findViewById(R.id.mViewPager);
        ll_more_columns = v.findViewById(R.id.ll_more_columns);

        ll_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_channel = new Intent(mActivity, ChannelActivity.class);
                startActivityForResult(intent_channel, CHANNELREQUEST);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

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
        PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager(), columnsData, fragments);

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
                    //XLog.d("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray arraySet = new JSONArray(json.getJSONObject("data").getString("set"));
                        JSONArray arrayUnset = new JSONArray(json.getJSONObject("data").getString("unset"));
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
                                _no_data_iv.setVisibility(View.GONE);
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
                //XLog.d("error:" + error);
                hideRe();
            }
        });
    }


    /***/
    private void hideRe() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _no_data_iv.setVisibility(View.GONE);
                //加载完数据设置为不刷新状态，将下拉进度收起来
//                if (mSwipeRefreshLayout.isRefreshing()) {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
            }
        });

    }

    /**
     * 消息
     */
    private void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
            }
        });
    }

    /**
     * 广播接收
     */
    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }

}
