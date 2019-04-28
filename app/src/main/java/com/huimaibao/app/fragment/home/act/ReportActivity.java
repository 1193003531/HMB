package com.huimaibao.app.fragment.home.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.adapter.ReportAdapter;
import com.huimaibao.app.fragment.home.entity.ReportEntity;
import com.huimaibao.app.fragment.mine.server.GeneralLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.NoScrollListView;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 举报
 */
public class ReportActivity extends BaseActivity {

    private String mType = "";

    private NoScrollListView mListView;
    private EditText _report_other_et;
    private TextView _submit_btn;

    private ReportAdapter mAdapter;
    private List<ReportEntity> listData;
    private List<String> reportL;
    private String[] reportData = {"垃圾营销", "涉黄信息", "违法信息", "人身攻击我", "侵权"};
    //其他,举报目标,举报类型
    private String _report_other_value = "", _target_value = "", _type_value = "";

    //是否存在选择
    private boolean isCHeck = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_report);

        setNeedBackGesture(true);

        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
            _target_value = intent.getStringExtra("vUrl");
        }
        setTopTitle("举报");
        setShowLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        initView();
        initData();
    }

    /***/
    private void initView() {
        mListView = findViewById(R.id.report_list);
        _report_other_et = findViewById(R.id.report_other_ed);
        _submit_btn = findViewById(R.id.report_submit_btn);
        _submit_btn.setEnabled(false);
        _report_other_et.addTextChangedListener(otherWatcher);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position).getReportIsCheck()) {
                    listData.get(position).setReportIsCheck(false);
                } else {
                    listData.get(position).setReportIsCheck(true);
                }


                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getReportIsCheck()) {
                        isCHeck = true;
                        break;
                    } else {
                        isCHeck = false;
                    }
                }
                mAdapter.notifyDataSetChanged();

                //提交按钮背景变化
                _report_other_value = _report_other_et.getText().toString();
                if (isCHeck && !XEmptyUtils.isSpace(_report_other_value)) {
                    _submit_btn.setBackgroundResource(R.drawable.btn_blue_r5_bg);
                    _submit_btn.setEnabled(true);
                } else {
                    _submit_btn.setBackgroundResource(R.drawable.btn_hui_r5_bg);
                    _submit_btn.setEnabled(false);
                }
            }
        });
    }

    /***/
    private void initData() {
        //投诉类型（（1.互推圈投诉，2.文库投诉，3.文库评论投诉，4.个人网页投诉，5.动态投诉，6.动态评论投诉,7.名片'）
        if (mType.equals("名片")) {
            _type_value = "7";
        } else if (mType.equals("互推圈")) {
            _type_value = "1";
        } else if (mType.equals("文章")) {
            _type_value = "2";
        } else if (mType.equals("网页")) {
            _type_value = "4";
        } else if (mType.equals("动态")) {
            _type_value = "5";
        }


        listData = new ArrayList<>();
        for (int i = 0; i < reportData.length; i++) {
            ReportEntity entity = new ReportEntity();
            entity.setReportId("" + i);
            entity.setReportIsCheck(false);
            entity.setReportName(reportData[i]);
            listData.add(entity);
        }

        mAdapter = new ReportAdapter(mActivity, listData);
        mListView.setAdapter(mAdapter);
    }

    /***/
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.report_submit_btn:
                _report_other_value = _report_other_et.getText().toString();
                if (!isCHeck) {
                    ToastUtils.showCenter("请选择投诉原因");
                } else if (XEmptyUtils.isSpace(_report_other_value)) {
                    ToastUtils.showCenter("请输入其他理由");
                } else {
                    reportL = new ArrayList<>();
                    for (int i = 0; i < listData.size(); i++) {
                        if (listData.get(i).getReportIsCheck()) {
                            reportL.add(listData.get(i).getReportName());
                        }
                    }

                    getComplaint(reportL.toString().replace("[", "").replace("]", "").trim(), _report_other_value);
                }
                break;
        }
    }

    /**
     * 其他理由输入监听
     */
    private TextWatcher otherWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (isCHeck && s.length() > 0) {
                _submit_btn.setBackgroundResource(R.drawable.btn_blue_r5_bg);
                _submit_btn.setEnabled(true);
            } else {
                _submit_btn.setBackgroundResource(R.drawable.btn_hui_r5_bg);
                _submit_btn.setEnabled(false);
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
     * 举报
     */
    private void getComplaint(String item, String content) {
        HashMap<String, Object> map = new HashMap<>();
//        map.put("target", Integer.parseInt(_target_value));
//        map.put("type", Integer.parseInt(_type_value));
//        map.put("item", item);
//        map.put("content", content);

        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("content_id", _target_value);
        map.put("complaint_type", _type_value);
        map.put("title", item);
        map.put("advise", content);

        LogUtils.debug("json:" + map);
        GeneralLogic.Instance(mActivity).getComplaintApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("举报成功");
                                finish();
                            }
                        });
                    } else {
                        showToast("举报失败," + json.getString("message"));
                    }
                } catch (Exception e) {
                    LogUtils.debug("json:" + e.toString());
                    showToast("举报失败");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("json:" + error);
                showToast("举报失败");
            }
        });
    }


}
