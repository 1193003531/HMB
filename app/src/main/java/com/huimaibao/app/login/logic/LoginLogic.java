package com.huimaibao.app.login.logic;

import android.app.Activity;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XNetworkUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.utils.http.HttpCallBack;
import com.youth.xframe.utils.http.XHttp;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * @Used 登录界面相关接口
 */
public class LoginLogic {
    private Activity mActivity;
    public static DialogUtils mDialogUtils;
    private static LoginLogic _Instance = null;

    public static LoginLogic Instance(Activity activity) {
        _Instance = new LoginLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private LoginLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 刷新token
     */
    public void refreshTokenApi() {
        if (XNetworkUtils.isConnected()) {

            XHttp.obtain().post(ServerApi.REFRESH_TOKEN_URL, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                }

                @Override
                public void dismissProgress() {

                }

                @Override
                public void onSuccess(Object o) {
                    try {
                        //{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGkuNTFodWltYWliYW8uY25cL2Zyb250XC9yZWZyZXNoIiwiaWF0IjoxNTQ3ODExNjM1LCJleHAiOjE1NTA0MDM4MjcsIm5iZiI6MTU0NzgxMTgyNywianRpIjoiSWo5UlA0QngyQVV4cjhjcSIsInN1YiI6Nzc4LCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.3iFuE7ysD0MbflwB_ffxQbWE_M97vweDtGldZRk8lmw","expire":"2592000"}
                        //
                        JSONObject json = new JSONObject(o.toString());
                        LogUtils.debug("refreshTokenApi:" + json);

                        if (!XEmptyUtils.isSpace(json.optString("token", ""))) {
                            //final JSONObject jsonD = new JSONObject(json.optString("data"));
                            XPreferencesUtils.put("token", json.optString("token", ""));
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailed(String error) {
                    LogUtils.debug("refreshTokenApi:" + error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 登录
     */
    public void LoginApi(String url, HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {

            XHttp.obtain().post(url, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("登录中...");

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
     * 微信登录
     */
    public void LoginWeChatApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.LOGIN_WECHAT_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("登录中...");

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
     * 短信验证码
     * type -0其他1登录2注册3改密4换绑5设置支付密码
     */
    public void LoginSendSMSApi(String phone, String type, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("phone_number", phone);
            map.put("type", type);
            XHttp.obtain().post(ServerApi.SEND_SMS_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("发送中...");

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
     * 返回签名短信验证
     */
    public void LoginValidateSMSApi(String phone, String code, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("phone", phone);
            map.put("code", code);
            XHttp.obtain().post(ServerApi.VALIDATE_SMS_URL, map, new HttpCallBack() {
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
     * 设置密码
     */
    public void LoginRestPWDApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.REST_PWD_URL, map, new HttpCallBack() {
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
     * 微信获取userToken
     */
    public void LoginWXApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.GetCodeRequest, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("登录中...");

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
     * 微信获取个人信息
     */
    public void getUserInfoWXApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.GetUserInfo, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    //if (!mActivity.isFinishing())
                    //   mDialogUtils.showLoadingDialog("登录中...");

                }

                @Override
                public void dismissProgress() {
                    // if (!mActivity.isFinishing())
                    //    mDialogUtils.dismissDialog();

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
     * 注册
     */
    public void LoginRegisterApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.REGISTER_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("注册中...");

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
     * 绑定手机号
     */
    public void LoginBindApi(HashMap<String, Object> map, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.BIND_PHONE_URL, map, new HttpCallBack() {
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
     * 判断是否存在手机号
     */
    public void LoginPhoneExistApi(String phone, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("phone", phone);
            XHttp.obtain().post(ServerApi.PHONE_EXIST_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog("发送中...");

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
     * 获取用户信息
     * get
     */
    public void getUserInfoApi(String id, final boolean isShowLoad, final ResultBack resultBack) {
        //http://wthrcdn.etouch.cn/weather_mini?citykey=101010100
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.GET_USERINFO_URL, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowLoad)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShowLoad)
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    if (o.equals("401")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialogUtils.showNoTokenDialog();
                                return;
                            }
                        });

                    }
                    try {
                        JSONObject json = new JSONObject(o.toString());
                        LogUtils.debug("mine:" + json);
                        if (json.getString("status").equals("0")) {
                            final JSONObject jsonD = new JSONObject(json.optString("data"));
                            XPreferencesUtils.put("user_id", jsonD.optString("id", "")); //用户id
                            XPreferencesUtils.put("card_id", jsonD.optString("card_id", ""));//名片id
                            XPreferencesUtils.put("vip_level", jsonD.optString("vip_level", "0"));//会员等级
                            XPreferencesUtils.put("card_num", jsonD.optString("card_num", "0"));//会员num
                            XPreferencesUtils.put("phone", jsonD.optString("phone", ""));//手机号
                            XPreferencesUtils.put("name", jsonD.optString("name", ""));//名称
                            XPreferencesUtils.put("portrait", jsonD.optString("portrait", ""));//头像
                            XPreferencesUtils.put("industry", jsonD.optString("industry", ""));//行业
                            XPreferencesUtils.put("profession", jsonD.optString("profession", ""));//职业
                            XPreferencesUtils.put("company", jsonD.optString("company", ""));//公司
                            XPreferencesUtils.put("payment_pwd", jsonD.optBoolean("payment_pwd", false));//是否设置支付密码
                            XPreferencesUtils.put("is_activity", jsonD.optBoolean("is_activity"));//是否显示邀请好友得1元
                            XPreferencesUtils.put("has_usertemp", jsonD.optBoolean("has_usertemp", false));//是否制作个人微网
                            XPreferencesUtils.put("has_market", jsonD.optBoolean("has_market", false));//是否制作营销网页
                            XPreferencesUtils.put("type", jsonD.optString("type", "4"));//3-业务员,4-普通
                            XPreferencesUtils.put("qq", jsonD.optString("qq", ""));
                            XPreferencesUtils.put("email", jsonD.optString("email", ""));
                            XPreferencesUtils.put("money", jsonD.optString("money", "0"));//钱包
                            XPreferencesUtils.put("reward", jsonD.optString("reward", "0"));//营销奖励
                            XPreferencesUtils.put("maibao", jsonD.optString("maibao", "0"));//脉宝
                            XPreferencesUtils.put("motto", jsonD.optString("motto", ""));//广告语
                            XPreferencesUtils.put("invitation_code", jsonD.optString("invitation_code", ""));//邀请码
                            XPreferencesUtils.put("is_perfect", jsonD.optString("is_perfect", "10"));//是否完善
                            XPreferencesUtils.put("background", jsonD.optString("background", ""));//背景图片
                            if (XEmptyUtils.isSpace(jsonD.optString("vip_expire", ""))) {
                                XPreferencesUtils.put("member_data", "开通会员");//会员到期时间
                            } else {
                                XPreferencesUtils.put("member_data", XTimeUtils.StringToYMD(jsonD.optString("vip_expire", "")) + "到期");
                            }


                            if (resultBack != null)
                                resultBack.onSuccess(o);
                        } else {
                            if (resultBack != null)
                                resultBack.onFailed(json.optString("message"));
                        }
                    } catch (Exception e) {
                        if (resultBack != null)
                            resultBack.onFailed(e.toString());
                    }

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
     * 根据用户id获取信息
     * get
     */
    public void getUserInfoIDApi(String id, final boolean isShowLoad, final ResultBack resultBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.GET_USERINFO_ID_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShowLoad)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShowLoad)
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
     * 修改用户信息
     * post
     */
    public void updateUserInfoApi(HashMap<String, Object> map, final String msg, final ResultBack resultBack) {

        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.UPDATE_USERINFO_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog(msg);

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
     * 获取app更新信息
     * post
     */
    public void appVersionApi(final String msg, final ResultBack resultBack) {

        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.APP_VERSION_URL, null, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (!mActivity.isFinishing())
                        mDialogUtils.showLoadingDialog(msg);

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