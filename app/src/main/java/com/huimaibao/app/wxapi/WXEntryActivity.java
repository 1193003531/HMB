package com.huimaibao.app.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.LoginActivity;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.share.WXShare;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private Activity mActivity;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        BaseApplication.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        //finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == RETURN_MSG_TYPE_SHARE) {
            WXShareRes(resp);
//            switch (resp.errCode) {
//                case BaseResp.ErrCode.ERR_OK:
//                    WXShareRes("分享成功");
//                    break;
//                case BaseResp.ErrCode.ERR_USER_CANCEL:
//                    WXShareRes("分享取消");
//                    break;
//                case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                    WXShareRes("分享被拒绝");
//                    break;
//                default:
//                    WXShareRes("分享返回");
//                    break;
//            }
            return;
        }
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                XToast.normal("拒绝授权微信登录");
                showLogin();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                XToast.normal("取消了微信登录");
                showLogin();
                break;
            case BaseResp.ErrCode.ERR_OK:
                /**登录成功*/
                getUserToken(((SendAuth.Resp) resp).code);
                break;
        }
    }

    /**
     * 获取userToken
     */
    private void getUserToken(String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", ServerApi.WX_APP_ID);
        map.put("secret", ServerApi.WX_APP_SECRET);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        LoginLogic.Instance(this).LoginWXApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                //XLog.d("object--" + object);
                if (!object.equals("")) {
                    try {
                        JSONObject json = new JSONObject(object.toString());
                        // String access_token = json.getString("access_token"); //接口调用凭证
                        // String openid = json.getString("openid"); //授权用户唯一标识
                        //XCache.get(WXEntryActivity.this).put("access_token", json.getString("access_token"));
                        //XCache.get(WXEntryActivity.this).put("openid", json.getString("openid"));
                        //XCache.get(WXEntryActivity.this).put("unionid", json.getString("unionid"));
                        // XCache.get(WXEntryActivity.this).put("WXUserToken", "onSuccess");
                        XPreferencesUtils.put("access_token", json.getString("access_token"));
                        XPreferencesUtils.put("openid", json.getString("openid"));
                        XPreferencesUtils.put("unionid", json.getString("unionid"));
                        XPreferencesUtils.put("WXUserToken", "onSuccess");
                        //当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
                        // String unionid = json.getString("unionid");
                        //showLogin();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        XPreferencesUtils.put("WXUserToken", "");
                        WXLoginErr();
                    }
                } else {
                    XPreferencesUtils.put("WXUserToken", "");
                    WXLoginErr();
                }
            }

            @Override
            public void onFailed(String error) {
                XPreferencesUtils.put("WXUserToken", "");
                WXLoginErr();
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        BaseApplication.mWxApi.handleIntent(intent, this);
        finish();
    }

    /**
     * 微信登录失败
     */
    private void WXLoginErr() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal("微信登录失败,请重新登录");
                finish();
            }
        });
    }


    /**
     * 微信分享回调
     */
    private void WXShareRes(BaseResp result) {
        Intent intent = new Intent(WXShare.ACTION_SHARE_RESPONSE);
        //intent.putExtra(WXShare.EXTRA_RESULT, result);
        intent.putExtra(WXShare.EXTRA_RESULT, new WXShare.Response(result));
        sendBroadcast(intent);
        finish();
    }

    private void showLogin() {
        Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
