package com.huimaibao.app.fragment.web;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.act.ReportActivity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.mine.act.CardClipDetailActivity;
import com.huimaibao.app.fragment.mine.act.FeedbackActivity;
import com.huimaibao.app.fragment.mine.act.MemberActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.pay.PayActivity;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.widget.XToast;
import com.youth.xframe.widget.CircleImageView;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 用户个人微网详情
 */
public class PersonalWebDetailsActivity extends BaseActivity {

    private WebView mWebView;
    private ProgressBar progressBar = null;

    private WXShare mWxShare;

    private String mUrl = "";

    private DialogUtils mDialogUtils;

    //用户头像，克隆，感兴趣，分享
    private CircleImageView _head_iv;
    private TextView _clone_tv, _interest_tv, _interest_num, _share_num;
    private String _personal_id_value = "", _user_id_value = "", _head_value = "", _name_value = "", _clone_money_value = "", _interest_num_value = "", _share_num_value = "";
    private String is_clone = "0";
    private boolean is_interest = false;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;


    //分享
    private String share_title = "", share_des = "", share_imageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_personal_web_details);
        setNeedBackGesture(false);
        mWxShare = WXShare.Instance(mActivity);

        Intent intent = getIntent();
        if (intent.getAction().equals("two")) {
            Bundle bundle = intent.getExtras();
            _personal_id_value = bundle.getString("id");
            mUrl = bundle.getString("vUrl");
            share_title = bundle.getString("share_title");
            share_des = bundle.getString("share_des");
            share_imageUrl = bundle.getString("share_imageUrl");
        }
        mDialogUtils = new DialogUtils(mActivity);
        setTopTitle("");
        setTopLeft(true, true, false, "");
        setTopRight(true, true, false, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.showGeneralDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(FeedbackActivity.class, "意见反馈", _user_id_value);
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ReportActivity.class, "名片", _user_id_value);
                        mDialogUtils.dismissDialog();
                    }
                });
            }
        });
        init();
        initData();
    }

    /**
     * 初始化控件
     */
    public void init() {

        _head_iv = findViewById(R.id.personal_web_details_head);
        _clone_tv = findViewById(R.id.personal_web_details_clone);
        _interest_tv = findViewById(R.id.personal_web_details_interest_tv);
        _interest_num = findViewById(R.id.personal_web_details_interest_num);
        _share_num = findViewById(R.id.personal_web_details_share_num);


        mWebView = findViewById(R.id.personal_web_details_wv);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setDomStorageEnabled(true);//设置适应Html5 //重点是这个设置

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//
            webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // 添加js交互接口类，并起别名 androidJS
        mWebView.addJavascriptInterface(new JavascriptListner(), "androidJS");
        progressBar = findViewById(R.id.progressBar1);
        progressBar.setMax(100);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                mWebView.loadUrl(s);
                return true;

            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

        });


        mWebView.setWebChromeClient(new WebChromeClient() {

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                setTopTitle("" + s);
            }

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


        LogUtils.debug("mUrl:" + mUrl);

        mWebView.loadUrl(mUrl);
        //mWebView.loadUrl("http://weixin.yuhongrocky.top/#/app/match?materialType=1&materialId=784");

    }


    /***/
    private void initData() {
        getPersonalDetailsData(_personal_id_value);
    }


    /**
     * 点击
     */
    public void onAction(View v) {
        switch (v.getId()) {
            //头像
            case R.id.personal_web_details_head:
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + _user_id_value + ServerApi.HOME_PAGE_WEB_TOKEN);
                break;
            //名片
            case R.id.personal_web_details_card:
                startActivity(CardClipDetailActivity.class, _user_id_value);
                break;
            //克隆
            case R.id.personal_web_details_clone:
                if (XPreferencesUtils.get("vip_level", "0").equals("0")) {
                    mDialogUtils.showNoTitleDialog("开通会员，克隆网页", "取消", "开通", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(MemberActivity.class, "会员中心");
                            mDialogUtils.dismissDialog();
                        }
                    });
                } else {
                    if (XStringUtils.m1(_clone_money_value) > 0) {
                        toClonePayData(_personal_id_value, _clone_money_value);
                    } else {
                        startActivity(MessageWebActivity.class, "克隆", ServerApi.PERSONAL_CLONE_URL + _personal_id_value + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                        finish();
                    }
                }
                break;
            //感兴趣
            case R.id.personal_web_details_interest:
                if (XPreferencesUtils.get("user_id", "").equals(_user_id_value)) {
                    showToast("自己不能对自己感兴趣");
                } else {
                    if (is_interest) {
                        showToast("已经感兴趣过了");
                    } else {
                        getAddInterest();
                    }
                }

                break;
            //分享
            case R.id.personal_web_details_share:
                setShare(share_title, share_des, ServerApi.PERSONAL_DETAILS_URL + "detail/" + _personal_id_value, share_imageUrl);
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

    // js通信接口
    public class JavascriptListner {

        //返回
        @JavascriptInterface
        public void toBackView() {   //提供给js调用的方法
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                XPreferencesUtils.put("isRefresh", true);
                finish();
            }
        }

        //返回并刷新数据
        @JavascriptInterface
        public void toBackAndRefresh() {   //提供给js调用的方法
            XPreferencesUtils.put("isRefresh", true);
            finish();
        }

        //分享
        @JavascriptInterface
        public void toShareView(String str) {   //提供给js调用的方法
            try {
                JSONObject json = new JSONObject(str);
                String title = json.optString("title", "");
                String des = json.optString("desc", "");
                String link = json.optString("link", "");
                String imgUrl = json.optString("imgUrl", "");
                setShare(title, des, link, imgUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分享
     */
    private void setShare(final String title, final String des, final String url, final String imagUrl) {

        mWxShare.showShareDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //好友
                getShare(0, title, des, url, imagUrl);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //朋友圈
                getShare(1, title, des, url, imagUrl);
            }
        });
    }

    private void getShare(int sceneFlag, String title, String des, String url, String imagUrl) {
        //"我是" + _name_value + " " + "邀请您与我共享千万营销人名片", XEmptyUtils.isSpace(X) ? "汇脉宝--共享流量，专注营销" : shareDes,
        //String des = "" + XPreferencesUtils.get("motto", "");

        ImageSize imageSize = new ImageSize(120, 120);
        Bitmap thumb;
        try {
            thumb = ImageLoader.getInstance().loadImageSync(imagUrl, imageSize);
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher);
        }

        mWxShare.WXShareWeb(title, XEmptyUtils.isSpace(des) ? "汇脉宝--共享流量，专注营销" : des, url, thumb, sceneFlag, new OnResponseListener() {
            @Override
            public void onSuccess() {
                XToast.normal("分享成功");
                HomeLogic.Instance(mActivity).getAddShareApi(_personal_id_value, "2", new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getPersonalDetailsData(_personal_id_value);
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });

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


    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    public static Uri getImageContentUri(Context context, String filePath) {//File imageFile
        //String filePath = imageFile.getAbsolutePath();//根据文件来获取路径
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (XEmptyUtils.isSpace(filePath)) {//imageFile.exists()判断文件存不存在
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 获取用户信息
     */
    private void getPersonalDetailsData(String id) {
        HomeLogic.Instance(mActivity).getPersonalWebDetailsApi(id, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    if (json.optString("status").equals("0")) {
                        JSONObject data = new JSONObject(json.optString("data"));
                        try {
                            JSONObject userData = new JSONObject(data.optString("user"));
                            _head_value = userData.optString("portrait", "");
                            _user_id_value = userData.optString("id", "");
                            _name_value = userData.optString("name", "");
                        } catch (Exception e) {
                            _user_id_value = data.optString("user_id", "");
                        }
                        is_clone = data.optString("is_clone", "");
                        is_interest = data.optBoolean("is_interest", false);
                        _clone_money_value = data.optString("clone_price", "");
                        _interest_num_value = data.optString("interest", "");
                        _share_num_value = data.optString("share", "");
                        share_des = data.optString("motto", "");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageLoaderManager.loadImage(_head_value, _head_iv, R.drawable.ic_launcher);
                                if (XPreferencesUtils.get("user_id", "").equals(_user_id_value)) {
                                    _clone_tv.setVisibility(View.GONE);
                                    is_interest = false;
                                } else {
                                    if (is_clone.equals("1")) {
                                        _clone_tv.setVisibility(View.VISIBLE);
                                        if (XStringUtils.m1(_clone_money_value) > 0) {
                                            _clone_tv.setText(_clone_money_value + "元克隆");
                                        } else {
                                            _clone_tv.setText("免费克隆");
                                        }
                                    } else {
                                        _clone_tv.setVisibility(View.GONE);
                                    }
                                }

                                if (is_interest) {
                                    _interest_tv.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.personal_web_details_interest_true_icon), null, null);
                                } else {
                                    _interest_tv.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.personal_web_details_interest_false_icon), null, null);
                                }

                                _interest_num.setText(_interest_num_value);
                                _share_num.setText(_share_num_value);

                                //setTopTitle(_name_value + "的网页");
                            }
                        });

                    } else {
                        showToast(json.optString("message"));
                    }
                } catch (Exception e) {
                    LogUtils.debug("home=s=" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("home=s=" + error);
            }
        });
    }


    /**
     * 获取克隆预支付信息
     */
    private void toClonePayData(String temp_id, final String money) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("temp_id", temp_id);
        HomeLogic.Instance(mActivity).toClonePayApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    final JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //final String data = json.getString("data");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mActivity, PayActivity.class);
                                intent.putExtra("vType", "克隆付费");
                                intent.putExtra("payType", "个人微网克隆");
                                intent.putExtra("payMoney", money);
                                intent.putExtra("payId", json.optJSONObject("data").optString("clone_id"));
                                startActivity(intent);
                                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                    } else {
                        showToast(message);
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

    /**
     * 感兴趣登记
     */
    private void getAddInterest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("target", _user_id_value);
        HomeLogic.Instance(mActivity).getAddInterestApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    final JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //final String data = json.getString("data");
                    if (status.equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("感兴趣成功");
                                is_interest = true;
                                _interest_tv.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.personal_web_details_interest_true_icon), null, null);
                            }
                        });
                    } else {
                        showToast("感兴趣失败," + message);
                    }
                } catch (Exception e) {
                    LogUtils.debug("home=s=" + e);
                    e.printStackTrace();
                    showToast("感兴趣失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("home=s=" + error);
                showToast("感兴趣失败");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isPaymentMoney", false)) {
            XPreferencesUtils.put("isPaymentMoney", false);
            startActivity(MessageWebActivity.class, "克隆", ServerApi.PERSONAL_CLONE_URL + _personal_id_value + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
            finish();
        }

        //名片是否收藏
        XPreferencesUtils.put("is_collect_card", false);

    }
}
