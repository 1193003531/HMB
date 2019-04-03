package com.huimaibao.app.fragment.message.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.message.adapter.MessageAdapter;
import com.huimaibao.app.fragment.message.adapter.MessageDelAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.fragment.mine.act.CardClipDetailActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message2Activity extends BaseActivity {
    private String mType = "";

    private LinearLayout _no_data;
    private ListView mListView;
    private MessageAdapter mAdapter;
    private MessageDelAdapter mDelAdapter;
    private List<MessageEntity> listData, ListPushData, ListXMBData, ListOtherData;


    private XSwipeRefreshView mSwipeRefreshView;
    private View mTopView;
    //人气，感兴趣
    private TextView _top_left, _top_right;
    private String _top_left_value = "", _top_right_value = "";
    //推送消息
    private LinearLayout _push_ll;
    private TextView _push_time, _push_content;
    private ImageView _push_dian;
    //小脉宝
    private LinearLayout _xmb_ll;
    private TextView _xmb_time, _xmb_content;
    private ImageView _xmb_dian;


    private int countPage = 1;

    //private DialogUtils mDialogUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_message);

        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        mActivity = this;
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        // mDialogUtils = new DialogUtils(mActivity);
        initView();
        initEvent();
        initData();
    }


    private void initView() {
        mSwipeRefreshView = findViewById(R.id.swipeRefreshView);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);
        if (mType.equals("系统消息")) {
            mTopView = getLayoutInflater().inflate(R.layout.act_message_push_top, null);
            //推送消息
            _push_ll = mTopView.findViewById(R.id.list_message_push_ll);
            _push_time = mTopView.findViewById(R.id.list_message_push_time);
            _push_content = mTopView.findViewById(R.id.list_message_push_content);
            _push_dian = mTopView.findViewById(R.id.list_message_push_dian);
            //小脉宝
            _xmb_ll = mTopView.findViewById(R.id.list_message_xmb_ll);
            _xmb_time = mTopView.findViewById(R.id.list_message_xmb_time);
            _xmb_content = mTopView.findViewById(R.id.list_message_xmb_content);
            _xmb_dian = mTopView.findViewById(R.id.list_message_xmb_dian);
        } else {
            mTopView = getLayoutInflater().inflate(R.layout.act_message_top, null);
            //_top_ll = mTopView.findViewById(R.id.act_message_top_ll);
            _top_left = mTopView.findViewById(R.id.act_message_top_left);
            _top_right = mTopView.findViewById(R.id.act_message_top_right);
        }
        mListView.addHeaderView(mTopView);

        if (mType.equals("系统消息")) {
            setShoweLine(true);
            setNeedBackGesture(false);
        } else {
            setShoweLine(false);
            setNeedBackGesture(true);
            if (mType.equals("人气")) {
                _top_right.setVisibility(View.VISIBLE);
                _top_left.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.msg_act_rq_icon), null,
                        null, null);
                _top_left.setText("总人气 " + 0);
                _top_right.setText("今日人气 " + 0);
            } else {
                _top_right.setVisibility(View.GONE);
                _top_left.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.msg_act_gxq_icon), null,
                        null, null);
                _top_left.setText("总人数 " + 0);
            }
        }

        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);

        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);
        mSwipeRefreshView.setItemCount(5);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSwipeRefreshView.setLoading(false);
//                if (mType.equals("系统消息")) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", mAdapter.getItem(position-1).getMessageId());
//                    bundle.putString("name", mAdapter.getItem(position-1).getMessageName());
//                    bundle.putString("head", mAdapter.getItem(position-1).getMessageImage());
//                    startActivity(MessageListActivity.class, bundle);
//                } else {
                if (listData.get(position - 1).getMessageId().equals("0")) {
                    DialogUtils.of(mActivity).showNoSureDialog("平台外用户访问了您", "知道了");
                } else {
                    startActivity(CardClipDetailActivity.class, listData.get(position - 1).getMessageId());
                }

            }
        });

    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.list_message_push_ll:
                startActivity(MessageListActivity.class, "推送消息");
                break;
            case R.id.list_message_xmb_ll:
                startActivity(MessageListActivity.class, "小脉宝");
                break;
        }
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

    private void initData() {
        countPage = 1;
        if (mType.equals("人气")) {
            getPopularityData(countPage, false);
        } else if (mType.equals("系统消息")) {
            getPushData(false);
            getXMBData(false);
            getOtherData(countPage, false);
        } else {
            getInterestedData(countPage, false);
        }
    }

    private void loadMoreData() {
        countPage++;
        if (mType.equals("人气")) {
            getPopularityData(countPage, false);
        } else if (mType.equals("系统消息")) {
            getPushData(false);
            getXMBData(false);
            getOtherData(countPage, false);
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
                        _top_left_value = json.getJSONObject("data").getString("all");
                        _top_right_value = json.getJSONObject("data").getString("today");
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
                                _top_left.setText("总人气 " + _top_left_value);
                                _top_right.setText("今日人气 " + _top_right_value);
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
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
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
                                _top_left.setText("总人数 " + _top_left_value);
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
                }
            }

            @Override
            public void onFailed(String error) {
                // XLog.e("error:" + error);
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
                    // XLog.e("json:" + json);
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
                                if (ListPushData.size() == 0) {
                                    _no_data.setVisibility(View.VISIBLE);
                                    _push_ll.setVisibility(View.GONE);
                                } else {
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
                // XLog.e("error:" + error);
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
                                if (ListXMBData.size() == 0) {
                                    _no_data.setVisibility(View.VISIBLE);
                                    _xmb_ll.setVisibility(View.GONE);
                                } else {
                                    _no_data.setVisibility(View.GONE);
                                    _xmb_ll.setVisibility(View.VISIBLE);
                                    _xmb_dian.setVisibility(View.GONE);
//                                    if (_xmb_time.getText().equals(XTimeUtils.getTimeRange(ListXMBData.get(0).getMessageTime()))) {
//                                        _xmb_dian.setVisibility(View.GONE);
//                                    } else {
//                                        _xmb_dian.setVisibility(View.VISIBLE);
//                                    }
                                    _xmb_time.setText(XTimeUtils.getTimeRange(ListXMBData.get(0).getMessageTime()));
                                    _xmb_content.setText(ListXMBData.get(0).getMessageName());
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
                //XLog.e("error:" + error);
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
                //XLog.e("error:" + error);
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

}
