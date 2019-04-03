package com.huimaibao.app.fragment.electcircle.server;

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
 * @Used 互推圈相关接口
 */
public class ElectLogic {
    private Activity mActivity;
    private static DialogUtils mDialogUtils;
    private static ElectLogic _Instance = null;

    public static ElectLogic Instance(Activity activity) {
        _Instance = new ElectLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private ElectLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 获取互推列表
     */
    public void getUsersApi(final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.ELECT_USER_URL, null, new HttpCallBack() {
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
     * 获取登录用户互推信息
     */
    public void getElectUserApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.ELECT_GET_USER_URL, map, new HttpCallBack() {
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
     * 点击获取互推用户信息
     */
    public void getElectPerApi(String id, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.ELECT_INFO_URL + id, null, new HttpCallBack() {
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
     * 生成互推记录
     */
    public void getElectAddApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.ELECT_ADD_URL, map, new HttpCallBack() {
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
     * 设置素材
     */
    public void getMaterialApi(HashMap<String, Object> map, final boolean isShow, final String msg, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().put(ServerApi.ELECT_MATERIAL_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog(msg);
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
     * 分享记录，分享任务get
     */
    public void getShareApi(HashMap<String, Object> map, final boolean isShow, final String msg, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.ELECT_SHARE_LIST_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog(msg);
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
     * 分享被动匹配put
     */
    public void getPassiveShareApi(String id, final boolean isShow, final String msg, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().put(ServerApi.ELECT_PASSIVE_SHARE_URL + id, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog(msg);
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
     * 消息提醒
     */
    public void getShareMessageApi(HashMap<String, Object> map, final boolean isShow, final String msg, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.ELECT_MES_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog(msg);
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