package com.huimaibao.app.fragment.electcircle.materialLib;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.electcircle.server.ElectLogic;
import com.huimaibao.app.fragment.home.server.CardLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.RoundedImagView;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 名片
 */
public class CardFragment extends BaseFragment {


    private LinearLayout mTopView;
    private View _TopView, _top_ll;
    private RoundedImagView _top_head;
    private ImageView _top_head_btn;
    private TextView _top_name, _top_jobs, _top_company, _top_phone, _top_wechat, _top_addr;

    private String _style_value = "", _head_value = "";
    private CheckBox _check_cb;
    private TextView _check_btn;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_e_m_l_card, container, false);

        initView(view);

        return view;
    }


    @Override
    protected void initData() {
//        initTopView(R.layout.act_home_card_2_view, R.drawable.card_1_view);
//        setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
//
        getCardDetails();

        if (XPreferencesUtils.get("material_style", "").equals("4")) {
            _check_cb.setChecked(true);
        }

    }


    private void initView(View v) {
        mTopView = v.findViewById(R.id.act_e_m_l_card_ll);
        _check_cb = v.findViewById(R.id.act_e_m_l_card_check);
        _check_btn = v.findViewById(R.id.act_e_m_l_card_btn);
//        _style_value = "" + XPreferencesUtils.get("style", "style3");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_check_cb.isChecked()) {
                    _check_cb.setChecked(false);
                } else {
                    _check_cb.setChecked(true);
                    getElectUser();
                }
            }
        });
    }

    /**
     * 顶部名片
     */
    private void setTopView() {
        if (_style_value.equals("style4")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_2_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style5")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_4_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style6")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_3_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style7")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_11_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.white);
        } else if (_style_value.equals("style9")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_12_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style10")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_5_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style11")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_6_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style12")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_9_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style13")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_13_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style14")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_10_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style15")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_7_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style16")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_14_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style17")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_8_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_1_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        }
    }

    /**
     * 初始化顶部控件
     */
    private void initTopView(int resource, int id) {
        mTopView.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        _TopView = getLayoutInflater().inflate(resource, null);

        _top_ll = _TopView.findViewById(R.id.card_top_ll);
        _top_head = _TopView.findViewById(R.id.card_top_head);
        _top_head_btn = _TopView.findViewById(R.id.card_top_head_btn);
        _top_head_btn.setVisibility(View.GONE);
        _top_name = _TopView.findViewById(R.id.card_top_name);
        _top_jobs = _TopView.findViewById(R.id.card_top_jobs);
        _top_company = _TopView.findViewById(R.id.card_top_company);
        _top_phone = _TopView.findViewById(R.id.card_top_phone);
        _top_wechat = _TopView.findViewById(R.id.card_top_wechat);
        _top_addr = _TopView.findViewById(R.id.card_top_addr);

        mTopView.addView(_TopView, params);

        _top_ll.setBackground(getResources().getDrawable(id));

        setTopText();

    }

    private void setTopTextColor(int nid, int jid, int oid) {
        _top_name.setTextColor(getResources().getColor(nid));
        _top_jobs.setTextColor(getResources().getColor(jid));
        _top_company.setTextColor(getResources().getColor(oid));
        _top_phone.setTextColor(getResources().getColor(oid));
        _top_wechat.setTextColor(getResources().getColor(oid));
        _top_addr.setTextColor(getResources().getColor(oid));
    }

    private void setTopText() {
        _head_value = XEmptyUtils.isSpace(XPreferencesUtils.get("logo", "") + "") ? XPreferencesUtils.get("portrait", "") + "" : XPreferencesUtils.get("logo", "") + "";
        ImageLoaderManager.loadImage(_head_value, _top_head, R.drawable.ic_default);
        _top_name.setText("" + XPreferencesUtils.get("name", ""));
        _top_jobs.setText("" + XPreferencesUtils.get("profession", ""));
        _top_company.setText("" + XPreferencesUtils.get("company", ""));
        _top_phone.setText("电话：" + XPreferencesUtils.get("phone", ""));

        if (XEmptyUtils.isSpace(""+XPreferencesUtils.get("wechat", ""))) {
            _top_wechat.setVisibility(View.GONE);
        } else {
            _top_wechat.setVisibility(View.VISIBLE);
            _top_wechat.setText("微信：" + XPreferencesUtils.get("wechat", ""));
        }

        if (XEmptyUtils.isSpace(""+XPreferencesUtils.get("address_detail", ""))) {
            _top_addr.setVisibility(View.GONE);
        } else {
            _top_addr.setVisibility(View.VISIBLE);
            _top_addr.setText("地址：" + XPreferencesUtils.get("address_detail", ""));
        }
    }


    /**
     * 设置素材
     */
    private void getElectUser() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("material_id", XPreferencesUtils.get("card_id", ""));
        map.put("material_style", "4");
        ElectLogic.Instance(mActivity).getMaterialApi(map, true, "设置素材中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("tElectUser:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                XPreferencesUtils.put("material_style", "4");
                                XPreferencesUtils.put("material_id", XPreferencesUtils.get("card_id", ""));
                                XPreferencesUtils.put("elect_lib_name", _top_name.getText());
                                XPreferencesUtils.put("isMaterial", true);
                                mActivity.finish();
                            }
                        });
                    }
                } catch (Exception e) {
                    LogUtils.debug("tElectUser:" + e);
                    showToast("设置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("tElectUser:" + error);
                showToast("设置失败");
            }
        });
    }

    /**
     * 设置提示
     */
    private void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
                XPreferencesUtils.put("material_style", "");
                XPreferencesUtils.put("material_id", "");
                XPreferencesUtils.put("elect_lib_name", "");
                XPreferencesUtils.put("isMaterial", false);
            }
        });
    }

    /**
     * 获取信息
     */
    private void getCardDetails() {
        CardLogic.Instance(mActivity).getCardForIdApi(new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                _style_value = "" + XPreferencesUtils.get("style", "style3");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTopView();
                        //setTopText();
                    }
                });

            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error" + error);
            }
        });
    }

}
