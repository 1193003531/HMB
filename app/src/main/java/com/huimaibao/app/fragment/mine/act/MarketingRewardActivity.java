package com.huimaibao.app.fragment.mine.act;

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
import com.huimaibao.app.fragment.mine.adapter.MarketRewardAdapter;
import com.huimaibao.app.fragment.mine.entity.MarketRewardEntity;
import com.huimaibao.app.fragment.mine.server.MarkatRewardLogic;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 营销奖励
 */
public class MarketingRewardActivity extends BaseActivity {

    private String mType = "";

    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;


    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private View mTopView;
    private TextView _top_money, _top_total_money;
    private String totalAmount = "0";

    private MarketRewardAdapter mAdapter;
    private List<MarketRewardEntity> mlList;

    private int countPage = 1;

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_marketing_reward);
        setNeedBackGesture(true);
        mDialogUtils = new DialogUtils(mActivity);
        initView();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        _top_layout = findViewById(R.id.top_layout);
        params = _top_layout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        _top_layout.setLayoutParams(params);
        _top_layout.setBackgroundResource(R.drawable.mine_market_jlj_titl_bg);
        params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
        View mViewNeedOffset = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageView(mActivity, 0, mViewNeedOffset);
    }

    private void initView() {
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mSwipeRefreshView.setEnabled(false);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);


        mTopView = getLayoutInflater().inflate(R.layout.act_mine_marketing_reward_top, null);
        _top_money = mTopView.findViewById(R.id.mine_marketing_reward_money);
        _top_total_money = mTopView.findViewById(R.id.mine_marketing_reward_total_money);

        _top_money.setText("￥" + XPreferencesUtils.get("reward", "0"));
        _top_total_money.setText("累计营销奖励金" + 0 + "元");

        mListView.addHeaderView(mTopView);


        if (mlList != null) {
            mlList.clear();
        } else {
            mlList = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new MarketRewardAdapter(mActivity, mlList);
        }
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position - 1).getMarketRewardType().equals("1") || mAdapter.getItem(position - 1).getMarketRewardType().equals("2")) {
                    if (mAdapter.getItem(position - 1).getMarketRewardUserId().equals("0")) {
                        ToastUtils.showCenter("用户不存在");
                        return;
                    }
                    startActivity(CardClipDetailActivity.class, mAdapter.getItem(position - 1).getMarketRewardUserId());
                }
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


    private void initData() {
        getUserInfo();
        countPage = 1;
        getMarketingRewardData(countPage, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("MarketingRewardToWallet", false)) {
            countPage = 1;
            getMarketingRewardData(countPage, true);
            XPreferencesUtils.put("MarketingRewardToWallet", false);
        }

        if ((boolean) XPreferencesUtils.get("isStrategy", false)) {
            finish();
            XPreferencesUtils.put("isStrategy", false);
        }
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                //ToastUtils.showCenter("待开放");
                startActivity(MessageWebActivity.class, "赚钱攻略", ServerApi.MAKE_MONEY_URL);
                break;

            case R.id.marketing_reward_to_wallet:
                if (XPreferencesUtils.get("vip_level", "0").equals("0")) {
                    mDialogUtils.showNoTitleDialog("您还不是会员,是否充值会员?", "取消", "充值", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(MemberActivity.class, "会员中心");
                            mDialogUtils.dismissDialog();
                        }
                    });
                } else {
                    if (XStringUtils.m1("" + XPreferencesUtils.get("reward", "0")) > 0)
                        startActivity(MarketingRewardToWalletActivity.class, "转入钱包");
                    else
                        XToast.normal("您还没有可转入的金额");
                }
                break;
        }
    }

    private void loadMoreData() {
        countPage++;
        getMarketingRewardData(countPage, false);
    }


    /**
     * 获取营销奖励
     */
    private void getMarketingRewardData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        MarkatRewardLogic.Instance(mActivity).marketingRewardApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("marketingReward=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");

                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.optJSONObject("data").optJSONObject("list").optString("data"));
                        totalAmount = json.optJSONObject("data").optString("total");
                        //final String rewardAll = json.getJSONObject("data").getString("rewardAll");
                        //XLog.d("array:" + array);
                        if (countPage == 1) {
                            mlList = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            MarketRewardEntity ili = new MarketRewardEntity();
                            ili.setMarketRewardType(array.optJSONObject(i).optString("type"));
                            ili.setMarketRewardIncomeType(array.optJSONObject(i).optString("income_type"));
                            ili.setMarketRewardDescribe(array.optJSONObject(i).optString("describe"));
                            ili.setMarketRewardMoney(array.optJSONObject(i).optString("money"));
                            ili.setMarketRewardTime(array.optJSONObject(i).optString("created_at"));
                            try {
                                JSONObject userData = new JSONObject(array.optJSONObject(i).optString("user"));
                                ili.setMarketRewardUserId(userData.optString("id", ""));
                                ili.setMarketRewardName(userData.optString("name", ""));
                            } catch (Exception e) {
                                ili.setMarketRewardUserId("");
                                ili.setMarketRewardName("");
                            }


                            mlList.add(ili);
                        }
                        //XLog.d("size:" + mlList.size());
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                _top_total_money.setText("累计营销奖励金" + totalAmount + "元");

                                if (countPage == 1) {
                                    if (mlList.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new MarketRewardAdapter(mActivity, mlList);
                                    mListView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });
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
     * 获取用户信息
     */
    private void getUserInfo() {
        LoginLogic.Instance(mActivity).getUserInfoApi("", false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _top_money.setText("￥" + XPreferencesUtils.get("reward", "0"));
                    }
                });
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }


}
