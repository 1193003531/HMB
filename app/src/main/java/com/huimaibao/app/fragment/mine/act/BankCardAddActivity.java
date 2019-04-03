package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 添加银行卡
 */
public class BankCardAddActivity extends BaseActivity {

    private String mType = "";

    private EditText _bank_card_name, _bank_card_num;
    private String _bank_card_name_v = "", _bank_card_num_v = "";

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
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        initView();
    }

    /***/
    private void initView() {
        _bank_card_name = findViewById(R.id.bank_add_name);
        _bank_card_num = findViewById(R.id.bank_add_number);
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.bank_add_toast:
                DialogUtils.of(mActivity).showNoSureDialog("暂时只支持工商银行卡", "确定");
                break;
            case R.id.bank_add_sure:
                _bank_card_name_v = _bank_card_name.getText().toString();
                _bank_card_num_v = _bank_card_num.getText().toString();
                if (XEmptyUtils.isSpace(_bank_card_name_v)) {
                    ToastUtils.showCenter("请输入持卡人姓名");
                } else if (XEmptyUtils.isSpace(_bank_card_num_v)) {
                    ToastUtils.showCenter("请输入银行卡号");
                } else {
                    getBankCardAdd(_bank_card_num_v, _bank_card_name_v);
                }
                break;
        }
    }


    /***/
    private void getBankCardAdd(String card_no, String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("bank_name", "工商银行");
        map.put("card_no", card_no);
        map.put("card_no_confirmation", card_no);
        map.put("name", name);
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
                        showToast(msg);
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

}
