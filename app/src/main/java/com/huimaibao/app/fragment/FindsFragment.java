package com.huimaibao.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.finds.act.FindsAddActivity;
import com.huimaibao.app.fragment.finds.act.FindsMSGActivity;
import com.huimaibao.app.fragment.finds.act.FindsMyActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsEntity;
import com.huimaibao.app.fragment.finds.server.FindsLogic;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发现
 */
public class FindsFragment extends BaseFragment {
    private Activity mActivity = this.getActivity();

    private View _needOffsetView;

    //我的动态
    private TextView _finds_my_btn;
    //新消息
    private View _view_top;
    private LinearLayout _finds_msg_ll;
    private CircleImageView _finds_msg_head;
    private TextView _finds_msg_content;
    //添加动态
    private ImageView _finds_add_btn;
    //数据集合
    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private FindsAdapter mAdapter;
    private List<FindsEntity> listData;
    private List<String> listImage;

    private int countPage = 1, totalPage = 1, praise_num = 0;
    //动态图片集合
    private String[] mUrls;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finds, container, false);
        mActivity = this.getActivity();
        listData = new ArrayList<>();

        initView(view);
        return view;
    }

    private void initView(View v) {
        _needOffsetView = v.findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);

        //我的动态
        _finds_my_btn = v.findViewById(R.id.finds_my_btn);

        //新消息
        _view_top = View.inflate(mActivity, R.layout.fragment_finds_top, null);
        _finds_msg_ll = _view_top.findViewById(R.id.finds_msg_ll);
        _finds_msg_head = _view_top.findViewById(R.id.finds_msg_head);
        _finds_msg_content = _view_top.findViewById(R.id.finds_msg_content);

        //添加动态
        _finds_add_btn = v.findViewById(R.id.finds_add_btn);

        //数据集合
        mSwipeRefreshView = v.findViewById(R.id.list_swipe_value);
        mListView = v.findViewById(R.id.list_pull_value);
        _no_data = v.findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);


        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        // 手动调用,通知系统去测量
        // mSwipeRefreshView.measure(0, 0);
        //mSwipeRefreshView.setRefreshing(true);
        mSwipeRefreshView.setItemCount(5);

        initEvent();


    }


    @Override
    public void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("add_dynamic", false)) {
            XPreferencesUtils.put("add_dynamic", false);
            mSwipeRefreshView.setRefreshing(true);
            countPage = 1;
            getDYListData(countPage, false);
        }
        if ((boolean) XPreferencesUtils.get("is_comment_num", false)) {
            XPreferencesUtils.put("is_comment_num", false);
            if (listData != null && listData.size() > 0) {
                listData.get((int) XPreferencesUtils.get("comment_num_pos", 0)).setFindsCommentsNum("" + XPreferencesUtils.get("commentCount", "0"));
                mAdapter.notifyDataSetChanged();
            }
        }


    }

    private void initEvent() {
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //initData();
                getNewMSGData();
                countPage = 1;
                getDYListData(countPage, false);
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

    @Override
    protected void initData() {
//        getNewMSGData();
//        countPage = 1;
//        getDYListData(countPage, false);
    }

    private void loadMoreData() {
        if (countPage >= totalPage) {
            mSwipeRefreshView.setLoading(false);
            showToast("没有数据了");
            return;
        }

        countPage++;
        getDYListData(countPage, false);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
        if (!hidden) {
            getNewMSGData();
            countPage = 1;
            getDYListData(countPage, true);
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //我的动态
        _finds_my_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FindsMyActivity.class, "我的动态");
            }
        });
        //添加动态
        _finds_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FindsAddActivity.class, "发布动态");
            }
        });
        //新消息
        _finds_msg_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FindsMSGActivity.class, "新消息");
                XPreferencesUtils.put("finds_new_msg", false);
                //移除新消息
                mListView.removeHeaderView(_view_top);
            }
        });

    }


    /**
     * 获取动态
     */
    private void getDYListData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
