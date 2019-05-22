package com.huimaibao.app.fragment.home.act;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.zxing.encoding.EncodingHandler;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.RoundedImagView;
import com.youth.xframe.widget.XHorizontalScrollView;
import com.youth.xframe.widget.XToast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成名片分享
 */
public class CardShareActivity extends BaseActivity {

    private String mType = "";

    private String _style_value = "";
    private LinearLayout mTopView, _share_ll;
    private View _TopView, _top_ll;
    private RoundedImagView _top_head;
    private ImageView _top_head_btn;
    private TextView _top_name, _top_jobs, _top_company, _top_phone, _top_wechat, _top_addr;

    private ImageView _share_qrcode;

    private WXShare mWxShare;
    private String imagePath = "", _head_value = "";


    private int[] imageData = {R.drawable.card_1_icon, R.drawable.card_2_icon,
            R.drawable.card_3_icon, R.drawable.card_4_icon, R.drawable.card_5_icon,
            R.drawable.card_6_icon, R.drawable.card_7_icon, R.drawable.card_8_icon,
            R.drawable.card_9_icon, R.drawable.card_10_icon, R.drawable.card_11_icon,
            R.drawable.card_12_icon, R.drawable.card_13_icon, R.drawable.card_14_icon};
    private List<Boolean> isCheckData = new ArrayList<>();
    private String _check_style = "style3", _check_head = "头像";

    private XHorizontalScrollView mXHorizontalScrollView;
    private LinearLayout mRadioGroup_content;


    //系统相册目录
    private String galleryPath = Environment.getExternalStorageDirectory()
            + File.separator + Environment.DIRECTORY_DCIM
            + File.separator + "HMB" + File.separator;
    //Camera
    // private String galleryPath = BaseApplication.getApp().getFilePath() + "cardimage/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_card_share);
        setNeedBackGesture(true);
//        Intent intent = getIntent();
//        if (intent != null) {
//            mType = intent.getStringExtra("vType");
//        }
//
//        setTopTitle(mType);
//        setTopLeft(true, true, false, "");
//        setTopRight(false, true, false, "", null);

