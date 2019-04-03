package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.zxing.encoding.EncodingHandler;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XPreferencesUtils;

/**
 * 我的二维码名片
 */
public class MyQRCodeActivity extends BaseActivity {
    private String mType = "";
    //二维码
    private ImageView mImageView;
    //头像,名称，职业，行业，公司
    private ImageView _Head_iv;
    private TextView _name_tv, _jobs_tv, _industry_tv, _company_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_my_qrcode);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);


        mImageView = findViewById(R.id.my_qrcode_image);
        _Head_iv = findViewById(R.id.card_item_image);
        _name_tv = findViewById(R.id.card_item_name);
        _jobs_tv = findViewById(R.id.card_item_jobs);
        _industry_tv = findViewById(R.id.card_item_industry);
        _company_tv = findViewById(R.id.card_item_company);
        initData();
    }

    private void initData() {
        ImageLoaderManager.loadImage(XPreferencesUtils.get("portrait", "").toString(), _Head_iv, R.drawable.ic_launcher);
        _name_tv.setText("" + XPreferencesUtils.get("name", ""));
        _jobs_tv.setText("" + XPreferencesUtils.get("profession", ""));
        //_industry_tv.setText(""+XPreferencesUtils.get("industry", ""));
        _company_tv.setText("" + XPreferencesUtils.get("company", ""));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                //"" + ((int)(Math.random()*90000)+100000)
                mImageView.setImageBitmap(EncodingHandler.createQRCode(ServerApi.CARD_URL + XPreferencesUtils.get("user_id", ""), XDensityUtils.getScreenWidth() - 200, XDensityUtils.getScreenWidth() - 200, ((BitmapDrawable) _Head_iv.getDrawable()).getBitmap()));
            }
        }, 200);


    }


    public void onAction(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
