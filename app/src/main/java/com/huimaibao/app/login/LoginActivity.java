package com.huimaibao.app.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.main.MainActivity;
import com.huimaibao.app.share.WXUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDateUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 账号登录
 */
public class LoginActivity extends Activity {
    private static Activity mActivity;
    //手机号，密码
    private EditText _phone_et, _pwd_et;
    private String _phone_value = "", _pwd_value = "";
    //清除手机号,隐藏密码
    private ImageView _ed_phone_clear, _hint_pwd;
    //是否显示密码
    private boolean isShowPWD = false;
    private String unionid = "", access_token = "", openid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        XStatusBar.setColor(this, getResources().getColor(R.color.colorPrimary));
        mActivity = this;
        initView();
        initData();
    }

    /***/
    private void initView() {
        _phone_et = findViewById(R.id.login_phone);
        _pwd_et = findViewById(R.id.login_pwd);
        _ed_phone_clear = findViewById(R.id.login_phone_del);
        _hint_pwd = findViewById(R.id.login_pwd_show);

        _phone_et.addTextChangedListener(accountWatcher);
        if (!XEmptyUtils.isEmpty(XPreferencesUtils.get("phone", ""))) {
            _phone_et.setText("" + XPreferencesUtils.get("phone", ""));
            _phone_et.setSelection(XPreferencesUtils.get("phone", "").toString().length());//将光标移至文字末尾
        }

    }

    private void initData() {
        _ed_phone_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ed_phone_clear.setVisibility(View.GONE);
                _phone_et.setText("");
            }
        });

        _phone_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (_phone_et.getText().toString().length() > 0)
                        _ed_phone_clear.setVisibility(View.VISIBLE);
                    else
                        _ed_phone_clear.setVisibility(View.GONE);
                } else {
                    _ed_phone_clear.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 账号输入监听
     */
    private TextWatcher accountWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _ed_phone_clear.setVisibility(View.VISIBLE);
            } else {
                _ed_phone_clear.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public void onAction(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //是否显示密码
            case R.id.login_pwd_show:
                if (isShowPWD) {
                    isShowPWD = false;
                    //从密码可见模式变为密码不可见模式
                    _pwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _hint_pwd.setImageResource(R.drawable.login_pwd_n);
                } else {
                    isShowPWD = true;
                    //从密码不可见模式变为密码可见模式
                    _pwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _hint_pwd.setImageResource(R.drawable.login_pwd_s);
                }
                // 重新设置光标位置
                _pwd_et.setSelection(_pwd_et.getText().length());
                break;
            //注册
            case R.id.login_register:
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("vType", "注册");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            //登录
            case R.id.login:
                _phone_value = _phone_et.getText().toString();
                _pwd_value = _pwd_et.getText().toString();
                if (XEmptyUtils.isSpace(_phone_value)) {
                    ToastUtils.showCenter("请输入手机号");
                } else if (!XRegexUtils.checkMobile(_phone_value)) {
                    ToastUtils.showCenter("输入的手机号有误,请重新输入");
                } else {

                    if (XEmptyUtils.isSpace(_pwd_value)) {
                        ToastUtils.showCenter("请输入密码");
                    } else if (_pwd_value.length() < 6) {
                        ToastUtils.showCenter("输入密码必须大于6位");
                    } else {
                        Login(_phone_value, _pwd_value);
                    }
                }
                break;
            //短信登录
            case R.id.login_sms:
                XPreferencesUtils.put("phone", _phone_et.getText().toString().trim());
                intent.setClass(LoginActivity.this, LoginSMSActivity.class);
                intent.putExtra("vType", "短信登录");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            //忘记密码
            case R.id.login_forgot:
                XPreferencesUtils.put("phone", _phone_et.getText().toString().trim());
                intent.setClass(LoginActivity.this, ForgotActivity.class);
                intent.putExtra("vType", "找回密码");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            //微信登录
            case R.id.login_wx_login:
                WXLogin();
                break;
        }
    }

    /**
     * 进入主界面
     */
    private void toMainView() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /**
     * 完善信息
     */
    private void toPerfect(String type) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, PerfectBActivity.class);
        intent.putExtra("vType", type);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }


