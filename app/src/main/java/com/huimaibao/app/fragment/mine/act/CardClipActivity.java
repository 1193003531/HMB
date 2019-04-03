package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.adapter.CardAdapter;
import com.huimaibao.app.fragment.mine.entity.CardEntity;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 名片夹
 */
public class CardClipActivity extends BaseActivity {

    private String mType = "";

    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private CardAdapter mAdapter;
    private List<CardEntity> mlList;
    private List<String> cIDList;

    //private View mTopView;
    private LinearLayout _sreach_ll;
    private EditText editText;
    private ImageView ed_clear;

    private LinearLayout _buttom_ll;
    private TextView _buttom_all_btn, _buttom_del_btn;

    private boolean isLoad = true;
    private boolean isAllCheck = false;

    public DialogUtils mDialogUtils;

    private int countPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_card_clip);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setShoweLine(false);
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(true, false, true, "编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTopRight().equals("编辑")) {
                    setTopRight("取消");
                    mSwipeRefreshView.setEnabled(false);
                    mSwipeRefreshView.seLoaded(true);

                    _buttom_ll.setVisibility(View.VISIBLE);
                    _sreach_ll.setVisibility(View.GONE);
                    setShoweData(true);
                } else {
                    setTopRight("编辑");
                    mSwipeRefreshView.setEnabled(true);
                    mSwipeRefreshView.seLoaded(false);
                    isLoad = true;
                    _buttom_ll.setVisibility(View.GONE);
                    _sreach_ll.setVisibility(View.VISIBLE);
                    setShoweData(false);
                }
                setAllCheckData(false);
            }
        });

        mDialogUtils = new DialogUtils(mActivity);

        initView();

        initEvent();
        initData();

    }

    /***/
    private void initView() {
        _buttom_ll = findViewById(R.id.card_clip_ll);
        _buttom_all_btn = findViewById(R.id.card_clip_ll_all_btn);
        _buttom_del_btn = findViewById(R.id.card_clip_ll_del_btn);
        _buttom_ll.setVisibility(View.GONE);

        _no_data = findViewById(R.id.list_no_data);
        _no_image_data = findViewById(R.id.list_no_data_iv);
        _no_tv_data = findViewById(R.id.list_no_data_tv);
        _no_image_data.setImageResource(R.drawable.blank_pages_5_icon);
        _no_tv_data.setText(R.string.no_datas_5);
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);

