package com.huimaibao.app.fragment.mine.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.settings.server.SettingLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;

import org.json.JSONObject;

/**
 * 邀请人
 */
public class InviterActivity extends BaseActivity {
    //手机号
    private EditText _phone_et;
    private String _phone_value = "";
    private ImageView _ed_phone_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_inviter);
        //XStatusBar.setColor(this, getResources().getColor(R.color.colorPrimary));
        initView();
        initData();
    }

    /***/
    private void initView() {
        _phone_et = findViewById(R.id.inviter_phone);
        _ed_phone_clear = findViewById(R.id.inviter_phone_del);

        _phone_et.addTextChangedListener(accountWatcher);

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
            case R.id.back_btn:
                this.finish();
                break;
            //获取验证码
            case R.id.inviter_phone_sure:
                _phone_value = _phone_et.getText().toString();
                if (XEmptyUtils.isSpace(_phone_value)) {
                    ToastUtils.showCenter("请输入手机号");
                } else if (!XRegexUtils.checkMobile(_phone_value)) {
                    ToastUtils.showCenter("输入的手机号有误,请重新输入");
                } else {
                    setInviterPhone(_phone_value);
                }
                break;
        }
    }


    /**
     * 获取邀请人
     */
    private void setInviterPhone(final String phone) {
        SettingLogic.Instance(mActivity).setInvitationPhoneApi(phone, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.optString("status");
                    //String message = json.getString("message");
                    if (status.equals("0")) {
                        showToast("设置成功");
                        XPreferencesUtils.put("inviter_phone", phone);
                        finish();
                    } else {
                        showToast("设置失败," + json.optString("message", ""));
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
