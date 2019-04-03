package com.huimaibao.app.fragment.mine.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.settings.server.SettingLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 验证手机号
 */
public class VerifyPhoneActivity extends BaseActivity {
    private String mType = "";

    //手机号
    private TextView _phone_tv;
    //验证码
    private EditText _code_et;
    private String _phone_value, _code_value;
    ;
    //获取验证码,确定
    private TextView _code_tv;//, _next_btn;


    //验证码倒计时
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_verify_phone);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        initView();
        initData();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        _phone_tv = findViewById(R.id.setting_account_verify_phone_tv);
        _code_et = findViewById(R.id.setting_account_verify);
        _code_tv = findViewById(R.id.setting_account_get_verify);
        //_next_btn = findViewById(R.id.setting_account_new_pwd_sure);
    }

    /**
     * 初始化事件,数据
     */
    private void initData() {
        _phone_value = XPreferencesUtils.get("phone", "").toString();
        _phone_tv.setText("当前绑定手机号：" + _phone_value);
    }

    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.setting_account_get_verify:
                if (mType.equals("更换手机号"))
                    sendSMS(_phone_value, "4");
                else
                    sendSMS(_phone_value, "5");
                break;
            case R.id.setting_verify_next_btn:
                _code_value = _code_et.getText().toString();
                if (XEmptyUtils.isSpace(_code_value)) {
                    ToastUtils.showCenter("请输入验证码");
                } else {
                    if (mType.equals("更换手机号"))
                        validateOldPhone(_code_value);
                    else
                        validatePayment(_code_value);
                }
                break;
            case R.id.setting_verify_tell_btn:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:023-68452158"));
                startActivity(intent);//调用上面这个intent实现拨号
                break;
        }
    }


    /**
     * 发送验证码
     */
    private void sendSMS(final String phone, final String type) {
        LoginLogic.Instance(mActivity).LoginSendSMSApi(phone, type, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                showToast("发送成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer = new CountDownTimer(60 * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                _code_tv.setEnabled(false);
                                _code_tv.setText("" + millisUntilFinished / 1000 + "S");
                            }

                            @Override
                            public void onFinish() {
                                _code_tv.setEnabled(true);
                                _code_tv.setText("获取验证码");
                            }
                        }.start();
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                showToast("发送失败");
            }
        });
    }


    /**
     * 验证旧手机号
     */
    private void validateOldPhone(String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        SettingLogic.Instance(mActivity).validateOldPhoneApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("rest=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONObject jsonD = new JSONObject(data);
                        finish();
                        startActivity(AccountPhoneActivity.class, "" + jsonD.getString("sign"));
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
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
     * 验证支付手机号
     */
    private void validatePayment(String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        SettingLogic.Instance(mActivity).validatePaymentSMSApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("rest=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        JSONObject jsonD = new JSONObject(data);
                        finish();
                        startActivity(PaymentPWDActivity.class, mType, "" + jsonD.getString("sign"));
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
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
            _code_tv.setEnabled(true);
            _code_tv.setText("获取验证码");
            timer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }
}
