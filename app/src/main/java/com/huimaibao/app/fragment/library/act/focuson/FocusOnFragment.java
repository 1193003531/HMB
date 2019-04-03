package com.huimaibao.app.fragment.library.act.focuson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.library.adapter.LibNewsAdapter;
import com.huimaibao.app.fragment.library.bean.LibNewsEntity;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.fragment.web.LibDetailsWebActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FocusOnFragment extends Fragment {

    //  private View _needOffsetView;
    private Activity mActivity;
    private ImageView _no_data_iv;
    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;


    private List<LibNewsEntity> newsList;
    private LibNewsAdapter mAdapter;

    private String userId;

    private int countPage = 1;

//
//    @Override
//    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lib_focus_on_list, null);
//        Bundle args = getArguments();
//        userId = args != null ? args.getString("id", "0") : "0";
//        initView(view);
//        return view;
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lib_focus_on_list, null);
        Bundle args = getArguments();
        userId = args != null ? args.getString("id", "0") : "0";
        mActivity = getActivity();
        initView(view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //if (!hidden) {
        //XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
        // }
        countPage = 1;
    }

    private void initData() {
        countPage = 1;
        getFocusOnArticles(countPage, userId);
    }

    private void initView(View v) {
        _no_data_iv = v.findViewById(R.id.list_no_iv);
        _no_data = v.findViewById(R.id.list_no_data);
        mSwipeRefreshView = v.findViewById(R.id.list_swipe_value);
        mListView = v.findViewById(R.id.list_pull_value);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mActivity, LibDetailsWebActivity.class);
                intent.putExtra("vType", mAdapter.getItem(position).getLibNewsId());
                if (mAdapter.getItem(position).getLibNewsType().equals("3")) {
                    intent.putExtra("vUrl", mAdapter.getItem(position).getLibNewsUrl());
                } else {
                    intent.putExtra("vUrl", ServerApi.ARTICLE_DETAILS_URL + mAdapter.getItem(position).getLibNewsId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                }
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    @Override
    public void onResume() {
        super.onResume();
        XPreferencesUtils.put("isRefresh", false);
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
        getFocusOnArticles(countPage, userId);
    }


    /**
     * 获取用户分类文章
     */
    private void getFocusOnArticles(final int page, String author) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 5);
        map.put("page", page);
        map.put("author", author);
        LibLogic.Instance(mActivity).getFocusOnArticlesApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        if (page == 1) {
                            newsList = new ArrayList<>();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            LibNewsEntity libNewsE = new LibNewsEntity();
                            libNewsE.setLibLibNewsState("关注");

                            if (array.getJSONObject(i).getString("cover").contains("[")) {
                                if (JSON.parseArray(array.getJSONObject(i).getString("cover")).size() > 1) {
                                    libNewsE.setLibNewsType("1");
                                    try {
                                        libNewsE.setLibNewsImage1(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage1("");
                                    }
                                    try {
                                        libNewsE.setLibNewsImage2(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(1).toString());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage2("");
                                    }
                                    try {
                                        libNewsE.setLibNewsImage3(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(2).toString());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage3("");
                                    }
                                } else {
                                    libNewsE.setLibNewsType("0");
                                    try {
                                        libNewsE.setLibNewsImageRight(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImageRight("");
                                    }
                                }
                            } else {
                                libNewsE.setLibNewsType("0");
                                try {
                                    libNewsE.setLibNewsImageRight(array.getJSONObject(i).getString("cover"));
                                } catch (Exception e) {
                                    libNewsE.setLibNewsImageRight("");
                                }
                            }

                            libNewsE.setLibNewsTitle(array.getJSONObject(i).getString("title"));
                            libNewsE.setLibNewsName(array.getJSONObject(i).getString("author_name"));
                            libNewsE.setLibLibNewsNameImage(array.getJSONObject(i).getString("author_avatar"));
                            libNewsE.setLibNewsBrowse(array.getJSONObject(i).getString("pageviews"));
                            libNewsE.setLibNewsTime(array.getJSONObject(i).getString("created_at"));
                            libNewsE.setLibNewsId(array.getJSONObject(i).getString("id"));
                            newsList.add(libNewsE);
                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshView.setVisibility(View.VISIBLE);
                                _no_data_iv.setVisibility(View.GONE);
                                if (page == 1) {
                                    if (newsList.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new LibNewsAdapter(mActivity, newsList);
                                    mListView.setAdapter(mAdapter);
                                    //加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    if (mAdapter == null) {
                                        mAdapter = new LibNewsAdapter(mActivity, newsList);
                                        mListView.setAdapter(mAdapter);
                                    } else {
                                        mAdapter.notifyDataSetChanged();
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
                    //XLog.d("e:" + e.toString());
                }
            }

            @Override
            public void onFailed(String error) {
                // XLog.d("error:" + error);
            }
        });
    }

    /**
     * 消息
     */
    private void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
            }
        });
    }


    /* 摧毁视图 */
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        //Log.d("onDestroyView", "channel_id = " + channel_id);
        mAdapter = null;
    }

    /* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //Log.d(TAG, "channel_id = " + channel_id);
    }
}
