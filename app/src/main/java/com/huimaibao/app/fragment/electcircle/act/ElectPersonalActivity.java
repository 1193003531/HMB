package com.huimaibao.app.fragment.electcircle.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.electcircle.server.ElectLogic;
import com.huimaibao.app.fragment.home.act.PersonalActivity;
import com.huimaibao.app.fragment.home.act.ReportActivity;
import com.huimaibao.app.fragment.mine.act.FeedbackActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XScrollView;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 互推人
 */
public class ElectPersonalActivity extends BaseActivity {

    //  private String mType = "", mRight = "";


    private XScrollView mScrollView;
    private LinearLayout _top_layout, _title_ll;
    ViewGroup.LayoutParams params;
    private View _needOffsetView;

    //用户信息
    private CircleImageView _top_head_iv, _head_iv;
    private TextView _top_name_tv, _content_name_tv, _name_tv, _browse_tv, _elect_tv;
    private ImageView _vip_iv;

    private String _material_id_value = "", _user_id_value = "", _head_value = "", _name_value = "", _browse_value = "", _elect_value = "", _cover_value = "", _title_value = "";
    //1,文章，2个人网页，3，营销网页，4，个人名片
    private int _material_style_value = 4;


    private WebView mWebView;
    private ProgressBar progressBar = null;


    //返回，举报
    private ImageView _back_btn, _report_btn;
    //分享次数
    private TextView _share_btn;
    private String _des_content = "";
    private WXShare mWxShare;

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_elect_personal);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            _user_id_value = intent.getStringExtra("vType");
        }

        mDialogUtils = new DialogUtils(mActivity);
        mWxShare = WXShare.Instance(mActivity);

        init();
        initData();

    }


    private void init() {

        _needOffsetView = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);


        _top_layout = findViewById(R.id.top_layout);
        mScrollView = findViewById(R.id.scrollView);

        _title_ll = findViewById(R.id.elect_personal_title_ll);
        _back_btn = findViewById(R.id.back_btn);
        _report_btn = findViewById(R.id.report_btn);
        _share_btn = findViewById(R.id.elect_personal_share_btn);

        _content_name_tv = findViewById(R.id.elect_personal_web_title);

        _top_head_iv = findViewById(R.id.t_image);
        _top_name_tv = findViewById(R.id.t_name);

        _head_iv = findViewById(R.id.mine_basic_image);
        _vip_iv = findViewById(R.id.mine_basic_iv);
        _name_tv = findViewById(R.id.mine_basic_name);
        _browse_tv = findViewById(R.id.mine_basic_browse);
        _elect_tv = findViewById(R.id.mine_basic_elect);


        mScrollView.setOnScrollListener(new XScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > 255) {
                    scrollY = 255;
                    _title_ll.setVisibility(View.VISIBLE);
                    _back_btn.setImageResource(R.drawable.ico_arrow_left);
                    _report_btn.setImageResource(R.drawable.wallet_top_right_icon);
                    XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
                } else {
                    _title_ll.setVisibility(View.INVISIBLE);
                    _back_btn.setImageResource(R.drawable.back_icon_w_left);
                    _report_btn.setImageResource(R.drawable.wallet_top_right_b_icon);
                    XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);
                }
                params = _top_layout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
                _top_layout.setLayoutParams(params);
                _top_layout.setBackgroundColor(Color.argb(scrollY, 255, 255, 255));

                //滑动隐藏分享按钮
                // _share_btn.setVisibility(View.GONE);
            }
        });

