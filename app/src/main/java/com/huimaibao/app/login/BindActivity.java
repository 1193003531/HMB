package com.huimaibao.app.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.settings.UserAgreementActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;

import org.json.JSONObject;

/**
 * 绑定手机号
 */
public class BindActivity extends BaseActivity {
    //手机号
    private EditText _phone_et;
    private String _phone_value = "";
    private ImageView _ed_phone_clear;

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_bind);
        XStatusBar.setColor(this, getResources().getColor(R.color.colorPrimary));
        mDialogUtils = new DialogUtils(mActivity);
        initView();
        initData();
    }

    /***/
    private void initView() {
        _phone_et = findViewById(R.id.login_phone);
        _ed_phone_clear = findViewById(R.id.login_phone_del);

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
        switch (v.getId()) {
            case R.id.login_back:
                this.finish();
                break;
            //获取验证码
            case R.id.login_sms_send:
                _phone_value = _phone_et.getText().toString();
                if (XEmptyUtils.isSpace(_phone_value)) {
                    ToastUtils.showCenter("请输入手机号");
                } else if (!XRegexUtils.checkMobile(_phone_value)) {
                    ToastUtils.showCenter("输入的手机号有误,请重新输入");
                } else {
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
                        XPreferencesUtils.put("phone", phone);
                        startActivity(VerificationCodeActivity.class, "绑定");
                        //finish();
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
     * 验证手机号是否存在
     */
    private void phoneExist(final String phone) {
        LoginLogic.Instance(mActivity).LoginPhoneExistApi(phone, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                if (!XEmptyUtils.isEmpty(object)) {
                    try {
                        JSONObject json = new JSONObject(object.toString());
                        //XLog.d("responseObj=s=" + json);
                        String status = json.getString("status");
                        //String message = json.getString("message");
                        String data = json.getString("data");
                        if (status.equals("0")) {
                            JSONObject dataJ = new JSONObject(data);
                            if (dataJ.getBoolean("is_exist")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialogUtils.showNoTitleDialog("您的手机号已存在,去登录", "取消", "登录", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                                mDialogUtils.dismissDialog();
                                            }
                                        });
                                    }
                                });
                            } else {
                                sendSMS(phone, "4");
                            }
                        } else {
                            showToast("发送失败,请重新发送验证码");
                        }
                    } catch (Exception e) {
                        showToast("发送失败,请重新发送验证码");
                    }
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("发送失败,请重新发送验证码");
            }
        });
    }


}