//        map.put("myConcern", "1");
        map.put("page", page);
        map.put("pageSize", "10");
        LogUtils.debug("finds:" + map);
        FindsLogic.Instance(mActivity).getDYListApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json.toString());
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
                            entity.setFindsIsFocus(array.getJSONObject(i).optString("concern"));
                            entity.setFindsCardId(array.getJSONObject(i).optString("cards_id"));
                            entity.setFindsIsPraise(array.getJSONObject(i).optString("praise"));
                            entity.setFindsPraiseNum(array.getJSONObject(i).optString("praise_number"));
                            entity.setFindsTime(array.getJSONObject(i).optString("created_at"));
                            entity.setFindsUserId(array.getJSONObject(i).optString("user_id"));
                            entity.setFindsUserHead(array.getJSONObject(i).optString("head_picture"));
                            entity.setFindsUserName(array.getJSONObject(i).optString("user_name"));


                            listImage = new ArrayList<>();
                            try {
                                if (!XEmptyUtils.isSpace(array.getJSONObject(i).optString("image_path"))) {
                                    mUrls = new String[]{};
                                    mUrls = array.getJSONObject(i).optString("image_path").split(",");
                                    for (int j = 0; j < mUrls.length; j++) {
                                        listImage.add(mUrls[j]);
                                    }
                                }

                            } catch (Exception e) {

                            }


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
                                    mAdapter = new FindsAdapter(mActivity, "发现", listData);
                                    mListView.setAdapter(mAdapter);
                                    //mAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }

                                    mAdapter.setOnItemFocusClickListener(new FindsAdapter.onItemFocusClickListener() {
                                        @Override
                                        public void onItemFocusClick(int position) {
                                            getCardAdd(listData.get(position).getFindsCardId(), position);
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
                                        mAdapter = new FindsAdapter(mActivity, "发现", listData);
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
                    LogUtils.debug("finds:" + e.toString());
                    showToast();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("finds:" + error);
                showToast();
            }
        });
    }

    /**
     * 获取新消息
     */
    private void getNewMSGData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        FindsLogic.Instance(mActivity).getNewMSGApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    if (json.getString("status").equals("0")) {
                        final JSONObject data = new JSONObject(json.getString("data"));
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data.optString("count", "0").equals("0")) {
                                    XPreferencesUtils.put("finds_new_msg", false);
                                } else {
                                    XPreferencesUtils.put("finds_new_msg", true);
                                    ImageLoaderManager.loadImage(data.optString("head_picture"), _finds_msg_head);
                                    String type;
                                    if (data.optString("dynamic_type").equals("1")) {
                                        type = "人回复了你";
                                    } else if (data.optString("dynamic_type").equals("2")) {
                                        type = "人点赞了你";
                                    } else {
                                        type = "人评论了你";
                                    }
                                    //data.optString("user_name") + "等"
                                    _finds_msg_content.setText(data.optString("count", "0") + "条新消息");
                                    //添加新消息
                                    mListView.removeHeaderView(_view_top);
                                    mListView.addHeaderView(_view_top);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailed(String error) {
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
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("点赞成功");
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
                        setPraiseError("点赞失败", position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setPraiseError("点赞失败", position);
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                setPraiseError("点赞失败", position);
            }
        });
    }

    /**
     * 添加收藏
     */
    private void getCardAdd(final String card_id_value, final int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("card_id", card_id_value);
        CardClipLogic.Instance(mActivity).getCardClipAddApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    String msg = json.getString("message");
                    LogUtils.debug("finds:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("关注成功");
                                for (int i = 0; i < listData.size(); i++) {
                                    if (listData.get(position).getFindsCardId().equals(card_id_value)) {
                                        listData.get(position).setFindsIsFocus("1");
                                    }
                                }

                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        setFocusError("关注失败," + msg, position);
                    }
                } catch (Exception e) {
                    setFocusError("关注失败", position);
                }
            }

            @Override
            public void onFailed(String error) {
                // showToast("关注失败");
                LogUtils.debug("finds:" + error);
                setFocusError("关注失败", position);
            }
        });
    }


    /**
     * 关注失败
     */
    private void setFocusError(final String msg, final int position) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
                listData.get(position).setFindsIsFocus("0");
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 点赞失败
     */
    private void setPraiseError(final String msg, final int position) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
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
    public void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
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
