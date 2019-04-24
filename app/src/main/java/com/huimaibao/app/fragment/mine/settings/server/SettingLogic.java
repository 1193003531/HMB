package com.huimaibao.app.fragment.mine.settings.server;

import android.app.Activity;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.utils.XNetworkUtils;
import com.youth.xframe.utils.http.HttpCallBack;
import com.youth.xframe.utils.http.XHttp;
import com.youth.xframe.widget.XToast;

import java.util.HashMap;


/**
 * @Used 设置相关接口
 */
public class SettingLogic {
    private Activity mActivity;
    private static DialogUtils mDialogUtils;
    private static SettingLogic _Instance = null;

    public static SettingLogic Instance(Activity activity) {
        _Instance = new SettingLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private SettingLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 重置登陆密码
     */
    public void restLoginPWDApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.REST_LOGIN_PWD_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("重置中...");

                }

                @Override
                public void dismissProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }


    /**
     * 验证旧手机号
     */
    public void validateOldPhoneApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.VALIDATE_OLD_PHONE_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("验证中...");

                }

                @Override
                public void dismissProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 绑定新手机
     */
    public void changePhoneApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.CHANGE_PHONE_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("绑定中...");

                }

                @Override
                public void dismissProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });

        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 验证旧支付密码
     */
    public void validatePaymentPWDApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.VALIDATE_PAYMENT_PWD_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("验证中...");

                }

                @Override
                public void dismissProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 设置新支付密码
     */
    public void setPaymentPWDApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.SET_PAYMENT_PWD_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("设置中...");

                }

                @Override
                public void dismissProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 重置支付密码验证绑定手机号
     */
    public void validatePaymentSMSApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.VALIDATE_PAYMENT_SMS, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("验证中...");

                }

                @Override
                public void dismissProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 用户获取邀请人的电话号码
     */
    public void getInvitationPhoneApi(final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.INVITATION_PHONE_URL, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShow)
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 设置邀请人
     */
    public void setInvitationPhoneApi(String phone, final boolean isShow, final ResultBack resultBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.INVITATION_PHONE_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog("设置中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShow)
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }


}