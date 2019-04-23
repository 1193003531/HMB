package com.huimaibao.app.fragment.mine.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;

/**
 * 账号管理
 */
public class AccountActivity extends BaseActivity {
    private String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_account);
        mActivity = this;
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        setShowLine(false);
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.setting_account_pwd_btn:
                startActivity(AccountPWDActivity.class, "修改密码");
                break;
            case R.id.setting_account_phone_btn:
                startActivity(VerifyPhoneActivity.class, "更换手机号");
                break;
        }
    }


}
