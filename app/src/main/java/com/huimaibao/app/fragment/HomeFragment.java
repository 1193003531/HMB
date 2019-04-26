package com.huimaibao.app.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.home.act.CardDetailActivity;
import com.huimaibao.app.fragment.home.act.IncomeListActivity;
import com.huimaibao.app.fragment.home.act.LibraryActivity;
import com.huimaibao.app.fragment.home.act.MarketingListActivity;
import com.huimaibao.app.fragment.home.act.PersonalWebActivity;
import com.huimaibao.app.fragment.home.adapter.MarketingListAdapter;
import com.huimaibao.app.fragment.home.entity.IncomeListEntity;
import com.huimaibao.app.fragment.home.entity.MakingListEntity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.library.adapter.LibNewsHomeAdapter;
import com.huimaibao.app.fragment.library.bean.LibNewsEntity;
import com.huimaibao.app.fragment.library.server.LibLogic;
import com.huimaibao.app.fragment.message.act.MessageCMActivity;
import com.huimaibao.app.fragment.mine.act.MarketingTagActivity;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.fragment.web.LibDetailsWebActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.zxing.act.CaptureActivity;
import com.huimaibao.app.zxing.util.Constant;
import com.youth.xframe.banner.BannerItemBean;
import com.youth.xframe.banner.BannerViewPager;
import com.youth.xframe.banner.ImageLoaderInterface;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XScrollViewUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.NoScrollListView;
import com.youth.xframe.widget.XHorizontalScrollView;
import com.youth.xframe.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private Activity mActivity = this.getActivity();

    //联系我们
    private ImageView _btnTelUs;

    private XScrollView mScrollView;

    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;
    private View _needOffsetView;

    private TextView _left_top;
    private ImageView btn_zxing;


    //btn_generalize, btn_elect_circle,
    private LinearLayout btn_marking_card, btn_web_personality, btn_marketing_web;
    private LinearLayout btn_client_mg, btn_marketing_tag, btn_lib;
    private TextView _marketing_list_more, _income_list_more, _lib_list_more;
    private LinearLayout _lib_more_ll;

    //周榜，月榜，总榜
    private TextView _market_week_btn, _market_month_btn, _market_total_btn;

    //广告
    private BannerViewPager mBannerViewPager;
    private final int[] mData = {R.drawable.home_banner};
    List<BannerItemBean> bannerList;

    //精品文库
    //NoScroll
    private NoScrollListView mListViewLib;
    private List<LibNewsEntity> newsList;
    private LibNewsHomeAdapter mLibAdapter;

    //营销榜单
    //NoScroll
    private LinearLayout no_data_ll;
    private ListView mListView;
    private MarketingListAdapter mAdapter;
    private List<MakingListEntity> mlList;


    //收入榜单
    private XHorizontalScrollView mXHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    private List<IncomeListEntity> iLData;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity = this.getActivity();
        initView(view);

        return view;
    }


    @Override
    protected void initData() {
        // setBannerData();
        getBannerData();
        getRecommend();
        getMarketData("week", false);
        getIncomeData();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);
        //getRecommend();
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        mScrollView = v.findViewById(R.id.home_scrollview);
        _needOffsetView = v.findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);
        _left_top = v.findViewById(R.id.home_item_left);
        btn_zxing = v.findViewById(R.id.home_item_right);

        _top_layout = v.findViewById(R.id.top_layout);
        mScrollView.setOnScrollListener(new XScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > 255) {
                    scrollY = 255;
                    btn_zxing.setImageResource(R.drawable.main_qr_code_icon);
                    _left_top.setVisibility(View.VISIBLE);
                } else {
                    btn_zxing.setImageResource(R.drawable.fragment_home_top_ewm);
                    _left_top.setVisibility(View.INVISIBLE);
                }
                params = _top_layout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = XDensityUtils.dp2px(44) + XDensityUtils.getStatusBarHeight();
                _top_layout.setLayoutParams(params);
                _top_layout.setBackgroundColor(Color.argb(scrollY, 255, 255, 255));

                XStatusBar.setTranslucentForImageViewInFragment(mActivity, scrollY > 80 ? 80 : scrollY, _needOffsetView);

            }
        });


        _btnTelUs = v.findViewById(R.id.home_buttom_tel_us);
        //btn_generalize = v.findViewById(R.id.home_generalize_btn);
        //btn_elect_circle = v.findViewById(R.id.home_elect_circle_btn);
        btn_marking_card = v.findViewById(R.id.home_marking_card_btn);
        btn_web_personality = v.findViewById(R.id.home_web_personality_btn);
        btn_marketing_web = v.findViewById(R.id.home_marketing_web_btn);

        btn_client_mg = v.findViewById(R.id.home_client_mg_btn);
        btn_marketing_tag = v.findViewById(R.id.home_marketing_tag_btn);
        btn_lib = v.findViewById(R.id.home_marketing_lib_btn);

        mBannerViewPager = v.findViewById(R.id.home_banner_viewPager);

        _lib_list_more = v.findViewById(R.id.home_lib_list_more);
        _marketing_list_more = v.findViewById(R.id.home_marketing_list_more);
        _income_list_more = v.findViewById(R.id.home_income_list_more);

        _lib_more_ll = v.findViewById(R.id.home_lib_list_ll);


        _market_week_btn = v.findViewById(R.id.fragment_home_marketing_week);
        _market_month_btn = v.findViewById(R.id.fragment_home_marketing_month);
        _market_total_btn = v.findViewById(R.id.fragment_home_marketing_total);


        mXHorizontalScrollView = v.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = v.findViewById(R.id.mRadioGroup_content);
