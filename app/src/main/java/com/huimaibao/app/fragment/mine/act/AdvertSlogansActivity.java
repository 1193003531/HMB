package com.huimaibao.app.fragment.mine.act;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XEmptyUtils;

/**
 * 广告标语
 */
public class AdvertSlogansActivity extends BaseActivity {

    //内容
    private EditText _as_content;
    private TextView _as_num;
    private String _as_content_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_advertising_slogans);
        setNeedBackGesture(true);

        setTopTitle("广告标语");
        setShoweLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(true, false, true, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _as_content_value = _as_content.getText().toString();
                if (XEmptyUtils.isSpace(_as_content_value)) {
                    ToastUtils.showCenter("请输入广告标语");
                } else {
                    ToastUtils.showCenter("" + _as_content.getText().toString());
                }
            }
        });

        initView();
        initData();
    }

    /***/
    private void initView() {
        _as_content = findViewById(R.id.advertising_slogans_content);
        _as_num = findViewById(R.id.advertising_slogans_num);
    }

    /***/
    private void initData() {
        _as_content.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;

            private int editStart;

            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = _as_content.getSelectionStart();

                editEnd = _as_content.getSelectionEnd();

                _as_num.setText("还可输入" + String.valueOf(30 - temp.length()) + "字");//此处需要进行强制类型转换

                if (temp.length() > 30) {//条件判断可以实现其他功能

                    s.delete(editStart - 1, editEnd);

                    int tempSelection = editStart;

                    _as_content.setText(s);

                    _as_content.setSelection(tempSelection);
                    ToastUtils.showCenter("你输入的字数已经超过了");
                }
            }
        });
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.wallet_bank_add_btn:
                startActivity(BankCardAddActivity.class, "添加银行卡");
                break;
        }
    }


    /***/
    private void getBankCard() {
//        WalletLogic.Instance(mActivity).getBankCardApi(true, new ResultBack() {
//            @Override
//            public void onSuccess(Object object) {
//                try {
//                    JSONObject json = new JSONObject(object.toString());
//                    String msg = json.getString("message");
//                    if (json.getString("status").equals("0")) {
//                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
//                        ListData = new ArrayList<>();
//                        for (int i = 0; i < array.length(); i++) {
//                            BankCardEntity entity = new BankCardEntity();
//                            entity.setBankCardId(array.getJSONObject(i).getString("id"));
//                            entity.setBankCardName(array.getJSONObject(i).getString("bank_name"));
//                            entity.setBankCardNum(array.getJSONObject(i).getString("card_no"));
//                            entity.setBankCardType("储蓄卡");
//                            ListData.add(entity);
//                        }
//
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (ListData.size() == 0) {
//                                    showToast("还没有银行卡");
//                                }
//                                mAdapter = new BankCardAdapter(mActivity, ListData, "0");
//                                mListView.setAdapter(mAdapter);
//                            }
//                        });
//
//                    } else {
//                        showToast(msg);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(String error) {
//                //XLog.e("error:" + error);
//            }
//        });
    }

}
