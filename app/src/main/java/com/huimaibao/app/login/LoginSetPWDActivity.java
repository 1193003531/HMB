package com.huimaibao.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.main.MainActivity;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 设置登录密码
 */
public class LoginSetPWDActivity extends BaseActivity {

    private String mType = "";

    private TextView _title_tv, _set_pwd_tv;

    //手机号，密码
    private EditText _pwd_et;
    private String _phone_value = "", _pwd_value = "";
    //隐藏密码
    private ImageView _hint_pwd;
    //是否显示密码
    private boolean isShowPWD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_set_pwd);
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
        _title_tv = findViewById(R.id.login_set_pwd_title);
        _set_pwd_tv = findViewById(R.id.login_set_pwd);

        _pwd_et = findViewById(R.id.login_pwd);
        _hint_pwd = findViewById(R.id.login_pwd_show);
    }

    private void initData() {
        _phone_value = XPreferencesUtils.get("phone", "").toString();
        if (mType.equals("忘记密码")) {
            _title_tv.setText("设置新密码");
            _set_pwd_tv.setText("完成");
            _pwd_et.setHint("输入新的密码");
        } else if (mType.equals("绑定")) {
            _title_tv.setText("设置登录密码");
            _set_pwd_tv.setText("完成");
            _pwd_et.setHint("输入密码");
        } else {
            _title_tv.setText("设置登录密码");
            _set_pwd_tv.setText("注册");
            _pwd_et.setHint("输入密码");
        }
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                this.finish();
                break;
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
            //完成/注册
            case R.id.login_set_pwd:
                _pwd_value = _pwd_et.getText().toString();
                if (XEmptyUtils.isSpace(_pwd_value)) {
                    ToastUtils.showCenter("请输入密码");
                } else {
                    if (mType.equals("忘记密码")) {
                        RestPWD(_pwd_value);
                    } else if (mType.equals("注册")) {
                        Register(_pwd_value);
                    } else {
                        toPerfect("微信");
                    }
                }
                break;

        }
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
        intent.setClass(LoginSetPWDActivity.this, PerfectBActivity.class);
        intent.putExtra("vType", type);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }


    /**
     * 找回密码
     */
    private void RestPWD(String pwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", _phone_value);
        map.put("sign", XPreferencesUtils.get("VCode", ""));
        map.put("password", pwd);
        LoginLogic.Instance(this).LoginRestPWDApi(map, new ResultBack() {
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
                            //XCache.get(OtherActivity.this).put("token", dataJ.getString("token"));
                            XPreferencesUtils.put("token", dataJ.getString("token"));
                            XPreferencesUtils.put("phone", _phone_value);
                            showToast("设置密码成功");
                            toMainView();
                        } else {
                            showToast(message);
                        }

                    } catch (Exception e) {
                        showToast("设置密码失败");
                    }
                } else {
                    showToast("设置密码失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showToast("设置密码失败");
            }
        });
    }

    /**
     * 注册
     */
    private void Register(String pwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", _phone_value);
        map.put("sign", XPreferencesUtils.get("VCode", ""));
        map.put("password", pwd);
        LoginLogic.Instance(this).LoginRegisterApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null && !"".equals(object)) {
                    try {
                        JSONObject json = new JSONObject(object.toString());
                        String status = json.getString("status"); //国家
                        String message = json.getString("message"); //国家
                        String data = json.getString("data"); //国家
                        if (status.equals("0")) {
                            JSONObject dataJ = new JSONObject(data);
                            XPreferencesUtils.put("token", dataJ.getString("token"));
                            toPerfect("注册");
                        } else {
                            showToast(message);
                        }
                    } catch (Exception e) {
                        showToast("注册失败，请重新注册");
                    }
                } else {
                    showToast("注册失败，请重新注册");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("注册失败，请重新注册");
            }
        });
    }


}
