package com.huimaibao.app.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseApplication;
import com.youth.xframe.pickers.common.LineConfig;
import com.youth.xframe.pickers.entity.Province;
import com.youth.xframe.pickers.listeners.OnLinkageListener;
import com.youth.xframe.pickers.picker.AddressPicker;
import com.youth.xframe.pickers.util.ConvertUtils;

import java.util.ArrayList;


/**
 * 获取地址数据并显示地址选择器
 */
public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private Activity activity;
    //private ProgressDialog dialog;
    private DialogUtils mDialogUtils;
    private Callback callback;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    public AddressPickTask(Activity activity) {
        this.activity = activity;
        mDialogUtils = new DialogUtils(activity);
    }

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        //dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
        mDialogUtils.showLoadingDialog("初始化数据...");
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<Province> data = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(activity.getAssets().open("area.json"));
            data.addAll(JSON.parseArray(json, Province.class));
            BaseApplication.getApp().getAreaData().addAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
        mDialogUtils.dismissDialog();
        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker(activity, result);
            picker.setCanLoop(false);
            picker.setHideProvince(hideProvince);
            picker.setHideCounty(hideCounty);
            picker.setTitleText("选择地址");
            picker.setTitleTextColor(activity.getResources().getColor(R.color.color000000));
            picker.setTitleTextSize(16);
            picker.setCancelTextColor(activity.getResources().getColor(R.color.color999999));
            picker.setCancelTextSize(14);
            picker.setSubmitTextColor(activity.getResources().getColor(R.color.FF274FF3));
            picker.setSubmitTextSize(14);
            picker.setSelectedTextColor(activity.getResources().getColor(R.color.color000000));
            picker.setUnSelectedTextColor(activity.getResources().getColor(R.color.color999999));
            LineConfig config = new LineConfig();
            config.setColor(activity.getResources().getColor(R.color.linee7e7e7));//线颜色
            //config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
            picker.setLineConfig(config);
            picker.setWheelModeEnable(true);
            if (hideCounty) {
                picker.setColumnWeight(1 / 3.0f, 2 / 3.0f);//将屏幕分为3份，省级和地级的比例为1:2
            } else {
                picker.setColumnWeight(2 / 8.0f, 3 / 8.0f, 3 / 8.0f);//省级、地级和县级的比例为2:3:3
            }
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnLinkageListener(callback);
            picker.show();
        } else {
            callback.onAddressInitFailed();
        }
    }

    public interface Callback extends OnLinkageListener {

        void onAddressInitFailed();

    }

}
