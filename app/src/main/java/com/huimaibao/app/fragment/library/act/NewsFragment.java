package com.huimaibao.app.fragment.library.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.home.act.LibraryActivity;
import com.huimaibao.app.fragment.library.act.focuson.FocusOnActivity;
import com.huimaibao.app.fragment.library.adapter.LibNewsAdapter;
import com.huimaibao.app.fragment.library.bean.FocusOnUserEntity;
import com.huimaibao.app.fragment.library.bean.LibNewsEntity;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.fragment.web.LibDetailsWebActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XHorizontalScrollView;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends Fragment {

    private Activity mActivity = this.getActivity();

    private ImageView _no_data_iv;
    private LinearLayout _no_data;
    private TextView _no_data_btn;//关注刚兴趣的人
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private List<LibNewsEntity> newsList;
    private LibNewsAdapter mAdapter;

    //关注用户
    private ArrayList<FocusOnUserEntity> userList;

    private String text;
    private int channel_id;

    View mTopView;
    XHorizontalScrollView _focus_on_hs;
    LinearLayout _focus_on_hs_ll;

    private int countPage = 1;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lib_news, null);

        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "";
        channel_id = args != null ? args.getInt("id", 0) : 0;
        mActivity = this.getActivity();

        initView(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        countPage = 1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _no_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        });
    }

    /***/
    private void initData() {
        if (text.equals("关注")) {
            _no_data_iv.setImageResource(R.drawable.lib_focus_on_bg);
            //XLog.d("MainActivity---------7");
            countPage = 1;
            getFocusOnUser();
        } else if (text.equals("推荐")) {
            //XLog.d("MainActivity---------6");
            mListView.removeFooterView(mTopView);
            countPage = 1;
            getRecommend(countPage, true);
        } else {
            countPage = 1;
            getArticlesForId(countPage, false, channel_id);
        }
    }

    /***/
    private void initView(View v) {
        mTopView = getLayoutInflater().inflate(R.layout.fragment_lib_news_item_head, null);
        _focus_on_hs = mTopView.findViewById(R.id.lib_focus_on_hs);
        _focus_on_hs_ll = mTopView.findViewById(R.id.lib_focus_on_hs_ll);
        _focus_on_hs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 移动的起点
                        mSwipeRefreshView.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 移动的终点距离
                        //mUpY = ev.getY();
                        mSwipeRefreshView.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 移动的终点
                        mSwipeRefreshView.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        _no_data_iv = v.findViewById(R.id.list_no_data_iv);
        _no_data = v.findViewById(R.id.list_no_data);
        _no_data_btn = v.findViewById(R.id.list_no_data_btn);
        _no_data.setVisibility(View.GONE);


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
//        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
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
        if (text.equals("关注")) {
            _no_data_iv.setImageResource(R.drawable.lib_focus_on_bg);
            if (userList.size() > 0) {
                countPage++;
                getFocusOnArticles(countPage, false, userList.get(0).getFocusOnUserId());
            }
        } else if (text.equals("推荐")) {
            countPage++;
            getRecommend(countPage, true);
        } else {
            countPage++;
            getArticlesForId(countPage, false, channel_id);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (text.equals("关注")) {
            getFocusOnUser();
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (text.equals("关注")) {
            getFocusOnUser();
        } else if (text.equals("推荐")) {
            mListView.removeHeaderView(mTopView);
            countPage = 1;
            getRecommend(countPage, true);
        } else {
            mListView.removeHeaderView(mTopView);
            countPage = 1;
            getArticlesForId(countPage, false, channel_id);
        }
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

    /**
     * 关注
     */
    private void setFocusOnList() {
        _focus_on_hs_ll.removeAllViews();
        _focus_on_hs.setParam(mActivity, XDensityUtils.getScreenWidth(), _focus_on_hs_ll, null, null, null, null);
        for (int i = 0; i < userList.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(XDensityUtils.getScreenWidth() / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            final View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_lib_news_item_section, null);
            CircleImageView columnImage = view.findViewById(R.id.fragment_lib_news_item_section_iv);
            TextView columnName = view.findViewById(R.id.fragment_lib_news_item_section_name);
            view.setId(i);
            columnName.setId(i);
            columnImage.setTag(i + "");
            ImageLoaderManager.loadImage(userList.get(i).getFocusOnUserHead(), columnImage);
            //XImage.getInstance().load(columnImage, "http://n.sinaimg.cn/tech/crawl/93/w550h343/20181109/h6aT-hnprhzw6400508.jpg", R.drawable.ic_launcher);
            columnName.setText(userList.get(i).getFocusOnUserName());


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, FocusOnActivity.class);
                    // LogUtils.debug("LibFocusOnUser:" + userList);
                    // XPreferencesUtils.put("LibFocusOnUser", userList.toString());
                    intent.putExtra("position", view.getId());
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_up_ac, R.anim.slide_out_down_ac);
                }
            });

            _focus_on_hs_ll.addView(view, i, params);
        }
        mListView.removeHeaderView(mTopView);
        mListView.addHeaderView(mTopView);

    }


    /**
     * 获取关注用户
     */
    private void getFocusOnUser() {
        LibLogic.Instance(mActivity).getFocusOnUserApi(false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(data);
                        userList = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            FocusOnUserEntity entity = new FocusOnUserEntity();
                            entity.setFocusOnUserId(array.getJSONObject(i).getString("id"));
                            entity.setFocusOnUserHead(array.getJSONObject(i).getString("avatar"));
                            entity.setFocusOnUserName(array.getJSONObject(i).getString("account"));
                            userList.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userList.size() > 0) {
                                    _no_data_iv.setVisibility(View.GONE);
                                    mSwipeRefreshView.setVisibility(View.VISIBLE);
                                    setFocusOnList();

                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }

                                    countPage = 1;
                                    getFocusOnArticles(countPage, false, userList.get(0).getFocusOnUserId());
                                } else {
                                    mSwipeRefreshView.setVisibility(View.GONE);
                                    _no_data_iv.setVisibility(View.GONE);
                                    _no_data.setVisibility(View.VISIBLE);
                                    if (countPage == 1) {
                                        //加载完数据设置为不刷新状态，将下拉进度收起来
                                        if (mSwipeRefreshView.isRefreshing()) {
                                            mSwipeRefreshView.setRefreshing(false);
                                        }
                                    } else {
                                        // 加载完数据设置为不加载状态，将加载进度收起来
                                        mSwipeRefreshView.setLoading(false);
                                    }
                                }
                            }
                        });

                    } else {
                        showToast(message);
                    }

                } catch (Exception e) {
                    //XLog.d("e:" + e.toString());
                    showHide();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showHide();
            }
        });
    }

    /**
     * 获取推荐文章
     */
    private void getRecommend(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
        LibLogic.Instance(mActivity).getRecommendApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("lib=s=" + json);
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
                            libNewsE.setLibLibNewsState(text);
                            if (array.getJSONObject(i).getBoolean("is_ad")) {
                                libNewsE.setLibNewsType("3");
                                libNewsE.setLibNewsUrl(array.getJSONObject(i).getString("url"));
                                try {
                                    libNewsE.setLibNewsImage(array.getJSONObject(i).getString("cover"));
                                } catch (Exception e) {
                                    //XLog.e("e3:" + e);
                                    libNewsE.setLibNewsImage("");
                                }
                            } else {
                                if (array.getJSONObject(i).getString("cover").contains("[")) {
                                    if (JSON.parseArray(array.getJSONObject(i).getString("cover")).size() > 1) {
                                        libNewsE.setLibNewsType("1");
                                        try {
                                            libNewsE.setLibNewsImage1(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImage1("");
                                        }
                                        try {
                                            libNewsE.setLibNewsImage2(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(1).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImage2("");
                                        }
                                        try {
                                            libNewsE.setLibNewsImage3(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(2).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImage3("");
                                        }
                                    } else {
                                        libNewsE.setLibNewsType("0");
                                        try {
                                            libNewsE.setLibNewsImageRight(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImageRight("");
                                        }
                                    }
                                } else {
                                    libNewsE.setLibNewsType("0");
                                    try {
                                        libNewsE.setLibNewsImageRight(array.getJSONObject(i).getString("cover").replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImageRight("");
                                    }
                                }

                            }

                            libNewsE.setLibNewsTitle(array.getJSONObject(i).getString("title"));
                            libNewsE.setLibNewsName(array.getJSONObject(i).getString("author"));
                            libNewsE.setLibNewsBrowse(array.getJSONObject(i).getString("pageviews"));
                            libNewsE.setLibNewsTime(array.getJSONObject(i).getString("created_at"));
                            libNewsE.setLibNewsId(array.getJSONObject(i).getString("id"));
                            newsList.add(libNewsE);
                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //mSwipeRefreshView.setVisibility(View.VISIBLE);
                                if (page == 1) {
                                    if (newsList.size() > 0) {
                                        _no_data_iv.setVisibility(View.GONE);
                                    } else {
                                        _no_data_iv.setVisibility(View.VISIBLE);
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
                //XLog.d("error:" + error);
            }
        });
    }

    /**
     * 获取分类文章
     */
    private void getArticlesForId(final int page, boolean isShow, int cate_id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
        map.put("cate_id", cate_id);
        LibLogic.Instance(mActivity).getArticlesForIdApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("lib=s=" + json);
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
                            libNewsE.setLibLibNewsState(text);
                            if (array.getJSONObject(i).getBoolean("is_ad")) {
                                libNewsE.setLibNewsType("3");
                                libNewsE.setLibNewsUrl(array.getJSONObject(i).getString("url"));
                                try {
                                    libNewsE.setLibNewsImage(array.getJSONObject(i).getString("cover"));
                                } catch (Exception e) {
                                    //XLog.e("e3:" + e);
                                    libNewsE.setLibNewsImage("");
                                }

                            } else {
                                if (array.getJSONObject(i).getString("cover").contains("[")) {
                                    if (JSON.parseArray(array.getJSONObject(i).getString("cover")).size() > 1) {
                                        libNewsE.setLibNewsType("1");
                                        try {
                                            libNewsE.setLibNewsImage1(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImage1("");
                                        }
                                        try {
                                            libNewsE.setLibNewsImage2(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(1).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImage2("");
                                        }
                                        try {
                                            libNewsE.setLibNewsImage3(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(2).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImage3("");
                                        }
                                    } else {
                                        libNewsE.setLibNewsType("0");
                                        try {
                                            libNewsE.setLibNewsImageRight(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            libNewsE.setLibNewsImageRight("");
                                        }
                                    }
                                } else {
                                    libNewsE.setLibNewsType("0");
                                    try {
                                        libNewsE.setLibNewsImageRight(array.getJSONObject(i).getString("cover").replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImageRight("");
                                    }
                                }

                            }

                            libNewsE.setLibNewsTitle(array.getJSONObject(i).getString("title"));
                            libNewsE.setLibNewsName(array.getJSONObject(i).getString("author"));
                            libNewsE.setLibNewsBrowse(array.getJSONObject(i).getString("pageviews"));
                            libNewsE.setLibNewsTime(array.getJSONObject(i).getString("created_at"));
                            libNewsE.setLibNewsId(array.getJSONObject(i).getString("id"));
                            newsList.add(libNewsE);
                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // mSwipeRefreshView.setVisibility(View.VISIBLE);

                                if (page == 1) {
                                    if (newsList.size() > 0) {
                                        _no_data_iv.setVisibility(View.GONE);
                                    } else {
                                        _no_data_iv.setVisibility(View.VISIBLE);
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
                    showHide();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showHide();
            }
        });
    }

    /**
     * 获取用户分类文章
     */
    private void getFocusOnArticles(final int page, boolean isShow, String author) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
        map.put("author", author);
        LibLogic.Instance(mActivity).getFocusOnArticlesApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("lib=s=" + json);
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
                            libNewsE.setLibLibNewsState(text);

                            if (array.getJSONObject(i).getString("cover").contains("[")) {
                                if (JSON.parseArray(array.getJSONObject(i).getString("cover")).size() > 1) {
                                    libNewsE.setLibNewsType("1");
                                    try {
                                        libNewsE.setLibNewsImage1(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage1("");
                                    }
                                    try {
                                        libNewsE.setLibNewsImage2(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(1).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage2("");
                                    }
                                    try {
                                        libNewsE.setLibNewsImage3(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(2).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage3("");
                                    }
                                } else {
                                    libNewsE.setLibNewsType("0");
                                    try {
                                        libNewsE.setLibNewsImageRight(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImageRight("");
                                    }
                                }
                            } else {
                                libNewsE.setLibNewsType("0");
                                try {
                                    libNewsE.setLibNewsImageRight(array.getJSONObject(i).getString("cover").replace("\"", "").trim());
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
                                //mSwipeRefreshView.setVisibility(View.VISIBLE);
                                if (page == 1) {
                                    if (newsList.size() > 0) {
                                        _no_data_iv.setVisibility(View.GONE);
                                    } else {
                                        _no_data_iv.setVisibility(View.VISIBLE);
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
                    showHide();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showHide();
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
                //加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }

    private void showHide() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }

}
