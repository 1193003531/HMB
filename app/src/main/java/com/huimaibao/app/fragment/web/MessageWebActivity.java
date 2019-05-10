package com.huimaibao.app.fragment.web;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.act.LibraryActivity;
import com.huimaibao.app.fragment.home.act.MakingCardActivity;
import com.huimaibao.app.fragment.home.act.PersonalWebActivity;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.ToastUtils;
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
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

public class MessageWebActivity extends BaseActivity {

    private Context mContext;
    private WebView mWebView;
    private ProgressBar progressBar = null;

    private WXShare mWxShare;

    private String mType = "", mUrl = "";


    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_message_web);
        setNeedBackGesture(false);
        mContext = this;
        mWxShare = WXShare.Instance(mActivity);

        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
            mUrl = intent.getStringExtra("vUrl");
        }

        if (mType.equals("邀请好友") || mType.equals("招商合作") || mType.equals("营销攻略") || mType.equals("赚钱攻略") || mType.equals("产品价值")) {
            setShowTitle(true);
        } else {
            setShowTitle(false);
        }
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        init();
    }


    public void init() {
        mWebView = findViewById(R.id.message_web_value);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setDomStorageEnabled(true);//设置适应Html5 //重点是这个设置
//Android WebView从Lollipop开始默认不允许混合模式，https当中不能加载http资源，需要设置开启
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
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
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
                final String title = json.optString("title", "");
                final String des = json.optString("desc", "");
                final String link = json.optString("link", "");
                final String imgUrl = json.optString("imgUrl", "");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setShare(title, des, link, imgUrl);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 营销攻略
         */
        //完善名片
        @JavascriptInterface
        public void toCardView() {   //提供给js调用的方法
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(MakingCardActivity.class, "完善名片");
                    //XPreferencesUtils.put("isStrategy", true);
                    // finish();
                }
            });
        }

        /**
         * 赚钱攻略,营销攻略
         */
        //个人微网(去制作)
        @JavascriptInterface
        public void toPersonalView() {   //提供给js调用的方法
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(PersonalWebActivity.class, "个人微网");
                    XPreferencesUtils.put("isStrategy", true);
                    finish();
                }
            });
        }

        //文章(文库)
        @JavascriptInterface
        public void toArticleView() {   //提供给js调用的方法
            startActivity(LibraryActivity.class, "文库");
            XPreferencesUtils.put("isStrategy", true);
            finish();
        }

        //互推圈
        @JavascriptInterface
        public void toElectView() {   //提供给js调用的方法
            XPreferencesUtils.put("isStrategyElect", true);
            XPreferencesUtils.put("isStrategy", true);
            finish();
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

    /**
     * 选择文件类
     */
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
        if (XEmptyUtils.isEmpty(results)) {
            ToastUtils.showCenter("选择图片失败，请重新选择");
        }
    }


//用于自己写的选择图片返回onActivityForResult()方法回传的是一个放置选中图片绝对路径的List<String> path=new List();在5.0及以上系统有些选择图片之后不能显
//示
//    public static Uri getImageContentUri(Context context, String filePath) {//File imageFile
//        //String filePath = imageFile.getAbsolutePath();//根据文件来获取路径
//        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
//                new String[]{filePath}, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
//            Uri baseUri = Uri.parse("content://media/external/images/media");
//            return Uri.withAppendedPath(baseUri, "" + id);
//        } else {
//            if (XEmptyUtils.isSpace(filePath)) {//imageFile.exists()判断文件存不存在
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.DATA, filePath);
//                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            } else {
//                return null;
//            }
//        }
//    }

}
