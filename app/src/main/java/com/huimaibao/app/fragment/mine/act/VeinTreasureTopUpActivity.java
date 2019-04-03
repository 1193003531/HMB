package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;

/**
 * 充值脉宝
 */
public class VeinTreasureTopUpActivity extends BaseActivity {

    private String mType = "";
    //
    private TextView _1000_tv, _1500_tv, _2000_tv, _2500_tv, _3000_tv;
    private EditText _auto_tv;

    //类型，支付金额，钱包
    private String _type_value = "", _pay_money_value = "1000", _wallet_value = "0";
    //钱包，微信，支付宝，支付
    private TextView _wallet_tv, _pay_wechat_tv, _pay_alipay_tv, _pay_btn;
    private Switch _wallet_btn;
    private CheckBox _pay_wechat_btn, _pay_alipay_btn;
    private boolean isOtherPay = true;
    private String _pay_type_value = "1";//0-支付宝，1-微信,3-钱包抵扣


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_vein_treasure_top_up);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        /***
         * 不可编辑状态：
         * editText.setFocusable(false);
         *
         * editText.setFocusableInTouchMode(false);
         *
         *
         *
         * 可编辑状态：
         *
         * editText.setFocusableInTouchMode(true);
         *
         * editText.setFocusable(true);
         *
         * editText.requestFocus();
         */

        initView();
        initData();
    }

    /***/
    private void initView() {
        _1000_tv = findViewById(R.id.verin_topup_1000_tv);
        _1500_tv = findViewById(R.id.verin_topup_1500_tv);
        _2000_tv = findViewById(R.id.verin_topup_2000_tv);
        _2500_tv = findViewById(R.id.verin_topup_2500_tv);
        _3000_tv = findViewById(R.id.verin_topup_3000_tv);
        _auto_tv = findViewById(R.id.verin_topup_auto_tv);
        _auto_tv.addTextChangedListener(autoWatcher);

        _wallet_tv = findViewById(R.id.verin_topup_wallet_tv);
        _wallet_btn = findViewById(R.id.verin_topup_wallet_btn);
        _pay_wechat_tv = findViewById(R.id.verin_topup_wechat_tv);
        _pay_alipay_tv = findViewById(R.id.verin_topup_alipay_tv);
        _pay_wechat_btn = findViewById(R.id.verin_topup_wechat_cb);
        _pay_alipay_btn = findViewById(R.id.verin_topup_alipay_cb);
        _pay_btn = findViewById(R.id.verin_topup_pay_btn);
    }


    /***/
    private void initData() {
        _wallet_value = XPreferencesUtils.get("money", "0").toString().trim();
        _wallet_tv.setText("账户余额共￥" + _wallet_value);
        _pay_btn.setText("实付款" + _pay_money_value + "元");
        _wallet_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setPayMoney();
                } else {
                    _wallet_tv.setText("账户余额共￥" + _wallet_value);
                    _pay_btn.setText("实付款" + _pay_money_value + "元");
                }
            }
        });
    }

    /**
     * 计算金额
     */
    private void setPayMoney() {
        if (XStringUtils.m1(_pay_money_value) >= XStringUtils.m1(_wallet_value)) {
            _wallet_tv.setText("账户余额共￥" + _wallet_value + ",抵￥" + _wallet_value);
            _pay_wechat_tv.setTextColor(getResources().getColor(R.color.color000000));
            _pay_alipay_tv.setTextColor(getResources().getColor(R.color.color000000));
            isOtherPay = true;
            _pay_btn.setText("实付款" + (XStringUtils.m1(_pay_money_value) - XStringUtils.m1(_wallet_value)) + "元");
        } else {
            _wallet_tv.setText("账户余额共￥" + _wallet_value + ",抵￥" + (XStringUtils.m1(_wallet_value) - XStringUtils.m1(_pay_money_value)));
            _pay_wechat_tv.setTextColor(getResources().getColor(R.color.color999999));
            _pay_alipay_tv.setTextColor(getResources().getColor(R.color.color999999));
            isOtherPay = false;
            _pay_wechat_btn.setChecked(false);
            _pay_alipay_btn.setChecked(false);
            _pay_btn.setText("账户抵扣" + _pay_money_value + "元");
            _pay_type_value = "3";
        }
    }


    public void onAction(View v) {
        switch (v.getId()) {
            //1000
            case R.id.verin_topup_1000_tv:
                _1000_tv.setBackgroundResource(R.drawable.btn_blue_w_r4_bg);
                _1500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _3000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _auto_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1000_tv.setTextColor(getResources().getColor(R.color.FF274FF3));
                _1500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _3000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setHint("其他数额");
                _pay_money_value = "1000";
                setPayMoney();
                break;
            //1500
            case R.id.verin_topup_1500_tv:
                _1000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1500_tv.setBackgroundResource(R.drawable.btn_blue_w_r4_bg);
                _2000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _3000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _auto_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _1500_tv.setTextColor(getResources().getColor(R.color.FF274FF3));
                _2000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _3000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setHint("其他数额");
                _pay_money_value = "1500";
                setPayMoney();
                break;
            //2000
            case R.id.verin_topup_2000_tv:
                _1000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2000_tv.setBackgroundResource(R.drawable.btn_blue_w_r4_bg);
                _2500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _3000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _auto_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _1500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2000_tv.setTextColor(getResources().getColor(R.color.FF274FF3));
                _2500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _3000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setHint("其他数额");
                _pay_money_value = "2000";
                setPayMoney();
                break;
            //2500
            case R.id.verin_topup_2500_tv:
                _1000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2500_tv.setBackgroundResource(R.drawable.btn_blue_w_r4_bg);
                _3000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _auto_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _1500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2500_tv.setTextColor(getResources().getColor(R.color.FF274FF3));
                _3000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setHint("其他数额");
                _pay_money_value = "2500";
                setPayMoney();
                break;
            //3000
            case R.id.verin_topup_3000_tv:
                _1000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _3000_tv.setBackgroundResource(R.drawable.btn_blue_w_r4_bg);
                _auto_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _1500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _3000_tv.setTextColor(getResources().getColor(R.color.FF274FF3));
                _auto_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setHint("其他数额");
                _pay_money_value = "3000";
                setPayMoney();
                break;
            //auto
            case R.id.verin_topup_auto_tv:
                _1000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _1500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _2500_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _3000_tv.setBackgroundResource(R.drawable.btn_hui_w_r4_bg);
                _auto_tv.setBackgroundResource(R.drawable.btn_blue_w_r4_bg);
                _1000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _1500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _2500_tv.setTextColor(getResources().getColor(R.color.color101010));
                _3000_tv.setTextColor(getResources().getColor(R.color.color101010));
                _auto_tv.setTextColor(getResources().getColor(R.color.FF274FF3));
                _auto_tv.setHint("");
                _pay_money_value = "0";
                setPayMoney();
                break;
            //微信支付
            case R.id.verin_topup_wechat_maoey_btn:
                if (isOtherPay) {
                    _pay_type_value = "1";
                    _pay_wechat_btn.setChecked(true);
                    _pay_alipay_btn.setChecked(false);
                }
                break;
            //支付宝支付
            case R.id.verin_topup_alipay_btn:
                if (isOtherPay) {
                    _pay_type_value = "0";
                    _pay_wechat_btn.setChecked(false);
                    _pay_alipay_btn.setChecked(true);
                }
                break;
            //支付
            case R.id.verin_topup_pay_btn:
                if (_pay_type_value.equals("0")) {

                }
                break;
        }
    }

    /**
     * 账号输入监听
     */
    private TextWatcher autoWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _pay_money_value = s.toString();
            } else {
                _pay_money_value = "0";
            }

            setPayMoney();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
