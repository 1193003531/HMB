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
public class AddressPickTaskUtils {
    private Activity activity;


    private static AddressPickTaskUtils _Instance = null;

    public static AddressPickTaskUtils Instance(Activity activity) {
        _Instance = new AddressPickTaskUtils(activity);
        return _Instance;
    }

    public AddressPickTaskUtils(Activity activity) {
        this.activity = activity;
    }

    public void showAddressDialog(ArrayList<Province> result, String province, String city, String county, OnLinkageListener listener) {
        AddressPicker picker = new AddressPicker(activity, result);
        picker.setCanLoop(false);
        picker.setHideProvince(false);
        picker.setHideCounty(false);
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
        picker.setColumnWeight(1 / 3.0f, 2 / 3.0f);//将屏幕分为3份，省级和地级的比例为1:2
        picker.setSelectedItem(province, city, county);
        picker.setOnLinkageListener(listener);
        picker.show();
    }


}
