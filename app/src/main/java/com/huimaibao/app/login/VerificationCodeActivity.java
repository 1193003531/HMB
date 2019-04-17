package com.huimaibao.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.main.MainActivity;
import com.huimaibao.app.view.SixEditView;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 验证码
 */
public class VerificationCodeActivity extends BaseActivity {

    private String mType = "";
    //手机号
    private SixEditView _code_et;
    private TextView _phone_tv, _time_tv;
    private String _phone_value = "";

    //验证码倒计时
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_verification_code);
        XStatusBar.setColor(this, getResources().getColor(R.color.colorPrimary));

        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        initView();
        initData();
    }

    /***/
    private void initView() {
        _phone_value = XPreferencesUtils.get("phone", "").toString();
        _code_et = findViewById(R.id.login_v_code_value);
        _phone_tv = findViewById(R.id.login_v_code_phone);
        _time_tv = findViewById(R.id.login_v_code_send);

        if (!XEmptyUtils.isEmpty(_phone_value)) {
            _phone_tv.setText("已发送到 " + _phone_value);
        }

    }

    private void initData() {
        showTimes();
        _code_et.setListener(new SixEditView.SixEditListener() {
            @Override
            public void onInputComplete(String passWord) {
                if (mType.equals("忘记密码") || mType.equals("注册")) {
                    ValidateSMS(_phone_value, passWord);
                } else if (mType.equals("登录")) {
                    Login(_phone_value, passWord);
                } else {
                    BindPhone(_phone_value, passWord);
                }
            }
        });

    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                this.finish();
                break;
            //获取验证码
            case R.id.login_v_code_send:
                if (mType.equals("忘记密码")) {
                    sendSMS(_phone_value, "3");
                } else if (mType.equals("注册")) {
                    sendSMS(_phone_value, "2");
                } else if (mType.equals("登录")) {
                    sendSMS(_phone_value, "1");
                } else {
                    //绑定
                    sendSMS(_phone_value, "4");
                }
                break;
        }
    }


    /**
     * 发送验证码
     */
    private void sendSMS(final String phone, final String type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginLogic.Instance(mActivity).LoginSendSMSApi(phone, type, new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {
                        showToast("发送成功");
                        showTimes();
                    }

                    @Override
                    public void onFailed(String error) {
                        showToast("发送失败");
                    }
                });
            }
        });
    }


    /**
     * 验证验证码
     */
    private void ValidateSMS(final String phone, final String code) {

        LogUtils.debug("responseObj=s=" + code+"-"+phone);
        LoginLogic.Instance(mActivity).LoginValidateSMSApi(phone, code, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("responseObj=s=" + json);
                    /**
                     * {"status":0,"message":"success","data":{"sign":"5dc53c36fc482b23906f424520eff91a"}}
                     */
                    String status = json.getString("status");
                    String message = json.getString("message");
                    final String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONObject dataJ = new JSONObject(data);
                        showToast("验证成功");
                        XPreferencesUtils.put("VCode", dataJ.getString("sign"));
                        startActivity(LoginSetPWDActivity.class, mType);
                        finish();
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    LogUtils.debug("error:" + e);
                    showToast("验证失败");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("验证失败");
            }
        });

    }

    /**
     * 取消倒计时
     */
    private void cancelTimer() {
        if (timer != null) {
            _time_tv.setEnabled(true);
            _time_tv.setText("重新获取");
            timer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    /**
     * 显示倒计时
     */
    private void showTimes() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer = new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        _time_tv.setEnabled(false);
                        _time_tv.setTextColor(mActivity.getResources().getColor(R.color.color999999));
                        _time_tv.setText("" + millisUntilFinished / 1000 + "S");
                    }

                    @Override
                    public void onFinish() {
                        _time_tv.setEnabled(true);
                        _time_tv.setTextColor(mActivity.getResources().getColor(R.color.color666666));
                        _time_tv.setText("重新获取");
                    }
                }.start();
            }
        });
    }


    /**
     * 进入主界面
     */
    private void toMainView() {
        Intent intent = new Intent();
        intent.setClass(mActivity, MainActivity.class);
        startActivity(intent);
        intent = new Intent("com.huimaibao.app.CHOOSE_LOGIN_FINISH");
        mActivity.sendBroadcast(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 完善信息
     */
    private void toPerfect(String type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, PerfectBActivity.class);
        intent.putExtra("vType", type);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /**
     * 短信登录
     */
    private void Login(final String phone, String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);

        LoginLogic.Instance(mActivity).LoginApi(ServerApi.LOGIN_SMS_URL, map, new ResultBack() {
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
     * 绑定手机号
     */
    private void BindPhone(final String phone, String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("open_id", XPreferencesUtils.get("openid", ""));
        map.put("union_id", XPreferencesUtils.get("unionid", ""));
        map.put("wechat_name", XPreferencesUtils.get("nickname", ""));
        map.put("head_portrait", XPreferencesUtils.get("headimgurl", ""));

        LoginLogic.Instance(this).LoginBindApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null && !"".equals(object)) {
                    try {
                        JSONObject json = new JSONObject(object.toString());
                        //XLog.d("json:" + json);
                        String status = json.getString("status");
                        String message = json.getString("message");
                        String data = json.getString("data");
                        if (status.equals("0")) {
                            JSONObject dataJ = new JSONObject(data);
                            XPreferencesUtils.put("token", dataJ.getString("token"));
                            XPreferencesUtils.put("phone", phone);
                            if (dataJ.optBoolean("is_perfect")) {
                                toMainView();
                            } else {
                                startActivity(LoginSetPWDActivity.class, "绑定");
                                finish();
                                //toPerfect("微信");
                            }
                        } else {
                            showToast(message);
                        }
                    } catch (Exception e) {
                        showToast("绑定失败，请重新绑定");
                    }
                } else {
                    showToast("绑定失败，请重新绑定");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("绑定失败，请重新绑定");
            }
        });
    }

}
