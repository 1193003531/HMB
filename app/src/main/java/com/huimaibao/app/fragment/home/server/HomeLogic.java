package com.huimaibao.app.fragment.home.server;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XNetworkUtils;
import com.youth.xframe.utils.http.HttpCallBack;
import com.youth.xframe.utils.http.XHttp;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


/**
 * @Used 首页界面相关接口
 */
public class HomeLogic {
    private Activity mActivity;
    public static DialogUtils mDialogUtils;
    private static HomeLogic _Instance = null;

    public static HomeLogic Instance(Activity activity) {
        _Instance = new HomeLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private HomeLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * banner
     */
    public void homeBannerApi(final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {

            XHttp.obtain().get(ServerApi.HOME_BANNER_URL, null, new HttpCallBack() {
                @Override
                public void showProgress() {
//                    if (!mActivity.isFinishing())
//                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
//                    if (!mActivity.isFinishing())
//                        mDialogUtils.dismissDialog();

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
     * 营销榜单-去克隆
     */
    public void homeMarketApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.HOME_MARKETING_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 克隆预支付
     */
    public void toClonePayApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.TO_CLONE_PAY_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");
                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 收入
     */
    public void homeIncomeApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.HOME_INCOME_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 自己的营销网页
     */
    public void userMarketingApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.USER_MARKETING_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 删除自己的营销网页
     */
    public void userMarketingDelApi(String id, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().delete(ServerApi.USER_MARKETING_DEL_URL + id, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("删除中...");
                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 系统模板
     */
    public void getTemplatesApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.TEMPLATES_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 自己的个人微网
     */
    public void userPersonalApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.USER_PERSONAL_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 用户个人微网详情
     */
    public void getPersonalWebDetailsApi(String id, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.USER_PERSONAL_WEB_DETAILS_URL + id, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");
                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 感兴趣登记
     */
    public void getAddInterestApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.USER_ADD_INTEREST_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("添加中...");
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
     * 删除个人网页
     */
    public void userTempDelApi(String id, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().delete(ServerApi.USER_AMEND_URL + id, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("删除中...");
                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 修改个人网页
     */
    public void userAmendApi(HashMap<String, Object> map, String id, final boolean isShowD, final String msg, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().put(ServerApi.USER_AMEND_URL + id, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog(msg);
                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 设置某个用户模板为默认
     */
    public void userTempDefApi(String id, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.USER_TEMP_DEF_URL + id, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("设置中...");
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
     * 获取用户的营销网页 get
     */
    public void getPMarketApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.PERSONAL_MARKETING_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");
                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 获取用户的个人微网 get
     */
    public void getPPersonalApi(HashMap<String, Object> map, final boolean isShowD, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.PERSONAL_PERSONAL_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowD)
                        mDialogUtils.showLoadingDialog("加载中...");
                }

                @Override
                public void dismissProgress() {
                    if (isShowD)
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
     * 分享统计
     * type 1文章2个人网页3名片4营销
     */
    public void getAddShareApi(String id, String type, final ResultBack resultBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.APP_SHARE_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    //mDialogUtils.showLoadingDialog("加载中...");
                }

                @Override
                public void dismissProgress() {
                    //mDialogUtils.dismissDialog();
                }

                @Override
                public void onSuccess(Object o) {
                    if (resultBack != null)
                        resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    if (resultBack != null)
                        resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 领取1元奖励
     */
    public void getReceiveAwardApi(final ResultBack resultBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("amount", "1");
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.RECEIVE_AWARD_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("领取中...");
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
     * 修改个人广告语
     */
    public void getUpdateMottoApi(String motto, final ResultBack resultBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("motto", motto);
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.UPDATE_MOTTO_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    mDialogUtils.showLoadingDialog("设置中...");
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