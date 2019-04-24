package com.huimaibao.app.fragment.mine.act;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.appload.AppDownloadManager;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.fragment.mine.settings.AboutUsActivity;
import com.huimaibao.app.fragment.mine.settings.AccountActivity;
import com.huimaibao.app.fragment.mine.settings.InviterActivity;
import com.huimaibao.app.fragment.mine.settings.PaymentActivity;
import com.huimaibao.app.fragment.mine.settings.UserAgreementActivity;
import com.huimaibao.app.fragment.mine.settings.VerifyPhoneActivity;
import com.huimaibao.app.fragment.mine.settings.server.SettingLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.LoginActivity;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.huimaibao.app.zxing.act.CaptureActivity;
import com.huimaibao.app.zxing.util.Constant;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XAppUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XFileUtils;
import com.youth.xframe.utils.XPreferencesUtils;

import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

/**
 * 设置
 */
public class SetActivity extends BaseActivity {

    private String mType = "";
    //缓存，版本，邀请人
    private String _inviter_phone_value = "";
    private LinearLayout _inviter_ll;
    private TextView _cache_value, _version_tv, _inviter_tv;
    private ImageView _version_new_iv, _inviter_iv;
    private DialogUtils mDialogUtils;
    private AppDownloadManager mDownloadManager;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        setShowLine(false);

        mDialogUtils = new DialogUtils(mActivity);

        _cache_value = findViewById(R.id.setting_clear_cache_value);
        _version_new_iv = findViewById(R.id.setting_about_hmb_new_value);
        _version_tv = findViewById(R.id.setting_about_hmb_value);
        _inviter_ll = findViewById(R.id.setting_inviter_btn);
        _inviter_tv = findViewById(R.id.setting_inviter_phone_value);
        _inviter_iv = findViewById(R.id.setting_inviter_iv);

        _version_tv.setText("版本 " + XAppUtils.getVersionName(mActivity));
        _cache_value.setText(XFileUtils.getFilesSize(BaseApplication.getApp().getFilePath()));

        if (XStringUtils.m1(XPreferencesUtils.get("version", "" + XAppUtils.getVersionCode(mActivity)).toString().replace(".", "").trim()) > XAppUtils.getVersionCode(mActivity)) {
            _version_new_iv.setVisibility(View.VISIBLE);
            mDownloadManager = new AppDownloadManager(mActivity);
        } else {
            _version_new_iv.setVisibility(View.GONE);
        }

//        _inviter_phone_value = XPreferencesUtils.get("inviter_phone", "").toString().trim();
//        if (XEmptyUtils.isSpace(_inviter_phone_value)) {
        getInviterPhone();
        //}

    }


    public void onAction(View v) {
        //账号管理,支付管理,用户协议,关于我们,退出

        switch (v.getId()) {
            case R.id.setting_account_btn:
                startActivity(AccountActivity.class, "账号管理");
                break;
            case R.id.setting_payment_btn:
                if ((boolean) XPreferencesUtils.get("payment_pwd", false)) {
                    startActivity(PaymentActivity.class, "支付管理");
                } else {
                    mDialogUtils.showNoTitleDialog("您还没有设置支付密码,马上去设置?", "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(VerifyPhoneActivity.class, "设置支付密码");
                            mDialogUtils.dismissDialog();
                        }
                    });
                }
                break;
            case R.id.setting_user_btn:
                startActivity(UserAgreementActivity.class, "用户协议");
                break;
            case R.id.setting_about_us_btn:
                startActivity(AboutUsActivity.class, "联系我们");
                break;
            case R.id.setting_inviter_btn:
                startActivity(InviterActivity.class, "邀请人");
                break;
            //关于汇脉宝
            case R.id.setting_about_hmb_btn:
                upDataAPP();
                break;
            //清除缓存
            case R.id.setting_clear_cache_btn:
                mDialogUtils.showNoTitleDialog("要清除缓存吗?", "取消", "清除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本

                            XPermission.requestPermissions(mActivity, 1006, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, new XPermission.OnPermissionListener() {
                                //权限申请成功时调用
                                @Override
                                public void onPermissionGranted() {
                                    if (XFileUtils.delAllFile(BaseApplication.getApp().getFilePath())) {
                                        _cache_value.setText(XFileUtils.getFilesSize(BaseApplication.getApp().getFilePath()));
                                    }
                                }

                                //权限被用户禁止时调用
                                @Override
                                public void onPermissionDenied() {
                                    //给出友好提示，并且提示启动当前应用设置页面打开权限
                                    XPermission.showTipsDialog(mActivity);
                                }
                            });
                        } else {
                            if (XFileUtils.delAllFile(BaseApplication.getApp().getFilePath())) {
                                _cache_value.setText(XFileUtils.getFilesSize(BaseApplication.getApp().getFilePath()));
                            }
                        }


                        mDialogUtils.dismissDialog();
                    }
                });
                break;
            /**退出*/
            case R.id.setting_exit_btn:
                XPreferencesUtils.put("token", "");
                XPreferencesUtils.put("isLoginOut", true);
                //startActivity(LoginActivity.class, "");
                finish();
                XActivityStack.getInstance().finishiAll();
                startActivity(LoginActivity.class, "");
                break;
        }
    }


    /**
     * 更新
     * "汇脉宝APP版本更新"
     * "http://gdown.baidu.com/data/wisegame/9db3f36158334e25/baiduwangpan_517.apk"
     */
    private void upDataAPP() {

        LoginLogic.Instance(mActivity).appVersionApi("检查更新...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("app=s=" + json);
                    String status = json.optString("status");
                    //String message = json.getString("message");
                    String data = json.optString("data");
                    if (status.equals("0")) {
                        JSONObject jsonD = new JSONObject(data);
                        if (jsonD.optString("type").equals("1")) {//1.android,2.ios
                            if (jsonD.optString("is_allow").equals("1")) {//是否生效:1.是,2.否
//                                try {
//                                    int number = Integer.parseInt(jsonD.getString("number").replace(".", "").trim());
//                                    XLog.d("number" + number);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                                XPreferencesUtils.put("version", jsonD.optString("number", "" + XAppUtils.getVersionCode(mActivity)).replace(".", "").trim());
                                if (XStringUtils.m1(jsonD.optString("number").replace(".", "").trim()) > XAppUtils.getVersionCode(mActivity)) {
                                    if (jsonD.optString("force").equals("1")) {//是否强制更新：1 是， 0否
                                        //jsonD.getString("url")
                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), true);
                                    } else {
                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), false);
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            XToast.normal("该版本已是最新版");
                                        }
                                    });
                                }

