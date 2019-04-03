package com.huimaibao.app.fragment.home.server;

import android.app.Activity;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XNetworkUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.http.HttpCallBack;
import com.youth.xframe.utils.http.XHttp;
import com.youth.xframe.widget.XToast;

import java.util.HashMap;


/**
 * @Used 名片相关接口
 */
public class CardLogic {
    private Activity mActivity;
    public static DialogUtils mDialogUtils;
    private static CardLogic _Instance = null;

    public static CardLogic Instance(Activity activity) {
        _Instance = new CardLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private CardLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 根据id查询用户名片信息
     */
    public void getCardForIdApi(final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("user_id", XPreferencesUtils.get("user_id", ""));
            XHttp.obtain().get(ServerApi.GET_CARD_FOR_ID_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    try {
                        org.json.JSONObject json = new org.json.JSONObject(o.toString());
                        LogUtils.debug("card:" + json);
                        final String msg = json.optString("message");
                        if (json.getString("status").equals("0")) {
                            final org.json.JSONObject jsonD = new org.json.JSONObject(json.optString("data"));
                            XPreferencesUtils.put("card_id", jsonD.optString("card_id"));//名片id
                            XPreferencesUtils.put("has_holder", jsonD.optString("has_holder"));//是否收藏改名片
                            XPreferencesUtils.put("phone", jsonD.optString("phone", ""));//手机号
                            XPreferencesUtils.put("name", jsonD.optString("name", ""));//名称
                            XPreferencesUtils.put("portrait", jsonD.optString("portrait", ""));//头像
                            XPreferencesUtils.put("logo", jsonD.optString("logo", ""));//头像
                            XPreferencesUtils.put("industry", jsonD.optString("industry", ""));//行业
                            XPreferencesUtils.put("profession", jsonD.optString("profession", ""));//职业
                            XPreferencesUtils.put("company", jsonD.optString("company", ""));//公司
                            XPreferencesUtils.put("wechat", jsonD.optString("wechat", ""));//
                            XPreferencesUtils.put("website", jsonD.optString("website", ""));//
                            XPreferencesUtils.put("address", jsonD.optString("address", ""));//
                            XPreferencesUtils.put("address_detail", jsonD.optString("address_detail", ""));//
                            XPreferencesUtils.put("self_introduction", jsonD.optString("self_introduction", ""));//是否制作营销网页
                            XPreferencesUtils.put("open_phone", jsonD.optString("open_phone", ""));//是否公开手机号
                            XPreferencesUtils.put("qq", jsonD.optString("qq", ""));
                            XPreferencesUtils.put("email", jsonD.optString("email", ""));
                            XPreferencesUtils.put("introduce", jsonD.optString("introduce", ""));//个人介绍
                            XPreferencesUtils.put("style", jsonD.optString("style", "style3"));//样式
                            XPreferencesUtils.put("album", jsonD.optString("album", ""));//相册
                            XPreferencesUtils.put("motto", jsonD.optString("motto", ""));//广告语
                            XPreferencesUtils.put("birthday", jsonD.optString("birthday", ""));//生日
                            XPreferencesUtils.put("sex", jsonD.optString("sex", ""));//性别
                            XPreferencesUtils.put("province", jsonD.optString("province", ""));//省id
                            XPreferencesUtils.put("city", jsonD.optString("city", ""));//市id
                            XPreferencesUtils.put("area", jsonD.optString("area", ""));//区id
                            if (resultBack != null)
                                resultBack.onSuccess(o);
                        } else {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    XToast.normal(msg);
                                }
                            });
                        }
                    } catch (Exception e) {
                        if (resultBack != null)
                            resultBack.onFailed(e.toString());
                    }
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
     * 根据id查询用户名片信息
     */
    public void getCardClipApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.GET_CARD_FOR_ID_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("加载中...");
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


}