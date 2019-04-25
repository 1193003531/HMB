package com.huimaibao.app.fragment.mine.act;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.entity.VipEntity;
import com.huimaibao.app.fragment.mine.server.MemberLogic;
import com.huimaibao.app.fragment.mine.server.WalletLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.pay.PayActivity;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XHorizontalScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员中心
 */
public class MemberActivity extends BaseActivity {

    private String mType = "";
    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;

    private CircleImageView _head_image;
    private TextView _head_name, _vip_type, _vip_date, _total_money, _payment_btn;
    private String _total_money_value = "0", _pay_type_id_value = "1", _type_value = "";
    private int level;//等级
    private String time;//到期时间


    private String[] VipName = {"月度会员", "季度会员", "年度会员", "超级会员"};
    private String[] VipPrice = {"39", "90", "320", "698"};
    private String[] VipMoney = {"￥55", "￥120", "￥468", "￥960"};
    private String[] VipDay = {"30天", "90天", "365天", "1095天"};

    private List<VipEntity> vipData;

    private ArrayList<Boolean> isCheckData = new ArrayList<>();
    private XHorizontalScrollView mXHorizontalScrollView;
    LinearLayout mRadioGroup_content;

    private int mScreenWidth = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_member);
        setNeedBackGesture(true);

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
        _top_layout.setBackgroundResource(R.drawable.member_title_bg);
        params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
        View mViewNeedOffset = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageView(mActivity, 0, mViewNeedOffset);
    }

    /***/
    private void initView() {
        mScreenWidth = XDensityUtils.getScreenWidth();

        _head_image = findViewById(R.id.member_vip_head_image);
        _head_name = findViewById(R.id.member_vip_head_name);
        _vip_type = findViewById(R.id.member_vip_type);
        _vip_date = findViewById(R.id.member_vip_date);
        _total_money = findViewById(R.id.member_vip_t_money);
        _payment_btn = findViewById(R.id.member_vip_open_btn);

        mXHorizontalScrollView = findViewById(R.id.member_vip_hs);
        mRadioGroup_content = findViewById(R.id.member_vip_ll);
        mXHorizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 移动的起点
                        setNeedBackGesture(false);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 移动的终点距离
                        //mUpY = ev.getY();
                        setNeedBackGesture(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 移动的终点
                        setNeedBackGesture(true);
                        break;
                }
                return false;
            }
        });


    }

    /***/
    private void initData() {
        ImageLoaderManager.loadImage(XPreferencesUtils.get("portrait", "").toString(), _head_image, R.drawable.ic_launcher);
        _head_name.setText("" + XPreferencesUtils.get("name", ""));
        if (XPreferencesUtils.get("vip_level", "0").equals("0")) {
            _vip_type.setText("普通用户");
            _vip_date.setText("未开通会员");
        } else {
            getProfileVipData();
        }
        getWallet();
        getProductsVipData();
        // setVipList();
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.member_vip_open_btn:
                Intent intent = new Intent(mActivity, PayActivity.class);
                intent.putExtra("vType", "开通会员");
                intent.putExtra("payType", _type_value);
                intent.putExtra("payMoney", _total_money_value);
                intent.putExtra("payId", _pay_type_id_value);
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    /**
     * vip列表
     */
    private void setVipList() {
        mRadioGroup_content.removeAllViews();
        mXHorizontalScrollView.setParam(mActivity, mScreenWidth, mRadioGroup_content, null, null, null, null);
        for (int i = 0; i < vipData.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 15;
            params.rightMargin = 15;
            View v = LayoutInflater.from(mActivity).inflate(R.layout.act_mine_member_item, null);
            final LinearLayout itemLl = v.findViewById(R.id.member_vip_item_ll);
            final TextView itemName = v.findViewById(R.id.member_vip_item_name);
            final TextView itemPrice = v.findViewById(R.id.member_vip_item_price);
            TextView itemMoney = v.findViewById(R.id.member_vip_item_money);
            TextView itemDay = v.findViewById(R.id.member_vip_item_day);
            final ImageView itemTrue = v.findViewById(R.id.member_vip_item_true);
            //添加删除线
            itemMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            v.setId(i);
            itemLl.setId(i);
            itemPrice.setId(i);
            itemName.setId(i);
            itemTrue.setId(i);
            itemDay.setId(i);

            itemName.setText(vipData.get(i).getVipName());
            itemPrice.setText("￥" + vipData.get(i).getVipMoney());
            itemMoney.setText("￥" + vipData.get(i).getVipLastMoney());
            itemDay.setText(vipData.get(i).getVipDays() + "天");
//            if (i == 0) {
//                itemLl.setBackground(getResources().getDrawable(R.drawable.btn_member_vip_true_bg));
//            } else {
//                itemLl.setBackground(getResources().getDrawable(R.drawable.btn_member_vip_false_bg));
//            }

            if (isCheckData.get(i)) {
                itemLl.setBackground(getResources().getDrawable(R.drawable.btn_member_vip_true_bg));
                itemTrue.setVisibility(View.VISIBLE);
            } else {
                itemLl.setBackground(getResources().getDrawable(R.drawable.btn_member_vip_false_bg));
                itemTrue.setVisibility(View.INVISIBLE);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // refresh(v.getId());
                    _pay_type_id_value = vipData.get(v.getId()).getVipId();
                    _total_money_value = vipData.get(v.getId()).getVipMoney();
                    _type_value = "汇脉宝" + itemName.getText().toString();
                    _total_money.setText("总计: " + _total_money_value + "元");
                    for (int i = 0; i < vipData.size(); i++) {
                        if (i == v.getId()) {
                            isCheckData.set(i, true);
                        } else {
                            isCheckData.set(i, false);
                        }
                    }
                    setVipList();
                }
            });
            mRadioGroup_content.addView(v, i, params);
        }
    }


    private void refresh(int position) {
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            if (j == position) {
                checkView.setBackground(getResources().getDrawable(R.drawable.btn_member_vip_true_bg));
            } else {
                checkView.setBackground(getResources().getDrawable(R.drawable.btn_member_vip_false_bg));
            }
        }
    }


    /**
     * 购买后刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isPaymentMoney", false)) {
            getProfileVipData();
            getUserInfo();
            XPreferencesUtils.put("isPaymentMoney", false);
            XPreferencesUtils.put("updateVip", true);
        }
    }

    /**
     * 获取vip用户信息
     */
    private void getProfileVipData() {
        //HashMap<String, Object> map = new HashMap<>();
        //map.put("page", page);
        MemberLogic.Instance(mActivity).profileVipApi(null, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("home=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");

                    if (status.equals("0")) {
                        try {
                            level = json.getJSONObject("data").getInt("level");
                        } catch (Exception e) {
                            level = 0;
                        }

                        if (level != 0) {
                            time = json.getJSONObject("data").getString("expire_at");
                        }


                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (level) {
                                    case 0:
                                        _vip_type.setText("普通用户");
                                        _vip_date.setText("未开通会员");
                                        break;
                                    case 1:
                                        _vip_type.setText(VipName[0]);
                                        break;
                                    case 2:
                                        _vip_type.setText(VipName[1]);
                                        break;
                                    case 3:
                                        _vip_type.setText(VipName[2]);
                                        break;
                                    case 4:
                                        _vip_type.setText(VipName[3]);
                                        break;
                                }
                                if (level != 0) {
                                    _vip_date.setText(XTimeUtils.StringToYMD(time) + "到期");
                                    XPreferencesUtils.put("member_data", XTimeUtils.StringToYMD(time) + "到期");
                                } else {
                                    XPreferencesUtils.put("member_data", "开通会员");
                                }

                                if (!XEmptyUtils.isSpace(time)) {
                                    if (System.currentTimeMillis() < XTimeUtils.string2Millis(time)) {
                                        _payment_btn.setText("立即续费");
                                    } else {
                                        _payment_btn.setText("立即开通");
                                    }

                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    //XLog.d("error:" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
            }
        });
    }

    /**
     * 获取vip用户信息
     */
    private void getProductsVipData() {
        // HashMap<String, Object> map = new HashMap<>();
        //map.put("page", page);
        MemberLogic.Instance(mActivity).productsVipApi(null, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("vip=s=" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    String data = json.getString("data");

                    if (status.equals("0")) {
                        JSONArray array = new JSONArray(data);
                        vipData = new ArrayList<>();
                        isCheckData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            VipEntity entity = new VipEntity();
                            entity.setVipId(array.getJSONObject(i).getString("id"));
                            entity.setVipName(array.getJSONObject(i).getString("name"));
                            entity.setVipMoney(array.getJSONObject(i).getString("money"));
                            entity.setVipLastMoney(array.getJSONObject(i).getString("last_money"));
                            entity.setVipDays(array.getJSONObject(i).getString("days"));
                            if (i == 0) {
                                isCheckData.add(true);
                            } else {
                                isCheckData.add(false);
                            }
                            vipData.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _total_money_value = vipData.get(0).getVipMoney();
                                _total_money.setText("总计: " + vipData.get(0).getVipMoney() + "元");
                                _type_value = "汇脉宝" + vipData.get(0).getVipName();
                                setVipList();
                            }
                        });
                    }
                } catch (Exception e) {
                    //XLog.d("error:" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
            }
        });
    }

    /**
     * 刷新用户信息
     */
    private void getUserInfo() {
        LoginLogic.Instance(mActivity).getUserInfoApi("", false, null);
    }

    /***/
    private void getWallet() {
        WalletLogic.Instance(mActivity).getWalletApi(false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    XPreferencesUtils.put("money", json.getJSONObject("data").getString("money"));
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

}