//        mTopView = getLayoutInflater().inflate(R.layout.act_mine_card_clip_top, null);
//        mListView.addHeaderView(mTopView);
        _sreach_ll = findViewById(R.id.sreach_card_clip_ll_btn);
        editText = findViewById(R.id.et_search);
        ed_clear = findViewById(R.id.et_search_del);
        editText.addTextChangedListener(textWatcher);
        ed_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if (XEmptyUtils.isSpace(editText.getText().toString())) {
                        ToastUtils.showCenter("请输入搜索字段");
                    } else {
                        countPage = 1;
                        getCardClip(countPage, true, editText.getText().toString());
                        editText.setText("");
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                } else {
                    return false;
                }
                return true;
            }
        });


        if (mlList != null) {
            mlList.clear();
        } else {
            mlList = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new CardAdapter(mActivity, mlList);
        }
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (getTopRight().equals("编辑")) {
                    startActivity(CardClipDetailActivity.class, mAdapter.getItem(position).getCardId());
                } else {
                    isAllCheck = false;
                    if (mAdapter.getItem(position).getCardIsCheck()) {
                        mlList.get(position).setCardIsCheck(false);
                        _buttom_all_btn.setText("全选");
                        // cList.set(position, cList.get(position));
                    } else {
                        mlList.get(position).setCardIsCheck(true);
                        // cList.set(position, cList.get(position));
                    }

                    int count = 0;

                    for (int i = 0; i < mlList.size(); i++) {
                        if (mlList.get(i).getCardIsCheck()) {
                            count++;
                        }
                    }
                    _buttom_del_btn.setText("删除(" + count + ")");

                    mAdapter.notifyDataSetChanged();
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

        mSwipeRefreshView.setItemCount(5);

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
        getCardClip(countPage, false, "");
    }

    private void initData() {
        countPage = 1;
        getCardClip(countPage, false, "");
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.card_clip_ll_all_btn:
                if (isAllCheck) {
                    isAllCheck = false;
                    _buttom_all_btn.setText("全选");
                    _buttom_del_btn.setText("删除(0)");
                } else {
                    isAllCheck = true;
                    _buttom_all_btn.setText("取消全选");
                    _buttom_del_btn.setText("删除(" + mlList.size() + ")");
                }
                setAllCheckData(isAllCheck);
                break;
            case R.id.card_clip_ll_del_btn:
                if (cIDList != null) {
                    cIDList.clear();
                } else {
                    cIDList = new ArrayList<>();
                }
                for (int i = 0; i < mlList.size(); i++) {
                    if (mlList.get(i).getCardIsCheck()) {
                        cIDList.add(mlList.get(i).getCardId());
                    }
                }
                if (cIDList.size() > 0) {
                    getCardClipDel(cIDList);
                } else {
                    ToastUtils.showCenter("请选择要删除的名片");
                }
                break;
        }
    }


    /**
     * 刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("is_collect_card", false)) {
            XPreferencesUtils.put("is_collect_card", false);
            countPage = 1;
            getCardClip(countPage, true, "");
        }
    }

    /**
     * 获取用户收藏名片
     */
    private void getCardClip(final int page, boolean isShow, String keyword) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 5);
        map.put("page", page);
        map.put("keyword", keyword);

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
                            mlList = new ArrayList<>();
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
                            mlList.add(ili);
                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshView.setVisibility(View.VISIBLE);
                                _no_data.setVisibility(View.GONE);
                                if (page == 1) {
                                    if (mlList.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    }
                                    mAdapter = new CardAdapter(mActivity, mlList);
                                    mListView.setAdapter(mAdapter);
                                    //加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    if (mAdapter == null) {
                                        mAdapter = new CardAdapter(mActivity, mlList);
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
     * 删除用户收藏的文章
     */
    private void getCardClipDel(List<String> ids) {
        try {
            // 创建json对象
            JSONObject jsonObject = new JSONObject();
            // 1个数组参数
            JSONArray jsonArray = new JSONArray();
            for (String tag : ids) {
                jsonArray.put(tag);
            }
            jsonObject.put("ids", jsonArray);
            // 3个字符串参数

            String data = jsonObject.toString();
            // 构造请求体
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), data);
            Request request = new Request.Builder()
                    .addHeader("authorization", "bearer " + XPreferencesUtils.get("token", "").toString())
                    .url(ServerApi.CARD_CLIP_URL)
                    .delete(body)
                    .build();
            // 向服务器异步请求数据
            mDialogUtils.showLoadingDialog("删除中...");

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // LogUtils.i(TAG, "失败");
                    mDialogUtils.dismissDialog();
                    showToast("删除失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mDialogUtils.dismissDialog();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("删除成功");
                            countPage = 1;
                            getCardClip(countPage, true, "");
                            setTopRight("编辑");
                            mSwipeRefreshView.setEnabled(true);
                            mSwipeRefreshView.seLoaded(false);
                            isLoad = true;
                            _buttom_ll.setVisibility(View.GONE);
                            _sreach_ll.setVisibility(View.VISIBLE);
                            setShoweData(false);
                            setAllCheckData(false);
                        }
                    });
//                    try {
//                        LogUtils.debug("card:" + response.message());
//                        JSONObject json = new JSONObject(response.body().toString());
//                        LogUtils.debug("card:" + json);
//                        if (json.getString("status").equals("0")) {
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    countPage = 1;
//                                    getCardClip(countPage, true, "");
//                                    setTopRight("编辑");
//                                    mSwipeRefreshView.setEnabled(true);
//                                    mSwipeRefreshView.seLoaded(false);
//                                    isLoad = true;
//                                    _buttom_ll.setVisibility(View.GONE);
//                                    _sreach_ll.setVisibility(View.VISIBLE);
//                                    setShoweData(false);
//                                    setAllCheckData(false);
//                                }
//                            });
//                        } else {
//                            showToast("删除失败," + json.getString("message"));
//                        }
//                    } catch (Exception e) {
//                        LogUtils.debug("card:" + e);
//                        showToast("删除失败");
//                    }

                    //LogUtils.i(TAG, "返回数据：" + body.string());


                }
            });
        } catch (JSONException e) {
            mDialogUtils.dismissDialog();
            e.printStackTrace();
        }

    }


    /**
     * 是否编辑
     */
    private void setShoweData(boolean isshow) {
        for (int i = 0; i < mlList.size(); i++) {
            mAdapter.getItem(i).setCardIsShow(isshow);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 全选
     */
    private void setAllCheckData(boolean isc) {
        for (int i = 0; i < mlList.size(); i++) {
            mAdapter.getItem(i).setCardIsCheck(isc);
        }
        mAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XKeyboardUtils.closeKeyboard(this);
    }

}
