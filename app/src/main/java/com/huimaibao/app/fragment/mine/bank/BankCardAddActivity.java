package com.huimaibao.app.fragment.mine.bank;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.bank.bean.BankInfoBean;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 添加银行卡
 */
public class BankCardAddActivity extends BaseActivity {

    private String mType = "";
    //持卡人，卡号，开户行
    private EditText _bank_cardholder, _bank_num, _bank_of_deposit;
    //银行卡
    private TextView _bank_name;
    private String _bank_cardholder_v = "", _bank_num_v = "", _bank_name_value = "", _bank_of_deposit_value = "";

    private BankInfoBean bankinfobean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_bank_card_add);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setShowLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        initView();
    }

    /***/
    private void initView() {
        _bank_cardholder = findViewById(R.id.bank_add_cardholder);
        _bank_num = findViewById(R.id.bank_add_number);
        _bank_name = findViewById(R.id.bank_add_name);
        _bank_of_deposit = findViewById(R.id.bank_add_bank_of_deposit);
        _bank_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                _bank_num_v = s.toString().trim();
                LogUtils.debug("_bank_num_v:" + _bank_num_v);
                if (_bank_num_v != null && checkBankCard(_bank_num_v)) {
                    bankinfobean = new BankInfoBean(_bank_num_v);
                    _bank_name.setText(bankinfobean.getBankName());
                    // tv_cardtype.setText(bankinfobean.getCardType());
                } else {
                    _bank_name.setText("");
                }
//                else {
//                     showToast("卡号 " + _bank_num_v + " 不合法,请重新输入");
//                }
            }
        });

    }


    public void onAction(View v) {
        switch (v.getId()) {
//            case R.id.bank_add_toast:
//                DialogUtils.of(mActivity).showNoSureDialog("暂时只支持工商银行卡", "确定");
//                break;
            case R.id.bank_add_name:
                _bank_num_v = _bank_num.getText().toString();
                if (XEmptyUtils.isSpace(_bank_num_v)) {
                    ToastUtils.showCenter("请输入银行卡号");
                } else {
                    if (_bank_num_v != null && checkBankCard(_bank_num_v)) {
                        bankinfobean = new BankInfoBean(_bank_num_v);
                        _bank_name.setText(bankinfobean.getBankName());
                        // tv_cardtype.setText(bankinfobean.getCardType());
                    } else {
                        showToast("卡号 " + _bank_num_v + " 不合法,请重新输入");
                    }
                    if (_bank_name.getText().toString().trim().equals("未知")) {
                        getBankCardAdd(_bank_num_v);
                    }
                }
                break;
            case R.id.bank_add_sure:
                _bank_cardholder_v = _bank_cardholder.getText().toString();
                _bank_num_v = _bank_num.getText().toString();
                _bank_name_value = _bank_name.getText().toString();
                _bank_of_deposit_value = _bank_of_deposit.getText().toString();
                if (XEmptyUtils.isSpace(_bank_cardholder_v)) {
                    ToastUtils.showCenter("请输入持卡人姓名");
                } else if (XEmptyUtils.isSpace(_bank_num_v)) {
                    ToastUtils.showCenter("请输入银行卡号");
                } else if (XEmptyUtils.isSpace(_bank_of_deposit_value)) {
                    ToastUtils.showCenter("请输入开户行");
                } else {
                    getBankCardAdd(_bank_name_value, _bank_num_v, _bank_cardholder_v, _bank_of_deposit_value);
                }
                break;
        }
    }


    /**
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 添加
     */
    private void getBankCardAdd(String bank_type, String bank_num, String cardholder, String bank_of_deposit) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_name", bank_type);
        map.put("card_no", bank_num);
        map.put("card_no_confirmation", bank_num);
        map.put("name", cardholder);
        map.put("opening_bank", bank_of_deposit);
        WalletLogic.Instance(mActivity).getBankCardAddApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("添加成功");
                                XPreferencesUtils.put("BankCardAdd", true);
                                finish();
                            }
                        });

                    } else {
                        showToast("添加失败," + msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("添加失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showToast("添加失败");
            }
        });
    }

    /**
     * 添加
     */
    private void getBankCardAdd(String bank_num) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("card_no", bank_num);
        WalletLogic.Instance(mActivity).getBankInfoApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    String msg = json.getString("message");
                    LogUtils.debug("json:" + json);
                    if (json.getString("status").equals("0")) {
                        final JSONObject data = new JSONObject(json.optString("data"));
/** "id":4,
 "bankname":"中国工商银行",
 "bankcode":"ICBC",
 "card_type":"DC",
 "card_type_name":"储蓄卡"*/
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _bank_name.setText(data.optString("bankname", ""));
                            }
                        });

                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //showToast("添加失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                //showToast("添加失败");
            }
        });
    }

}
