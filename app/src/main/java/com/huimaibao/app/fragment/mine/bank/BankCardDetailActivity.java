package com.huimaibao.app.fragment.mine.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;

/**
 * 银行卡详情
 */
public class BankCardDetailActivity extends BaseActivity {

    private String _bank_logo_value = "", _bank_card_name_value = "", _bank_card_num_value = "";

    private ImageView _bank_logo;
    private TextView _bank_card_name, _bank_card_num;
    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_bank_card_detail);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent.getAction().equals("two")) {
            Bundle bundle = intent.getExtras();
            _bank_card_name_value = bundle.getString("BankCardName");
            _bank_card_num_value = bundle.getString("BankCardNum");
            _bank_logo_value = bundle.getString("BankCardLogo");

        }
        mDialogUtils = new DialogUtils(mActivity);
        setTopTitle("银行卡");
        setShowLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(true, true, false, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.showBankDelDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(BankCardDelActivity.class, "");
                        mDialogUtils.dismissDialog();
                    }
                });

            }
        });

        initView();
    }

    /***/
    private void initView() {
        _bank_logo = findViewById(R.id.bank_card_detail_logo);
        _bank_card_name = findViewById(R.id.bank_card_detail_name);
        _bank_card_num = findViewById(R.id.bank_card_detail_num);
        _bank_card_name.setText(_bank_card_name_value + "储蓄卡");
        _bank_card_num.setText(XRegexUtils.cardIdHide2(_bank_card_num_value));
        ImageLoaderManager.loadImage(_bank_logo_value, _bank_logo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("BankCardAdd", false)) {
            finish();
        }
    }

}
