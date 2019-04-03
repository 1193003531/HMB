package com.huimaibao.app.fragment.home.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.server.CardLogic;
import com.huimaibao.app.fragment.mine.adapter.MarketRewardAdapter;
import com.huimaibao.app.fragment.mine.adapter.MoneyDetailAdapter;
import com.huimaibao.app.fragment.mine.entity.MarketRewardEntity;
import com.huimaibao.app.fragment.mine.entity.MoneyDetailEntity;
import com.huimaibao.app.fragment.mine.server.MarkatRewardLogic;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.GifView;
import com.youth.xframe.widget.NoScrollListView;
import com.youth.xframe.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户主页
 */
public class PersonalActivity extends BaseActivity {

    //private String mType = "";

    private XScrollView mScrollView;
    //
//    private LinearLayout _top_layout, _title_ll, _top_state_ll;
//    ViewGroup.LayoutParams params;
//    private View _needOffsetView;

    private LinearLayout _no_data;
    // private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
//    private MarketRewardAdapter mAdapter;
//    private List<MarketRewardEntity> mlList;

    private MoneyDetailAdapter mAdapter;
    private List<MoneyDetailEntity> listData;

    //用户信息
    private CircleImageView _head_iv;
    private TextView _name_tv, _num_tv, _money_tv;
    //服务商，认证用户，会员，商家，互推达人
    private ImageView _fws_iv, _rzyh_iv, _vip_iv, _shop_iv, _htdr_iv;
    private String totalAmount = "0";

    private String _user_id_value = "", _user_type_value = "4", _head_value = "", _name_value = "", _jobs_value = "", _industry_value = "", _company_value = "", _moto_value = "";
    private int _vip_value = 0, index = 0;


    //加载更多
    private View mFooterView;
    private GifView _footer_gif;
    private boolean isLoading = false;
    private int countPage = 1;

