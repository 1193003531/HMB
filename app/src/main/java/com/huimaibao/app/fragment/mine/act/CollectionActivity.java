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

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.fragment.mine.adapter.CollectionAdapter;
import com.huimaibao.app.fragment.mine.entity.CollectionEntity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
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
 * 收藏
 */
public class CollectionActivity extends BaseActivity {

    private String mType = "";

    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data;
    private CollectionAdapter mAdapter;
    private List<CollectionEntity> cList;
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
        setContentView(R.layout.act_mine_collection);
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


        _buttom_ll = findViewById(R.id.collection_ll);
        _buttom_all_btn = findViewById(R.id.collection_ll_all_btn);
        _buttom_del_btn = findViewById(R.id.collection_ll_del_btn);
        _buttom_ll.setVisibility(View.GONE);

        _no_data = findViewById(R.id.list_no_data);
        _no_image_data = findViewById(R.id.list_no_data_iv);
        _no_tv_data = findViewById(R.id.list_no_data_tv);
        _no_image_data.setImageResource(R.drawable.blank_pages_6_icon);
        _no_tv_data.setText(R.string.no_datas_6);
        //_no_data.setVisibility(View.GONE);
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);

        //mTopView = getLayoutInflater().inflate(R.layout.act_mine_collection_top, null);
        _sreach_ll = findViewById(R.id.sreach_ll_btn);
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
                        getCollect(countPage, true, editText.getText().toString());
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


        //mListView.addHeaderView(mTopView);

        if (cList != null) {
            cList.clear();
        } else {
            cList = new ArrayList<>();
        }
        mAdapter = new CollectionAdapter(mActivity, cList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (getTopRight().equals("编辑")) {
                    startActivity(MessageWebActivity.class, "", ServerApi.ARTICLE_DETAILS_URL + cList.get(position).getCollectionId());
                } else {
                    //XToast.normal("" + mAdapter.getItem(position).getCollectionIsCheck());
                    isAllCheck = false;
                    if (mAdapter.getItem(position).getCollectionIsCheck()) {
                        cList.get(position).setCollectionIsCheck(false);
                        _buttom_all_btn.setText("全选");
                        // cList.set(position, cList.get(position));
                    } else {
                        cList.get(position).setCollectionIsCheck(true);
                        // cList.set(position, cList.get(position));
                    }

                    int count = 0;

                    for (int i = 0; i < cList.size(); i++) {
                        if (cList.get(i).getCollectionIsCheck()) {
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
                if (isLoad)
                    loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        countPage++;
        getCollect(countPage, false, "");
    }


    private void initData() {
        countPage = 1;
        getCollect(countPage, false, "");
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.collection_ll_all_btn:
                if (isAllCheck) {
                    isAllCheck = false;
                    _buttom_all_btn.setText("全选");
                    _buttom_del_btn.setText("删除(0)");
                } else {
                    isAllCheck = true;
                    _buttom_all_btn.setText("取消全选");
                    _buttom_del_btn.setText("删除(" + cList.size() + ")");
                }
                setAllCheckData(isAllCheck);
                break;
            case R.id.collection_ll_del_btn:
                if (cIDList != null) {
                    cIDList.clear();
                } else {
                    cIDList = new ArrayList<>();
                }
                for (int i = 0; i < cList.size(); i++) {
                    if (cList.get(i).getCollectionIsCheck()) {
                        cIDList.add(cList.get(i).getCollectionId());
                    }
                }
                if (cIDList.size() > 0) {
                    getCollectDel(cIDList);
                } else {
                    ToastUtils.showCenter("请选择要删除的文章");
                }

                break;
        }
    }


    /**
     * 是否编辑
     */
    private void setShoweData(boolean isshow) {
        for (int i = 0; i < cList.size(); i++) {
            mAdapter.getItem(i).setCollectionIsShow(isshow);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 全选
     */
    private void setAllCheckData(boolean isc) {
        for (int i = 0; i < cList.size(); i++) {
            mAdapter.getItem(i).setCollectionIsCheck(isc);
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 获取用户分类文章
     */
    private void getCollect(final int page, boolean isShow, String keyword) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 5);
        map.put("page", page);
        map.put("keyword", keyword);

        LibLogic.Instance(mActivity).getCollectApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        if (page == 1) {
                            cList = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            CollectionEntity ce = new CollectionEntity();
                            ce.setCollectionId(array.getJSONObject(i).getString("id"));
                            if (array.getJSONObject(i).getString("cover").contains("[")) {
                                if (JSON.parseArray(array.getJSONObject(i).getString("cover")).size() > 1) {
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
                                        ce.setCollectionImage(array.getJSONObject(i).optString("cover").replace("[", "").replace("]", "").replace("\"", "").trim());
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
                            ce.setCollectionIsShow(false);
                            ce.setCollectionIsCheck(false);
                            ce.setCollectionTitle(array.getJSONObject(i).getString("title"));
                            ce.setCollectionName(array.getJSONObject(i).getString("author"));
                            ce.setCollectionBrowse(array.getJSONObject(i).getString("pageviews"));
                            ce.setCollectionTime(array.getJSONObject(i).getString("created_at"));

                            cList.add(ce);
                        }
                        //XLog.d("newsList:" + newsList);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshView.setVisibility(View.VISIBLE);
                                if (page == 1) {
                                    if (cList.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new CollectionAdapter(mActivity, cList);
                                    mListView.setAdapter(mAdapter);
                                    //加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    if (mAdapter == null) {
                                        mAdapter = new CollectionAdapter(mActivity, cList);
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
    private void getCollectDel(List<String> ids) {
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
                    .url(ServerApi.LIB_COLLECT_URL)
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
                    //ResponseBody body = response.body();
                    mDialogUtils.dismissDialog();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("删除成功");
                            countPage = 1;
                            getCollect(countPage, true, "");
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
//                        JSONObject json = new JSONObject(response.body().toString());
//                        //XLog.d("card:" + json);
//                        if (json.getString("status").equals("0")) {
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    countPage = 1;
//                                    getCollect(countPage, true, "");
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
//                            showToast("删除失败,"+json.getString("message"));
//                        }
//                    } catch (Exception e) {
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
     * 无数据
     */
    private void showNoData() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _no_data.setVisibility(View.VISIBLE);
            }
        });
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
