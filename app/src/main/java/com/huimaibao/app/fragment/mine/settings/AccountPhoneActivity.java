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
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 账号管理-更换手机号
 */
public class AccountPhoneActivity extends BaseActivity {

    private String mType = "";

    //手机号,验证码
    private EditText _phone_et, _code_et;
    private String _phone_value, _code_value;
    private ImageView _ed_phone_clear;
    //获取验证码
    private TextView _code_tv;


    //验证码倒计时
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_account_phone);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle("绑定新手机");
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        _phone_et = findViewById(R.id.setting_account_new_phone);
        _code_et = findViewById(R.id.setting_account_new_phone_verify);
        _ed_phone_clear = findViewById(R.id.setting_account_new_phone_del);
        _code_tv = findViewById(R.id.setting_account_new_phone_get_verify);
        //_sure_btn = findViewById(R.id.setting_account_new_pwd_sure);
    }

    /**
     * 初始化事件,数据
     */
    private void initData() {
        _phone_et.addTextChangedListener(phoneWatcher);

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
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.setting_account_new_phone_get_verify:
                _phone_value = _phone_et.getText().toString();
                if (XEmptyUtils.isSpace(_phone_value)) {
                    ToastUtils.showCenter("请输入手机号");
                } else {
                    sendSMS(_phone_value, "4");
                }
                break;
            case R.id.setting_account_new_phone_sure_btn:
                _phone_value = _phone_et.getText().toString();
                _code_value = _code_et.getText().toString();
                if (XEmptyUtils.isSpace(_phone_value)) {
                    ToastUtils.showCenter("请输入手机号");
                } else if (XEmptyUtils.isSpace(_code_value)) {
                    ToastUtils.showCenter("请输入验证码");
                } else {
                    changePhone(_code_value, _phone_value);
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
     * 绑定新手机
     */
    private void changePhone(String code, String phone) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("phone", phone);
        map.put("sign", mType);
        SettingLogic.Instance(mActivity).changePhoneApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {

                try {
                    JSONObject json = new JSONObject(object.toString());
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        showToast("绑定成功");
                        finish();
                    } else {
                        showToast(message);
                        showError();
                    }
                } catch (Exception e) {
                    showToast("绑定失败");
                    showError();
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("绑定失败");
                showError();
            }
        });
    }

    /**绑定失败提示*/
    private void showError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelTimer();
                _phone_et.setText("");
                _code_et.setText("");
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
     * 手机号输入监听
     */
    private TextWatcher phoneWatcher = new TextWatcher() {

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

}