    private WXShare mWxShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_personal);
        mActivity = this;
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            _user_id_value = intent.getStringExtra("vType");
        }


        mWxShare = WXShare.Instance(mActivity);
        initView();
        initData();
    }


    /***/
    private void initView() {
        // _needOffsetView = findViewById(R.id.fake_status_bar);
        // XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);

        mFooterView = View.inflate(mActivity, R.layout.view_footer, null);
        _footer_gif = mFooterView.findViewById(R.id.load_gif);
        _footer_gif.setMovieResource(com.youth.xframe.R.raw.load_gif_icon);

        //_top_state_ll = findViewById(R.id.top_state_ll);
        // _top_layout = findViewById(R.id.top_layout);
        mScrollView = findViewById(R.id.scrollView);

        // _title_ll = findViewById(R.id.top_title_ll);
        // _back_btn = findViewById(R.id.back_btn);
        //_share_btn = findViewById(R.id.share_btn);


        _head_iv = findViewById(R.id.home_personal_image);
        _name_tv = findViewById(R.id.home_personal_name);
        _num_tv = findViewById(R.id.home_personal_num);
        _money_tv = findViewById(R.id.home_personal_money);

        _fws_iv = findViewById(R.id.home_personal_fws_iv);
        _rzyh_iv = findViewById(R.id.home_personal_rzyh_iv);
        _vip_iv = findViewById(R.id.home_personal_vip_iv);
        _shop_iv = findViewById(R.id.home_personal_shop_iv);
        _htdr_iv = findViewById(R.id.home_personal_ht_iv);
        _fws_iv.setVisibility(View.GONE);
        _rzyh_iv.setVisibility(View.GONE);
        _vip_iv.setVisibility(View.GONE);
        _shop_iv.setVisibility(View.GONE);
        _htdr_iv.setVisibility(View.GONE);


//        _top_state_p = findViewById(R.id.top_state_personal_tv);
//        _top_state_m = findViewById(R.id.top_state_market_tv);
//        _home_p = findViewById(R.id.home_personal_p_tv);
//        _home_m = findViewById(R.id.home_personal_m_tv);
//        _top_state_piv = findViewById(R.id.top_state_personal_iv);
//        _top_state_miv = findViewById(R.id.top_state_market_iv);
//        _home_piv = findViewById(R.id.home_personal_p_iv);
//        _home_miv = findViewById(R.id.home_personal_m_iv);


//        mScrollView.setOnScrollListener(new XScrollView.OnScrollListener() {
//            @Override
//            public void onScroll(int scrollY) {
//                // ToastUtils.showCenter("" + scrollY);
//                // XLog.e(""+scrollY);
//                if (scrollY > 455) {
//                    _top_state_ll.setVisibility(View.VISIBLE);
//                } else {
//                    _top_state_ll.setVisibility(View.GONE);
//                }
//
//                if (scrollY > 255) {
//                    scrollY = 255;
//                    _title_ll.setVisibility(View.VISIBLE);
//                    _back_btn.setImageResource(R.drawable.ico_arrow_left);
//                    _share_btn.setImageResource(R.drawable.personal_top_share_b);
//                    XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
//                } else {
//                    _title_ll.setVisibility(View.INVISIBLE);
//                    _back_btn.setImageResource(R.drawable.back_icon_w_left);
//                    _share_btn.setImageResource(R.drawable.personal_top_share_w);
//                    XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);
//                }
//                params = _top_layout.getLayoutParams();
//                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
//                _top_layout.setLayoutParams(params);
//                _top_layout.setBackgroundColor(Color.argb(scrollY, 255, 255, 255));
//
//            }
//        });


        _no_data = findViewById(R.id.list_no_data);
        // mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);
        mListView.setFocusable(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                if (isCheck.equals("个人")) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", listData.get(position).getWebId());
//                    bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + listData.get(position).getWebId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
//                    bundle.putString("share_title", listData.get(position).getWebTitle());
//                    bundle.putString("share_des", "");
//                    bundle.putString("share_imageUrl", listData.get(position).getWebImage());
//
//                    startActivity(PersonalWebDetailsActivity.class, bundle);
//                } else {
//                    startActivity(MessageWebActivity.class, "营销网页", ServerApi.MARKETING_DETAILS_URL + listData.get(position).getWebId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
//                }
            }
        });


        mScrollView.setScanScrollChangedListener(new XScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                if (!isLoading) {
                    isLoading = true;
                    mListView.removeFooterView(mFooterView);
                    mListView.addFooterView(mFooterView);
                    loadMoreData();
                }

            }

            @Override
            public void onScrolledToTop() {

            }
        });
    }


    private void loadMoreData() {
        //_footer_pro.setVisibility(View.VISIBLE);
        // _footer_tv.setText("正在加载中...");
        countPage++;
        // getMarketingRewardData(countPage, false);
        getWalletBillData(countPage, false);
    }


    private void initData() {
        getWallet();
        getCardDetails();
        countPage = 1;
        // getMarketingRewardData(countPage, true);
        getWalletBillData(countPage, true);
    }

    /***/
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                startActivity(MessageWebActivity.class, "赚钱攻略", ServerApi.MAKE_MONEY_URL);
                break;
        }
    }

    /**
     * 获取信息
     */
    private void getCardDetails() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", _user_id_value);
        CardLogic.Instance(mActivity).getCardClipApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(object.toString());
                    LogUtils.debug("card:" + json);
                    if (json.getString("status").equals("0")) {
                        JSONObject jsonD = new JSONObject(json.optString("data"));
//                        XPreferencesUtils.put("card_id", jsonD.getString("card_id"));//名片id
//                        XPreferencesUtils.put("has_holder", jsonD.getString("has_holder"));//是否收藏改名片
//                        XPreferencesUtils.put("phone", jsonD.getString("phone"));//手机号
//                        XPreferencesUtils.put("name", jsonD.getString("name"));//名称
//                        XPreferencesUtils.put("portrait", jsonD.getString("portrait"));//头像
//                        XPreferencesUtils.put("industry", jsonD.getString("industry"));//行业
//                        XPreferencesUtils.put("profession", jsonD.getString("profession"));//职业
//                        XPreferencesUtils.put("company", jsonD.getString("company"));//公司
//                        XPreferencesUtils.put("wechat", jsonD.getString("wechat"));//
//                        XPreferencesUtils.put("website", jsonD.getString("website"));//
//                        XPreferencesUtils.put("address", jsonD.getString("address"));//
//                        XPreferencesUtils.put("address_detail", jsonD.getString("address_detail"));//
//                        XPreferencesUtils.put("self_introduction", jsonD.getString("self_introduction"));//是否制作营销网页
//                        XPreferencesUtils.put("open_phone", jsonD.getString("open_phone"));//是否公开手机号
//                        XPreferencesUtils.put("qq", jsonD.getString("qq"));
//                        XPreferencesUtils.put("email", jsonD.getString("email"));
//                        XPreferencesUtils.put("introduce", jsonD.getString("introduce"));//个人介绍
//                        XPreferencesUtils.put("style", jsonD.getString("style"));//样式
//                        XPreferencesUtils.put("album", jsonD.getString("album"));//相册
//                        XPreferencesUtils.put("motto", jsonD.getString("motto"));//广告语
//XPreferencesUtils.put("vip_level", jsonD.getString("vip_level"));//VIP等级
                        //XPreferencesUtils.put("user_type", jsonD.getString("user_type"));//是否服务商4-普通,3-服务商
                        //private int _vip_value = 0
                        try {
                            _vip_value = Integer.parseInt(jsonD.optString("vip_level", "0"));
                        } catch (Exception e) {
                            _vip_value = 0;
                        }
                        _user_type_value = jsonD.optString("user_type");
                        _head_value = jsonD.optString("portrait");
                        _name_value = jsonD.optString("name");
                        _jobs_value = jsonD.optString("profession");
                        _company_value = jsonD.optString("company");
                        _industry_value = jsonD.optString("industry");
                        _moto_value = jsonD.optString("motto", "");

                        setShowAll();
                    } else {
                        showToast("查询失败");
                    }
                } catch (Exception e) {
                    showToast("查询失败");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("查询失败");
            }
        });
    }


    /**
     * 显示数据
     */
    private void setShowAll() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (_vip_value) {
                    case 0:
                        _vip_iv.setVisibility(View.GONE);
                        break;
                    case 1:
                        _vip_iv.setVisibility(View.VISIBLE);
                        _vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
                        break;
                    case 2:
                        _vip_iv.setVisibility(View.VISIBLE);
                        _vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
                        break;
                    case 3:
                        _vip_iv.setVisibility(View.VISIBLE);
                        _vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
                        break;
                    case 4:
                        _vip_iv.setVisibility(View.VISIBLE);
                        _vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
                        break;
                }

                ImageLoaderManager.loadImage(_head_value, _head_iv, R.drawable.ic_launcher);


                if (_user_type_value.equals("3")) {
                    _fws_iv.setVisibility(View.VISIBLE);
                } else {
                    _fws_iv.setVisibility(View.GONE);
                }
                _name_tv.setText(_name_value);


            }
        });
    }


