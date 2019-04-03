package com.huimaibao.app.fragment.mine.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.settings.server.SettingLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.huimaibao.app.view.VerificationCodeView;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 支付管理
 */
public class PaymentPWDActivity extends BaseActivity {

    private String mType = "", _sign_value;

    private TextView _tv, _commit_btn;
    private String _pwd_1 = "", _pwd_2 = "", _pwd_3 = "";

    //密码
    private VerificationCodeView verificationcodeview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_payment_amend_pwd);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
            _sign_value = intent.getStringExtra("vUrl");
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
        XKeyboardUtils.openKeyboard(mActivity);
        _tv = findViewById(R.id.setting_amend_pwd_tv);
        _commit_btn = findViewById(R.id.setting_amend_pwd_commit_btn);
        verificationcodeview = findViewById(R.id.setting_amend_pwd_value);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (mType.equals("修改支付密码"))
            _tv.setText("请输入六位支付密码，完成身份验证");
        else
            _tv.setText("请输入支付密码");

        verificationcodeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                if (mType.equals("修改支付密码")) {
                    if (XEmptyUtils.isSpace(_pwd_1)) {
                        _pwd_1 = content;
                        validatePaymentPWD(_pwd_1);
                    } else if (XEmptyUtils.isSpace(_pwd_2)) {
                        _pwd_2 = content;
                        _tv.setText("请再次输入新的支付密码");
                    } else {
                        _pwd_3 = content;
                        if (_pwd_2.equals(_pwd_3)) {
                            _commit_btn.setVisibility(View.VISIBLE);
                            verificationcodeview.setIsClear(false);
                        } else {
                            _pwd_2 = "";
                            _pwd_3 = "";
                            _tv.setText("请输入新的支付密码");
                            ToastUtils.showCenter("两次输入的密码不同,请重新输入");
                        }
                    }
                } else {
                    if (XEmptyUtils.isSpace(_pwd_1)) {
                        _pwd_1 = content;
                        _tv.setText("请再次输入支付密码");
                    } else {
                        _pwd_2 = content;
                        if (_pwd_1.equals(_pwd_2)) {
                            _commit_btn.setVisibility(View.VISIBLE);
                            verificationcodeview.setIsClear(false);
                        } else {
                            _pwd_1 = "";
                            _pwd_2 = "";
                            ToastUtils.showCenter("两次输入的密码不同,请重新输入");
                        }
                    }
                }
                // XToast.normal("" + content);
            }
        });

//        _commit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (_pwd_2.equals(_pwd_3)) {
//                    _pwd_2 = "";
//                    _pwd_3 = "";
//                    XToast.normal("两次输入的密码不同,请重新输入");
//                    _tv.setText("请输入新的支付密码");
//                    _commit_btn.setVisibility(View.GONE);
//                } else {
//                    XToast.normal("设置新密码成功");
//                }
//            }
//        });
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.setting_amend_pwd_commit_btn:
                setPaymentPWD(_sign_value, _pwd_2);
                break;
        }
    }


    /**
     * 验证旧支付密码
     */
    private void validatePaymentPWD(String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("password", password);
        SettingLogic.Instance(mActivity).validatePaymentPWDApi(map, new ResultBack() {
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
                        _sign_value = jsonD.getString("sign");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _tv.setText("请输入新的支付密码");
                            }
                        });
                    } else {
                        showToast(message);
                        _pwd_1 = "";
                    }
                } catch (Exception e) {
                    showToast("验证失败");
                    _pwd_1 = "";
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("验证失败");
                _pwd_1 = "";
            }
        });
    }

    /**
     * 设置支付密码
     */
    private void setPaymentPWD(String sign, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sign", sign);
        map.put("password", password);

        SettingLogic.Instance(mActivity).setPaymentPWDApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    // XLog.d("rest=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");
                    if (status.equals("0")) {
                        showToast("设置成功");
                        XPreferencesUtils.get("set_payment_pwd", true);
                        XPreferencesUtils.put("payment_pwd", true);
                        finish();
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    showToast("设置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("设置失败");
            }
        });
    }


}
