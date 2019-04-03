package com.huimaibao.app.fragment.mine.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.act.SreachActivity;
import com.huimaibao.app.fragment.mine.adapter.CardAdapter;
import com.huimaibao.app.fragment.mine.adapter.VeinTreasureAdapter;
import com.huimaibao.app.fragment.mine.entity.CardEntity;
import com.huimaibao.app.fragment.mine.entity.VeinTreasureEntity;
import com.huimaibao.app.fragment.mine.server.VeinLogic;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.widget.XSwipeRefreshView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 赠送脉宝
 */
public class VeinTreasureGivingActivity extends BaseActivity {

    private String mType = "";

    private Activity mActivity = this;

    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    //private View mTopView;

    private CardAdapter mAdapter;
    private List<CardEntity> mlList;

    private int countPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_vein_treasure_giving);
        mActivity = this;
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


    private void initView() {
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mSwipeRefreshView.setEnabled(false);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);


//        mTopView = getLayoutInflater().inflate(R.layout.act_mine_vein_treasure_giving_top, null);
//
//
//        mListView.addHeaderView(mTopView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                toDetailsView("收入榜单");
            }
        });

        if (mlList != null) {
            mlList.clear();
        } else {
            mlList = new ArrayList<>();
        }


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
        getVeinFriendData(countPage, true);


    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.sreach_ll_btn:
                startActivity(SreachActivity.class, "脉宝赠送");
                break;
        }
    }

    private void loadMoreData() {
        countPage++;
        getVeinFriendData(countPage, false);
    }

    private void toDetailsView(String type) {
        Intent intent = new Intent();

        intent.putExtra("vType", mType);
        intent.putExtra("vUrl", "http://www.baidu.com");
        intent.setClass(mActivity, MessageWebActivity.class);
        startActivity(intent);

    }


    /**
     * 可转账朋友
     */
    private void getVeinFriendData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        VeinLogic.Instance(mActivity).veinFriendApi(map, isShow, new ResultBack() {
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
                            CardEntity ili = new CardEntity();
                            ili.setCardId(array.getJSONObject(i).getString("user_id"));
                            ili.setCardName(array.getJSONObject(i).getString("user_name"));
                            ili.setCardJobs(array.getJSONObject(i).getString("profession"));
                            ili.setCardIndustry(array.getJSONObject(i).getString("industry"));
                            ili.setCardCompany(array.getJSONObject(i).getString("company"));
                            ili.setCardImage(array.getJSONObject(i).getString("head_picture"));
                            mlList.add(ili);
                        }
                        //XLog.d("size:" + mlList.size());
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (countPage == 1) {
                                    if (mlList.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new CardAdapter(mActivity, mlList);
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
                   // XLog.d("error:" + e);
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
