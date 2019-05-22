package com.huimaibao.app.fragment.home.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.adapter.ToCloneAdapter;
import com.huimaibao.app.fragment.home.entity.ToCloneEntity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.mine.act.MemberActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.fragment.web.PersonalWebDetailsActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.pay.PayActivity;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 去克隆
 */
public class ToCloneActivity extends BaseActivity {

    private String mType = "";


    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private ToCloneAdapter mAdapter;
    private List<ToCloneEntity> listData;

    private String _to_clone_value = "";

    private int countPage = 1, last_page = 1;

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_to_clone);
        mActivity = this;
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        mDialogUtils = new DialogUtils(mActivity);
        listData = new ArrayList<>();
        initView();

    }

    /***/
    private void initView() {
        _no_data = findViewById(R.id.list_no_data);
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //startActivity(MessageWebActivity.class, "个人微网", ServerApi.PERSONAL_DETAILS_URL + "detail/" + listData.get(position).getToCloneId());
                Bundle bundle = new Bundle();
                bundle.putString("userId", listData.get(position).getToCloneUserId());
                bundle.putString("id", listData.get(position).getToCloneId());
                bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + listData.get(position).getToCloneId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                bundle.putString("share_title", listData.get(position).getToCloneTitle());
                bundle.putString("share_des", "");
                bundle.putString("share_imageUrl", listData.get(position).getToCloneImage());

                startActivity(PersonalWebDetailsActivity.class, bundle);
                // startActivity(PersonalWebDetailsActivity.class, listData.get(position).getToCloneId(), ServerApi.PERSONAL_DETAILS_URL + "detail/" + listData.get(position).getToCloneId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
            }
        });


        // 不能在onCreate中设置，这个表示当前是刷新状态，如果一进来就是刷新状态，SwipeRefreshLayout会屏蔽掉下拉事件
        //swipeRefreshLayout.setRefreshing(true);

        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        //mSwipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        mSwipeRefreshView.setItemCount(5);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);

        initEvent();
        initData();
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
        if (countPage <= last_page) {
            getData(countPage, false);
        }else{
            // 加载完数据设置为不加载状态，将加载进度收起来
            mSwipeRefreshView.setLoading(false);
        }
    }


    private void initData() {
        countPage = 1;
        getData(countPage, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isPaymentMoney", false)) {
            startActivity(MessageWebActivity.class, "克隆", ServerApi.PERSONAL_CLONE_URL + _to_clone_value + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
            XPreferencesUtils.put("isPaymentMoney", false);
            finish();
        }
    }

    /**
     * 获取克隆网页
     */
    private void getData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
        map.put("is_clone", "1");
        HomeLogic.Instance(mActivity).toCloneApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("rank"));
                        last_page = new JSONObject(json.getJSONObject("data").optString("meta", "")).optInt("last_page", 1);
//                        XLog.d("array:" + array);
                        if (countPage == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            ToCloneEntity entity = new ToCloneEntity();
                            entity.setToCloneId(array.getJSONObject(i).getString("id"));
                            entity.setToCloneImage(array.getJSONObject(i).getString("cover"));
                            entity.setToCloneTitle(array.getJSONObject(i).getString("title"));
                            entity.setToCloneNum(array.getJSONObject(i).getString("share_count"));
                            entity.setToCloneBrowse(array.getJSONObject(i).getString("popularity"));
                            entity.setToCloneIsClone(array.getJSONObject(i).getString("is_clone"));
                            entity.setToCloneMoney(array.getJSONObject(i).getString("clone_price"));
                            entity.setToCloneHead(array.getJSONObject(i).getJSONObject("user").getString("portrait"));
                            entity.setToCloneName(array.getJSONObject(i).getJSONObject("user").getString("name"));
                            entity.setToCloneUserId(array.getJSONObject(i).getJSONObject("user").getString("id"));
                            listData.add(entity);
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (countPage == 1) {
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    }
                                    mAdapter = new ToCloneAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);
                                    mAdapter.setOnItemCloneListener(new ToCloneAdapter.onItemCloneListener() {
                                        @Override
                                        public void onItemCloneClick(int position) {
                                            if (XPreferencesUtils.get("vip_level", "0").equals("0")) {
                                                mDialogUtils.showNoTitleDialog("开通会员，克隆网页", "取消", "开通", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        startActivity(MemberActivity.class, "会员中心");
                                                        mDialogUtils.dismissDialog();
                                                    }
                                                });
                                            } else {
                                                _to_clone_value = listData.get(position).getToCloneId();
                                                if (XStringUtils.m1(listData.get(position).getToCloneMoney()) > 0) {
                                                    toClonePayData(listData.get(position).getToCloneId(), listData.get(position).getToCloneMoney());
                                                } else {
                                                    startActivity(MessageWebActivity.class, "克隆", ServerApi.PERSONAL_CLONE_URL + listData.get(position).getToCloneId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                                                }
                                            }
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
                        hide();
                    }
                } catch (Exception e) {
                    // XLog.d("error:" + e);
                    e.printStackTrace();
                    hide();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                hide();
            }
        });
    }

    /**
     * 获取克隆预支付信息
     */
    private void toClonePayData(String temp_id, final String money) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("temp_id", temp_id);
        HomeLogic.Instance(mActivity).toClonePayApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    final JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //final String data = json.getString("data");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mActivity, PayActivity.class);
                                intent.putExtra("vType", "克隆付费");
                                intent.putExtra("payType", "个人微网克隆");
                                intent.putExtra("payMoney", money);
                                intent.putExtra("payId", json.optJSONObject("data").optString("clone_id"));
                                startActivity(intent);
                                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    // XLog.d("error:" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
            }
        });
    }


    private void hide() {
        runOnUiThread(new Runnable() {
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
                // 加载完数据设置为不加载状态，将加载进度收起来
                mSwipeRefreshView.setLoading(false);
            }
        });
    }

}
