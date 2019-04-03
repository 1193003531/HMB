package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.server.MarkatRewardLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 营销奖励-转入余额
 */
public class MarketingRewardToWalletActivity extends BaseActivity {

    private String mType = "";

    //输入金额
    private EditText _input_money_et;
    private ImageView _input_money_del;
    //余额,确认提现
    private TextView _all_money_tv, _sure_withdrawal_btn;
    private String _input_money_value = "0", _all_money_value = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_marketing_reward_to_wallet);
        mActivity = this;
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);


        initView();
    }


    /***/
    private void initView() {
        _input_money_et = findViewById(R.id.marketing_reward_to_wallet_money);
        _input_money_del = findViewById(R.id.marketing_reward_to_wallet_money_del);
        _all_money_tv = findViewById(R.id.marketing_reward_to_wallet_t_money);
        _sure_withdrawal_btn = findViewById(R.id.marketing_reward_to_wallet_sure);

        _input_money_et.addTextChangedListener(moneyWatcher);

        _all_money_value = XPreferencesUtils.get("reward", "0").toString();
        // _all_money_value = "30";
        _all_money_tv.setText("可转入余额 " + _all_money_value + "元");
    }


    public void onAction(View v) {
        switch (v.getId()) {
            //删除输入金额
            case R.id.marketing_reward_to_wallet_money_del:
                _input_money_et.setText("");
                break;
            //全部金额
            case R.id.marketing_reward_to_wallet_all:
                _input_money_et.setText("" + _all_money_value);
                break;
            //提现
            case R.id.marketing_reward_to_wallet_sure:
                _input_money_value = _input_money_et.getText().toString().trim();
                if (XStringUtils.m1(_input_money_value) == 0) {
                    ToastUtils.showCenter("请输入金额");
                } else {
                    getMarketingRewardToWallet(_input_money_value);
                }
                break;
        }
    }

    /**
     * 金额输入监听
     */
    private TextWatcher moneyWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _input_money_del.setVisibility(View.VISIBLE);
                _sure_withdrawal_btn.setBackgroundResource(R.drawable.btn_blue_r5_bg);
                if (XStringUtils.m1(s.toString()) > XStringUtils.m1(_all_money_value)) {
                    _input_money_et.setText("" + _all_money_value);
                }

            } else {
                _input_money_del.setVisibility(View.GONE);
                _sure_withdrawal_btn.setBackgroundResource(R.drawable.btn_hui_r5_bg);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 转入
     */
    private void getMarketingRewardToWallet(String money) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("money", money);
        MarkatRewardLogic.Instance(mActivity).marketingRewardToWalletApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("ToWallet:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        showToast("转入成功");
                        XPreferencesUtils.put("MarketingRewardToWallet", true);
                        finish();
                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    LogUtils.debug("ToWallet:" + e.toString());
                    e.printStackTrace();
                    showToast("转入失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("ToWallet:" + error);
                showToast("转入失败");
            }
        });
    }

    /**
     * 成功or失败
     */
    private void resultData(final String result) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(WithdrawalDetailsActivity.class, result);
            }
        });
    }


}
