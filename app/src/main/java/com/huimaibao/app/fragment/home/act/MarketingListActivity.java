package com.huimaibao.app.fragment.home.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.adapter.MarketingListAdapter;
import com.huimaibao.app.fragment.home.entity.MakingListEntity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.fragment.web.PersonalWebDetailsActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 营销榜单
 */
public class MarketingListActivity extends BaseActivity {

    private String mType = "";

    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;
//    private View mViewNeedOffset;
//    private XScrollView mScrollView;

    private Activity mActivity = this;


    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private View mTopView;

    private MarketingListAdapter mAdapter;
    private List<MakingListEntity> mlList;

    //你的网页目前排名
    private TextView _m_num;
    private String ranking = "0";
    //周榜，月榜，总榜
    private TextView _market_week_btn, _market_month_btn, _market_total_btn;
    private String sort_value = "week";

    private int countPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_m_list);
        mActivity = this;
        setNeedBackGesture(true);

        initView();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        _top_layout = findViewById(R.id.top_layout);
        params = _top_layout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = XDensityUtils.dp2px(44) + XDensityUtils.getStatusBarHeight();
        _top_layout.setLayoutParams(params);
        _top_layout.setBackgroundResource(R.drawable.marketing_list_top_bg);

        View mViewNeedOffset = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageView(mActivity, 0, mViewNeedOffset);
    }

    private void initView() {


        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mSwipeRefreshView.setEnabled(false);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);

        mTopView = getLayoutInflater().inflate(R.layout.act_home_m_list_top, null);
        _m_num = mTopView.findViewById(R.id.a_h_m_num);
        _market_week_btn = mTopView.findViewById(R.id.a_h_m_week);
        _market_month_btn = mTopView.findViewById(R.id.a_h_m_month);
        _market_total_btn = mTopView.findViewById(R.id.a_h_m_total);
        mListView.addHeaderView(mTopView);
        _m_num.setText("你的网页目前排名 " + 0);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Bundle bundle = new Bundle();
//                bundle.putString("id", mlList.get(position - 1).getMakingListId());
//                bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + mlList.get(position - 1).getMakingListId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
//                bundle.putString("share_title", mlList.get(position - 1).getMakingListTitle());
//                bundle.putString("share_des", "");
//                bundle.putString("share_imageUrl", mlList.get(position - 1).getMakingListImage());
//
//                startActivity(PersonalWebDetailsActivity.class, bundle);
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL);
            }
        });


        initEvent();
        initData();

    }

    private void initEvent() {
        // 设置上拉加载更多
        mSwipeRefreshView.setOnLoadMoreListener(new XSwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        if (mlList.size() >= 100) {
            XToast.normal("营销榜单显示前100名");
            // 加载完数据设置为不加载状态，将加载进度收起来
            mSwipeRefreshView.setLoading(false);
            return;
        }
        countPage++;
        getMarketData(countPage, sort_value, false);
    }


    private void initData() {
        countPage = 1;
        getMarketData(countPage, sort_value, true);

    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.tv_title_right:
                // ToastUtils.showCenter("待开放");
                startActivity(MessageWebActivity.class, "营销攻略", ServerApi.MARKETING_STRATEGY_URL);
                break;
            case R.id.a_h_m_week:
                _market_week_btn.setTextColor(getResources().getColor(R.color.colore262626));
                _market_month_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_total_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_week_btn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                _market_month_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _market_total_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _market_week_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.fragment_home_marketing_icon));
                _market_month_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_total_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                sort_value = "week";
                countPage = 1;
                getMarketData(countPage, sort_value, true);
                break;
            case R.id.a_h_m_month:
                _market_week_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_month_btn.setTextColor(getResources().getColor(R.color.colore262626));
                _market_total_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_week_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
                _market_month_btn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//常规
                _market_total_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _market_week_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_month_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.fragment_home_marketing_icon));
                _market_total_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                sort_value = "month";
                countPage = 1;
                getMarketData(countPage, sort_value, true);
                break;
            case R.id.a_h_m_total:
                _market_week_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_month_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_total_btn.setTextColor(getResources().getColor(R.color.colore262626));
                _market_week_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
                _market_month_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _market_total_btn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//常规
                _market_week_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_month_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_total_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.fragment_home_marketing_icon));
                sort_value = "";
                countPage = 1;
                getMarketData(countPage, sort_value, true);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isStrategy", false)) {
            finish();
            XPreferencesUtils.put("isStrategy", false);
        }
    }


    /**
     * 获取营销奖励
     */
    private void getMarketData(int page, String sort, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 20);
        map.put("page", page);
        if (!XEmptyUtils.isSpace(sort)) {
            map.put("sort", sort);
        }
        HomeLogic.Instance(mActivity).homeMarketApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("rank"));
                        ranking = json.getJSONObject("data").getString("ranking");
//                        XLog.d("array:" + array);
                        if (countPage == 1) {
                            mlList = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }


                        for (int i = 0; i < array.length(); i++) {
                            MakingListEntity mli = new MakingListEntity();
                            mli.setMakingListId(array.getJSONObject(i).getString("id"));
                            //HomeLogic.Instance(mActivity).getJsonImageUrls(array.getJSONObject(i).getString("content"))
                            mli.setMakingListImage(array.getJSONObject(i).getString("cover"));
                            mli.setMakingListTitle(array.getJSONObject(i).getString("title"));
                            mli.setMakingListBrowse(array.getJSONObject(i).getString("popularity"));
                            mli.setMakingListShare(array.getJSONObject(i).getString("share_count"));
                            mli.setMakingListInterested(array.getJSONObject(i).optString("interested", "0"));
                            mli.setMakingListHead(array.getJSONObject(i).getJSONObject("user").getString("portrait"));
                            mli.setMakingListName(array.getJSONObject(i).getJSONObject("user").getString("name"));
                            //XLog.d("image:" + HomeLogic.Instance(mActivity).getJsonImageUrls(array.getJSONObject(i).getString("content")));
                            mlList.add(mli);
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _m_num.setText("你的网页目前排名 " + ranking);
                                _no_data.setVisibility(View.GONE);
                                mListView.setVisibility(View.VISIBLE);

                                if (countPage == 1) {
                                    mAdapter = new MarketingListAdapter(mActivity, mlList);
                                    mListView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    //.d("error:" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
            }
        });
    }


}