        mWxShare = WXShare.Instance(mActivity);
        initView();
    }

    private void initView() {

        mXHorizontalScrollView = findViewById(R.id.making_card_hs);
        mRadioGroup_content = findViewById(R.id.making_card_hs_ll);

        mXHorizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 移动的起点
                        setNeedBackGesture(false);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 移动的终点距离
                        //mUpY = ev.getY();
                        setNeedBackGesture(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 移动的终点
                        setNeedBackGesture(true);
                        break;
                }
                return false;
            }
        });

        isCheckData = new ArrayList<>();
        for (int i = 0; i < imageData.length; i++) {
            if (i == 0) {
                isCheckData.add(true);
            } else {
                isCheckData.add(false);
            }
        }


        mTopView = findViewById(R.id.card_detail_top);
        _share_ll = findViewById(R.id.card_share_ll);
        _share_qrcode = findViewById(R.id.card_share_qrcode_iv);

        _style_value = "" + XPreferencesUtils.get("style", "style3");

        initTopView(R.layout.act_home_card_2_view, R.drawable.card_1_view);
        setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        setTopView();

        setCardList();
    }


    /**
     * 顶部名片
     */
    private void setTopView() {
        if (_check_style.equals("style4")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_2_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
            initCardSelect(1);
        } else if (_check_style.equals("style5")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_4_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
            initCardSelect(3);
        } else if (_check_style.equals("style6")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_3_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
            initCardSelect(2);
        } else if (_check_style.equals("style7")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_11_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.white);
            initCardSelect(10);
        } else if (_check_style.equals("style9")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_12_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
            initCardSelect(11);
        } else if (_check_style.equals("style10")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_5_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
            initCardSelect(4);
        } else if (_check_style.equals("style11")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_6_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
            initCardSelect(5);
        } else if (_check_style.equals("style12")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_9_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
            initCardSelect(8);
        } else if (_check_style.equals("style13")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_13_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
            initCardSelect(12);
        } else if (_check_style.equals("style14")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_10_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
            initCardSelect(9);
        } else if (_check_style.equals("style15")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_7_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
            initCardSelect(6);
        } else if (_check_style.equals("style16")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_14_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
            initCardSelect(13);
        } else if (_check_style.equals("style17")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_8_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
            initCardSelect(7);
        } else {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_1_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
            initCardSelect(0);
        }
    }

    /**
     * 切换顶部名片
     */
    private void setTopView(int position) {
        switch (position) {
            case 0:
                initTopView(R.layout.act_home_card_1_view, R.drawable.card_1_view);
                setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
                _check_style = "style3";
                break;
            case 1:
                initTopView(R.layout.act_home_card_1_view, R.drawable.card_2_view);
                setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
                _check_style = "style4";
                break;
            case 2:
                initTopView(R.layout.act_home_card_1_view, R.drawable.card_3_view);
                setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
                _check_style = "style6";
                break;
            case 3:
                initTopView(R.layout.act_home_card_1_view, R.drawable.card_4_view);
                setTopTextColor(R.color.white, R.color.white, R.color.white);
                _check_style = "style5";
                break;
            case 4:
                initTopView(R.layout.act_home_card_1_view, R.drawable.card_5_view);
                setTopTextColor(R.color.white, R.color.white, R.color.white);
                _check_style = "style10";
                break;
            case 5:
                initTopView(R.layout.act_home_card_1_view, R.drawable.card_6_view);
                setTopTextColor(R.color.white, R.color.white, R.color.white);
                _check_style = "style11";
                break;
            case 6:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_7_view);
                setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
                _check_style = "style15";
                break;
            case 7:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_8_view);
                setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
                _check_style = "style17";
                break;
            case 8:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_9_view);
                setTopTextColor(R.color.white, R.color.white, R.color.white);
                _check_style = "style12";
                break;
            case 9:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_10_view);
                setTopTextColor(R.color.white, R.color.white, R.color.white);
                _check_style = "style14";
                break;
            case 10:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_11_view);
                setTopTextColor(R.color.color000000, R.color.color333333, R.color.white);
                _check_style = "style7";
                break;
            case 11:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_12_view);
                setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
                _check_style = "style9";
                break;
            case 12:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_13_view);
                setTopTextColor(R.color.white, R.color.white, R.color.white);
                _check_style = "style13";
                break;
            case 13:
                initTopView(R.layout.act_home_card_2_view, R.drawable.card_14_view);
                setTopTextColor(R.color.white, R.color.white, R.color.white);
                _check_style = "style16";
                break;
        }

    }


    /**
     * 初始化顶部控件
     */
    private void initTopView(int resource, int id) {
        mTopView.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        _TopView = getLayoutInflater().inflate(resource, null);

        _top_ll = _TopView.findViewById(R.id.card_top_ll);
        _top_head = _TopView.findViewById(R.id.card_top_head);
        _top_head_btn = _TopView.findViewById(R.id.card_top_head_btn);
        _top_head_btn.setVisibility(View.GONE);
        _top_name = _TopView.findViewById(R.id.card_top_name);
        _top_jobs = _TopView.findViewById(R.id.card_top_jobs);
        _top_company = _TopView.findViewById(R.id.card_top_company);
        _top_phone = _TopView.findViewById(R.id.card_top_phone);
        _top_wechat = _TopView.findViewById(R.id.card_top_wechat);
        _top_addr = _TopView.findViewById(R.id.card_top_addr);

        mTopView.addView(_TopView, params);

        _top_ll.setBackground(getResources().getDrawable(id));

        setTopText();

    }

    private void setTopTextColor(int nid, int jid, int oid) {
        _top_name.setTextColor(getResources().getColor(nid));
        _top_jobs.setTextColor(getResources().getColor(jid));
        _top_company.setTextColor(getResources().getColor(oid));
        _top_phone.setTextColor(getResources().getColor(oid));
        _top_wechat.setTextColor(getResources().getColor(oid));
        _top_addr.setTextColor(getResources().getColor(oid));
    }

    private void setTopText() {
        _head_value = XEmptyUtils.isSpace(XPreferencesUtils.get("logo", "") + "") ? XPreferencesUtils.get("portrait", "") + "" : XPreferencesUtils.get("logo", "") + "";

        ImageLoaderManager.loadImage(_head_value, _top_head, R.drawable.ic_default);
        _top_name.setText("" + XPreferencesUtils.get("name", ""));
        _top_jobs.setText("" + XPreferencesUtils.get("profession", ""));
        _top_company.setText("" + XPreferencesUtils.get("company", ""));
        _top_phone.setText("电话：" + XPreferencesUtils.get("phone", ""));
        _top_wechat.setText(XEmptyUtils.isSpace(XPreferencesUtils.get("wechat", "") + "") ? "" : "微信：" + XPreferencesUtils.get("wechat", ""));
        _top_addr.setText(XEmptyUtils.isSpace(XPreferencesUtils.get("address_detail", "") + "") ? "" : "地址：" + XPreferencesUtils.get("address_detail", ""));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //BitmapFactory.decodeResource(getResources(), R.mipmap.ic_default)
                //"" + ((int)(Math.random()*90000)+100000)
                _share_qrcode.setImageBitmap(EncodingHandler.createQRCode(ServerApi.CARD_URL + XPreferencesUtils.get("user_id", ""), 100, 100, ((BitmapDrawable) _top_head.getDrawable()).getBitmap()));
            }
        }, 200);

    }


    /**
     * 横向名片模板
     */
    private void setCardList() {
        mRadioGroup_content.removeAllViews();
        mXHorizontalScrollView.setParam(mActivity, XDensityUtils.getScreenWidth(), mRadioGroup_content, null, null, null, null);
        for (int i = 0; i < imageData.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;

            View v = LayoutInflater.from(mActivity).inflate(R.layout.act_home_card_item, null);

            ImageView imageView = v.findViewById(R.id.card_image_value);
            ImageView imageTrue = v.findViewById(R.id.card_image_true);
            imageView.setImageResource(imageData[i]);
            imageView.setId(i);
            imageTrue.setId(i);
            v.setId(i);
            if (isCheckData.get(i)) {
                imageTrue.setVisibility(View.VISIBLE);
            } else {
                imageTrue.setVisibility(View.INVISIBLE);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTopView(v.getId());
                    cardSelect(v.getId());
                }
            });
            mRadioGroup_content.addView(v, i, params);
        }

    }

    /**
     * 名片选中
     */
    private void cardSelect(int position) {
        isCheckData = new ArrayList<>();
        for (int i = 0; i < imageData.length; i++) {
            if (i == position) {
                isCheckData.add(true);
            } else {
                isCheckData.add(false);
            }
        }
        setCardList();

    }

    /**
     * 初始化横向名片移动
     */
    private void initCardSelect(int position) {
        isCheckData = new ArrayList<>();
        for (int i = 0; i < imageData.length; i++) {
            if (i == position) {
                isCheckData.add(true);
            } else {
                isCheckData.add(false);
            }
        }
        setCardList();

        for (int i = 0; i < imageData.length; i++) {
            if (i == position) {
                //偏移的距离
                int offset = 192 * position;
                //让水平的滚动视图按照执行的x的偏移量进行移动
                mXHorizontalScrollView.smoothScrollTo(offset, 0);
                break;
            }

        }

    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.share_btn:
                setShare();
                break;
            case R.id.card_share_save_btn:
                //_share_image.setImageBitmap(convertViewToBitmap(_share_ll));
                if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本

                    XPermission.requestPermissions(mActivity, 1006, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, new XPermission.OnPermissionListener() {
                        //权限申请成功时调用
                        @Override
                        public void onPermissionGranted() {
                            saveWebImage(convertViewToBitmap2(_share_ll));
                        }

                        //权限被用户禁止时调用
                        @Override
                        public void onPermissionDenied() {
                            //给出友好提示，并且提示启动当前应用设置页面打开权限
                            XPermission.showTipsDialog(mActivity);
                        }
                    });
                } else {
                    saveWebImage(convertViewToBitmap2(_share_ll));
                }

                break;
        }
    }


    /**
     * 对View进行量测，布局后截图
     *
     * @param view
     * @return
     */
    public Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = null;
        try {
            bitmap = view.getDrawingCache();
        } catch (Exception e) {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = view.getDrawingCache();
            }
        }
        return bitmap;
    }

    public Bitmap convertViewToBitmap2(View view) {
        Bitmap shareBitmap = null;
        try {
            shareBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                    view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            if (shareBitmap != null) {
                shareBitmap.recycle();
                shareBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                        view.getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);
            }
        }
        Canvas c = new Canvas(shareBitmap);
        view.draw(c);
        return shareBitmap;
    }

    /**
     * 保存图片到手机相册
     */
    public void saveWebImage(Bitmap bmp) {
        if (bmp == null) {
            XToast.normal("保存失败");
            //initView();
            return;
        }

        //BaseApplication.getApp().getFilePath() + "images/"
        // 首先保存图片
        File appDir = new File(galleryPath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        try {
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(galleryPath, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //imagePath = BaseApplication.getApp().getFilePath() + "images/" + fileName;
            XToast.normal("保存成功");
            fos.flush();
            fos.close();
            //通知相册更新
//            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
//                    bmp, fileName, null);

            // 其次把文件插入到系统图库
            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), file.getAbsolutePath(), fileName, null);
            // 最后通知图库更新
            mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));


        } catch (Exception e) {
            e.printStackTrace();
            XToast.normal("保存失败");
        } finally {
            if (bmp != null) {
                bmp.recycle();
            }
        }
        //initView();

    }

    /**
     * 分享
     */
    private void setShare() {
        mWxShare.showShareDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //好友
                getShare(0);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //朋友圈
                getShare(1);
            }
        });
    }

    private void getShare(int sceneFlag) {
        Bitmap bm = convertViewToBitmap2(_share_ll);
        //initView();
        mWxShare.WXShareImage(bm, imagePath, 200, 200, sceneFlag, new OnResponseListener() {
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
        if (bm != null)
            bm.recycle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWxShare.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWxShare.unregister();
    }

}
