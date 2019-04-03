package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.adapter.MoneyDetailAdapter;
import com.huimaibao.app.fragment.mine.entity.MoneyDetailEntity;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XSwipeRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 资金明细
 */
public class MoneyDetailActivity extends BaseActivity {

    private String mType = "";

    private LinearLayout _no_data;

    private ListView mListView;
    private XSwipeRefreshView mSwipeRefreshView;
    private MoneyDetailAdapter mAdapter;
    private List<MoneyDetailEntity> listData;
    private int countPage = 1;


    private LinearLayout _check_ll;
    //总金额，全部，选择全部，奖励金转入，克隆收益，克隆支出,会员充值，提现
    private TextView _total_money_tv, _all_tv, _check_all, _check_jlj, _check_clone_s, _check_clone_z, _check_top_up, _check_withdrawal;
    private boolean isShowCheck = false;
    private String _total_money_value = "", _check_type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_money_detail);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        initView();
    }

    /***/
    private void initView() {
        _all_tv = findViewById(R.id.money_detail_all);
        _check_ll = findViewById(R.id.money_detail_check_ll);
        _check_all = findViewById(R.id.money_detail_check_all);
        _check_jlj = findViewById(R.id.money_detail_check_jlj);

        _check_clone_s = findViewById(R.id.money_detail_check_clone_s);
        _check_clone_z = findViewById(R.id.money_detail_check_clone_z);
        _check_top_up = findViewById(R.id.money_detail_check_top_up);
        _check_withdrawal = findViewById(R.id.money_detail_check_withdrawal);

        _total_money_tv = findViewById(R.id.money_detail_total_money);

        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mSwipeRefreshView.setEnabled(false);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);

        // 设置上拉加载更多
        mSwipeRefreshView.setOnLoadMoreListener(new XSwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });


        getWallet();
        getWalletBillData(countPage, true, _check_type);

    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.money_detail_all:
            case R.id.money_detail_check_ll:
                showCheck();
                break;
            case R.id.money_detail_check_all:
                _all_tv.setText("全部");
                _check_all.setTextColor(getResources().getColor(R.color.FF274FF3));
                _check_jlj.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_s.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_z.setTextColor(getResources().getColor(R.color.color000000));
                _check_top_up.setTextColor(getResources().getColor(R.color.color000000));
                _check_withdrawal.setTextColor(getResources().getColor(R.color.color000000));

                _check_all.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wallet_true_icon), null);
                _check_jlj.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_s.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_z.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_top_up.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_withdrawal.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                showCheck();
                countPage = 1;
                _check_type = "";
                getWalletBillData(countPage, true, _check_type);
                break;
            case R.id.money_detail_check_jlj:
                _all_tv.setText("奖励金转入");
                _check_all.setTextColor(getResources().getColor(R.color.color000000));
                _check_jlj.setTextColor(getResources().getColor(R.color.FF274FF3));
                _check_clone_s.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_z.setTextColor(getResources().getColor(R.color.color000000));
                _check_top_up.setTextColor(getResources().getColor(R.color.color000000));
                _check_withdrawal.setTextColor(getResources().getColor(R.color.color000000));

                _check_jlj.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wallet_true_icon), null);
                _check_all.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_s.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_z.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_top_up.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_withdrawal.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                showCheck();

                countPage = 1;
                _check_type = "1";
                getWalletBillData(countPage, true, _check_type);
                break;
            case R.id.money_detail_check_clone_s:
                _all_tv.setText("克隆收益");
                _check_all.setTextColor(getResources().getColor(R.color.color000000));
                _check_jlj.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_s.setTextColor(getResources().getColor(R.color.FF274FF3));
                _check_clone_z.setTextColor(getResources().getColor(R.color.color000000));
                _check_top_up.setTextColor(getResources().getColor(R.color.color000000));
                _check_withdrawal.setTextColor(getResources().getColor(R.color.color000000));

                _check_clone_s.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wallet_true_icon), null);
                _check_clone_z.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_top_up.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_jlj.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_all.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_withdrawal.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                showCheck();
                countPage = 1;
                _check_type = "2";
                getWalletBillData(countPage, true, _check_type);
                break;
            case R.id.money_detail_check_clone_z:
                _all_tv.setText("克隆支出");
                _check_all.setTextColor(getResources().getColor(R.color.color000000));
                _check_jlj.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_s.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_z.setTextColor(getResources().getColor(R.color.FF274FF3));
                _check_top_up.setTextColor(getResources().getColor(R.color.color000000));
                _check_withdrawal.setTextColor(getResources().getColor(R.color.color000000));

                _check_clone_s.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_z.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wallet_true_icon), null);
                _check_top_up.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_jlj.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_all.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_withdrawal.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                showCheck();
                countPage = 1;
                _check_type = "6";
                getWalletBillData(countPage, true, _check_type);
                break;
            case R.id.money_detail_check_top_up:
                _all_tv.setText("会员充值");
                _check_all.setTextColor(getResources().getColor(R.color.color000000));
                _check_jlj.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_s.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_z.setTextColor(getResources().getColor(R.color.color000000));
                _check_top_up.setTextColor(getResources().getColor(R.color.FF274FF3));
                _check_withdrawal.setTextColor(getResources().getColor(R.color.color000000));

                _check_clone_s.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_z.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_top_up.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wallet_true_icon), null);
                _check_jlj.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_all.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_withdrawal.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                showCheck();
                countPage = 1;
                _check_type = "7";
                getWalletBillData(countPage, true, _check_type);
                break;
            case R.id.money_detail_check_withdrawal:
                _all_tv.setText("提现");
                _check_all.setTextColor(getResources().getColor(R.color.color000000));
                _check_jlj.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_s.setTextColor(getResources().getColor(R.color.color000000));
                _check_clone_z.setTextColor(getResources().getColor(R.color.color000000));
                _check_top_up.setTextColor(getResources().getColor(R.color.color000000));
                _check_withdrawal.setTextColor(getResources().getColor(R.color.FF274FF3));

                _check_withdrawal.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.wallet_true_icon), null);
                _check_jlj.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_s.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_clone_z.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_top_up.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                _check_all.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                showCheck();
                countPage = 1;
                _check_type = "5";
                getWalletBillData(countPage, true, _check_type);
                break;
        }
    }

    /***/
    private void showCheck() {
        if (isShowCheck) {
            isShowCheck = false;
            _check_ll.setVisibility(View.GONE);
        } else {
            isShowCheck = true;
            _check_ll.setVisibility(View.VISIBLE);
        }
    }


    private void loadMoreData() {
        countPage++;
        getWalletBillData(countPage, false, _check_type);

    }

    /***/
    /**
     * 获取资金明细
     */
    private void getWalletBillData(int page, boolean isShow, String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
        if (!XEmptyUtils.isSpace(type)) {
            map.put("type", type);
        }

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
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });
                    } else {
                        showToast(json.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
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
                    _total_money_value = json.getJSONObject("data").getString("money");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _total_money_tv.setText(_total_money_value);
                            }
                        });

                    } else {
                        _total_money_value = "0";
                    }
                } catch (Exception e) {
                    _total_money_value = "0";
                }
            }

            @Override
            public void onFailed(String error) {
                _total_money_value = "0";
            }
        });
    }

}
