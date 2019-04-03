package com.huimaibao.app.fragment.genseralize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;


/**
 * 推广
 */
public class GeneralizeActivity extends BaseActivity {


    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_generalize);
        mContext = this;
        setNeedBackGesture(true);
    }


    public void onAction(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.generalize_release_back:
                finish();
                break;
            case R.id.generalize_release:
                intent.putExtra("vType", "发布");
                intent.setClass(mContext, ReleaseActivity.class);
                startActivity(intent);
                break;
            case R.id.generalize_release_right:
                intent.putExtra("vType", "我的发布");
                intent.setClass(mContext, ReleaseShareActivity.class);
                startActivity(intent);
                break;

        }
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
