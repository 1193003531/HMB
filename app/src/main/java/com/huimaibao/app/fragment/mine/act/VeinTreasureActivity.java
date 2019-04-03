package com.huimaibao.app.fragment.mine.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.adapter.VeinTreasureAdapter;
import com.huimaibao.app.fragment.mine.entity.VeinTreasureEntity;
import com.huimaibao.app.fragment.mine.server.VeinLogic;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.XSwipeRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 脉宝
 */
public class VeinTreasureActivity extends BaseActivity {

    private String mType = "";

    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;


    private Activity mActivity = this;

    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private View mTopView;
    private TextView _top_num, _top_hq_tv, _top_zc_tv;
    private String _top_num_value = "0";

    private ImageView _top_hq_iv, _top_zc_iv;
    private String _cheack_state = "1";//1：脉宝赠送，2：脉宝支出

    private VeinTreasureAdapter mAdapter;
    private List<VeinTreasureEntity> mlList, mlList2;

    private int countPage_1 = 1, countPage_2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_vein_treasure);
        mActivity = this;
        setNeedBackGesture(true);

        initView();
        initData();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        _top_layout = findViewById(R.id.top_layout);
        params = _top_layout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        _top_layout.setLayoutParams(params);
        _top_layout.setBackgroundResource(R.drawable.vein_top_bg);
        params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
        View mViewNeedOffset = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageView(mActivity, 0, mViewNeedOffset);
    }

    private void initView() {
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mSwipeRefreshView.setEnabled(false);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);


        mTopView = getLayoutInflater().inflate(R.layout.act_mine_vein_treasure_top, null);
        _top_num = mTopView.findViewById(R.id.mine_vein_treasure_top_num);
        _top_hq_tv = mTopView.findViewById(R.id.mine_vein_treasure_top_hq_tv);
        _top_hq_iv = mTopView.findViewById(R.id.mine_vein_treasure_top_hq_iv);
        _top_zc_tv = mTopView.findViewById(R.id.mine_vein_treasure_top_zc_tv);
        _top_zc_iv = mTopView.findViewById(R.id.mine_vein_treasure_top_zc_iv);


        mListView.addHeaderView(mTopView);


        mlList = new ArrayList<>();
        mlList2 = new ArrayList<>();
        initEvent();

    }

    private void initEvent() {
        _top_hq_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _cheack_state = "1";
                _top_hq_tv.setTextColor(getResources().getColor(R.color.color000000));
                _top_hq_iv.setVisibility(View.VISIBLE);
                _top_zc_tv.setTextColor(getResources().getColor(R.color.color999999));
                _top_zc_iv.setVisibility(View.INVISIBLE);
                initData();
            }
        });

        _top_zc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _cheack_state = "2";
                _top_hq_tv.setTextColor(getResources().getColor(R.color.color999999));
                _top_hq_iv.setVisibility(View.INVISIBLE);
                _top_zc_tv.setTextColor(getResources().getColor(R.color.color000000));
                _top_zc_iv.setVisibility(View.VISIBLE);
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
        getVeinWalletData();
        if (_cheack_state.equals("1")) {
            if (mlList.size() > 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                countPage_1 = 1;
                mlList.clear();
                getVeinIncomeData(countPage_1, true);
            }
        } else {
            if (mlList2.size() > 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                countPage_2 = 1;
                mlList2.clear();
                getVeinEXPData(countPage_2, true);
            }
        }

        // m_no_data.setVisibility(View.GONE);


    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.mine_vein_treasure_giveing_btn:
                startActivity(VeinTreasureGivingActivity.class, "脉宝赠送");
                break;
            case R.id.mine_vein_treasure_topup_btn:
                startActivity(VeinTreasureTopUpActivity.class, "充值脉宝");
                break;
        }
    }

    private void loadMoreData() {
        if (_cheack_state.equals("1")) {
            countPage_1++;
            getVeinIncomeData(countPage_1, false);
        } else {
            countPage_2++;
            getVeinEXPData(countPage_2, false);
        }
    }

    /**
     * 获取脉宝余额
     */
    private void getVeinWalletData() {
        //HashMap<String, Object> map = new HashMap<>();
        //map.put("page", page);
        VeinLogic.Instance(mActivity).veinWalletApi(null, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");

                    if (status.equals("0")) {
                        _top_num_value = json.getJSONObject("data").getString("maibao");
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _top_num.setText("" + _top_num_value);
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
     * 收入
     */
    private void getVeinIncomeData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        VeinLogic.Instance(mActivity).veinIncomeApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("home=s=" + json);
                    String status = json.getString("status");
                    //String message = json.getString("message");
                    // String data = json.getString("data");

                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));


                        for (int i = 0; i < array.length(); i++) {
                            VeinTreasureEntity ili = new VeinTreasureEntity();
                            ili.setVeinTreasureId(array.getJSONObject(i).optString("id"));
                            ili.setVeinTreasureMoney(array.getJSONObject(i).optString("amount"));

//                            if (i % 2 == 1) {
//                                ili.setVeinTreasureType("全民推广");
//                            } else {
//                                ili.setVeinTreasureType(" 赠送我");
//                            }
                            ili.setVeinTreasureType(array.getJSONObject(i).optString("cate"));
                            ili.setVeinTreasureState(_cheack_state);
                            ili.setVeinTreasureTime(array.getJSONObject(i).optString("created_at"));
                            ili.setVeinTreasureName(array.getJSONObject(i).optString("title"));
                            ili.setVeinTreasureImage(array.getJSONObject(i).optString("head_picture"));
                            mlList.add(ili);
                        }
                        //XLog.d("size:" + mlList.size());
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (countPage_1 == 1) {
                                    if (mlList.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new VeinTreasureAdapter(mActivity, mlList);
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
     * 支出
     */
    private void getVeinEXPData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        VeinLogic.Instance(mActivity).veinExpenditureApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("home=s=" + json);
                    String status = json.getString("status");
                    //String message = json.getString("message");
                    // String data = json.getString("data");

                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));


                        for (int i = 0; i < array.length(); i++) {
                            VeinTreasureEntity ili = new VeinTreasureEntity();
                            ili.setVeinTreasureId(array.getJSONObject(i).getString("id"));
                            ili.setVeinTreasureMoney(array.getJSONObject(i).getString("amount"));

                            if (i % 2 == 1) {
                                ili.setVeinTreasureType("全民推广");
                            } else {
                                ili.setVeinTreasureType("赠送给 ");
                            }
                            ili.setVeinTreasureState(_cheack_state);
                            ili.setVeinTreasureTime(array.getJSONObject(i).getString("created_at"));
                            ili.setVeinTreasureName(array.getJSONObject(i).getString("title"));
                            mlList2.add(ili);
                        }
                        //XLog.d("size:" + mlList.size());
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (countPage_2 == 1) {
                                    if (mlList2.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new VeinTreasureAdapter(mActivity, mlList2);
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


}