//                                if (!jsonD.optString("number","1.0.0").equals(XAppUtils.getVersionName(mActivity))) {
//                                    XPreferencesUtils.put("version", jsonD.optString("number"));
//                                    if (jsonD.optString("force").equals("1")) {//是否强制更新：1 是， 0否
//
//                                        //jsonD.getString("url")
//                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), true);
//                                    } else {
//                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), false);
//                                    }
//                                } else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            XToast.normal("该版本已是最新版");
//                                        }
//                                    });
//                                }

                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    XToast.normal("该版本已是最新版");
                                }
                            });
                        }

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                XToast.normal("该版本已是最新版");
                            }
                        });
                    }
                } catch (Exception e) {
                    //XLog.d("e:" + e);
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            XToast.normal("该版本已是最新版");
                        }
                    });
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        XToast.normal("该版本已是最新版");
                    }
                });

            }
        });

    }

    /**
     * 显示弹出框
     */
    private void showAppDialog(final String version, final String des, final String url, final boolean isfocrce) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // UpdateAPPUtils.Instance(mActivity).checkUpdateInfo(version, des, url, isfocrce);
                mDialogUtils.showAppDialog(version, des, isfocrce, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // mDownloadManager = new AppDownloadManager(mActivity);
                        if (mDownloadManager == null) {
                            mDownloadManager = new AppDownloadManager(mActivity);
                        }
                        mDownloadManager.downloadApk(url, "汇脉宝", "版本更新");
                        ToastUtils.showCenter("已在通知栏下载更新");
                        mDialogUtils.dismissDialog();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDownloadManager != null) {
            mDownloadManager.resume();
        }

        if (!XEmptyUtils.isSpace(XPreferencesUtils.get("inviter_phone", "").toString().trim())) {
            _inviter_tv.setText(XPreferencesUtils.get("inviter_phone", "") + "");
            _inviter_iv.setVisibility(View.GONE);
            _inviter_ll.setEnabled(false);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDownloadManager != null) {
            mDownloadManager.onPause();
        }
    }

    /**
     * 获取邀请人
     */
    private void getInviterPhone() {
        SettingLogic.Instance(mActivity).getInvitationPhoneApi(true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.optString("status");
                    //String message = json.getString("message");
                    if (status.equals("0")) {
                        JSONObject data = new JSONObject(json.optString("data", ""));
                        final String phone = data.optString("phone", "").trim();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (XEmptyUtils.isSpace(phone) || phone.length() > 5) {
                                    XPreferencesUtils.put("inviter_phone", "");
                                    _inviter_tv.setText("请填写邀请人电话号码");
                                    _inviter_iv.setVisibility(View.VISIBLE);
                                    _inviter_ll.setEnabled(true);
                                } else {
                                    XPreferencesUtils.put("inviter_phone", phone);
                                    _inviter_tv.setText(phone);
                                    _inviter_iv.setVisibility(View.GONE);
                                    _inviter_ll.setEnabled(false);
                                }
                            }
                        });

                    } else {

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }


}
