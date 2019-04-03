package com.huimaibao.app.fragment.home.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.act.CardClipDetailActivity;
import com.huimaibao.app.fragment.mine.adapter.CardAdapter;
import com.huimaibao.app.fragment.mine.entity.CardEntity;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.flowlayout.FlowLayout;
import com.youth.xframe.flowlayout.TagAdapter;
import com.youth.xframe.flowlayout.TagFlowLayout;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * 搜索
 */
public class SreachActivity extends BaseActivity {

    private String mType = "";
    private Context mContext;

    private EditText editText;
    private ImageView ed_clear;
    private String sreach_value = "";

    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private LinearLayout _no_data;
    private int countPage = 1;
    //关注
    private CardAdapter mCardAdapter;
    private List<CardEntity> ListCardData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sreach);
        mContext = this;
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        initView();
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.sreach:
                addHistory();
                break;
//            case R.id.sreach_history_clear:
//                //XPreferencesUtils.remove("sreachHis");
//                //initHisData();
//                break;
        }
    }


    /**
     * 初始化控件
     */
    private void initView() {
        editText = findViewById(R.id.et_search);
        ed_clear = findViewById(R.id.et_search_del);
        XKeyboardUtils.openKeyboard(mContext, editText);
        editText.addTextChangedListener(textWatcher);
        ed_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_clear.setVisibility(View.GONE);
                editText.setText("");
            }
        });


        /**
         * 监听输入键盘更换后的搜索按键
         * 调用时刻：点击键盘上的搜索键时
         */
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    addHistory();
                } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                } else {
                    return false;
                }
                return true;
            }
        });

        _no_data = findViewById(R.id.list_no_data);
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);
//
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(CardClipDetailActivity.class, mCardAdapter.getItem(position).getCardId());

            }
        });

        // 设置下拉进度的主题颜色
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        mSwipeRefreshView.setItemCount(2);


    }

    private void initEvent() {
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
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
        countPage++;
        getCardClip(countPage, false, sreach_value);
    }


    private void getData() {
        countPage = 1;
        getCardClip(countPage, true, sreach_value);

    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                ed_clear.setVisibility(View.VISIBLE);
            } else {
                ed_clear.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 搜索添加
     */
    private void addHistory() {
        sreach_value = editText.getText().toString().trim();
        if (XEmptyUtils.isSpace(sreach_value)) {
            XToast.normal("请输入搜索内容");
        } else {
            XKeyboardUtils.closeKeyboard(this);
            countPage = 1;
            getCardClip(countPage, true, sreach_value);
        }

    }

    /**
     * 获取用户收藏名片
     */
    private void getCardClip(final int page, boolean isShow, String key) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 5);
        map.put("page", page);
        map.put("keyword", key);

        CardClipLogic.Instance(mActivity).getCardClipApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getString("data"));
                        if (page == 1) {
                            ListCardData = new ArrayList<>();
                            if (array.length() == 0) {
                                showToast("没有搜索到数据");
                            }
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }

                        for (int i = 0; i < array.length(); i++) {
                            CardEntity ili = new CardEntity();
                            ili.setCardType("名片夹");
                            ili.setCardId(array.getJSONObject(i).getString("user_id"));
                            ili.setCardName(array.getJSONObject(i).getString("name"));
                            ili.setCardJobs(array.getJSONObject(i).getString("profession"));
                            ili.setCardIndustry(array.getJSONObject(i).getString("industry"));
                            ili.setCardCompany(array.getJSONObject(i).getString("company"));
                            ili.setCardAddr(array.getJSONObject(i).getString("address"));
                            ili.setCardPhone(array.getJSONObject(i).getString("phone"));
                            ili.setCardWechat(array.getJSONObject(i).getString("wechat"));
                            ili.setCardHead(array.getJSONObject(i).getString("cover"));
                            ili.setCardIsVip(array.getJSONObject(i).getString("vip_level"));
                            ili.setCardStyle(array.getJSONObject(i).optString("style", "style3"));
                            ListCardData.add(ili);
                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshView.setVisibility(View.VISIBLE);
                                _no_data.setVisibility(View.GONE);
                                if (page == 1) {
                                    if (ListCardData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    }
                                    mCardAdapter = new CardAdapter(mActivity, ListCardData);
                                    mListView.setAdapter(mCardAdapter);
                                    //加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    if (mCardAdapter == null) {
                                        mCardAdapter = new CardAdapter(mActivity, ListCardData);
                                        mListView.setAdapter(mCardAdapter);
                                    } else {
                                        mCardAdapter.notifyDataSetChanged();
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
                    showNoData();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showNoData();
            }
        });
    }


    /***/
    public void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }

    /***/
    public void showNoData() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }
}