//    /**
//     * 获取营销奖励
//     */
//    private void getMarketingRewardData(int page, boolean isShow) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("page", page);
//        MarkatRewardLogic.Instance(mActivity).marketingRewardApi(map, isShow, new ResultBack() {
//            @Override
//            public void onSuccess(Object object) {
//                try {
//                    JSONObject json = new JSONObject(object.toString());
//                    LogUtils.debug("marketingReward=s=" + json);
//                    String status = json.getString("status");
//                    String message = json.getString("message");
//                    String data = json.getString("data");
//
//                    if (status.equals("0")) {
//                        JSONArray array = new JSONArray(json.optJSONObject("data").optJSONObject("list").optString("data"));
//                        totalAmount = json.optJSONObject("data").optString("total");
//                        //final String rewardAll = json.getJSONObject("data").getString("rewardAll");
//                        //XLog.d("array:" + array);
//                        if (countPage == 1) {
//                            mlList = new ArrayList<>();
//                        } else {
//                            if (array.length() == 0) {
//                                showToast("没有数据了");
//                            }
//                        }
//                        for (int i = 0; i < array.length(); i++) {
//                            MarketRewardEntity ili = new MarketRewardEntity();
//                            ili.setMarketRewardType(array.optJSONObject(i).optString("type"));
//                            ili.setMarketRewardIncomeType(array.optJSONObject(i).optString("income_type"));
//                            ili.setMarketRewardDescribe(array.optJSONObject(i).optString("describe"));
//                            ili.setMarketRewardMoney(array.optJSONObject(i).optString("money"));
//                            ili.setMarketRewardTime(array.optJSONObject(i).optString("created_at"));
//                            try {
//                                JSONObject userData = new JSONObject(array.optJSONObject(i).optString("user"));
//                                ili.setMarketRewardUserId(userData.optString("id", ""));
//                                ili.setMarketRewardName(userData.optString("name", ""));
//                            } catch (Exception e) {
//                                ili.setMarketRewardUserId("");
//                                ili.setMarketRewardName("");
//                            }
//
//
//                            mlList.add(ili);
//                        }
//                        //XLog.d("size:" + mlList.size());
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                _money_tv.setText("总收入￥" + totalAmount + "元");
//
//                                if (countPage == 1) {
//                                    if (mlList.size() == 0) {
//                                        _no_data.setVisibility(View.VISIBLE);
//                                    } else {
//                                        _no_data.setVisibility(View.GONE);
//                                    }
//                                    mAdapter = new MarketRewardAdapter(mActivity, mlList);
//                                    mListView.setAdapter(mAdapter);
//                                } else {
//                                    mAdapter.notifyDataSetChanged();
//                                    // 加载完数据设置为不加载状态，将加载进度收起来
//                                    // mSwipeRefreshView.setLoading(false);
//                                }
//                            }
//                        });
//                    }
//                } catch (Exception e) {
//                    //XLog.d("error:" + e);
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(String error) {
//                //XLog.d("error:" + error);
//            }
//        });
//    }

    /**
     * 获取资金明细
     */
    private void getWalletBillData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
//        if (!XEmptyUtils.isSpace(type)) {
//            map.put("type", type);
//        }

        WalletLogic.Instance(mActivity).getWalletBillApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    // String message = json.getString("message");
                    //  String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        if (countPage == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            MoneyDetailEntity entity = new MoneyDetailEntity();
                            entity.setMoneyDetailId(array.getJSONObject(i).getString("id"));
                            entity.setMoneyDetailMoney(array.getJSONObject(i).getString("money"));
                            entity.setMoneyDetailName(array.getJSONObject(i).getString("describe"));
                            entity.setMoneyDetailTime(array.getJSONObject(i).getString("created_at"));
                            entity.setMoneyDetailType(array.getJSONObject(i).getString("income_type"));
                            entity.setMoneyDetailCate(array.getJSONObject(i).getString("type"));
                            listData.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (countPage == 1) {
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new MoneyDetailAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    //mSwipeRefreshView.setLoading(false);
                                    loadOver();
                                }
                            }
                        });
                    } else {
                        showToast(json.getString("message"));
                        loadOver();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadOver();
                }
            }

            @Override
            public void onFailed(String error) {
                loadOver();
            }
        });
    }


    /***/
    private void getWallet() {
        WalletLogic.Instance(mActivity).getWalletApi(false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    totalAmount = json.getJSONObject("data").getString("money");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _money_tv.setText("总收入￥" + totalAmount + "元");
                            }
                        });

                    } else {
                        totalAmount = "0";
                    }
                } catch (Exception e) {
                    totalAmount = "0";
                }
            }

            @Override
            public void onFailed(String error) {
                totalAmount = "0";
            }
        });
    }

    /**
     * 加载完成
     */
    private void loadOver() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                _footer_pro.setVisibility(View.GONE);
//                _footer_tv.setText("点击加载更多");
                isLoading = false;
                mListView.removeFooterView(mFooterView);
                //XScrollViewUtils.setListViewHeightBasedOnChildren(mListView);
            }
        });
    }

}
