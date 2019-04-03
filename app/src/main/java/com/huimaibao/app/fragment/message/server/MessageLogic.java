package com.huimaibao.app.fragment.message.server;

import android.app.Activity;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.utils.XNetworkUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.http.HttpCallBack;
import com.youth.xframe.utils.http.XHttp;
import com.youth.xframe.widget.XToast;

import java.util.HashMap;


/**
 * @Used 信息相关接口
 */
public class MessageLogic {
    private Activity mActivity;
    public static DialogUtils mDialogUtils;
    private static MessageLogic _Instance = null;

    public static MessageLogic Instance(Activity activity) {
        _Instance = new MessageLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private MessageLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 对我感兴趣
     */
    public void getInterestedApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.MSG_INTERESTED_URL, map, new HttpCallBack() {
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
     * 人气
     */
    public void getPopularityApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.MSG_POPULARITY_URL, map, new HttpCallBack() {
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
     * 获取推送消息
     */
    public void getPushApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.MSG_PUSH_URL, map, new HttpCallBack() {
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
     * 获取小脉宝消息
     */
    public void getXMBApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.MSG_XMB_URL, map, new HttpCallBack() {
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
     * 获取用户消息
     */
    public void getGroupUserApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.MSG_GROUP_USER_URL, map, new HttpCallBack() {
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
     * 获取某个用户消息
     */
    public void getUserApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.MSG_GET_USER_URL, map, new HttpCallBack() {
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
     * 删除某个用户消息
     */
    public void getDelUserApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.MSG_DEL_USER_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("删除中...");
                }

                @Override
                public void dismissProgress() {
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
     * 是否同意员工邀请
     */
    public void getStaffApi(HashMap<String, Object> map, final String msg, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.MSG_INVITE_STAFF_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog(msg);
                }

                @Override
                public void dismissProgress() {
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