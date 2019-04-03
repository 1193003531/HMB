package com.huimaibao.app.fragment.message.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.electcircle.act.ShareTaskActivity;
import com.huimaibao.app.fragment.message.adapter.PushAdapter;
import com.huimaibao.app.fragment.message.adapter.UserAdapter;
import com.huimaibao.app.fragment.message.adapter.XMBAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.fragment.mine.act.MemberActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageListActivity extends BaseActivity {
    private String mType = "", from_user_id = "", head = "";

    private LinearLayout _no_data;
    private ListView mListView;
    private PushAdapter mPushAdapter;
    private XMBAdapter mXMBAdapter;
    private UserAdapter mUserAdapter;
    private List<MessageEntity> listData;


    private XSwipeRefreshView mSwipeRefreshView;

    private int countPage = 1;

    //private DialogUtils mDialogUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_message_list);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent.getAction().equals("two")) {
            Bundle bundle = intent.getExtras();
            mType = bundle.getString("name");
            from_user_id = bundle.getString("id");
            head = bundle.getString("head");
        } else {
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
                if (mType.equals("推送消息")) {
                    startActivity(MessageWebActivity.class, "", listData.get(position).getMessageUrl());
                } else if (mType.equals("小脉宝")) {
                    //  getXMBData(countPage, false);
                } else {
                    if (listData.get(position).getMessageType().equals("2")) {
                        startActivity(ShareTaskActivity.class, " 分享任务");
                    } else if (listData.get(position).getMessageType().equals("1")) {
                        startActivity(MemberActivity.class, "会员中心");
                    }
                }
            }
        });

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
        if (mType.equals("推送消息")) {
            getPushData(countPage, false);
        } else if (mType.equals("小脉宝")) {
            getXMBData(countPage, false);
        } else {
            getOtherData(countPage, false);
        }
    }

    private void loadMoreData() {
        countPage++;
        if (mType.equals("推送消息")) {
            getPushData(countPage, false);
        } else if (mType.equals("小脉宝")) {
            getXMBData(countPage, false);
        } else {
            getOtherData(countPage, false);
        }
    }


    /**
     * 获取推送信息
     */
    private void getPushData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "2");
        map.put("limit", "10");
        map.put("page", page);
        MessageLogic.Instance(mActivity).getPushApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.e("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        }else {
                            if(array.length()==0){
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("id"));
                            item.setMessageImage(array.getJSONObject(i).getString("icon"));
                            item.setMessageName(array.getJSONObject(i).getString("title"));
                            item.setMessageContent(array.getJSONObject(i).getString("content"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageType(array.getJSONObject(i).getString("type"));
                            item.setMessageUrl(array.getJSONObject(i).getString("link"));
                            listData.add(item);
                        }
                        showData();
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
    private void getXMBData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", "10");
        map.put("page", page);
        MessageLogic.Instance(mActivity).getXMBApi(map, isShow, new ResultBack() {
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
                        }else {
                            if(array.length()==0){
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("id"));
                            item.setMessageImage("");
                            item.setMessageName(array.getJSONObject(i).getString("title"));
                            item.setMessageContent(array.getJSONObject(i).getString("content"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageType(array.getJSONObject(i).getString("type"));
                            item.setMessageMoney(array.getJSONObject(i).getString("money"));
                            listData.add(item);
                        }
                        showData();
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
        map.put("from_user_id", from_user_id);
        MessageLogic.Instance(mActivity).getUserApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("getUser:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        }else {
                            if(array.length()==0){
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("id"));
                            item.setMessageImage(head);
                            item.setMessageName(array.getJSONObject(i).getString("title"));
                            item.setMessageContent(array.getJSONObject(i).getString("content"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageType(array.getJSONObject(i).getString("type"));
                            listData.add(item);
                        }
                        showData();
                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("getUser:" + error);
            }
        });
    }


    /**
     * 显示数据
     */
    private void showData() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mType.equals("推送消息")) {
                    if (countPage == 1) {
                        if (listData.size() == 0) {
                            _no_data.setVisibility(View.VISIBLE);
                        } else {
                            _no_data.setVisibility(View.GONE);
                        }
                        if (mPushAdapter == null) {
                            mPushAdapter = new PushAdapter(mActivity, listData);
                        }
                        mListView.setAdapter(mPushAdapter);
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        if (mSwipeRefreshView.isRefreshing()) {
                            mSwipeRefreshView.setRefreshing(false);
                        }
                    } else {
                        mPushAdapter.notifyDataSetChanged();
                        // 加载完数据设置为不加载状态，将加载进度收起来
                        mSwipeRefreshView.setLoading(false);
                    }

                } else if (mType.equals("小脉宝")) {
                    if (countPage == 1) {
                        if (listData.size() == 0) {
                            _no_data.setVisibility(View.VISIBLE);
                        } else {
                            _no_data.setVisibility(View.GONE);
                        }
                        if (mXMBAdapter == null) {
                            mXMBAdapter = new XMBAdapter(mActivity, listData);
                        }
                        mListView.setAdapter(mXMBAdapter);
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        if (mSwipeRefreshView.isRefreshing()) {
                            mSwipeRefreshView.setRefreshing(false);
                        }
                    } else {
                        mXMBAdapter.notifyDataSetChanged();
                        // 加载完数据设置为不加载状态，将加载进度收起来
                        mSwipeRefreshView.setLoading(false);
                    }
                } else {
                    if (countPage == 1) {
                        if (listData.size() == 0) {
                            _no_data.setVisibility(View.VISIBLE);
                        } else {
                            _no_data.setVisibility(View.GONE);
                        }
                        if (mUserAdapter == null) {
                            mUserAdapter = new UserAdapter(mActivity, listData);
                        }
                        mListView.setAdapter(mUserAdapter);

                        mUserAdapter.setOnItemConsentListener(new UserAdapter.onItemConsentListener() {
                            @Override
                            public void onItemConsentClick(int position) {
                                getStaffData(listData.get(position).getMessageId(), "1", "加入");
                            }
                        });

                        mUserAdapter.setOnItemRefuseListener(new UserAdapter.onItemRefuseListener() {
                            @Override
                            public void onItemRefuseClick(int position) {
                                getStaffData(listData.get(position).getMessageId(), "2", "拒绝");
                            }
                        });

                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        if (mSwipeRefreshView.isRefreshing()) {
                            mSwipeRefreshView.setRefreshing(false);
                        }
                    } else {
                        mUserAdapter.notifyDataSetChanged();
                        // 加载完数据设置为不加载状态，将加载进度收起来
                        mSwipeRefreshView.setLoading(false);
                    }
                }
            }
        });
    }

    /**
     * 是否加入
     */
    private void getStaffData(String id, String is_agree, final String msg) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("is_agree", is_agree);//1同意2，不同意
        MessageLogic.Instance(mActivity).getStaffApi(map, msg + "中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("tStaff:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        showToast(msg + "成功");
                    } else {
                        showToast("失败," + msg);
                    }
                } catch (Exception e) {
                    LogUtils.debug("tStaff:" + e);
                    showToast(msg + "失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("tStaff:" + error);
                showToast(msg + "失败");
            }
        });
    }


    /**
     * 提示
     */
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
