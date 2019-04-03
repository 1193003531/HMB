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
 * @Used 反馈, 举报相关接口
 */
public class GeneralLogic {
    private Activity mActivity;
    private static DialogUtils mDialogUtils;
    private static GeneralLogic _Instance = null;

    public static GeneralLogic Instance(Activity activity) {
        _Instance = new GeneralLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private GeneralLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 举报
     */
    public void getComplaintApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.COMPLAINT_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("举报中...");

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
     * 反馈
     */
    public void getFeedBackApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.FEEDBACK_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("反馈中...");

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
     * 用户标签 get
     */
    public void getProfleLabelApi(final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.PROFLE_LABEL_URL, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("加载中...");
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
     * 用户标签 添加post
     */
    public void getProfleLabelAddApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.PROFLE_LABEL_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("保存中...");
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