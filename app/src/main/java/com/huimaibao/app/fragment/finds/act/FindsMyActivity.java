package com.huimaibao.app.fragment.finds.act;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsEntity;
import com.huimaibao.app.fragment.finds.server.FindsLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
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
 * 我的动态
 */
public class FindsMyActivity extends BaseActivity {

    //消息点
    private ImageView _top_msg_dian;

    //数据集合
    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private FindsAdapter mAdapter;
    private List<FindsEntity> listData;
    private List<String> listImage;

    private int countPage = 1, totalPage = 1, praise_num = 0;

    private String[] mUrls;

    private DialogUtils mDialogUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_list_my);
        setNeedBackGesture(true);
        mDialogUtils = new DialogUtils(mActivity);
        listData = new ArrayList<>();
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_msg_dian = findViewById(R.id.finds_list_top_msg_dian);

        if ((boolean) XPreferencesUtils.get("finds_new_msg", false)) {
            _top_msg_dian.setVisibility(View.VISIBLE);
        } else {
            _top_msg_dian.setVisibility(View.INVISIBLE);
        }

        //数据集合
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);
        _no_image_data = findViewById(R.id.list_no_data_iv);
        _no_tv_data = findViewById(R.id.list_no_data_tv);
        _no_image_data.setImageResource(R.drawable.blank_pages_11_icon);
        _no_tv_data.setText(R.string.no_datas_11);
        _no_data.setVisibility(View.GONE);


        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);
        mSwipeRefreshView.setItemCount(5);

        initEvent();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("is_comment_num", false)) {
            XPreferencesUtils.put("is_comment_num", false);
            if (listData != null && listData.size() > 0) {
                listData.get((int) XPreferencesUtils.get("comment_num_pos", 0)).setFindsCommentsNum("" + XPreferencesUtils.get("commentCount", "0"));
            }
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
        getMyDYListData(countPage, false);
    }

    private void loadMoreData() {
        if (countPage >= totalPage) {
            mSwipeRefreshView.setLoading(false);
            //showToast("没有数据了");
            return;
        }
        countPage++;
        getMyDYListData(countPage, false);
    }


    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.finds_list_top_msg_rl:
                startActivity(FindsMSGActivity.class, "消息记录");
                _top_msg_dian.setVisibility(View.INVISIBLE);
                XPreferencesUtils.put("finds_new_msg", false);
                break;
        }
    }

    /**
     * 获取我的动态
     */
    private void getMyDYListData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("page", page);
        map.put("pageSize", "10");
        FindsLogic.Instance(mActivity).getMyDYListApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
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
                            FindsEntity entity = new FindsEntity();
                            entity.setFindsId(array.getJSONObject(i).optString("dynamic_id"));
                            entity.setFindsCommentsNum(array.getJSONObject(i).optString("comment_times"));
                            entity.setFindsContent(array.getJSONObject(i).optString("content"));
                            entity.setFindsIsFocus("1");
                            entity.setFindsIsPraise(array.getJSONObject(i).optString("praise"));
                            entity.setFindsPraiseNum(array.getJSONObject(i).optString("praise_number"));
                            entity.setFindsTime(array.getJSONObject(i).optString("created_at"));
                            entity.setFindsUserId(array.getJSONObject(i).optString("user_id"));
                            entity.setFindsUserHead(array.getJSONObject(i).optString("head_picture"));
                            entity.setFindsUserName(array.getJSONObject(i).optString("user_name"));

                            listImage = new ArrayList<>();
                            mUrls = new String[]{};
                            try {
                                if (!XEmptyUtils.isSpace(array.getJSONObject(i).optString("image_path"))) {
                                    mUrls = array.getJSONObject(i).optString("image_path").split(",");
                                    for (int j = 0; j < mUrls.length; j++) {
                                        listImage.add(mUrls[j]);
                                    }
                                }
                            } catch (Exception e) {

                            }

//
//                            for (int j = 0; j < mUrls.length; j++) {
//                                listImage.add(mUrls[j]);
//                            }
                            entity.setFindsImageList(listImage);

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
                                    mAdapter = new FindsAdapter(mActivity, "我的", listData);
                                    mListView.setAdapter(mAdapter);

                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }


                                    mAdapter.setOnItemDeleteClickListener(new FindsAdapter.onItemDeleteListener() {
                                        @Override
                                        public void onDeleteClick(final int position) {
                                            mDialogUtils.showNoTitleDialog("是否删除该动态?", "取消", "删除", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mDialogUtils.dismissDialog();
                                                    getDelData(listData.get(position).getFindsId(), position);
                                                }
                                            });
                                        }
                                    });

                                    mAdapter.setOnItemFocusClickListener(new FindsAdapter.onItemFocusClickListener() {
                                        @Override
                                        public void onItemFocusClick(int position) {
                                            listData.get(position).setFindsIsFocus("1");
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });

                                    mAdapter.setOnItemPraiseClickListener(new FindsAdapter.onItemPraiseClickListener() {
                                        @Override
                                        public void onItemPraiseClick(int position) {
                                            getDYPraiseData(listData.get(position).getFindsId(), position);
                                        }
                                    });

                                } else {

                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        mAdapter = new FindsAdapter(mActivity, "我的", listData);
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
                showToast();
            }
        });
    }

    /**
     * 删除动态
     */
    private void getDelData(String dynamic_id, final int position) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", dynamic_id);
        FindsLogic.Instance(mActivity).getDYDelApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                XPreferencesUtils.put("add_dynamic", true);
                                showToast("删除成功");
                                if (listData.size() > 0) {
                                    listData.remove(position);
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
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

    /**
     * 点赞动态
     */
    private void getDYPraiseData(String dynamic_id, final int position) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", dynamic_id);
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        FindsLogic.Instance(mActivity).getDYPraiseApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    // String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listData.get(position).getFindsIsPraise().equals("0")) {
                                    praise_num = Integer.parseInt(listData.get(position).getFindsPraiseNum()) + 1;
                                    listData.get(position).setFindsIsPraise("1");
                                    listData.get(position).setFindsPraiseNum(praise_num + "");
                                } else {
                                    praise_num = Integer.parseInt(listData.get(position).getFindsPraiseNum()) - 1;
                                    listData.get(position).setFindsIsPraise("0");
                                    listData.get(position).setFindsPraiseNum(praise_num < 0 ? "0" : praise_num + "");
                                }

                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        setPraiseError(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setPraiseError(position);
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                setPraiseError(position);
            }
        });
    }


    /**
     * 点赞失败
     */
    private void setPraiseError(final int position) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listData.get(position).getFindsIsPraise().equals("0")) {
                    listData.get(position).setFindsIsPraise("1");
                } else {
                    listData.get(position).setFindsIsPraise("0");
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /***/
    public void showToast() {
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
