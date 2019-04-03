package com.huimaibao.app.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.message.act.MessageActivity;
import com.huimaibao.app.fragment.message.act.MessageListActivity;
import com.huimaibao.app.fragment.message.adapter.MessageDelAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.fragment.mine.act.MemberActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.GifView;
import com.youth.xframe.widget.NoScrollListView;
import com.youth.xframe.widget.XScrollView;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {
    private Activity mActivity = this.getActivity();


    private XScrollView mScrollView;
    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;
    private View _needOffsetView;
    private ImageView _title_line;
    //
    private LinearLayout btn_visitor, btn_fans, btn_system_messages;
    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private NoScrollListView mListView;
    //private MessageAdapter mAdapter;
    private MessageDelAdapter mAdapter;
    private List<MessageEntity> ListPushData, ListXMBData, ListOtherData;

    //顶部红点
    private ImageView _dian_1, _dian_2, _dian_3;

    //推送消息
    private LinearLayout _push_ll;
    private TextView _push_time, _push_content;
    private ImageView _push_dian;
    //小脉宝
    private LinearLayout _xmb_ll;
    private TextView _xmb_time, _xmb_content;
    private ImageView _xmb_dian;

    //加载更多
    private View mFooterView;
    private GifView _footer_gif;

    private int countPage = 1;

    private DialogUtils mDialogUtils;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mActivity = this.getActivity();
        initView(view);
        return view;
    }


    @Override
    protected void initData() {


        //m_no_data.setVisibility(View.GONE);
        // mListView.setVisibility(View.VISIBLE);
//        if (mAdapter == null) {
//            mAdapter = new MessageAdapter(mActivity, ListData);
//        }
//        mListView.setAdapter(mAdapter);

        //  mListView.setOnScrollListener(mAdapter);


        getPushData(false);
        getXMBData(false);
        countPage = 1;
        getOtherData(countPage, true);
    }

    private void initView(View v) {
        mDialogUtils = new DialogUtils(mActivity);

        //顶部滑动变化相关
        _title_line = v.findViewById(R.id.message_line);
        _needOffsetView = v.findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);

        _top_layout = v.findViewById(R.id.top_layout);
        params = _top_layout.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = XDensityUtils.getStatusBarHeight() + XDensityUtils.dp2px(45);
        mScrollView = v.findViewById(R.id.scrollView);
        mScrollView.setOnScrollListener(new XScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > 255) {
                    scrollY = 255;
                    _title_line.setVisibility(View.VISIBLE);
                } else {
                    _title_line.setVisibility(View.INVISIBLE);
                }
                //_top_layout.setLayoutParams(params);
                _top_layout.setBackgroundColor(Color.argb(scrollY, 255, 255, 255));
            }
        });
//
        btn_visitor = v.findViewById(R.id.message_item_1);
        btn_fans = v.findViewById(R.id.message_item_2);
        btn_system_messages = v.findViewById(R.id.message_item_3);

        _dian_1 = v.findViewById(R.id.message_item_1_dian);
        _dian_2 = v.findViewById(R.id.message_item_2_dian);
        _dian_3 = v.findViewById(R.id.message_item_3_dian);


        //推送消息
        _push_ll = v.findViewById(R.id.list_message_push_ll);
        _push_time = v.findViewById(R.id.list_message_push_time);
        _push_content = v.findViewById(R.id.list_message_push_content);
        _push_dian = v.findViewById(R.id.list_message_push_dian);
        _push_ll.setVisibility(View.GONE);
        //小脉宝
        _xmb_ll = v.findViewById(R.id.list_message_xmb_ll);
        _xmb_time = v.findViewById(R.id.list_message_xmb_time);
        _xmb_content = v.findViewById(R.id.list_message_xmb_content);
        _xmb_dian = v.findViewById(R.id.list_message_xmb_dian);
        _xmb_ll.setVisibility(View.GONE);

        //加载更多
        mFooterView = View.inflate(mActivity, R.layout.view_footer, null);
        _footer_gif= mFooterView.findViewById(R.id.load_gif);
        _footer_gif.setMovieResource(com.youth.xframe.R.raw.load_gif_icon);

        _no_data = v.findViewById(R.id.list_no_data);
        mListView = v.findViewById(R.id.message_list);
        mListView.setFocusable(false);

        mSwipeRefreshView = v.findViewById(R.id.swipeRefreshView);
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);
        mSwipeRefreshView.setProgressViewEndTarget(true, 210);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPushData(false);
                getXMBData(false);
                countPage = 1;
                getOtherData(countPage, false);
            }
        });

