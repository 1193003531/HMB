package com.huimaibao.app.fragment.mine.bank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.adapter.BankCardAdapter;
import com.huimaibao.app.fragment.mine.entity.BankCardEntity;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.NoScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行卡
 */
public class BankCardActivity extends BaseActivity {

    private View _needOffsetView;

    private String mType = "";
    private NoScrollListView mListView;
    private BankCardAdapter mAdapter;
    private List<BankCardEntity> ListData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_bank_card);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        initView();
        initData();
    }

    /***/
    private void initView() {
        _needOffsetView = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);
        mListView = findViewById(R.id.wallet_bank_add_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XPreferencesUtils.put("BankCardId", ListData.get(position).getBankCardId());

                Bundle bundle = new Bundle();
                bundle.putString("BankCardName", ListData.get(position).getBankCardName());
                bundle.putString("BankCardNum", ListData.get(position).getBankCardNum());
                bundle.putString("BankCardLogo", ListData.get(position).getBankCardLogoM());

                startActivity(BankCardDetailActivity.class, bundle);
            }
        });

    }

    /***/
    private void initData() {
        getBankCard();
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.wallet_bank_add_btn:
                startActivity(BankCardAddActivity.class, "添加银行卡");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("BankCardAdd", false)) {
            getBankCard();
            XPreferencesUtils.put("BankCardAdd", false);
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
                    LogUtils.debug("json:" + json);
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("data"));
                        ListData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            BankCardEntity entity = new BankCardEntity();
                            entity.setBankCardId(array.getJSONObject(i).getString("id"));
                            entity.setBankCardName(array.getJSONObject(i).getString("bank_name"));
                            entity.setBankCardNum(array.getJSONObject(i).getString("card_no"));
                            entity.setBankCardLogoM(array.getJSONObject(i).optString("multi_logo_path", ""));
                            entity.setBankCardLogoT(array.getJSONObject(i).optString("transparent_logo_path", ""));
                            entity.setBankCardLogoW(array.getJSONObject(i).optString("white_logo_path", ""));

                            if (XEmptyUtils.isSpace(array.getJSONObject(i).optString("begin_rgb", "fd6868"))) {
                                entity.setBankCardSColor(Color.parseColor("#" + "fd6868"));
                            } else {
                                entity.setBankCardSColor(Color.parseColor("#" + (array.getJSONObject(i).optString("begin_rgb", "fd6868"))));
                            }
                            if (XEmptyUtils.isSpace(array.getJSONObject(i).optString("end_rgb", "f94849"))) {
                                entity.setBankCardEColor(Color.parseColor("#" + "f94849"));
                            } else {
                                entity.setBankCardEColor(Color.parseColor("#" + (array.getJSONObject(i).optString("end_rgb", "f94849"))));
                            }

                            entity.setBankCardType("储蓄卡");
                            ListData.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ListData.size() == 0) {
                                    showToast("还没有银行卡");
                                }
                                mAdapter = new BankCardAdapter(mActivity, ListData, "0");
                                mListView.setAdapter(mAdapter);
                            }
                        });

                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.debug("json:" + e.toString());
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                LogUtils.debug("json:" + error);
            }
        });
    }

}
