package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.youth.xframe.utils.XPreferencesUtils;

/**
 * 提现详情
 */
public class WithdrawalDetailsActivity extends BaseActivity {

    private String mType = "";
    private ImageView _image_iv;
    private TextView _text_tv_1, _text_tv_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_withdrawal_detail);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle("提现详情");
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        initView();
        initData();
    }

    /***/
    private void initView() {
        _image_iv = findViewById(R.id.withdrawal_detail_iv);
        _text_tv_1 = findViewById(R.id.withdrawal_detail_tv);
        _text_tv_2 = findViewById(R.id.withdrawal_detail_tv2);
    }

    /***/
    private void initData() {
        if (mType.equals("成功")) {
            _image_iv.setImageResource(R.drawable.wallet_tx_success_icon);
            _text_tv_1.setText("提现申请成功");
            _text_tv_2.setText("提现1~3个工作日之内到账，节假日除外");
        } else {
            _image_iv.setImageResource(R.drawable.wallet_tx_error_icon);
            _text_tv_1.setText("提现申请失败");
            _text_tv_2.setText("如有疑问,请拨打023-65739176");
        }
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.waller_detail_sure:
                finish();
                break;
        }
    }

}