//
//        mScrollView.setOnTouchListener(new View.OnTouchListener() {
//            private int lastY = 0;
//            private int touchEventId = -9983761;
//            Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    View scroller = (View) msg.obj;
//                    if (msg.what == touchEventId) {
//                        if (lastY == scroller.getScrollY()) {
//                            handleStop(scroller);
//                        } else {
//                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
//                            lastY = scroller.getScrollY();
//                        }
//                    }
//                }
//            };
//
//
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
//                }
//                return false;
//            }
//
//            //处理真正的事件
//            private void handleStop(Object view) {
//                XScrollView scroller = (XScrollView) view;
//                // scrollY = scroller.getScrollY();
//                _share_btn.setVisibility(View.VISIBLE);
//            }
//        });

        mWebView = findViewById(R.id.elect_personal_web);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);//设置适应Html5 重点是这个设置

        progressBar = findViewById(R.id.progressBar1);
        progressBar.setMax(100);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                mWebView.loadUrl(s);
                return super.shouldOverrideUrlLoading(webView, s);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

        });

        //mWebView.loadUrl("http://weixin.yuhongrocky.top/#/app/match?materialType=1&materialId=784");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    // 加载中
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }

            }
        });
    }

    /**
     * 加载数据
     */
    private void initData() {
        if (!XEmptyUtils.isEmpty(XPreferencesUtils.get("today_share_num", "0"))) {
            if (Integer.parseInt(XPreferencesUtils.get("today_share_num", "0") + "") > 0) {
                _share_btn.setBackgroundResource(R.drawable.btn_blue_r20_bg);
                _share_btn.setText("开始互推(今日剩余" + XPreferencesUtils.get("today_share_num", "2") + "次)");
                _share_btn.setEnabled(true);
            } else {
                _share_btn.setBackgroundResource(R.drawable.btn_hui_r20_bg);
                _share_btn.setText("开始互推(今日剩余0次)");
                _share_btn.setEnabled(false);
            }
        }


        getElectUser(true);
    }

    public void onAction(View v) {
        switch (v.getId()) {
            //返回
            case R.id.back_btn:
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
            //用户主页
            case R.id.mine_basic_ll_btn:
                startActivity(PersonalActivity.class, _user_id_value);
                break;
            //举报
            case R.id.report_btn:
                mDialogUtils.showGeneralDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(FeedbackActivity.class, "意见反馈", _user_id_value);
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ReportActivity.class, "互推圈", _user_id_value);
                        mDialogUtils.dismissDialog();
                    }
                });
                break;
            //分享
            case R.id.elect_personal_share_btn:
                setShare();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            /**退出*/
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mWxShare != null) {
            mWxShare.register();
        }

    }

    /**
     * 确保注销配置能够被释放
     */
    @Override
    public void onDestroy() {
        if (this.mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
        if (mWxShare != null) {
            mWxShare.unregister();
        }

    }


    /**
     * 获取用户互推信息
     */
    private void getElectUser(boolean isShow) {
        //HashMap<String, Object> map = new HashMap<>();
        // map.put("id", _user_id_value);
        ElectLogic.Instance(mActivity).getElectPerApi(_user_id_value, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("ElectUser:" + json);
                    if (json.getString("status").equals("0")) {
                        _user_id_value = json.optJSONObject("data").optString("user_id");
                        _head_value = json.optJSONObject("data").optString("head_picture");
                        _name_value = json.optJSONObject("data").optString("name");
                        _browse_value = json.optJSONObject("data").optString("use_num");
                        _elect_value = json.optJSONObject("data").optString("others_browse_num");
                        _des_content = json.optJSONObject("data").optJSONObject("interPushInfoMaterial").optString("sort_desc");
                        if (_des_content.length() > 50) {
                            _des_content = _des_content.substring(0, 50);
                        }

                        _material_style_value = json.optJSONObject("data").optInt("material_style", 4);


                        if (!XEmptyUtils.isSpace(json.optJSONObject("data").optString("interPushInfoMaterial", ""))) {
                            JSONObject titleData = new JSONObject(json.optJSONObject("data").optString("interPushInfoMaterial", ""));
                            _material_id_value = titleData.optString("id");
                            if (titleData.optString("cover").contains("[")) {
                                try {
                                    _cover_value = (JSON.parseArray(titleData.optString("cover")).get(0).toString().replace("\"", "").trim());
                                } catch (Exception e) {
                                    _cover_value = "";
                                }
                            } else {
                                try {
                                    _cover_value = titleData.optString("cover").replace("\"", "").trim();
                                } catch (Exception e) {
                                    _cover_value = "";
                                }
                            }
                            _title_value = titleData.optString("title");
                        }

                        //LogUtils.debug(_cover_value);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (_material_style_value) {
                                    case 1:
                                        _content_name_tv.setText(XEmptyUtils.isSpace(_name_value) ? "" : _name_value + "的文章");
                                        break;
                                    case 2:
                                        _content_name_tv.setText(XEmptyUtils.isSpace(_name_value) ? "" : _name_value + "的个人微网");
                                        break;
                                    case 3:
                                        _content_name_tv.setText(XEmptyUtils.isSpace(_name_value) ? "" : _name_value + "的营销网页");
                                        break;
                                    case 4:
                                        _content_name_tv.setText(XEmptyUtils.isSpace(_name_value) ? "" : _name_value + "的名片");
                                        _material_id_value = _user_id_value;
                                        break;
                                }

                                ImageLoaderManager.loadImage(_head_value, _top_head_iv, R.drawable.ic_launcher);
                                ImageLoaderManager.loadImage(_head_value, _head_iv, R.drawable.ic_launcher);

                                //ImageSize imageSize = new ImageSize(120, 120);
                                //_head_iv.setImageBitmap(ImageLoader.getInstance().loadImageSync(_cover_value.trim(), imageSize));

                                _name_tv.setText(_name_value);
                                _top_name_tv.setText(_name_value);
                                _browse_tv.setText("为他人带来浏览" + _browse_value + "次");
                                _elect_tv.setText("使用互推圈" + _elect_value + "次");
                                //XLog.e("web:" + (ServerApi.ELECT_WEB_URL + "materialType=" + _material_style_value + "&materialId=" + _material_id_value));
                                mWebView.loadUrl(ServerApi.ELECT_WEB_URL + "materialType=" + _material_style_value + "&materialId=" + _material_id_value);
                                _share_btn.setVisibility(View.VISIBLE);


                            }
                        });
                    }
                } catch (Exception e) {
                    LogUtils.debug("ElectUser:" + e);
                    //  hideDialog();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("ElectUser:" + error);
                // hideDialog();
            }
        });
    }

    /**
     * 分享
     */
    private void setShare() {
        mWxShare.showShareDialog("互推圈", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //好友
                getElectAdd(0);
                //getShare(0, title, url, imaUrl);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getShare(1, _title_value, _des_content, ServerApi.ARTICLE_DETAILS_URL + _material_id_value + "?created_at=" + XTimeUtils.getTimesYMD() + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + _user_id_value, _cover_value);

                //朋友圈
                 getElectAdd(1);
                //getShare(1, title, url, imaUrl);
            }
        });
    }

    private void getShare(int sceneFlag, String title, String des, String url, String imaUrl) {
        //"我是" + _name_value + " " + "邀请您与我共享千万营销人名片", XEmptyUtils.isSpace(X) ? "汇脉宝--共享流量，专注营销" : shareDes,
        ImageSize imageSize = new ImageSize(120, 120);
        Bitmap thumb;
        try {
            thumb = ImageLoader.getInstance().loadImageSync(imaUrl.trim(), imageSize);
            //thumb = ImageLoader.getInstance().loadImageSync(imaUrl.trim().toUpperCase());
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.layer_share_icon);
        }
        //String des = "" + XPreferencesUtils.get("motto", "");
        mWxShare.WXShareWeb(title, XEmptyUtils.isSpace(des) ? "汇脉宝--共享流量，专注营销" : des, url, thumb, sceneFlag, new OnResponseListener() {
            @Override
            public void onSuccess() {
                XToast.normal("分享成功");
                XPreferencesUtils.put("isMaterial", true);
                finish();
            }

            @Override
            public void onCancel() {
                //XToast.normal("分享取消");
            }

            @Override
            public void onFail(String message) {
                //XToast.normal("分享失败");
            }
        });

    }

    /**
     * 生成互推记录
     */
    private void getElectAdd(final int sceneFlag) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("material_style", XPreferencesUtils.get("material_style", ""));
        map.put("material_id", XPreferencesUtils.get("material_id", ""));
        map.put("target_user_id", _user_id_value);
        map.put("target_material_style", _material_style_value);
        map.put("target_material_id", _material_id_value);
        ElectLogic.Instance(mActivity).getElectAddApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("ElectAdd:" + json);
                    //{"status":0,"message":"success","data":{"record_id":2,"interpush_id":547}}
                    if (json.getString("status").equals("0")) {
                        final String record_id = json.optJSONObject("data").optString("record_id");
                        final String interpush_id = json.optJSONObject("data").optString("interpush_id");
                        XPreferencesUtils.put("isMaterial", true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (_material_style_value) {
                                    case 1:
                                        //+ "?meterial_id="
                                        // setShare(_title_value, ServerApi.ARTICLE_DETAILS_URL + "?meterial_id=" + _material_id_value + "&record_id=" + record_id + "&interpush_id=" + interpush_id, _cover_value);
                                        getShare(sceneFlag, _title_value, _des_content, ServerApi.ARTICLE_DETAILS_URL + _material_id_value + "/" + _user_id_value + "?created_at=" + XTimeUtils.getTimesYMD() + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + _user_id_value, _cover_value);
                                        break;
                                    case 2:
                                        // setShare(_title_value, ServerApi.CARD_URL + "?meterial_id=" + _material_id_value + "&record_id=" + record_id + "&interpush_id=" + interpush_id, _cover_value);
                                        getShare(sceneFlag, _title_value, _des_content, ServerApi.PERSONAL_DETAILS_URL + "detail/" + _material_id_value + "?created_at=" + XTimeUtils.getTimesYMD() + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + _user_id_value, _cover_value);
                                        break;
                                    case 3:
                                        // setShare(_title_value, ServerApi.CARD_URL + "?meterial_id=" + _material_id_value + "&record_id=" + record_id + "&interpush_id=" + interpush_id, _cover_value);
                                        getShare(sceneFlag, _title_value, _des_content, ServerApi.MARKETING_DETAILS_URL + _material_id_value + "?created_at=" + XTimeUtils.getTimesYMD() + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + _user_id_value, _cover_value);
                                        break;
                                    case 4:
                                        // setShare("我是" + _name_value + " " + "邀请您与我共享千万营销人名片", ServerApi.CARD_URL + "?meterial_id=" + _user_id_value + "&record_id=" + record_id + "&interpush_id=" + interpush_id, _head_value);
                                        getShare(sceneFlag, "我是" + _name_value + " " + "邀请您与我共享千万营销人名片", _des_content, ServerApi.CARD_URL + _user_id_value + "?created_at=" + XTimeUtils.getTimesYMD() + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + _user_id_value, _head_value);
                                        break;
                                }
                                mWxShare.setDismiss();
                            }
                        });

                    } else {
                        showToast(json.getString("message"));
                    }
                } catch (Exception e) {
                    LogUtils.debug("e:" + e);
                    //  hideDialog();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
                // hideDialog();
            }
        });
    }


}
