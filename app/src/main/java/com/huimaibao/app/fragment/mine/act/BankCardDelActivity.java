package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.fragment.mine.settings.server.SettingLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.view.PayPsdInputView;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 解绑银行卡
 */
public class BankCardDelActivity extends BaseActivity {

    private String mType = "";

    private PayPsdInputView _pay_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_bank_card_del);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle("银行卡");
        setShoweLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        initView();

        XKeyboardUtils.openKeyboard(mActivity);
    }

    /***/
    private void initView() {
        _pay_pwd = findViewById(R.id.bank_card_del);
        _pay_pwd.setComparePassword(new PayPsdInputView.onPasswordListener() {
            @Override
            public void onDifference(String oldPsd, String newPsd) {

            }

            @Override
            public void onEqual(String psd) {
            }

            @Override
            public void inputFinished(String inputPsd) {
                validatePaymentPWD(inputPsd);
            }
        });
    }

    public void onAction(View v) {

    }

    /**
     * 验证旧支付密码
     */
    private void validatePaymentPWD(String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("password", password);
        SettingLogic.Instance(mActivity).validatePaymentPWDApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("rest=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        getBankCardDel();
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {
                    showToast("验证失败");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("验证失败");
            }
        });
    }

    /**
     * 删除银行卡
     */
    private void getBankCardDel() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WalletLogic.Instance(mActivity).getBankCardDelApi("" + XPreferencesUtils.get("BankCardId", ""), new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {
                        try {
                            JSONObject json = new JSONObject(object.toString());
                            //XLog.d("rest=s=" + json);
                            String status = json.getString("status");
                            String message = json.getString("message");
                            //String data = json.getString("data");
                            if (status.equals("0")) {
                                XPreferencesUtils.put("BankCardAdd", true);
                                showToast("删除成功");
                                finish();
                            } else {
                                showToast(message);
                            }
                        } catch (Exception e) {
                            showToast("删除失败");
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        showToast("删除失败");
                    }
                });
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        XKeyboardUtils.closeKeyboard(mActivity);
    }
}