//        rl_column = v.findViewById(R.id.rl_column);
//        shade_left = v.findViewById(R.id.shade_left);
//        shade_right = v.findViewById(R.id.shade_right);

        no_data_ll = v.findViewById(R.id.marketing_list_no_data);
        mListView = v.findViewById(R.id.home_marketing_list);
        mListView.setFocusable(false);

        mListViewLib = v.findViewById(R.id.home_lib_list);
        mListViewLib.setFocusable(false);
    }


    /**
     * 开始扫码
     */
    private void startQrCode() {
        XPermission.requestPermissions(mActivity, 1002, new String[]{
                Manifest.permission.CAMERA
        }, new XPermission.OnPermissionListener() {
            //权限申请成功时调用
            @Override
            public void onPermissionGranted() {
                // 二维码扫码
                Intent intent = new Intent(mActivity, CaptureActivity.class);
                startActivityForResult(intent, Constant.REQ_QR_CODE);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(mActivity);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调 &&

        if (resultCode == mActivity.RESULT_OK && requestCode == Constant.REQ_QR_CODE) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            //XToast.normal(scanResult);
            //startActivity(CardClipDetailActivity.class, scanResult);
            startActivity(MessageWebActivity.class, "", scanResult);
        }
    }


    /**
     * 设置默认广告数据
     */
    private void setBannerData() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<BannerItemBean> pagerItemBeanList = new ArrayList<>(mData.length);
                for (int i = 0; i < mData.length; i++) {
                    pagerItemBeanList.add(new BannerItemBean(mData[i], "", ""));
                }

//.setViewPagerHeight((int) (XDensityUtils.getScreenWidth() * 0.44))
                mBannerViewPager.setData(pagerItemBeanList,//设置数据
                        new ImageLoaderInterface() {//设置图片加载器
                            @Override
                            public void displayImage(Context context, Object imgPath, ImageView imageView) {
                                ImageLoaderManager.loadImage(imgPath.toString(), imageView, R.drawable.home_banner);
                            }
                        })
                        .setPageTransformer(null)//设置切换效果
                        .setAutoPlay(true)//设置是否自动播放
                        .setHaveTitle(false)//设置是否显示标题
                        .setScaleType(ImageView.ScaleType.FIT_XY)//设置图片显示类型
                        .setPointGravity(Gravity.CENTER_HORIZONTAL);

            }
        });

    }

    /**
     * 精品文库
     */
    private void setLibList() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newsList.size() > 0) {
                    _lib_more_ll.setVisibility(View.VISIBLE);
                    // mListView.setVisibility(View.VISIBLE);
                    mLibAdapter = new LibNewsHomeAdapter(mActivity, newsList);
                    mListViewLib.setAdapter(mLibAdapter);
                    XScrollViewUtils.setListViewHeightBasedOnChildren(mListViewLib);
                    mListViewLib.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent intent = new Intent();
                            intent.setClass(mActivity, LibDetailsWebActivity.class);
                            intent.putExtra("vType", mLibAdapter.getItem(position).getLibNewsId());
                            if (mLibAdapter.getItem(position).getLibNewsType().equals("3")) {
                                intent.putExtra("vUrl", mLibAdapter.getItem(position).getLibNewsUrl());
                            } else {
                                intent.putExtra("vUrl", ServerApi.ARTICLE_DETAILS_URL + mLibAdapter.getItem(position).getLibNewsId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                            }
                            mActivity.startActivity(intent);
                            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                } else {
                    _lib_more_ll.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 营销榜单
     */
    private void setMarketingList() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mlList.size() > 0) {
                    no_data_ll.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mAdapter = new MarketingListAdapter(mActivity, mlList);
                    mListView.setAdapter(mAdapter);
                    XScrollViewUtils.setListViewHeightBasedOnChildren(mListView);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("id", mlList.get(position).getMakingListId());
//                            bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + mlList.get(position).getMakingListId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
//                            bundle.putString("share_title", mlList.get(position).getMakingListTitle());
//                            bundle.putString("share_des", "");
//                            bundle.putString("share_imageUrl", mlList.get(position).getMakingListImage());
//
//                            startActivity(PersonalWebDetailsActivity.class, bundle);
                            startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + mlList.get(position).getMakingListUserId() + ServerApi.HOME_PAGE_WEB_TOKEN);
                        }
                    });
                } else {
                    no_data_ll.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }

            }
        });


    }

    /**
     * 收入榜单
     */
    private void setIncomeList() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRadioGroup_content.removeAllViews();
                mXHorizontalScrollView.setParam(mActivity, XDensityUtils.getScreenWidth(), mRadioGroup_content, null, null, null, null);
                for (int i = 0; i < iLData.size(); i++) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = 5;
                    params.rightMargin = 5;
                    View v = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_income_item, null);
                    CircleImageView itemImage = v.findViewById(R.id.f_h_i_item_head);
                    TextView itemNum = v.findViewById(R.id.f_h_i_item_num);
                    TextView itemName = v.findViewById(R.id.f_h_i_item_name);
                    TextView itemMoney = v.findViewById(R.id.f_h_i_item_money);
                    v.setId(i);
                    itemNum.setText("NO." + (i + 1));
                    itemName.setId(i);
                    itemName.setText(iLData.get(i).getIncomeName());
                    itemMoney.setText("总收入￥" + iLData.get(i).getIncomeMoney());
                    ImageLoaderManager.loadImage(iLData.get(i).getIncomeImage(), itemImage, R.drawable.ic_launcher);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // startActivity(PersonalActivity.class, iLData.get(v.getId()).getIncomeId());
                            startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + iLData.get(v.getId()).getIncomeId() + ServerApi.HOME_PAGE_WEB_TOKEN);
                        }
                    });

                    mRadioGroup_content.addView(v, i, params);
                }
            }
        });

    }


    /**
     * 获取banner
     */
    private void getBannerData() {
        HomeLogic.Instance(mActivity).homeBannerApi(new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(data);
                        //XLog.d("array:" + array);
                        bannerList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            BannerItemBean itemBean = new BannerItemBean();
                            itemBean.setImg_path(array.getJSONObject(i).getString("images"));
                            itemBean.setUrl(array.getJSONObject(i).getString("url"));
                            bannerList.add(itemBean);
                        }


                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (bannerList.size() > 0) {
                                    mBannerViewPager.setData(bannerList,//设置数据
                                            new ImageLoaderInterface() {//设置图片加载器
                                                @Override
                                                public void displayImage(Context context, Object imgPath, ImageView imageView) {
                                                    ImageLoaderManager.loadImage(imgPath.toString(), imageView, R.drawable.home_banner);
                                                }
                                            })
                                            .setPageTransformer(null)//设置切换效果
                                            .setAutoPlay(true)//设置是否自动播放
                                            .setHaveTitle(false)//设置是否显示标题
                                            .setScaleType(ImageView.ScaleType.FIT_XY)//设置图片显示类型
                                            .setPointGravity(Gravity.CENTER_HORIZONTAL)
                                            //设置item的监听事件
                                            .setOnBannerItemClickListener(new BannerViewPager.OnBannerItemClickListener() {
                                                @Override
                                                public void OnClickLister(View view, int currentItem) {
                                                    //XToast.normal("" + currentItem);
                                                    startActivity(MessageWebActivity.class, "广告", bannerList.get(currentItem).getUrl());
                                                }
                                            });
                                } else {
                                    setBannerData();
                                }

//.setViewPagerHeight((int) (XDensityUtils.getScreenWidth() * 0.44))

                            }
                        });

                    } else {
                        setBannerData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setBannerData();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                setBannerData();
            }
        });
    }


    /**
     * 获取推荐文章
     */
    private void getRecommend() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("limit", 3);
