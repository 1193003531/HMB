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
 * @Used 营销奖励相关接口
 */
public class MarkatRewardLogic {
    private Activity mActivity;
    private static DialogUtils mDialogUtils;
    private static MarkatRewardLogic _Instance = null;

    public static MarkatRewardLogic Instance(Activity activity) {
        _Instance = new MarkatRewardLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private MarkatRewardLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 营销奖励奖励金详情
     */
    public void marketingRewardApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.MARKETING_REWARD_URL, map, new HttpCallBack() {
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
     * 营销奖励奖励金转入余额
     */
    public void marketingRewardToWalletApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.MARKETING_REWARD_WALLET_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("转入中...");
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