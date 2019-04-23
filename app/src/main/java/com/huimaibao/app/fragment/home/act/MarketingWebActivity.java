package com.huimaibao.app.fragment.home.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.adapter.MakingListAdapter;
import com.huimaibao.app.fragment.home.entity.MakingListEntity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.mine.act.MemberActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.DialogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 营销网页
 */
public class MarketingWebActivity extends BaseActivity {

    private String mType = "";

    private Activity mActivity = this;
    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private MakingListAdapter mAdapter;
    private List<MakingListEntity> listData;

    private String _marketing_id_value = "";

    private int countPage = 1;

    private WXShare mWxShare;

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_m_web);
        mActivity = this;
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setShowLine(false);
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        mWxShare = WXShare.Instance(mActivity);
        mDialogUtils = new DialogUtils(mActivity);

        initView();

    }


    /***/
    private void initView() {
        _no_data = findViewById(R.id.list_no_data);
        _no_image_data = findViewById(R.id.list_no_data_iv);
        _no_tv_data = findViewById(R.id.list_no_data_tv);
        _no_image_data.setImageResource(R.drawable.blank_pages_7_icon);
        _no_tv_data.setText(R.string.no_datas_7);
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);


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


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(MessageWebActivity.class, "营销网页", ServerApi.MARKETING_DETAILS_URL + listData.get(position).getMakingListId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
            }
        });


        initEvent();
        initData();
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
        getMarketData(countPage, false);
    }


    private void initData() {
        countPage = 1;
        getMarketData(countPage, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isRefresh", false)) {
            countPage = 1;
            getMarketData(countPage, true);
            XPreferencesUtils.put("isRefresh", false);
        }
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.marketing_web_qzz:
                if (listData.size() > 0) {
                    if (XPreferencesUtils.get("vip_level", "0").equals("0")) {
                        mDialogUtils.showNoTitleDialog("开通会员，制作多个网页", "取消", "开通", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(MemberActivity.class, "会员中心");
                                mDialogUtils.dismissDialog();
                            }
                        });
                    } else {
                        startActivity(MessageWebActivity.class, "营销网页", ServerApi.SERVER_WEB_URL + "/#/marketing/create" + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                    }
                } else {
                    startActivity(MessageWebActivity.class, "营销网页", ServerApi.SERVER_WEB_URL + "/#/marketing/create" + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                }
                break;
        }
    }

    /**
     * 获取营销网页
     */
    private void getMarketData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        //map.put("limit", 5);
        map.put("page", page);
        HomeLogic.Instance(mActivity).userMarketingApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
//                        XLog.d("array:" + array);
                        if (countPage == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0)
                                showToast("没有数据了");
                        }

                        for (int i = 0; i < array.length(); i++) {
                            MakingListEntity entity = new MakingListEntity();
                            entity.setMakingListId(array.getJSONObject(i).getString("id"));
                            entity.setMakingListImage(array.getJSONObject(i).getString("cover"));
                            entity.setMakingListTitle(array.getJSONObject(i).getString("title"));
                            entity.setMakingListBrowse(array.getJSONObject(i).getString("pop"));
                            entity.setMakingListShare(array.getJSONObject(i).getString("share"));
                            entity.setMakingListTime(array.getJSONObject(i).getString("created_at"));
                            entity.setMakingListType("1");
                            entity.setMakingListIsDuf("0");
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
                                    mAdapter = new MakingListAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);
                                    mAdapter.setOnItemShareListener(new MakingListAdapter.onItemShareListener() {
                                        @Override
                                        public void onItemShareClick(int position) {
                                            _marketing_id_value = listData.get(position).getMakingListId();
                                            setShare(listData.get(position).getMakingListTitle(), listData.get(position).getMakingListId(), listData.get(position).getMakingListImage());
                                        }
                                    });

                                    mAdapter.setOnItemSetListener(new MakingListAdapter.onItemSetListener() {
                                        @Override
                                        public void onItemSetClick(final int position) {
                                            mDialogUtils.showListDialog("营销", null, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(MessageWebActivity.class, "营销", ServerApi.SERVER_WEB_URL + "/#/marketing/edit/" + listData.get(position).getMakingListId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                                                    mDialogUtils.dismissDialog();
                                                }
                                            }, null, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    userMarketingDelData(listData.get(position).getMakingListId());
                                                    mDialogUtils.dismissDialog();
                                                }
                                            });
                                        }
                                    });

                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                    // 加载完数据设置为不加载状态，将加载进度收起来
                                    mSwipeRefreshView.setLoading(false);
                                }
                            }
                        });
                    } else {
                        showToast(message);
                        showNoRefresh();
                    }
                } catch (Exception e) {
                    // XLog.d("error:" + e);
                    showNoRefresh();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showNoRefresh();
            }
        });
    }

    /**
     * 删除营销
     */
    private void userMarketingDelData(String id) {
        HomeLogic.Instance(mActivity).userMarketingDelApi(id, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("userMarketingDel=s=" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countPage = 1;
                                getMarketData(countPage, true);
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    LogUtils.debug("userMarketingDel=s=" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("userMarketingDel=s=" + error);
            }
        });
    }

    /**
     * 消息
     */

    private void showNoRefresh() {
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

    /**
     * 分享
     */
    private void setShare(final String title, final String url, final String imageUrl) {

        mWxShare.showShareDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //好友
                getShare(0, title, url, imageUrl);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //朋友圈
                getShare(1, title, url, imageUrl);
            }
        });
        //
    }

    private void getShare(int sceneFlag, String title, String url, String imageUrl) {
        ImageSize imageSize = new ImageSize(120, 120);
        Bitmap thumb;
        try {
            thumb = ImageLoader.getInstance().loadImageSync(imageUrl.trim(), imageSize);
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher);
        }

        String des = "" + XPreferencesUtils.get("motto", "");
        mWxShare.WXShareWeb(title, XEmptyUtils.isSpace(des) ? "汇脉宝--共享流量，专注营销" : des, ServerApi.MARKETING_DETAILS_URL + url, thumb, sceneFlag, new OnResponseListener() {
            @Override
            public void onSuccess() {
                XToast.normal("分享成功");
                HomeLogic.Instance(mActivity).getAddShareApi(_marketing_id_value, "4", null);
            }

            @Override
            public void onCancel() {
                XToast.normal("分享取消");
            }

            @Override
            public void onFail(String message) {
                XToast.normal("分享失败");
            }
        });
        mWxShare.setDismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        mWxShare.register();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mWxShare.unregister();
    }

}