//        map.put("page", 1);
        LibLogic.Instance(mActivity).getHomeRecommendApi(new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("lib=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        newsList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            LibNewsEntity libNewsE = new LibNewsEntity();
                            libNewsE.setLibLibNewsState("推荐");

                            if (array.getJSONObject(i).getString("cover").contains("[")) {
                                if (JSON.parseArray(array.getJSONObject(i).getString("cover")).size() > 1) {
                                    libNewsE.setLibNewsType("1");
                                    try {
                                        libNewsE.setLibNewsImage1(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage1("");
                                    }
                                    try {
                                        libNewsE.setLibNewsImage2(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(1).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage2("");
                                    }
                                    try {
                                        libNewsE.setLibNewsImage3(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(2).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImage3("");
                                    }
                                } else {
                                    libNewsE.setLibNewsType("0");
                                    try {
                                        libNewsE.setLibNewsImageRight(JSON.parseArray(array.getJSONObject(i).getString("cover")).get(0).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        libNewsE.setLibNewsImageRight("");
                                    }
                                }
                            } else {
                                libNewsE.setLibNewsType("0");
                                try {
                                    libNewsE.setLibNewsImageRight(array.getJSONObject(i).getString("cover").replace("\"", "").trim());
                                } catch (Exception e) {
                                    libNewsE.setLibNewsImageRight("");
                                }

                            }

                            libNewsE.setLibNewsTitle(array.getJSONObject(i).getString("title"));
                            libNewsE.setLibNewsName(array.getJSONObject(i).getString("author"));
                            libNewsE.setLibNewsBrowse(array.getJSONObject(i).getString("pageviews"));
                            libNewsE.setLibNewsTime(array.getJSONObject(i).getString("created_at"));
                            libNewsE.setLibNewsId(array.getJSONObject(i).getString("id"));
                            newsList.add(libNewsE);
                        }
                        setLibList();


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
     * 获取营销奖励
     */
    private void getMarketData(String sort, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 5);
        map.put("page", 1);
        if (!XEmptyUtils.isSpace(sort)) {
            map.put("sort", sort);
        }

        HomeLogic.Instance(mActivity).homeMarketApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").optString("rank"));
                        LogUtils.debug("json:" + array);
                        mlList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            MakingListEntity mli = new MakingListEntity();
                            mli.setMakingListNum("" + (i + 1));
                            //HomeLogic.Instance(mActivity).getJsonImageUrls(array.getJSONObject(i).getString("content"))
                            mli.setMakingListId(array.getJSONObject(i).optString("id", ""));
                            mli.setMakingListImage("");
                            mli.setMakingListTitle("");
                            mli.setMakingListBrowse(array.getJSONObject(i).optString("popularity", "0"));
                            mli.setMakingListShare("0");
                            mli.setMakingListInterested(array.getJSONObject(i).optString("interested", "0"));
                            mli.setMakingListHead(array.getJSONObject(i).getJSONObject("user").optString("portrait"));
                            mli.setMakingListName(array.getJSONObject(i).getJSONObject("user").optString("name"));
                            mli.setMakingListUserId(array.getJSONObject(i).getJSONObject("user").optString("id", ""));
                            mlList.add(mli);
                        }
                        setMarketingList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.debug("home=s=" + e.toString());
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("home=s=" + error);
            }
        });
    }


    /**
     * 获取收入榜单
     */
    private void getIncomeData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 5);
        map.put("page", 1);
        HomeLogic.Instance(mActivity).homeIncomeApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        //XLog.d("array:" + array);
                        iLData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            IncomeListEntity listEntity = new IncomeListEntity();
                            listEntity.setIncomeId(array.getJSONObject(i).getString("user_id"));
                            listEntity.setIncomeImage(array.getJSONObject(i).getString("avatar"));
                            listEntity.setIncomeName(array.getJSONObject(i).getString("name"));
                            listEntity.setIncomeMoney(array.getJSONObject(i).getString("reward"));
                            iLData.add(listEntity);
                        }
                        setIncomeList();
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


    /**
     * 界面点击事件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //周榜
        _market_week_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _market_week_btn.setTextColor(getResources().getColor(R.color.colore262626));
                _market_month_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_total_btn.setTextColor(getResources().getColor(R.color.color999999));

                _market_week_btn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                _market_month_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _market_total_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规

                _market_week_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.fragment_home_marketing_icon));
                _market_month_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_total_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                getMarketData("week", true);
            }
        });

        //月榜
        _market_month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _market_week_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_month_btn.setTextColor(getResources().getColor(R.color.colore262626));
                _market_total_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_week_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
                _market_month_btn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//常规
                _market_total_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _market_week_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_month_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.fragment_home_marketing_icon));
                _market_total_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                getMarketData("month", true);
            }
        });

        //总榜
        _market_total_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _market_week_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_month_btn.setTextColor(getResources().getColor(R.color.color999999));
                _market_total_btn.setTextColor(getResources().getColor(R.color.colore262626));
                _market_week_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
                _market_month_btn.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//常规
                _market_total_btn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//常规
                _market_week_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_month_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                _market_total_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.fragment_home_marketing_icon));
                getMarketData("", true);
            }
        });

        btn_zxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
                //startContacts();
            }
        });
        btn_marking_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CardDetailActivity.class, "名片");
            }
        });
        btn_web_personality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if ((boolean) XPreferencesUtils.get("has_usertemp", false)) {
                startActivity(PersonalWebActivity.class, "产品微网");
//                } else {
//                    startActivity(MessageWebActivity.class, "个人微网", "http://weixin.51huimaibao.cn/#/tools/personal/customTemplate/");
//                }

            }
        });
        //主页
        btn_marketing_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if ((boolean) XPreferencesUtils.get("has_usertemp", false)) {
                //               startActivity(MarketingWebActivity.class, "营销网页");
//                } else {
//                    startActivity(MessageWebActivity.class, "营销网页", "http://weixin.yuhongrocky.top/#/tools/marketing/edit/");
//                }

                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + XPreferencesUtils.get("user_id", "") + ServerApi.HOME_PAGE_WEB_TOKEN);
            }
        });

        btn_client_mg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageCMActivity.class, "客户管理");
            }
        });

        btn_marketing_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MarketingTagActivity.class, "营销标签");
            }
        });

        btn_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LibraryActivity.class, "文库");
            }
        });
        _lib_list_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LibraryActivity.class, "文库");
            }
        });


        _marketing_list_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MarketingListActivity.class, "营销榜单");
            }
        });
        _income_list_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(IncomeListActivity.class, "收入榜单");
            }
        });
        _btnTelUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:023-65739176"));
                startActivity(intent);//调用上面这个intent实现拨号
            }
        });
    }


}
