package com.huimaibao.app.fragment.electcircle.materialLib;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.electcircle.adapter.ArticleAdapter;
import com.huimaibao.app.fragment.electcircle.adapter.WebAdapter;
import com.huimaibao.app.fragment.electcircle.entity.WebEntity;
import com.huimaibao.app.fragment.electcircle.server.ElectLogic;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.fragment.mine.entity.CollectionEntity;
import com.huimaibao.app.fragment.web.LibDetailsWebActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.fragment.web.PersonalWebDetailsActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文章-收藏
 * 个人-营销
 */
public class ListFragment extends BaseFragment {


    private String type = "", text = "推荐";

    private int countPage = 1;

    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private LinearLayout _no_data;
    private ArticleAdapter mAdapter;
    private List<CollectionEntity> listData;

    private WebAdapter mWebAdapter;
    private List<WebEntity> listWebData;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_e_m_l_list, container, false);
        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "推荐";
        type = args != null ? args.getString("type") : "文章";
        initView(view);
        initEvent();
        return view;
    }


    @Override
    protected void initData() {
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // getData();
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        _no_data = v.findViewById(R.id.list_no_data);
        //_no_data.setVisibility(View.GONE);
        mSwipeRefreshView = v.findViewById(R.id.list_swipe_value);
        mListView = v.findViewById(R.id.list_pull_value);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (type.equals("文章")) {
                    startActivity(LibDetailsWebActivity.class, listData.get(position).getCollectionId(), ServerApi.ARTICLE_DETAILS_URL + listData.get(position).getCollectionId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                } else {
                    if (text.equals("个人")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", listWebData.get(position).getWebId());
                        bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + listWebData.get(position).getWebId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                        bundle.putString("share_title", listWebData.get(position).getWebTitle());
                        bundle.putString("share_des", "");
                        bundle.putString("share_imageUrl", listWebData.get(position).getWebImage());

                        startActivity(PersonalWebDetailsActivity.class, bundle);
                    } else {
                        startActivity(MessageWebActivity.class, "", ServerApi.MARKETING_DETAILS_URL + listWebData.get(position).getWebId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                    }
                }
            }
        });


        // 不能在onCreate中设置，这个表示当前是刷新状态，如果一进来就是刷新状态，SwipeRefreshLayout会屏蔽掉下拉事件
        //swipeRefreshLayout.setRefreshing(true);

        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        //mSwipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        mSwipeRefreshView.setItemCount(2);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);

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
        if (type.equals("文章")) {
            if (text.equals("推荐")) {
                getArticle(countPage, ServerApi.LIB_RECOMMEND_URL);
            } else {
                getArticle(countPage, ServerApi.LIB_COLLECT_URL);
            }
        } else {
            if (text.equals("个人")) {
                getPersonalData(countPage, false);
            } else {
                getMarketData(countPage, false);
            }

        }
    }


    private void getData() {
        countPage = 1;
        if (type.equals("文章")) {
            if (text.equals("推荐")) {
                getArticle(countPage, ServerApi.LIB_RECOMMEND_URL);
            } else {
                getArticle(countPage, ServerApi.LIB_COLLECT_URL);
            }
        } else {
            if (text.equals("个人")) {
                getPersonalData(countPage, false);
            } else {
                getMarketData(countPage, false);
            }

        }
    }

    /**
     * 选择
     */
    private void setCheckData(int position) {
        if (type.equals("文章")) {
            for (int i = 0; i < listData.size(); i++) {
                if (i == position) {
                    listData.get(position).setCollectionIsCheck(true);
                    XPreferencesUtils.put("material_style", "1");
                    XPreferencesUtils.put("material_id", listData.get(position).getCollectionId());
                    XPreferencesUtils.put("elect_lib_name", listData.get(position).getCollectionTitle());
                    XPreferencesUtils.put("isMaterial", true);
                } else {
                    listData.get(i).setCollectionIsCheck(false);
                }
            }
            mAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < listWebData.size(); i++) {
                if (i == position) {
                    listWebData.get(position).setWebIsCheck(true);
                    if (text.equals("个人")) {
                        XPreferencesUtils.put("material_style", "2");
                    } else {
                        XPreferencesUtils.put("material_style", "3");
                    }
                    XPreferencesUtils.put("material_id", listWebData.get(position).getWebId());
                    XPreferencesUtils.put("elect_lib_name", listWebData.get(position).getWebTitle());
                    XPreferencesUtils.put("isMaterial", true);
                } else {
                    listWebData.get(i).setWebIsCheck(false);
                }
            }
            mWebAdapter.notifyDataSetChanged();
        }


    }


    /**
     * 获取文章
     */
    private void getArticle(final int page, String url) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 5);
        map.put("page", page);
        LibLogic.Instance(mActivity).getArticleApi(map, url, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("lib=s=" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    //String data = json.optString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").optString("data"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            CollectionEntity ce = new CollectionEntity();
                            ce.setCollectionId(array.getJSONObject(i).optString("id"));
                            if (!array.getJSONObject(i).optBoolean("is_ad", false)) {
                                if (array.getJSONObject(i).optString("cover").contains("[")) {
                                    if (JSON.parseArray(array.getJSONObject(i).optString("cover")).size() > 1) {
                                        ce.setCollectionType("1");
                                        try {
                                            ce.setCollectionImage1(JSON.parseArray(array.getJSONObject(i).optString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            ce.setCollectionImage1("");
                                        }
                                        try {
                                            ce.setCollectionImage2(JSON.parseArray(array.getJSONObject(i).optString("cover")).get(1).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            ce.setCollectionImage2("");
                                        }
                                        try {
                                            ce.setCollectionImage3(JSON.parseArray(array.getJSONObject(i).optString("cover")).get(2).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            ce.setCollectionImage3("");
                                        }
                                    } else {
                                        ce.setCollectionType("0");
                                        try {
                                            ce.setCollectionImage(JSON.parseArray(array.getJSONObject(i).optString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            ce.setCollectionImage("");
                                        }
                                    }
                                } else {
                                    ce.setCollectionType("0");
                                    try {
                                        ce.setCollectionImage(array.getJSONObject(i).optString("cover").replace("\"", "").trim());
                                    } catch (Exception e) {
                                        ce.setCollectionImage("");
                                    }
                                }

                                if (page == 1) {
                                    if (XPreferencesUtils.get("material_style", "1").equals("1")) {
                                        if (XPreferencesUtils.get("material_id", "").equals(array.getJSONObject(i).optString("id"))) {
                                            if (i == 0) {
                                                ce.setCollectionIsCheck(true);
                                                XPreferencesUtils.put("material_style", "1");
                                                XPreferencesUtils.put("material_id", array.getJSONObject(i).optString("id"));
                                                XPreferencesUtils.put("elect_lib_name", array.getJSONObject(i).optString("title"));
                                                XPreferencesUtils.put("isMaterial", true);
                                            } else {
                                                ce.setCollectionIsCheck(false);
                                            }
                                        } else {
                                            ce.setCollectionIsCheck(false);
                                        }
                                    }
                                }else{
                                    ce.setCollectionIsShow(true);
                                }


                                ce.setCollectionTitle(array.getJSONObject(i).optString("title"));
                                ce.setCollectionName(array.getJSONObject(i).optString("author"));
                                ce.setCollectionBrowse(array.getJSONObject(i).optString("pageviews"));
                                ce.setCollectionTime(array.getJSONObject(i).optString("created_at"));

                                listData.add(ce);
                            }

                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshView.setVisibility(View.VISIBLE);
                                _no_data.setVisibility(View.GONE);
                                if (page == 1) {
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    }
                                    mAdapter = new ArticleAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);

                                    mAdapter.setOnItemCheckListener(new ArticleAdapter.onItemCheckListener() {
                                        @Override
                                        public void onItemCheckClick(int position) {
                                            setCheckData(position);
                                            getElectUser(listData.get(position).getCollectionId(), "1", listData.get(position).getCollectionTitle());
                                        }
                                    });


                                    //加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    if (mAdapter == null) {
                                        mAdapter = new ArticleAdapter(mActivity, listData);
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
     * 获取个人微网
     */
    private void getPersonalData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        //map.put("limit", 5);
        map.put("page", page);
        HomeLogic.Instance(mActivity).userPersonalApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("Personal=s=" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.optJSONObject("data").optString("data"));
//                        XLog.d("array:" + array);
                        if (countPage == 1) {
                            listWebData = new ArrayList<>();
                        } else {
                            if (array.length() == 0)
                                showToast("没有数据了");
                        }

                        for (int i = 0; i < array.length(); i++) {
                            WebEntity entity = new WebEntity();
                            entity.setWebId(array.getJSONObject(i).optString("id"));
                            entity.setWebImage(array.getJSONObject(i).optString("cover"));
                            entity.setWebTitle(array.getJSONObject(i).optString("title"));
                            entity.setWebBrowse(array.getJSONObject(i).optString("popularity"));
                            entity.setWebShare(array.getJSONObject(i).optString("share"));
                            entity.setWebTime(array.getJSONObject(i).optString("created_at"));
                            if (page == 1) {
                                if (XPreferencesUtils.get("material_style", "2").equals("2")) {
                                    if (XPreferencesUtils.get("material_id", "").equals(array.getJSONObject(i).optString("id"))) {
                                        if (i == 0) {
                                            entity.setWebIsCheck(true);
                                            XPreferencesUtils.put("material_style", "2");
                                            XPreferencesUtils.put("material_id", array.getJSONObject(i).optString("id"));
                                            XPreferencesUtils.put("elect_lib_name", array.getJSONObject(i).optString("title"));
                                            XPreferencesUtils.put("isMaterial", true);
                                        } else {
                                            entity.setWebIsCheck(false);
                                        }
                                    } else {
                                        entity.setWebIsCheck(false);
                                    }
                                }
                            }else{
                                entity.setWebIsCheck(false);
                            }

                            listWebData.add(entity);

                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (countPage == 1) {
                                    if (listWebData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mWebAdapter = new WebAdapter(mActivity, listWebData);
                                    mListView.setAdapter(mWebAdapter);
                                    mWebAdapter.setOnItemCheckListener(new WebAdapter.onItemCheckListener() {
                                        @Override
                                        public void onItemCheckClick(int position) {
                                            setCheckData(position);
                                            getElectUser(listWebData.get(position).getWebId(), "2", listWebData.get(position).getWebTitle());
                                        }
                                    });

                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    mWebAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    //XLog.d("error:" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
            }
        });
    }

    /**
     * 获取营销网页
     */
    private void getMarketData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        //map.put("limit", 5);
        map.put("page", page);
        HomeLogic.Instance(mActivity).userMarketingApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("home=s=" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.optJSONObject("data").optString("data"));
//                        XLog.d("array:" + array);
                        if (countPage == 1) {
                            listWebData = new ArrayList<>();
                        } else {
                            if (array.length() == 0)
                                showToast("没有数据了");
                        }

                        for (int i = 0; i < array.length(); i++) {
                            WebEntity entity = new WebEntity();
                            entity.setWebId(array.getJSONObject(i).optString("id"));
                            entity.setWebImage(array.getJSONObject(i).optString("cover"));
                            entity.setWebTitle(array.getJSONObject(i).optString("title"));
                            entity.setWebBrowse(array.getJSONObject(i).optString("pop"));
                            entity.setWebShare(array.getJSONObject(i).optString("share"));
                            entity.setWebTime(array.getJSONObject(i).optString("created_at"));
                            if (page == 1) {
                                if (XPreferencesUtils.get("material_style", "3").equals("3")) {
                                    if (XPreferencesUtils.get("material_id", "").equals(array.getJSONObject(i).optString("id"))) {
                                        if (i == 0) {
                                            entity.setWebIsCheck(true);
                                            XPreferencesUtils.put("material_style", "3");
                                            XPreferencesUtils.put("material_id", array.getJSONObject(i).optString("id"));
                                            XPreferencesUtils.put("elect_lib_name", array.getJSONObject(i).optString("title"));
                                            XPreferencesUtils.put("isMaterial", true);
                                        } else {
                                            entity.setWebIsCheck(false);
                                        }
                                    } else {
                                        entity.setWebIsCheck(false);
                                    }
                                }
                            }else{
                                entity.setWebIsCheck(false);
                            }
                            listWebData.add(entity);

                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (countPage == 1) {
                                    if (listWebData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mWebAdapter = new WebAdapter(mActivity, listWebData);
                                    mListView.setAdapter(mWebAdapter);
                                    mWebAdapter.setOnItemCheckListener(new WebAdapter.onItemCheckListener() {
                                        @Override
                                        public void onItemCheckClick(int position) {
                                            setCheckData(position);
                                            getElectUser(listWebData.get(position).getWebId(), "3", listWebData.get(position).getWebTitle());
                                        }
                                    });

                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    mWebAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    //XLog.d("error:" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
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

    /**
     * 设置素材
     */
    private void getElectUser(final String id, final String type, final String title) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("material_id", id);
        map.put("material_style", type);
        ElectLogic.Instance(mActivity).getMaterialApi(map, true, "设置素材中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.e("json:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                XPreferencesUtils.put("material_style", type);
                                XPreferencesUtils.put("material_id", id);
                                XPreferencesUtils.put("elect_lib_name", title);
                                XPreferencesUtils.put("isMaterial", true);
                                mActivity.finish();
                            }
                        });
                    }
                } catch (Exception e) {
                    //XLog.e("e:" + e);
                    showLibToast("设置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showLibToast("设置失败");
            }
        });
    }

    /**
     * 设置提示
     */
    private void showLibToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
                XPreferencesUtils.put("material_style", "");
                XPreferencesUtils.put("material_id", "");
                XPreferencesUtils.put("elect_lib_name", "");
                XPreferencesUtils.put("isMaterial", false);
            }
        });
    }


}
