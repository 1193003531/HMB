package com.huimaibao.app.fragment.mine.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
 * 账号管理-忘记密码
 */
public class AccountPWDActivity extends BaseActivity {

    private String mType = "";

    //手机号
    private TextView _phone_tv;
    //验证码,密码
    private EditText _code_et, _pwd_et;
    private String _phone_value, _code_value, _pwd_value;
    private ImageView _ed_pwd_clear;
    //获取验证码
    private TextView _code_tv;


    //验证码倒计时
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_account_pwd);
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
        _phone_tv = findViewById(R.id.account_pwd_phone_tv);
        _code_et = findViewById(R.id.setting_account_verify_et);
        _pwd_et = findViewById(R.id.setting_account_new_pwd);
        _ed_pwd_clear = findViewById(R.id.setting_account_new_pwd_del);
        _code_tv = findViewById(R.id.setting_account_get_verify);
        //_sure_btn = findViewById(R.id.setting_account_new_pwd_sure);
    }

    /**
     * 初始化事件,数据
     */
    private void initData() {
        _phone_value = XPreferencesUtils.get("phone", "").toString();
        _phone_tv.setText("当前绑定手机号：" + _phone_value);

        _pwd_et.addTextChangedListener(pwdWatcher);

        _pwd_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (_pwd_et.getText().toString().length() > 0)
                        _ed_pwd_clear.setVisibility(View.VISIBLE);
                    else
                        _ed_pwd_clear.setVisibility(View.GONE);
                } else {
                    _ed_pwd_clear.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.setting_account_get_verify:
                sendSMS(_phone_value, "3");
                break;
            case R.id.setting_account_new_pwd_sure:
                _code_value = _code_et.getText().toString();
                _pwd_value = _pwd_et.getText().toString();
                if (XEmptyUtils.isSpace(_code_value)) {
                    ToastUtils.showCenter("请输入验证码");
                } else if (XEmptyUtils.isSpace(_pwd_value)) {
                    ToastUtils.showCenter("请输入密码");
                } else {
                    restLoginPWD(_code_value, _pwd_value);
                }
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
     * 重置登录密码
     */
    private void restLoginPWD(String code, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("password", password);
        SettingLogic.Instance(mActivity).restLoginPWDApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {

                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("rest=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        showToast("重置成功");
                        finish();
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    showToast("重置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("重置失败");
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

    /**
     * 密码输入监听
     */
    private TextWatcher pwdWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _ed_pwd_clear.setVisibility(View.VISIBLE);
                // _sure_btn.setBackgroundResource(R.drawable.btn_blue_r5_bg);
            } else {
                _ed_pwd_clear.setVisibility(View.GONE);
                // _sure_btn.setBackgroundResource(R.drawable.btn_hui_r5_bg);
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
}