//    /**
//     * 取消倒计时
//     */
//    private void cancelTimer() {
//        if (timer != null) {
//            _code_tv.setEnabled(true);
//            _code_tv.setText("获取验证码");
//            timer.cancel();
//        }
//    }


    /**
     * 微信登录
     */
    private void WXLogin() {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!WXUtils.isWeixinAvilible()) {
            XToast.normal("您还未安装微信客户端");
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_hmb";
        BaseApplication.mWxApi.sendReq(req);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (XPreferencesUtils.get("WXUserToken", "").equals("onSuccess")) {
            unionid = XPreferencesUtils.get("unionid", "").toString();
            access_token = XPreferencesUtils.get("access_token", "").toString();
            openid = XPreferencesUtils.get("openid", "").toString();
            LoginWeChat(unionid, access_token, openid);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        XPreferencesUtils.put("WXUserToken", "");
    }

    /**
     * 登录
     */
    private void Login(final String phone, String pwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", pwd);
        LoginLogic.Instance(mActivity).LoginApi(ServerApi.LOGIN_PHONE_URL, map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("responseObj=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONObject dataJ = new JSONObject(data);
                        XPreferencesUtils.put("token", dataJ.getString("token"));
                        //保存token到期时间,有效期1月
                        XPreferencesUtils.put("tokenExpire", XTimeUtils.getCurDate());
                        XPreferencesUtils.put("phone", phone);

//                            Intent intent = new Intent();
//                            intent.setClass(mActivity, PerfectBActivity.class);
//                            intent.putExtra("vType", "微信");
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        if (dataJ.optBoolean("is_perfect")) {
                            toMainView();
                        } else {
                            toPerfect("登录");
                        }
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    LogUtils.debug("error:" + e);
                    showToast("登录失败，请重新登录！");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
                showToast("登录失败，请重新登录！");
            }
        });
    }


    /**
     * 通过微信获取用户信息
     */
    private void LoginWeChat(String unionid, final String access_token, final String openid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("union_id", unionid);
        LoginLogic.Instance(mActivity).LoginWeChatApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    //{"status":-1,"message":"\u9700\u8981\u7ed1\u5b9a\u624b\u673a\u53f7\u7801\uff01","data":[]}
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.getString("status"); //国家
                    if (status.equals("-1")) {
                        getUserInfo(access_token, openid);
                    } else {
                        JSONObject data = new JSONObject(json.getString("data"));
                        XPreferencesUtils.put("token", data.getString("token"));
                        //保存token到期时间,有效期1月
                        XPreferencesUtils.put("tokenExpire", XTimeUtils.getCurDate());
                        if (XEmptyUtils.isSpace(data.optString("phone", ""))) {
                            getUserInfo(access_token, openid);
                        } else {
                            XPreferencesUtils.put("phone", data.optString("phone", ""));
                            if (data.optBoolean("is_perfect")) {
                                toMainView();
                            } else {
                                toPerfect("微信");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.debug("json--" + e);
                    showToast("微信登录失败,请重新登录");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("json--" + error);
                showToast("微信登录失败,请重新登录");
            }
        });
        // xCache.put("WXUserToken", "");
        XPreferencesUtils.put("WXUserToken", "");
    }

    /**
     * 微信登录失败
     */
    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
            }
        });
    }


    /**
     * 通过拼接的用户信息url获取用户信息
     */
    private void getUserInfo(final String access_token, final String openid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("access_token", access_token);
                map.put("openid", openid);
                LoginLogic.Instance(mActivity).getUserInfoWXApi(map, new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {
                        //XLog.d("UserInfo--" + object);
                        if (!object.equals("")) {
                            try {
                                JSONObject json = new JSONObject(object.toString());
                                XPreferencesUtils.put("sex", json.getString("sex"));
                                XPreferencesUtils.put("headimgurl", json.getString("headimgurl"));
                                XPreferencesUtils.put("nickname", json.getString("nickname"));
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, BindActivity.class);
                                intent.putExtra("vType", "绑定手机号");
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                //finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                                showToast("微信登录失败,请重新登录");
                            }
                        } else {
                            showToast("微信登录失败,请重新登录");
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        showToast("微信登录失败,请重新登录");
                    }
                });
            }
        });
    }


    /**
     * 广播接收关闭界面
     */
    public static class loginBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mActivity.finish();
        }
    }

//    @Override
//    public void finish() {
//        super.finish();
//    }

    //物理返回退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            XActivityStack.getInstance().appExit();
            return true;
        }

        //拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
