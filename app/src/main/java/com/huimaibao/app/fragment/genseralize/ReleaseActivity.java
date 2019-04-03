package com.huimaibao.app.fragment.genseralize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;


/**
 * 发布
 */
public   class ReleaseActivity extends BaseActivity {

    private String mType = "";
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_release);
        mContext = this;
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

}
