package com.huimaibao.app.fragment.mine.act;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.adapter.BankCardAdapter;
import com.huimaibao.app.fragment.mine.entity.BankCardEntity;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.fragment.mine.settings.VerifyPhoneActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.huimaibao.app.view.PayPsdInputView;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 提现
 */
public class WithdrawalActivity extends BaseActivity {

    private String mType = "";

    //银行卡信息
    private TextView _bank_card_name_tv, _bank_crad_num_tv;
    private String _bank_name_v = "", _bank_crad_num_v = "";
    //输入金额
    private EditText _input_money_et;
    private ImageView _input_money_del;
    //余额,确认提现
    private TextView _all_money_tv, _sure_withdrawal_btn;
    private String _input_money_value = "0", _all_money_value = "0";

    private BankCardAdapter mAdapter;
    private List<BankCardEntity> ListData;

    private DialogUtils mDialogUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_withdrawal);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        mDialogUtils = new DialogUtils(mActivity);

        initView();
        initData();
    }

    /***/
    private void initView() {
        _bank_card_name_tv = findViewById(R.id.withdrawal_bank_card_name);
        _bank_crad_num_tv = findViewById(R.id.withdrawal_bank_card_num);
        _input_money_et = findViewById(R.id.withdrawal_money);
        _input_money_del = findViewById(R.id.withdrawal_money_del);
        _all_money_tv = findViewById(R.id.withdrawal_t_money);
        _sure_withdrawal_btn = findViewById(R.id.withdrawal_sure);

        _input_money_et.addTextChangedListener(moneyWatcher);

        _all_money_value = XPreferencesUtils.get("money", "0").toString();
        _all_money_tv.setText("可提现余额 " + _all_money_value + "元");

    }

    /***/
    private void initData() {
        try {
            if (!XEmptyUtils.isEmpty(XPreferencesUtils.get("BankCardList", ""))) {
                JSONArray array = new JSONArray(XPreferencesUtils.get("BankCardList", "").toString());
                ListData = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    BankCardEntity entity = new BankCardEntity();
                    entity.setBankCardId(array.getJSONObject(i).getString("id"));
                    entity.setBankCardName(array.getJSONObject(i).getString("bank_name"));
                    entity.setBankCardNum(array.getJSONObject(i).getString("card_no"));
                    entity.setBankCardType("储蓄卡");
                    entity.setBankName(array.getJSONObject(i).getString("name"));
                    if (i == 0)
                        entity.setBankCardCheck(true);
                    else
                        entity.setBankCardCheck(false);
                    ListData.add(entity);
                }
                if (ListData.size() > 0) {
                    _bank_name_v = ListData.get(0).getBankName();
                    _bank_crad_num_v = ListData.get(0).getBankCardNum();
                    _bank_card_name_tv.setText(ListData.get(0).getBankCardName() + "储蓄卡");
                    _bank_crad_num_tv.setText(XRegexUtils.cardIdHide2(_bank_crad_num_v));
                }
            } else {
                getBankCard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onAction(View v) {
        switch (v.getId()) {
            //银行卡信息弹出框
            case R.id.withdrawal_bank_card_ll:
                showBankListDialog();
                break;
            //删除输入金额
            case R.id.withdrawal_money_del:
                _input_money_et.setText("");
                break;
            //全部金额
            case R.id.withdrawal_money_all:
                _input_money_et.setText("" + _all_money_value);
                break;
            //提现
            case R.id.withdrawal_sure:
                _input_money_value = _input_money_et.getText().toString().trim();
                if (XEmptyUtils.isSpace(_input_money_value)) {
                    ToastUtils.showCenter("请输入提现金额");
                } else if (XStringUtils.m1(_input_money_value) < 30) {
                    ToastUtils.showCenter("提现金额不足30元,不能提现哦");
                } else {
                    mDialogUtils.showBankPayPwdDialog(new PayPsdInputView.onPasswordListener() {
                        @Override
                        public void onDifference(String oldPsd, String newPsd) {

                        }

                        @Override
                        public void onEqual(String psd) {

                        }

                        @Override
                        public void inputFinished(String inputPsd) {
                            _input_money_et.setText("");
                            getWalletCash(_input_money_value, inputPsd);
                            mDialogUtils.dismissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(VerifyPhoneActivity.class, "忘记支付密码");
                            mDialogUtils.dismissDialog();
                        }
                    });
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
     * 银行卡列表弹出框
     */
    public void showBankListDialog() {

        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_bank_list_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
//        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, XDensityUtils.dp2px(314));
        dialog.show();

        ListView mListView = dialog.findViewById(R.id.dialog_bank_card_list);
        mAdapter = new BankCardAdapter(mActivity, ListData, "1");
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < ListData.size(); i++) {
                    if (i == position) {
                        ListData.get(position).setBankCardCheck(true);
                        // ListData.set(position, ListData.get(position));
                        _bank_card_name_tv.setText(ListData.get(position).getBankCardName() + ListData.get(position).getBankCardType());
                        _bank_crad_num_tv.setText(XRegexUtils.cardIdHide2(ListData.get(position).getBankCardNum()));
                        _bank_name_v = ListData.get(position).getBankName();
                        _bank_crad_num_v = ListData.get(position).getBankCardNum();
                    } else {
                        ListData.get(i).setBankCardCheck(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.dialog_bank_card_list_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 提现
     */
    private void getWalletCash(String money, String pwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("amount", money);
        map.put("pwd", pwd);
        map.put("bank_card_no", _bank_crad_num_v);
        map.put("bank_name", _bank_name_v);
        WalletLogic.Instance(mActivity).getWalletCashApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.e("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        resultData("成功");
                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    //XLog.e("e:" + e.toString());
                    e.printStackTrace();
                    resultData("失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                resultData("失败");
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


    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("Withdrawal", false)) {
            finish();
        }
    }

    /***/
    private void getBankCard() {
        WalletLogic.Instance(mActivity).getBankCardApi(true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        ListData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            BankCardEntity entity = new BankCardEntity();
                            entity.setBankCardId(array.getJSONObject(i).getString("id"));
                            entity.setBankCardName(array.getJSONObject(i).getString("bank_name"));
                            entity.setBankCardNum(array.getJSONObject(i).getString("card_no"));
                            entity.setBankCardType("储蓄卡");
                            entity.setBankName("持卡人");
                            if (i == 0)
                                entity.setBankCardCheck(true);
                            else
                                entity.setBankCardCheck(false);
                            ListData.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (ListData.size() > 0) {
                                    _bank_name_v = ListData.get(0).getBankName();
                                    _bank_crad_num_v = ListData.get(0).getBankCardNum();
                                    _bank_card_name_tv.setText(ListData.get(0).getBankCardName() + "储蓄卡");
                                    _bank_crad_num_tv.setText(XRegexUtils.cardIdHide2(_bank_crad_num_v));
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
            }
        });
    }
}
