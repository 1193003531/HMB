package com.huimaibao.app.fragment.mine.settings;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;

/**
 * 用户协议
 */
public class UserAgreementActivity extends BaseActivity {

    private String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_user);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle("用户协议");
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        TextView link = findViewById(R.id.user_agreement_url);
        link.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.user_agreement_url:
                startActivity(MessageWebActivity.class, mType, "http://www.51huimaibao.cn");
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