//        if (mScrollView != null) {
//            mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                @Override
//                public void onScrollChanged() {
//                    if (mSwipeRefreshView != null) {
//                        mSwipeRefreshView.setEnabled(mScrollView.getScrollY() == 0);
//                    }
//                }
//            });
//        }
//        ListOtherData = new ArrayList<>();
//        mAdapter = new MessageDelAdapter(mActivity, ListOtherData, "");
//        mListView.setAdapter(mAdapter);


        mScrollView.setScanScrollChangedListener(new XScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                mListView.removeFooterView(mFooterView);
                mListView.addFooterView(mFooterView);

            }

            @Override
            public void onScrolledToTop() {

            }
        });

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
//        if (!hidden) {
//            _dian_1.setVisibility(View.VISIBLE);
//            _dian_2.setVisibility(View.VISIBLE);
//            _dian_3.setVisibility(View.VISIBLE);
//                   }
        //mScrollView.scrollTo(0, 0);
    }

    /**
     * 点击事件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageActivity.class, "人气");
                _dian_1.setVisibility(View.INVISIBLE);
            }
        });
        btn_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XPreferencesUtils.get("vip_level", "0").equals("0")) {
                    mDialogUtils.showNoTitleDialog("开通会员才能看\"对我感兴趣\"", "取消", "开通", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(MemberActivity.class, "会员中心");
                            mDialogUtils.dismissDialog();
                        }
                    });
                } else {
                    startActivity(MessageActivity.class, "对我感兴趣");
                    _dian_2.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_system_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageActivity.class, "系统消息");
                _dian_3.setVisibility(View.INVISIBLE);
            }
        });

        _push_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageListActivity.class, "推送消息");
                _push_dian.setVisibility(View.GONE);
            }
        });

        _xmb_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageListActivity.class, "小脉宝");
                _xmb_dian.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 获取推送信息
     */
    private void getPushData(boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "2");
        map.put("limit", "1");
        MessageLogic.Instance(mActivity).getPushApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.e("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));


                        ListPushData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("id"));
                            item.setMessageImage(array.getJSONObject(i).getString("icon"));
                            item.setMessageName(array.getJSONObject(i).getString("title"));
                            item.setMessageContent(array.getJSONObject(i).getString("content"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageType(array.getJSONObject(i).getString("type"));
                            item.setMessageUrl(array.getJSONObject(i).getString("link"));
                            ListPushData.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ListPushData.size() > 0) {
                                    _no_data.setVisibility(View.GONE);
                                    _push_ll.setVisibility(View.VISIBLE);
                                    _push_dian.setVisibility(View.GONE);
//                                    if (_push_time.getText().equals(XTimeUtils.getTimeRange(ListPushData.get(0).getMessageTime()))) {
//                                        _push_dian.setVisibility(View.GONE);
//                                    } else {
//                                        _push_dian.setVisibility(View.VISIBLE);
//                                    }
                                    _push_time.setText(XTimeUtils.getTimeRange(ListPushData.get(0).getMessageTime()));
                                    _push_content.setText(ListPushData.get(0).getMessageName());
                                } else {
                                    _push_ll.setVisibility(View.GONE);
                                    _no_data.setVisibility(View.VISIBLE);
                                }

                                // 加载完数据设置为不刷新状态，将下拉进度收起来
                                if (mSwipeRefreshView.isRefreshing()) {
                                    mSwipeRefreshView.setRefreshing(false);
                                }
                            }
                        });

                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
            }
        });
    }

    /**
     * 获取小脉宝
     */
    private void getXMBData(boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", "1");
        MessageLogic.Instance(mActivity).getXMBApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.e("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));

                        //是否显示人气红点
                        final boolean pop_flag = json.getJSONObject("data").optBoolean("pop_flag");

                        ListXMBData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("id"));
                            item.setMessageImage("");
                            item.setMessageName(array.getJSONObject(i).getString("title"));
                            item.setMessageContent(array.getJSONObject(i).getString("content"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageType(array.getJSONObject(i).getString("type"));
                            item.setMessageMoney(array.getJSONObject(i).getString("money"));
                            ListXMBData.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ListXMBData.size() > 0) {
                                    _no_data.setVisibility(View.GONE);
                                    _xmb_ll.setVisibility(View.VISIBLE);
                                    _xmb_dian.setVisibility(View.GONE);
                                    if (pop_flag) {
                                        _dian_1.setVisibility(View.VISIBLE);
                                    } else {
                                        _dian_1.setVisibility(View.INVISIBLE);
                                    }

//                                    if (_xmb_time.getText().equals(XTimeUtils.getTimeRange(ListXMBData.get(0).getMessageTime()))) {
//                                        _xmb_dian.setVisibility(View.GONE);
//                                    } else {
//                                        _xmb_dian.setVisibility(View.VISIBLE);
//                                    }
                                    _xmb_time.setText(XTimeUtils.getTimeRange(ListXMBData.get(0).getMessageTime()));
                                    _xmb_content.setText(ListXMBData.get(0).getMessageName());
                                } else {
                                    _xmb_ll.setVisibility(View.GONE);
                                    _no_data.setVisibility(View.VISIBLE);
                                }

                                // 加载完数据设置为不刷新状态，将下拉进度收起来
                                if (mSwipeRefreshView.isRefreshing()) {
                                    mSwipeRefreshView.setRefreshing(false);
                                }
                            }
                        });

                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
            }
        });
    }

    /**
     * 获取其他
     */
    private void getOtherData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", "10");
        MessageLogic.Instance(mActivity).getGroupUserApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));
                        if (page == 1) {
                            ListOtherData = new ArrayList<>();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("from_user_id"));
                            item.setMessageImage(array.getJSONObject(i).getString("head_portrait"));
                            item.setMessageName(array.getJSONObject(i).getString("wechat_name"));
                            item.setMessageContent(array.getJSONObject(i).getString("content"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageType(array.getJSONObject(i).getString("type"));
                            item.setMessageUrl(array.getJSONObject(i).getString("title"));
                            ListOtherData.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (page == 1) {
                                    if (ListOtherData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }

                                    mAdapter = new MessageDelAdapter(mActivity, ListOtherData, "");
                                    mListView.setAdapter(mAdapter);
                                    mAdapter.setOnItemClickListener(new MessageDelAdapter.onItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", ListOtherData.get(position).getMessageId());
                                            bundle.putString("name", ListOtherData.get(position).getMessageName());
                                            bundle.putString("head", ListOtherData.get(position).getMessageImage());
                                            startActivity(MessageListActivity.class, bundle);
                                        }
                                    });

                                    mAdapter.setOnItemDeleteClickListener(new MessageDelAdapter.onItemDeleteListener() {
                                        @Override
                                        public void onDeleteClick(int position) {
                                            getDelData(ListOtherData.get(position).getMessageId());
                                        }
                                    });
                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });

                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
            }
        });
    }

    /**
     * 删除
     */
    private void getDelData(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("from_user_id", id);
        MessageLogic.Instance(mActivity).getDelUserApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.e("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        showToast("删除成功");
                        countPage = 1;
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                getOtherData(countPage, true);
                            }
                        });
                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("删除失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showToast("删除失败");
            }
        });
    }


    /***/
    private void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }

}
