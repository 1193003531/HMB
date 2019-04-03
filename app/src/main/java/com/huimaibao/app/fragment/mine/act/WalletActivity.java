package com.huimaibao.app.fragment.mine.act;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity {


    private DialogUtils mDialogUtils;
    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;

    private TextView _wallet_tv;
    private String _wallet_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_wallet);
        setNeedBackGesture(true);

        mDialogUtils = new DialogUtils(mActivity);
        initView();
        initData();
    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        _top_layout = findViewById(R.id.top_layout);
        params = _top_layout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        _top_layout.setLayoutParams(params);
        _top_layout.setBackgroundResource(R.drawable.wallet_top_bg);
        params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
        View mViewNeedOffset = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageView(mActivity, 0, mViewNeedOffset);
    }

    /***/
    private void initView() {
        _wallet_tv = findViewById(R.id.mine_wallet_top_money);
    }

    /***/
    private void initData() {
        //_wallet_tv.setText("30");
        getWallet();
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.wallet_zj_details_btn:
                startActivity(MoneyDetailActivity.class, "资金明细");
                break;
            case R.id.wallet_bank_btn:
                startActivity(BankCardActivity.class, "银行卡");
                break;
            case R.id.wallet_tx_btn:
                _wallet_value = _wallet_tv.getText().toString().trim();
                if (XStringUtils.m1(_wallet_value) < 30) {
                    mDialogUtils.showNoSureDialog("您的余额不足30元,不能提现哦", "确定");
                    return;
                }
                getBankCard();
                break;
        }
    }

    /***/
    private void getWallet() {
        WalletLogic.Instance(mActivity).getWalletApi(false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    _wallet_value = json.getJSONObject("data").getString("money");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _wallet_tv.setText(_wallet_value);
                            }
                        });

                    } else {
                        _wallet_value = "0";
                    }
                } catch (Exception e) {
                    _wallet_value = "0";
                }
            }

            @Override
            public void onFailed(String error) {
                _wallet_value = "0";
            }
        });
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
                        final JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));


                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (array.length() == 0) {
                                    mDialogUtils.showNoTitleDialog("您还没有绑定银行卡,请先绑定", "取消", "绑定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(BankCardAddActivity.class, "添加银行卡");
                                            mDialogUtils.dismissDialog();
                                        }
                                    });
                                } else {
                                    XPreferencesUtils.put("BankCardList", array);
                                    startActivity(WithdrawalActivity.class, "提现");
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

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("Withdrawal", false)) {
            getWallet();
            XPreferencesUtils.put("Withdrawal", false);
        }

        if ((boolean) XPreferencesUtils.get("BankCardAdd", false)) {
            getBankCard();
            XPreferencesUtils.put("BankCardAdd", false);
        }

    }

}
