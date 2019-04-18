package com.huimaibao.app.fragment.home.act;

import android.app.Activity;
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
import com.huimaibao.app.fragment.home.adapter.IncomeListAdapter;
import com.huimaibao.app.fragment.home.entity.IncomeListEntity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
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
 * 收入榜单
 */
public class IncomeListActivity extends BaseActivity {

    private String mType = "";
    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;


    private Activity mActivity = this;

    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private View mTopView;

    private IncomeListAdapter mAdapter;
    private List<IncomeListEntity> mlList;
    private List<IncomeListEntity> iListmData;

    //
    private CircleImageView _i_1_head, _i_2_head, _i_3_head;
    private TextView _i_1_name, _i_2_name, _i_3_name;
    private TextView _i_1_money, _i_2_money, _i_3_money;

    private int countPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_income_list);
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
        _top_layout.setBackgroundResource(R.drawable.income_num_title_icon);
        View mViewNeedOffset = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageView(mActivity, 0, mViewNeedOffset);
    }

    private void initView() {
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mSwipeRefreshView.setEnabled(false);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);


        mTopView = getLayoutInflater().inflate(R.layout.act_home_income_list_top, null);


        _i_1_head = mTopView.findViewById(R.id.a_h_i_1_head_image);
        _i_2_head = mTopView.findViewById(R.id.a_h_i_2_head_image);
        _i_3_head = mTopView.findViewById(R.id.a_h_i_3_head_image);

        _i_1_name = mTopView.findViewById(R.id.a_h_i_1_head_name);
        _i_2_name = mTopView.findViewById(R.id.a_h_i_2_head_name);
        _i_3_name = mTopView.findViewById(R.id.a_h_i_3_head_name);

        _i_1_money = mTopView.findViewById(R.id.a_h_i_1_money);
        _i_2_money = mTopView.findViewById(R.id.a_h_i_2_money);
        _i_3_money = mTopView.findViewById(R.id.a_h_i_3_money);

        mListView.addHeaderView(mTopView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // startActivity(PersonalActivity.class, iListmData.get(position - 1).getIncomeId());
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL+iListmData.get(position - 1).getIncomeId()+ServerApi.HOME_PAGE_WEB_TOKEN);
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
        countPage = 1;
        getIncomeData(countPage, true);
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.tv_title_right:
                // ToastUtils.showCenter("待开放");
                startActivity(MessageWebActivity.class, "赚钱攻略", ServerApi.MAKE_MONEY_URL);
                break;
            case R.id.a_h_i_1_head_ll:
            case R.id.a_h_i_1_money_ll:
                // startActivity(PersonalActivity.class, mlList.get(0).getIncomeId());
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL+ mlList.get(0).getIncomeId()+ServerApi.HOME_PAGE_WEB_TOKEN);
                break;
            case R.id.a_h_i_2_head_ll:
            case R.id.a_h_i_2_money_ll:
                // startActivity(PersonalActivity.class, mlList.get(1).getIncomeId());
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL+ mlList.get(1).getIncomeId()+ServerApi.HOME_PAGE_WEB_TOKEN);
                break;
            case R.id.a_h_i_3_head_ll:
            case R.id.a_h_i_3_money_ll:
                // startActivity(PersonalActivity.class, mlList.get(2).getIncomeId());
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL+ mlList.get(2).getIncomeId()+ServerApi.HOME_PAGE_WEB_TOKEN);
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

    private void loadMoreData() {
        if (iListmData.size() >= 97) {
            XToast.normal("收入榜单显示前100名");
            // 加载完数据设置为不加载状态，将加载进度收起来
            mSwipeRefreshView.setLoading(false);
            return;
        }
        countPage++;
        getIncomeData(countPage, false);

    }

    /**
     * 获取收入榜单
     */
    private void getIncomeData(int page, boolean isShow) {
        //ToastUtils.showCenter(""+page);
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 10);
        map.put("page", page);
        HomeLogic.Instance(mActivity).homeIncomeApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        //XLog.d("array:" + array);
                        if (countPage == 1) {
                            mlList = new ArrayList<>();
                            iListmData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            IncomeListEntity listEntity = new IncomeListEntity();
                            listEntity.setIncomeId(array.getJSONObject(i).getString("user_id"));
                            listEntity.setIncomeImage(array.getJSONObject(i).getString("avatar"));
                            listEntity.setIncomeName(array.getJSONObject(i).getString("name"));
                            listEntity.setIncomeMoney(array.getJSONObject(i).getString("reward"));
                            if (countPage == 1) {
                                if (i < 3) {
                                    mlList.add(listEntity);
                                } else {
                                    iListmData.add(listEntity);
                                }
                            } else {
                                iListmData.add(listEntity);
                            }

                        }
                        //XLog.d("size:" + mlList.size());
                        //XLog.d("size:" + iListmData.size());
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mlList.size() > 0 && mlList.size() >= 3) {
                                    _i_1_name.setText(mlList.get(0).getIncomeName());
                                    _i_1_money.setText("总收入￥" + mlList.get(0).getIncomeMoney());
                                    _i_2_name.setText(mlList.get(1).getIncomeName());
                                    _i_2_money.setText("总收入￥" + mlList.get(1).getIncomeMoney());
                                    _i_3_name.setText(mlList.get(2).getIncomeName());
                                    _i_3_money.setText("总收入￥" + mlList.get(2).getIncomeMoney());
                                    ImageLoaderManager.loadImage(mlList.get(0).getIncomeImage(), _i_1_head, R.drawable.ic_launcher);
                                    ImageLoaderManager.loadImage(mlList.get(1).getIncomeImage(), _i_2_head, R.drawable.ic_launcher);
                                    ImageLoaderManager.loadImage(mlList.get(2).getIncomeImage(), _i_3_head, R.drawable.ic_launcher);
                                }

                                _no_data.setVisibility(View.GONE);
                                if (countPage == 1) {
                                    mAdapter = new IncomeListAdapter(mActivity, iListmData);
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
