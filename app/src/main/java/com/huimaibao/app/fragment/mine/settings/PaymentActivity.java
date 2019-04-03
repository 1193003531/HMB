package com.huimaibao.app.fragment.mine.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;

/**
 * 支付管理
 */
public class PaymentActivity extends BaseActivity {

    private String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_payment);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.setting_payment_amend_pwd_btn:
                startActivity(PaymentPWDActivity.class, "修改支付密码","");
                break;
            case R.id.setting_payment_forget_pwd_btn:
                startActivity(VerifyPhoneActivity.class, "忘记支付密码");
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
