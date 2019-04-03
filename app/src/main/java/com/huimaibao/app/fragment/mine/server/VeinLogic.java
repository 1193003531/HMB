package com.huimaibao.app.fragment.mine.server;

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
 * @Used 脉宝相关接口
 */
public class VeinLogic {
    private Activity mActivity;
    private static DialogUtils mDialogUtils;
    private static VeinLogic _Instance = null;

    public static VeinLogic Instance(Activity activity) {
        _Instance = new VeinLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private VeinLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 脉宝余额
     */
    public void veinWalletApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.VEIN_WALLET_URL, map, new HttpCallBack() {
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
     * 脉宝收入
     */
    public void veinIncomeApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.VEIN_INCOME_URL, map, new HttpCallBack() {
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
     * 脉宝支出
     */
    public void veinExpenditureApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.VEIN_EXPENDITURE_URL, map, new HttpCallBack() {
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
     * 可转账朋友
     */
    public void veinFriendApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.VEIN_FRIEND_URL, map, new HttpCallBack() {
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
     * 给朋友赠送
     */
    public void veinGiveApi(HashMap<String, Object> map, String id, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.VEIN_GIVE_URL + id, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog("赠送中...");

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
     * 充值
     */
    public void veinRechargeApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.VEIN_RECHARGE_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog("充值中...");

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
     * 提醒对方充值vip
     */
    public void veinRemindApi(HashMap<String, Object> map, String id, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.VEIN_REMIND_URL + id, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog("充值中...");

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