package com.huimaibao.app.fragment.home.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
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
import com.huimaibao.app.fragment.web.PersonalWebDetailsActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.utils.ToastUtils;
import com.huimaibao.app.video.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 个人微网
 */
public class PersonalWebActivity extends BaseActivity {

    //引导
    private LinearLayout _guide_ll;

    private String mType = "";

    private WXShare mWxShare;
    private DialogUtils mDialogUtils;

    private LinearLayout _no_data;
    private ImageView _no_image_data;
    private TextView _no_tv_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private MakingListAdapter mAdapter;
    private List<MakingListEntity> listData;

    //视频
    private View mTopView;
    private ImageView _top_video_iv;
    private String _video_url = "";


    private String _personal_id_value = "";

    private int countPage = 1, last_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_personal_web);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setShowLine(false);
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(true, false, true, "产品价值", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageWebActivity.class, "产品价值", ServerApi.PRODUCT_URL);
            }
        });

        init();

        boolean isFirstPPGuide = (boolean) XPreferencesUtils.get("isFirstPPGuide", true);
        if (isFirstPPGuide) {
            _guide_ll.setVisibility(View.VISIBLE);
        } else {
            _guide_ll.setVisibility(View.GONE);
        }

    }


    /***/
    private void init() {
        listData = new ArrayList<>();

        mWxShare = WXShare.Instance(mActivity);
        mDialogUtils = new DialogUtils(mActivity);


        _guide_ll = findViewById(R.id.dialog_home_page_web_guide_ll);

        _no_data = findViewById(R.id.list_no_data);
        _no_image_data = findViewById(R.id.list_no_data_iv);
        _no_tv_data = findViewById(R.id.list_no_data_tv);
        _no_image_data.setImageResource(R.drawable.blank_pages_2_icon);
        _no_tv_data.setText(R.string.no_datas_2);
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);


        //mTopView = getLayoutInflater().inflate(R.layout.act_home_page_web_video, null);//线性布局可用
        mTopView = LayoutInflater.from(this).inflate(R.layout.act_home_page_web_video, mListView, false);
        _top_video_iv = mTopView.findViewById(R.id.dialog_home_page_web_video);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", "" + XPreferencesUtils.get("user_id", ""));
                bundle.putString("id", listData.get(position - 1).getMakingListId());
                bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + listData.get(position - 1).getMakingListId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                bundle.putString("share_title", listData.get(position - 1).getMakingListTitle());
                bundle.putString("share_des", "");
                bundle.putString("share_imageUrl", listData.get(position - 1).getMakingListImage());

                startActivity(PersonalWebDetailsActivity.class, bundle);
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

        initEvent();
        initData();
        getPersonalVideoData();
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
        if (countPage <= last_page) {
            getPersonalData(countPage, false);
        } else {
            // 加载完数据设置为不加载状态，将加载进度收起来
            mSwipeRefreshView.setLoading(false);
        }
    }


    private void initData() {
        countPage = 1;
        getPersonalData(countPage, false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isRefresh", false)) {
            countPage = 1;
            getPersonalData(countPage, true);
            XPreferencesUtils.put("isRefresh", false);
        }
    }

    //To clone
    public void onAction(View v) {
        switch (v.getId()) {
            //隐藏引导页
            case R.id.dialog_home_page_web_guide_ll:
            case R.id.dialog_home_page_web_guide_del:
                XPreferencesUtils.put("isFirstPPGuide", false);
                _guide_ll.setVisibility(View.GONE);
                break;
            //视频
            case R.id.dialog_home_page_web_video_btn:
            case R.id.dialog_home_page_web_video:
                if (XEmptyUtils.isSpace(_video_url)) {
                    ToastUtils.showCenter("视频文件为空");
                    return;
                }
                Uri uri = Uri.parse(_video_url);
                //调用系统自带的播放器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
                break;
            case R.id.personal_web_qzz:
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
                        startActivity(MessageWebActivity.class, "个人微网", ServerApi.PERSONAL_DETAILS_URL + "newEdit?create=true" + "&token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                    }
                } else {
                    startActivity(MessageWebActivity.class, "个人微网", ServerApi.PERSONAL_DETAILS_URL + "newEdit?create=true" + "&token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                }
                break;
            case R.id.personal_web_qkl:
                startActivity(ToCloneActivity.class, "去克隆");
                break;
        }
    }

    /**
     * 获取个人微网
     */
    private void getPersonalData(int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        //map.put("limit", 5);
        map.put("page", page);
        HomeLogic.Instance(mActivity).userPersonalApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.optJSONObject("data").optString("data"));
                        last_page = json.optJSONObject("data").optInt("last_page", 1);
//                        XLog.d("array:" + array);
                        if (countPage == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0)
                                showToast("没有数据了");
                        }

                        for (int i = 0; i < array.length(); i++) {
                            MakingListEntity msg = new MakingListEntity();
                            msg.setMakingListId(array.getJSONObject(i).optString("id"));
                            msg.setMakingListImage(array.getJSONObject(i).optString("cover"));
                            msg.setMakingListTitle(array.getJSONObject(i).optString("title"));
                            msg.setMakingListBrowse(array.getJSONObject(i).optString("popularity"));
                            msg.setMakingListShare(array.getJSONObject(i).optString("share"));
                            msg.setMakingListTime(array.getJSONObject(i).optString("created_at"));
                            msg.setMakingListType("1");
                            msg.setMakingListIsDuf(array.getJSONObject(i).optString("is_default"));
                            listData.add(msg);
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
                                        public void onItemShareClick(final int position) {
                                            _personal_id_value = listData.get(position).getMakingListId();
                                            setShare(listData.get(position).getMakingListTitle(), listData.get(position).getMakingListId(), listData.get(position).getMakingListImage());
                                        }
                                    });

                                    mAdapter.setOnItemSetListener(new MakingListAdapter.onItemSetListener() {
                                        @Override
                                        public void onItemSetClick(final int position) {
                                            mDialogUtils.showListDialog("个人",
                                                    //克隆设置
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mDialogUtils.dismissDialog();
                                                            mDialogUtils.showCloneDialog(new DialogUtils.onItemToCloneListener() {
                                                                @Override
                                                                public void onItemToCloneClick(boolean isClone, String money) {
                                                                    if (isClone) {
                                                                        if (XEmptyUtils.isSpace(money)) {
                                                                            ToastUtils.showCenter("有偿克隆请设置在1~10元");
                                                                        } else {
                                                                            int moneys = Integer.parseInt(money);
                                                                            if (moneys > 10) {
                                                                                ToastUtils.showCenter("有偿克隆请设置在1~10元");
                                                                            } else {
                                                                                userAmendData(listData.get(position).getMakingListId(), "1", moneys + "");
                                                                                mDialogUtils.dismissDialog();
                                                                            }
                                                                        }
                                                                    } else {
                                                                        userAmendData(listData.get(position).getMakingListId(), "0", "0");
                                                                        mDialogUtils.dismissDialog();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    },
                                                    //修改
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(MessageWebActivity.class, "个人", ServerApi.PERSONAL_DETAILS_URL + "newEdit/" + listData.get(position).getMakingListId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                                                            mDialogUtils.dismissDialog();
                                                        }
                                                    },
                                                    //重设title
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mDialogUtils.dismissDialog();
                                                            mDialogUtils.showTitleDialog(new DialogUtils.onItemTitleListener() {
                                                                @Override
                                                                public void onItemTitleClick(String title) {
                                                                    if (XEmptyUtils.isSpace(title)) {
                                                                        ToastUtils.showCenter("输入您需要修改的微网名称(30字)");
                                                                    } else {
                                                                        userTitleData(listData.get(position).getMakingListId(), title);
                                                                        mDialogUtils.dismissDialog();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    },
                                                    //设为默认
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            userTempDefData(listData.get(position).getMakingListId());
                                                            mDialogUtils.dismissDialog();
                                                        }
                                                    },
                                                    //删除
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (listData.get(position).getMakingListIsDuf().equals("1")) {
                                                                ToastUtils.showCenter("默认不能删除");
                                                                return;
                                                            } else {
                                                                mDialogUtils.dismissDialog();
                                                                mDialogUtils.showNoTitleDialog("是否删除产品?", "取消", "删除", new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        mDialogUtils.dismissDialog();
                                                                        userTempDelData(listData.get(position).getMakingListId());
                                                                    }
                                                                });
                                                            }


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
                    LogUtils.debug("error:" + e);
                    showNoRefresh();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
                showNoRefresh();
            }
        });
    }


    /**
     * 获取个人微网视频广告
     */
    private void getPersonalVideoData() {
        HomeLogic.Instance(mActivity).userPersonalVideoApi(new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        JSONObject data = new JSONObject(json.optString("data"));
                        _video_url = data.optString("video_path", "");
                        final String image_path = data.optString("image_path", "").trim();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ImageLoaderManager.loadImage(image_path, _top_video_iv);

                                mListView.addHeaderView(mTopView);
                            }
                        });
                    } else {
                        showToast(message);
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
     * 删除个人微网
     */
    private void userTempDelData(String id) {
        HomeLogic.Instance(mActivity).userTempDelApi(id, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("userTempDel=s=" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countPage = 1;
                                getPersonalData(countPage, true);
                            }
                        });
                    } else {
                        showToast(message);
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
     * 设置个人微网默认
     */
    private void userTempDefData(String id) {
        HomeLogic.Instance(mActivity).userTempDefApi(id, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("userTempDel=s=" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("设置成功");
                                countPage = 1;
                                getPersonalData(countPage, true);
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    //XLog.d("error:" + e);
                    e.printStackTrace();
                    showToast("设置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showToast("设置失败");
            }
        });
    }

    /**
     * 个人微网设置克隆
     */
    private void userAmendData(String id, String is_clone, String clone_price) {
        HashMap<String, Object> map = new HashMap<>();
        // map.put("id", id);
        map.put("is_clone", is_clone);//1-克隆,0-取消克隆
        map.put("clone_price", clone_price);
        HomeLogic.Instance(mActivity).userAmendApi(map, id, true, "设置中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("userAmend=s=" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("设置成功");
                                countPage = 1;
                                getPersonalData(countPage, true);
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    LogUtils.debug("userAmend=s=" + e);
                    e.printStackTrace();
                    showToast("设置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("userAmend=s=" + error);
                showToast("设置失败");
            }
        });
    }

    /**
     * 个人微网重设title
     */
    private void userTitleData(String id, String title) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);
        HomeLogic.Instance(mActivity).userAmendApi(map, id, true, "设置中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.optString("status");
                    String message = json.optString("message");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("设置成功");
                                countPage = 1;
                                getPersonalData(countPage, true);
                            }
                        });
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    LogUtils.debug("userAmend=s=" + e);
                    e.printStackTrace();
                    showToast("设置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("userAmend=s=" + error);
                showToast("设置失败");
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
                // 加载完数据设置为不加载状态，将加载进度收起来
                mSwipeRefreshView.setLoading(false);
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
            thumb = ImageLoader.getInstance().loadImageSync(imageUrl, imageSize);
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher);
        }
        String des = "" + XPreferencesUtils.get("motto", "");
        mWxShare.WXShareWeb(title, XEmptyUtils.isSpace(des) ? "汇脉宝--共享流量，专注营销" : des, ServerApi.PERSONAL_DETAILS_URL + "detail/" + url, thumb, sceneFlag, new OnResponseListener() {
            @Override
            public void onSuccess() {
                XToast.normal("分享成功");
                HomeLogic.Instance(mActivity).getAddShareApi(_personal_id_value, "2", null);
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
        if (mWxShare != null) {
            mWxShare.register();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWxShare != null) {
            mWxShare.unregister();
        }
    }

}
