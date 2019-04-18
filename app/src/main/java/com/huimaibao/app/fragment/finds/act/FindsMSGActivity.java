package com.huimaibao.app.fragment.finds.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsAdapter;
import com.huimaibao.app.fragment.finds.adapter.FindsMSGAdapter;
import com.huimaibao.app.fragment.finds.adapter.FindsPraiseAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsEntity;
import com.huimaibao.app.fragment.finds.entity.FindsMSGEntity;
import com.huimaibao.app.fragment.finds.entity.FindsPraiseEntity;
import com.huimaibao.app.fragment.finds.server.FindsLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新消息，消息记录
 */
public class FindsMSGActivity extends BaseActivity {

    private String mType = "";
    //title,clear
    private TextView _top_title, _top_clear;
    private RelativeLayout _top_msg_rl;


    //数据集合
    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private FindsMSGAdapter mAdapter;
    private List<FindsMSGEntity> listData;
    private List<String> listMSGIDData;
    private int countPage = 1, total = 0;

    private DialogUtils mDialogUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_list_my);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        mDialogUtils = new DialogUtils(mActivity);

        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_title = findViewById(R.id.finds_list_title);
        _top_clear = findViewById(R.id.finds_clear_btn);
        _top_msg_rl = findViewById(R.id.finds_list_top_msg_rl);

        _top_msg_rl.setVisibility(View.GONE);
        _top_title.setText(mType);
        if (mType.equals("新消息")) {
            _top_clear.setVisibility(View.INVISIBLE);
        } else {
            _top_clear.setVisibility(View.INVISIBLE);
        }

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
        if (mType.equals("新消息")) {
            getMyMSGListData("1", countPage, false);
        } else {
            getMyMSGListData("", countPage, false);
        }
    }

    private void loadMoreData() {
        countPage++;
        if (mType.equals("新消息")) {
            getMyMSGListData("1", countPage, false);
        } else {
            getMyMSGListData("", countPage, false);
        }
    }


    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.finds_clear_btn:
                //清空
                mDialogUtils.showNoTitleDialog("是否清空当前消息?", "取消", "清空", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogUtils.dismissDialog();
                        getClearData();
                    }
                });
                break;
        }
    }

    /**
     * 我的新消息列表
     */
    private void getMyMSGListData(String type, final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("type", type);
        map.put("page", page);
        map.put("pageSize", "10");
        FindsLogic.Instance(mActivity).getMyMSGListApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        total = json.getJSONObject("data").optInt("total", 0);
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("list"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }

                        for (int i = 0; i < array.length(); i++) {
                            FindsMSGEntity entity = new FindsMSGEntity();
                            entity.setFindsId(array.getJSONObject(i).optString("dynamic_id"));
                            entity.setFindsContent(array.getJSONObject(i).optString("content"));
                            entity.setFindsDYType(array.getJSONObject(i).optString("dynamic_type"));
                            entity.setFindsCommentId(array.getJSONObject(i).optString("comment_id"));
                            entity.setFindsImage(array.getJSONObject(i).optString("image_path"));
                            entity.setFindsTime(array.getJSONObject(i).optString("created_at"));
                            entity.setFindsNewMessageId(array.getJSONObject(i).optString("new_message_id", ""));
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
                                    mAdapter = new FindsMSGAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);

                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {

                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        mAdapter = new FindsMSGAdapter(mActivity, listData);
                                        mListView.setAdapter(mAdapter);
                                    }
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
     * 清空消息
     */
    private void getClearData() {
        listMSGIDData = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            listMSGIDData.add(listData.get(i).getFindsNewMessageId());
        }
        LogUtils.debug("listMSGIDData:" + listMSGIDData.toString());

        HashMap<String, Object> map = new HashMap<>();
        map.put("new_message_id", listMSGIDData.toString().replace("[", "").replace("]", "").trim());
        FindsLogic.Instance(mActivity).getClearMSGApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("清空数据成功");
                                countPage = 1;
                                getMyMSGListData("", countPage, true);
                            }
                        });

                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("清空数据失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showToast("清空数据失败");
            }
        });
    }


}
