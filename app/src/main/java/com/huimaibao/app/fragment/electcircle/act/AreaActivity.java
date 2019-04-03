package com.huimaibao.app.fragment.electcircle.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.electcircle.adapter.AreaAdapter;
import com.huimaibao.app.fragment.electcircle.entity.AreaEntity;
import com.huimaibao.app.fragment.electcircle.server.ElectLogic;
import com.huimaibao.app.fragment.mine.entity.IndustryEntity;
import com.huimaibao.app.http.ResultBack;
import com.youth.xframe.pickers.util.ConvertUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 推广区域
 */
public class AreaActivity extends BaseActivity {

    private String mType = "";
    private ListView mListView;
    private AreaAdapter mAdapter;
    private List<AreaEntity> listData;

    private TextView _area_tv;
    private String _area_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_elect_area);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        _area_tv = findViewById(R.id.elect_area_tv);
        mListView = findViewById(R.id.elect_area_list);
        _area_value = XPreferencesUtils.get("areaName", "全国").toString();
        _area_value = XEmptyUtils.isSpace(_area_value) ? "全国" : _area_value;
        _area_tv.setText(_area_value);

        listData = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(mActivity.getAssets().open("province.json"));
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                AreaEntity entity = new AreaEntity();
                entity.setAreaId(array.optJSONObject(i).getString("areaId"));
                entity.setAreaName(array.optJSONObject(i).getString("areaName"));
                if (array.optJSONObject(i).getString("areaName").contains(_area_value)) {
                    entity.setAreaIsShow(true);
                } else {
                    entity.setAreaIsShow(false);
                }
                listData.add(entity);
            }
            mAdapter = new AreaAdapter(mActivity, listData);
            mListView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getElectUser(listData.get(position).getAreaName(), listData.get(position).getAreaId());
            }
        });

    }

    /**
     * 设置素材
     */
    private void getElectUser(final String province, final String province_id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("province", province);
        map.put("province_id", province_id);
        ElectLogic.Instance(mActivity).getMaterialApi(map, true, "设置中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                XPreferencesUtils.put("areaName", province);
                                XPreferencesUtils.put("areaId", province_id);
                                XPreferencesUtils.put("isMaterial", true);
                                _area_tv.setText(province);
                                XToast.normal("设置成功");
                                finish();
                            }
                        });
                    } else {
                        showToast("设置失败,请重新选择区域");
                    }
                } catch (Exception e) {
                    LogUtils.debug("e:" + e);
                    showToast("设置失败,请重新选择区域");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
                showToast("设置失败,请重新选择区域");
            }
        });
    }

    /**
     * 设置提示
     */
    public void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
                XPreferencesUtils.put("areaName", "");
                XPreferencesUtils.put("areaId", "");
                XPreferencesUtils.put("isMaterial", false);
            }
        });
    }

}
