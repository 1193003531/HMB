package com.huimaibao.app.fragment.electcircle.act;

import android.content.Intent;
import android.os.Bundle;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;

/**
 * 规则
 */
public class RegulationActivity extends BaseActivity {

    private String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_elect_regulation);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
