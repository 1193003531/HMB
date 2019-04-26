package com.huimaibao.app.fragment.message.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.huimaibao.app.fragment.message.act.MessageActivity;
import com.huimaibao.app.fragment.message.act.MessageListActivity;
import com.huimaibao.app.fragment.message.adapter.MessageDelAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.fragment.mine.adapter.CardAdapter;
import com.huimaibao.app.fragment.mine.entity.CardEntity;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 提醒，关注，粉丝
 */
public class MsgListFragment extends BaseFragment {


    private String type = "", text = "";

    private int countPage = 1;

    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data;

    //提醒
    private MessageDelAdapter mDelAdapter;
    private List<MessageEntity> ListOtherData;

    //关注
    private CardAdapter mCardAdapter;
    private List<CardEntity> ListCardData;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_e_m_l_list, container, false);
        Bundle args = getArguments();
        text = args != null ? args.getString("name") : "关注";
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
        _no_data = v.findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);
        _no_image_data = v.findViewById(R.id.list_no_data_iv);
        _no_tv_data = v.findViewById(R.id.list_no_data_tv);
        if (text.equals("关注")) {
            _no_image_data.setImageResource(R.drawable.blank_pages_14_icon);
            _no_tv_data.setText(R.string.no_datas_14);
        } else if (text.equals("粉丝")) {
            _no_image_data.setImageResource(R.drawable.blank_pages_15_icon);
            _no_tv_data.setText(R.string.no_datas_15);
        } else {
            _no_image_data.setImageResource(R.drawable.blank_pages_16_icon);
            _no_tv_data.setText(R.string.no_datas_17);
        }


        mSwipeRefreshView = v.findViewById(R.id.list_swipe_value);
        mListView = v.findViewById(R.id.list_pull_value);


//
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (text.equals("关注") || text.equals("粉丝")) {
                    if (XEmptyUtils.isSpace(mCardAdapter.getItem(position).getCardId())) {
                        showToast("用户不存在");
                        return;
                    }
                    //startActivity(CardClipDetailActivity.class, mCardAdapter.getItem(position).getCardId());
                    startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + mCardAdapter.getItem(position).getCardId() + ServerApi.HOME_PAGE_WEB_TOKEN);
                } else {
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
        if (text.equals("关注")) {
            getCardClip("", countPage, false);
        } else if (text.equals("粉丝")) {
            getCardClip("fans", countPage, false);
        } else {
            getOtherData(countPage, false);
        }
    }


    private void getData() {
        countPage = 1;
        if (text.equals("关注")) {
            getCardClip("", countPage, false);
        } else if (text.equals("粉丝")) {
            getCardClip("fans", countPage, false);
        } else {
            getOtherData(countPage, false);
        }

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
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
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
                                    if (mDelAdapter == null) {
                                        mDelAdapter = new MessageDelAdapter(mActivity, ListOtherData, "");
                                    }
                                    mListView.setAdapter(mDelAdapter);
                                    mDelAdapter.setOnItemClickListener(new MessageDelAdapter.onItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            // XLog.e("position:"+position);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", ListOtherData.get(position).getMessageId());
                                            bundle.putString("name", ListOtherData.get(position).getMessageName());
                                            bundle.putString("head", ListOtherData.get(position).getMessageImage());
                                            startActivity(MessageListActivity.class, bundle);
                                        }
                                    });

                                    mDelAdapter.setOnItemDeleteClickListener(new MessageDelAdapter.onItemDeleteListener() {
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
                                    mDelAdapter.notifyDataSetChanged();
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
                    showNoData();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showNoData();
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
                    // XLog.e("json:" + json);
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
                // XLog.e("error:" + error);
                showToast("删除失败");
            }
        });
    }


    /**
     * 获取用户收藏名片
     */
    private void getCardClip(final String type, final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
        map.put("keyword", "");
        map.put("type", type);
        LogUtils.debug("lib=s=" + map);
        CardClipLogic.Instance(mActivity).getCardClipApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        if (type.equals("fans")) {
                            //json.getJSONObject("data").getString("count")
                            XPreferencesUtils.put("fans_num", json.getJSONObject("data").optString("count", "0"));
                            Intent intent = new Intent("android.intent.action.MESSAGE_FOCUS");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        } else {
                            XPreferencesUtils.put("card_num", json.getJSONObject("data").optString("count", "0"));
                            Intent intent = new Intent("android.intent.action.MESSAGE_FOCUS");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        }

                        JSONArray array = new JSONArray(json.getJSONObject("data").optString("list", "[]"));
                        if (page == 1) {
                            ListCardData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            CardEntity ili = new CardEntity();
                            ili.setCardType("名片夹");
                            ili.setCardId(array.getJSONObject(i).getString("user_id"));
                            ili.setCardName(array.getJSONObject(i).getString("name"));
                            ili.setCardJobs(array.getJSONObject(i).getString("profession"));
                            ili.setCardIndustry(array.getJSONObject(i).getString("industry"));
                            ili.setCardCompany(array.getJSONObject(i).getString("company"));
                            ili.setCardAddr(array.getJSONObject(i).getString("address"));
                            ili.setCardPhone(array.getJSONObject(i).getString("phone"));
                            ili.setCardWechat(array.getJSONObject(i).getString("wechat"));
                            ili.setCardHead(array.getJSONObject(i).getString("cover"));
                            ili.setCardIsVip(array.getJSONObject(i).getString("vip_level"));
                            ili.setCardStyle(array.getJSONObject(i).optString("style", "style3"));
                            ListCardData.add(ili);
                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshView.setVisibility(View.VISIBLE);
                                _no_data.setVisibility(View.GONE);
                                if (page == 1) {
                                    if (ListCardData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    }
                                    mCardAdapter = new CardAdapter(mActivity, ListCardData);
                                    mListView.setAdapter(mCardAdapter);
                                    //加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    if (mCardAdapter == null) {
                                        mCardAdapter = new CardAdapter(mActivity, ListCardData);
                                        mListView.setAdapter(mCardAdapter);
                                    } else {
                                        mCardAdapter.notifyDataSetChanged();
                                    }
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });

                    } else {
                        showToast(message);
                    }

                } catch (Exception e) {
                    LogUtils.debug("lib=s=" + e.toString());
                    showNoData();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("lib=s=" + error);
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
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }

}
