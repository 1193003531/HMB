package com.huimaibao.app.fragment.message.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.message.act.MessageListActivity;
import com.huimaibao.app.fragment.message.adapter.MessageAdapter;
import com.huimaibao.app.fragment.message.adapter.MessageDelAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.fragment.mine.act.CardClipDetailActivity;
import com.huimaibao.app.fragment.mine.adapter.CardAdapter;
import com.huimaibao.app.fragment.mine.entity.CardEntity;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 客户管理
 */
public class CMListFragment extends BaseFragment {


    private String type = "", text = "";

    private int countPage = 1;

    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data;

    private View mTopView;
    //人气，感兴趣
    private TextView _top_left, _top_right;
    private String _top_left_value = "", _top_right_value = "";

    private MessageAdapter mAdapter;
    private List<MessageEntity> listData;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_e_m_l_list, container, false);
        Bundle args = getArguments();
        text = args != null ? args.getString("name") : "人气";
        type = args != null ? args.getString("type") : "1";
        initView(view);
        initEvent();
        return view;
    }


    @Override
    protected void initData() {
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // getData();
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        listData = new ArrayList<>();

        mTopView = getLayoutInflater().inflate(R.layout.act_message_top, null);
        //_top_ll = mTopView.findViewById(R.id.act_message_top_ll);
        _top_left = mTopView.findViewById(R.id.act_message_top_left);
        _top_right = mTopView.findViewById(R.id.act_message_top_right);

        _no_data = v.findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);
        _no_image_data = v.findViewById(R.id.list_no_data_iv);
        _no_tv_data = v.findViewById(R.id.list_no_data_tv);

        mSwipeRefreshView = v.findViewById(R.id.list_swipe_value);
        mListView = v.findViewById(R.id.list_pull_value);

        mListView.addHeaderView(mTopView);

        if (text.equals("人气")) {
            _no_image_data.setImageResource(R.drawable.blank_pages_12_icon);
            _no_tv_data.setText(R.string.no_datas_12);
            // _top_right.setVisibility(View.VISIBLE);
            _top_left.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.msg_act_rq_icon), null,
                    null, null);
            _top_left.setText("总人气  " + 0);
            _top_right.setText("今日人气  " + 0);
        } else {
            _no_image_data.setImageResource(R.drawable.blank_pages_13_icon);
            _no_tv_data.setText(R.string.no_datas_13);
            //_top_right.setVisibility(View.GONE);
            _top_left.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.msg_act_gxq_icon), null,
                    null, null);
            _top_left.setText("总人数  " + 0);
            _top_right.setText("今日人数  " + 0);
        }

//
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    //ToastUtils.showCenter("" + position);
                    return;
                }
                if (XEmptyUtils.isSpace(listData.get(position - 1).getMessageId())) {
                    showToast("用户不存在");
                    return;
                }
                if (listData.get(position - 1).getMessageId().equals("0")) {
                    if (text.equals("人气")) {
                        DialogUtils.of(mActivity).showNoSureDialog("平台外用户访问了您", "知道了");
                    } else {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + listData.get(position - 1).getMessagePhone()));
                        startActivity(intent);//调用上面这个intent实现拨号
                    }
                } else {
                    //startActivity(CardClipDetailActivity.class, listData.get(position - 1).getMessageId());
                    startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + listData.get(position - 1).getMessageId() + ServerApi.HOME_PAGE_WEB_TOKEN);

                }
            }
        });


        // 不能在onCreate中设置，这个表示当前是刷新状态，如果一进来就是刷新状态，SwipeRefreshLayout会屏蔽掉下拉事件
        //swipeRefreshLayout.setRefreshing(true);

        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        //mSwipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        mSwipeRefreshView.setItemCount(2);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);

    }


    private void initEvent() {
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });


        // 设置上拉加载更多
        mSwipeRefreshView.setOnLoadMoreListener(new XSwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        countPage++;
        if (text.equals("人气")) {
            getPopularityData(countPage, false);
        } else {
            getInterestedData(countPage, false);
        }
    }


    private void getData() {
        countPage = 1;
        if (text.equals("人气")) {
            getPopularityData(countPage, false);
        } else {
            getInterestedData(countPage, false);
        }

    }

    /**
     * 获取人气
     */
    private void getPopularityData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", "10");
        MessageLogic.Instance(mActivity).getPopularityApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        _top_left_value = json.getJSONObject("data").optString("all", "0");
                        _top_right_value = json.getJSONObject("data").optString("today", "0");
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("user_id"));
                            item.setMessageImage(XEmptyUtils.isSpace(array.getJSONObject(i).getString("head_picture")) ? array.getJSONObject(i).getString("head_portrait") : array.getJSONObject(i).getString("head_picture"));
                            item.setMessageWechatName(XEmptyUtils.isSpace(array.getJSONObject(i).getString("user_name")) ? array.getJSONObject(i).getString("wechat_name") : array.getJSONObject(i).getString("user_name"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageName("");
                            item.setMessagePhone("");
                            listData.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _top_left.setText("总人气  " + _top_left_value);
                                _top_right.setText("今日人气  " + _top_right_value);
                                if (page == 1) {
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }

                                    mAdapter = new MessageAdapter(mActivity, listData, "人气");
                                    mListView.setAdapter(mAdapter);
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
                    LogUtils.debug("json:" + e);
                    showNoData();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("json:" + error);
                showNoData();
            }
        });
    }

    /**
     * 获取对我感兴趣
     */
    private void getInterestedData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", "10");
        MessageLogic.Instance(mActivity).getInterestedApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        _top_left_value = json.getJSONObject("data").getString("all");
                        _top_right_value = json.getJSONObject("data").getString("today");
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("user_id"));
                            item.setMessageImage(XEmptyUtils.isSpace(array.getJSONObject(i).getString("head_picture")) ? array.getJSONObject(i).getString("head_portrait") : array.getJSONObject(i).getString("head_picture"));
                            item.setMessageWechatName(XEmptyUtils.isSpace(array.getJSONObject(i).getString("user_name")) ? array.getJSONObject(i).getString("wechat_name") : array.getJSONObject(i).getString("user_name"));
                            item.setMessageName(array.getJSONObject(i).getString("name"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessagePhone(array.getJSONObject(i).getString("phone"));
                            listData.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _top_left.setText("总人数  " + _top_left_value);
                                _top_right.setText("今日人数  " + _top_right_value);
                                if (page == 1) {
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }

                                    if (mAdapter == null) {
                                        mAdapter = new MessageAdapter(mActivity, listData, "感兴趣");
                                    }
                                    mListView.setAdapter(mAdapter);
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
                    LogUtils.debug("json:" + e);
                    showNoData();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("json:" + error);
                showNoData();
            }
        });
    }


    /***/
    public void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
                if (listData.size() == 0) {
                    _no_data.setVisibility(View.VISIBLE);
                } else {
                    _no_data.setVisibility(View.GONE);
                }
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }

    /***/
    public void showNoData() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listData.size() == 0) {
                    _no_data.setVisibility(View.VISIBLE);
                } else {
                    _no_data.setVisibility(View.GONE);
                }
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }

}
