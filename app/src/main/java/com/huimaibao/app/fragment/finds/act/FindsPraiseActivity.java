package com.huimaibao.app.fragment.finds.act;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsPraiseAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsPraiseEntity;
import com.huimaibao.app.fragment.finds.server.FindsLogic;
import com.huimaibao.app.fragment.message.adapter.MessageAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 点赞的人
 */
public class FindsPraiseActivity extends BaseActivity {

    //title,clear
    private TextView _top_title;
    private RelativeLayout _top_msg_rl;


    //数据集合
    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private FindsPraiseAdapter mAdapter;
    private List<FindsPraiseEntity> listData;

    private int countPage = 1, totalPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_list_my);
        setNeedBackGesture(true);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_title = findViewById(R.id.finds_list_title);
        _top_msg_rl = findViewById(R.id.finds_list_top_msg_rl);

        _top_title.setText("点赞的人");
        _top_msg_rl.setVisibility(View.INVISIBLE);


        //数据集合
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);


        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);
        mSwipeRefreshView.setItemCount(5);

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

    private void initData() {
        countPage = 1;
        getPraiseUserListData(countPage, false);
    }

    private void loadMoreData() {
        if (countPage >= totalPage) {
            mSwipeRefreshView.setLoading(false);
            showToast("没有数据了");
            return;
        }
        countPage++;
        getPraiseUserListData(countPage, false);
    }


    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }


    /**
     * 获取点赞的人
     */
    private void getPraiseUserListData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", XPreferencesUtils.get("dynamic_id", ""));
        map.put("page", page);
        map.put("pageSize", "10");
        FindsLogic.Instance(mActivity).getPraiseUserListApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        totalPage = json.getJSONObject("data").optInt("total", 0);
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("list"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }

                        for (int i = 0; i < array.length(); i++) {
                            FindsPraiseEntity entity = new FindsPraiseEntity();
                            entity.setFindsUserId(array.getJSONObject(i).optString("user_id"));
                            entity.setFindsUserHead(array.getJSONObject(i).optString("head_picture"));
                            entity.setFindsUserName(array.getJSONObject(i).optString("user_name"));
                            listData.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (page == 1) {
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new FindsPraiseAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);

                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {

                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        mAdapter = new FindsPraiseAdapter(mActivity, listData);
                                        mListView.setAdapter(mAdapter);
                                    }
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });

                    } else {
                        showToast(msg);
                        showToast();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showToast();
            }
        });
    }

    /***/
    public void showToast() {
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
