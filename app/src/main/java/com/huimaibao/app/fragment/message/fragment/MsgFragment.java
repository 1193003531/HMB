package com.huimaibao.app.fragment.message.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息-通知
 */
public class MsgFragment extends BaseFragment {


    //推送消息
    private LinearLayout _push_ll;
    private TextView _push_time, _push_content;
    private ImageView _push_dian;
    //小脉宝
    private LinearLayout _xmb_ll;
    private TextView _xmb_time, _xmb_content;
    private ImageView _xmb_dian;

    private SwipeRefreshLayout mSwipeRefreshView;
    // private LinearLayout _no_data;


    private List<MessageEntity> ListPushData, ListXMBData;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_message_push_top, container, false);

        initView(view);

        return view;
    }

    @Override
    protected void initData() {
        getPushData();
        getXMBData();
    }


    /**
     * 初始化控件
     */
    private void initView(View v) {
        //推送消息
        _push_ll = v.findViewById(R.id.list_message_push_ll);
        _push_time = v.findViewById(R.id.list_message_push_time);
        _push_content = v.findViewById(R.id.list_message_push_content);
        _push_dian = v.findViewById(R.id.list_message_push_dian);
        //小脉宝
        _xmb_ll = v.findViewById(R.id.list_message_xmb_ll);
        _xmb_time = v.findViewById(R.id.list_message_xmb_time);
        _xmb_content = v.findViewById(R.id.list_message_xmb_content);
        _xmb_dian = v.findViewById(R.id.list_message_xmb_dian);

//        _no_data = v.findViewById(R.id.list_no_data);
//        _no_data.setVisibility(View.GONE);

        mSwipeRefreshView = v.findViewById(R.id.list_swipe_value);


        // 设置下拉进度的主题颜色
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });


    }

    /**
     * 获取推送信息
     */
    private void getPushData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "2");
        map.put("limit", "1");
        MessageLogic.Instance(mActivity).getPushApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));

                        ListPushData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).optString("id"));
                            item.setMessageImage(array.getJSONObject(i).optString("icon"));
                            item.setMessageName(array.getJSONObject(i).optString("title"));
                            item.setMessageContent(array.getJSONObject(i).optString("content"));
                            item.setMessageTime(array.getJSONObject(i).optString("created_at"));
                            item.setMessageType(array.getJSONObject(i).optString("type"));
                            item.setMessageUrl(array.getJSONObject(i).optString("link"));
                            ListPushData.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ListPushData.size() == 0) {
                                    //_no_data.setVisibility(View.VISIBLE);
                                    //_push_ll.setVisibility(View.GONE);
                                } else {
//                                   _no_data.setVisibility(View.GONE);
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
//                                // 加载完数据设置为不刷新状态，将下拉进度收起来
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
     * 获取小脉宝
     */
    private void getXMBData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", "1");
        MessageLogic.Instance(mActivity).getXMBApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
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
                                    //_no_data.setVisibility(View.VISIBLE);
                                    //_xmb_ll.setVisibility(View.GONE);
                                } else {
                                    // _no_data.setVisibility(View.GONE);
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
//                                // 加载完数据设置为不刷新状态，将下拉进度收起来
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
