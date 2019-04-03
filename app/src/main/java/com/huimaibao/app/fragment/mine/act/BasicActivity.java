package com.huimaibao.app.fragment.mine.act;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.fragment.home.server.CardLogic;
import com.huimaibao.app.fragment.mine.adapter.IndustryAdapter;
import com.huimaibao.app.fragment.mine.entity.IndustryEntity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.AddressPickTaskUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.takePhone.LoadCallback;
import com.huimaibao.app.takePhone.TakePhoneHelper;
import com.huimaibao.app.takePhone.TakePhotoActivity;
import com.huimaibao.app.utils.AddressPickTask;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.pickers.common.LineConfig;
import com.youth.xframe.pickers.entity.City;
import com.youth.xframe.pickers.entity.County;
import com.youth.xframe.pickers.entity.Province;
import com.youth.xframe.pickers.listeners.OnItemPickListener;
import com.youth.xframe.pickers.listeners.OnLinkageListener;
import com.youth.xframe.pickers.picker.DatePicker;
import com.youth.xframe.pickers.picker.SinglePicker;
import com.youth.xframe.pickers.util.ConvertUtils;
import com.youth.xframe.takephoto.model.TResult;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 基本信息
 */
public class BasicActivity extends TakePhotoActivity {

    private String mType = "";
    private TakePhoneHelper takePhoneHelper;

    private ScrollView mScrollView;
    private String _head_value;
    private CircleImageView _head_iv;

    private EditText _name_et, _jobs_et, _company_et, _moto_et, _phone_et, _wechat_et, _addrs_et, _eamil_et, _web_et;
    private TextView _industry_tv, _sex_tv, _birthday_tv, _city_tv;

    private Switch _is_phone_s;
    //1-男，2-女
    private String _industry_value = "", _sex_value = "男", _birthday_value = "1990-01-01", _city_value = "", _name_value = "", _jobs_value = "", _company_value = "", _moto_value = "", _phone_value = "", _wechat_value = "", _addrs_value = "", _eamil_value = "", _web_value = "";
    private String _province_id_value = "", _city_id_value = "", _area_id_value = "";
    private boolean _is_phone_value = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_basic);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        takePhoneHelper = TakePhoneHelper.of(this);

        initView();
        initData();

    }

    /***/
    private void initView() {
        mScrollView = findViewById(R.id.basic_scrollview);
        _head_iv = findViewById(R.id.basic_head_image);
        _name_et = findViewById(R.id.basic_name);
        _jobs_et = findViewById(R.id.basic_jobs);
        _company_et = findViewById(R.id.basic_company);
        _moto_et = findViewById(R.id.basic_slogan);
        _phone_et = findViewById(R.id.basic_phone);
        _wechat_et = findViewById(R.id.basic_wechat);
        _addrs_et = findViewById(R.id.basic_addr);
        _eamil_et = findViewById(R.id.basic_email);
        _web_et = findViewById(R.id.basic_web);

        _industry_tv = findViewById(R.id.basic_industry);
        _sex_tv = findViewById(R.id.basic_sex);
        _birthday_tv = findViewById(R.id.basic_birthday);
        _city_tv = findViewById(R.id.basic_city);

        _is_phone_s = findViewById(R.id.basic_is_phone);
    }


    /***/
    private void initData() {
        _is_phone_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    _is_phone_value = true;
                } else {
                    _is_phone_value = false;
                }
            }
        });

        getUserDetails();

    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.basic_head_image:
                takePhoneHelper.setTakePhone(1, true, true, 100, 100, true, 102400, 0, 0);
                takePhoneHelper.showTakePhoneDialog(getTakePhoto());
                break;
            case R.id.basic_industry:
                showIndustryDialog();
                break;
            case R.id.basic_sex:
                setSexPicker();
                break;
            case R.id.basic_birthday:
                int year = 1990, month = 01, day = 01;
                if (!XEmptyUtils.isSpace(_birthday_value)) {
                    String[] bir = _birthday_value.split("-");
                    try {
                        year = Integer.parseInt(bir[0].trim());
                    } catch (Exception e) {
                        year = 1990;
                    }
                    try {
                        month = Integer.parseInt(bir[1].trim());
                    } catch (Exception e) {
                        month = 01;
                    }
                    try {
                        day = Integer.parseInt(bir[2].trim());
                    } catch (Exception e) {
                        day = 01;
                    }
                    setYearMonthDayPicker(year, month, day);
                } else {
                    setYearMonthDayPicker(year, month, day);
                }
                break;
            case R.id.basic_city:
                String pr = "重庆市", ci = "重庆市", co = "渝北区";
                if (!XEmptyUtils.isSpace(_city_value)) {
                    String[] prs = _city_value.split(" ");
                    try {
                        pr = prs[0].trim();
                    } catch (Exception e) {
                        pr = "重庆市";
                    }
                    try {
                        ci = prs[1].trim();
                    } catch (Exception e) {
                        ci = "重庆市";
                    }
                    try {
                        co = prs[2].trim();
                    } catch (Exception e) {
                        co = "渝北区";
                    }
                    setAddressPicker(pr, ci, co);
                } else {
                    setAddressPicker(pr, ci, co);
                }

                break;
            case R.id.basic_save:
                _industry_value = _industry_tv.getText().toString();
                _sex_value = _sex_tv.getText().toString();
                _birthday_value = _birthday_tv.getText().toString();
                _city_value = _city_tv.getText().toString();
                _name_value = _name_et.getText().toString();
                _jobs_value = _jobs_et.getText().toString();
                _company_value = _company_et.getText().toString();
                _moto_value = _moto_et.getText().toString();
                _phone_value = _phone_et.getText().toString();
                _wechat_value = _wechat_et.getText().toString();
                _addrs_value = _addrs_et.getText().toString();
                _eamil_value = _eamil_et.getText().toString();
                _web_value = _web_et.getText().toString();

                if (XEmptyUtils.isSpace(_head_value)) {
                    ToastUtils.showCenter("请上传头像");
                } else if (XEmptyUtils.isSpace(_name_value)) {
                    ToastUtils.showCenter("请输入姓名");
                } else if (XEmptyUtils.isSpace(_jobs_value)) {
                    ToastUtils.showCenter("请输入职业");
                } else if (XEmptyUtils.isSpace(_company_value)) {
                    ToastUtils.showCenter("请输入公司名称");
                } else if (XEmptyUtils.isSpace(_phone_value)) {
                    ToastUtils.showCenter("请输入手机号");
                } else if (!XRegexUtils.checkMobile(_phone_value)) {
                    ToastUtils.showCenter("输入的手机号有误,请重新输入");
                } else {
                    updateUserInfo(_name_value, _company_value, _jobs_value, _phone_value);
                }
                break;
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        XToast.normal("取消获取图片");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        XToast.normal("获取图片错误:" + msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
//        if (imagesData != null) {
//            imagesData.clear();
//        } else {
//            imagesData = new ArrayList<>();
//        }
//        imagesData = result.getImages();
//        ArrayList<File> images = new ArrayList<>();
//        for (int i = 0; i < imagesData.size(); i++) {
//            images.add(new File(imagesData.get(i).getCompressPath()));
//        }
//        feedbackUploadApi(images);

        final String url = result.getImages().get(0).getCompressPath();
        //XLog.d("url:" + url);
        final String object = setImageUrl();

        putLoadImage(object, url, new LoadCallback() {
            @Override
            public void onSuccess(Object o, Object result) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("上传成功");
                        _head_value = ServerApi.OSS_IMAGE_URL + object;
                        ImageLoaderManager.loadImage(_head_value, _head_iv);
                    }
                });
            }

            @Override
            public void onFailure(Object o, ClientException clientException, ServiceException serviceException) {

            }
        });

    }


    /**
     * 获取信息
     */
    private void getUserDetails() {
        LoginLogic.Instance(mActivity).getUserInfoIDApi(XPreferencesUtils.get("user_id", "") + "", true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("mine:" + json);
                    if (json.getString("status").equals("0")) {
                        JSONObject jsonD = new JSONObject(json.optString("data"));
                        _head_value = "" + jsonD.optString("head_picture", "");
                        _name_value = "" + jsonD.optString("user_name", "");
                        _jobs_value = "" + jsonD.optString("profession", "");
                        _industry_value = "" + jsonD.optString("industry", "");
                        _company_value = "" + jsonD.optString("company", "");
                        _phone_value = "" + jsonD.optString("mobile_phone", "");
                        _wechat_value = "" + jsonD.optString("wechat_code", "");
                        _addrs_value = "" + jsonD.optString("address_detail", "");
                        _sex_value = jsonD.optString("sex", "1").equals("1") ? "男" : "女";
                        _birthday_value = "" + jsonD.optString("birthday", "");
                        _city_value = "" + jsonD.optString("address", "");
                        _moto_value = "" + jsonD.optString("motto", "");
                        _eamil_value = "" + jsonD.optString("email", "");
                        _web_value = "" + jsonD.optString("website", "");
                        _province_id_value = "" + jsonD.optString("province", "");
                        _city_id_value = "" + jsonD.optString("city", "");
                        _area_id_value = "" + jsonD.optString("area", "");

                        if (jsonD.optString("is_show_phone", "1").equals("1")) {
                            _is_phone_value = true;
                        } else {
                            _is_phone_value = false;
                        }

                        setShowAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error" + error);
            }
        });
    }

    /**
     * 显示数据
     */
    private void setShowAll() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageLoaderManager.loadImage(_head_value, _head_iv, R.drawable.ic_launcher);
                _name_et.setText(_name_value);
                _jobs_et.setText(_jobs_value);
                _company_et.setText(_company_value);
                _moto_et.setText(_moto_value);
                _phone_et.setText(_phone_value);
                _wechat_et.setText(_wechat_value);
                _addrs_et.setText(_addrs_value);
                _eamil_et.setText(_eamil_value);
                _web_et.setText(_web_value);

                _industry_tv.setText(_industry_value);
                _sex_tv.setText(_sex_value);
                _birthday_tv.setText(_birthday_value);
                _city_tv.setText(_city_value);

                _is_phone_s.setChecked(_is_phone_value);

                mScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    /**
     * 修改用户信息
     */
    private void updateUserInfo(String name, String company, String jobs, String phone) {
        if (XEmptyUtils.isSpace(_sex_value)) {
            _sex_value = "1";
        } else {
            if (_sex_value.equals("男")) {
                _sex_value = "1";
            } else {
                _sex_value = "2";
            }
        }

        final HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));//	用户id
        map.put("user_name", name);//	用户名称
        map.put("portrait", _head_value);//头像
        map.put("birthday", XEmptyUtils.isSpace(_birthday_value) ? XPreferencesUtils.get("birthday", "") : _birthday_value);//生日
        map.put("sex", _sex_value);//性别
        map.put("mobile_phone", phone);//手机号码
        map.put("company", company);//公司名称
        map.put("profession", jobs);//职业
        map.put("open_phone", _is_phone_value ? 1 : 0);//是否公开手机号

        map.put("industry", XEmptyUtils.isSpace(_industry_value) ? XPreferencesUtils.get("industry", "") : _industry_value);//行业
        map.put("wechat_code", XEmptyUtils.isSpace(_wechat_value) ? XPreferencesUtils.get("wechat_code", "") : _wechat_value);//微信号
        // map.put("qq", XEmptyUtils.isSpace(_birthday_value)?XPreferencesUtils.get("qq",""):_birthday_value);//QQ
        map.put("email", XEmptyUtils.isSpace(_eamil_value) ? XPreferencesUtils.get("email", "") : _eamil_value);//邮箱
        map.put("website", XEmptyUtils.isSpace(_web_value) ? XPreferencesUtils.get("website", "") : _web_value);//网页
        map.put("province", XEmptyUtils.isSpace(_province_id_value) ? XPreferencesUtils.get("province", "") : _province_id_value);//省id,
        map.put("city", XEmptyUtils.isSpace(_city_id_value) ? XPreferencesUtils.get("city", "") : _city_id_value);//市id
        map.put("area", XEmptyUtils.isSpace(_area_id_value) ? XPreferencesUtils.get("area", "") : _area_id_value);//区id
        map.put("address_detail", XEmptyUtils.isSpace(_addrs_value) ? XPreferencesUtils.get("address_detail", "") : _addrs_value);//详情地址
        map.put("motto", XEmptyUtils.isSpace(_moto_value) ? XPreferencesUtils.get("motto", "") : _moto_value);//自我描述

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginLogic.Instance(mActivity).updateUserInfoApi(map, "保存中...", new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {

                        try {
                            JSONObject json = new JSONObject(object.toString());
                            //XLog.d("json" + json);
                            if (json.getString("status").equals("0")) {
                                showToast("保存成功");
                                XPreferencesUtils.put("updateBasic", true);
                                finish();
                            } else {
                                showToast("保存失败");
                            }
                        } catch (Exception e) {
                            showToast("保存失败");
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        //XLog.d("error:" + error);
                        showToast("保存失败");
                    }
                });
            }
        });


    }

    /**
     * 性别弹出框
     */
    private void setSexPicker() {
        SinglePicker<String> picker = new SinglePicker<>(mActivity, new String[]{"男", "女"});
        picker.setTitleText("选择性别");
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(getResources().getColor(R.color.white));
        picker.setTopHeight(50);
        picker.setTopLineColor(getResources().getColor(R.color.linee7e7e7));
        picker.setTopLineHeight(1);
        picker.setTitleTextColor(getResources().getColor(R.color.color000000));
        picker.setTitleTextSize(16);
        picker.setCancelTextColor(getResources().getColor(R.color.color999999));
        picker.setCancelTextSize(14);
        picker.setSubmitTextColor(getResources().getColor(R.color.FF274FF3));
        picker.setSubmitTextSize(14);
        picker.setSelectedTextColor(getResources().getColor(R.color.color000000));
        picker.setUnSelectedTextColor(getResources().getColor(R.color.color999999));
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(getResources().getColor(R.color.linee7e7e7));//线颜色
        //config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(XDensityUtils.getScreenWidth());
        picker.setBackgroundColor(getResources().getColor(R.color.white));
        picker.setSelectedIndex(0);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                // showToast("index=" + index + ", item=" + item);
                _sex_value = item;
                _sex_tv.setText(_sex_value);
            }
        });
        picker.show();
    }

    /**
     * 地址弹出框
     */
    private void setAddressPicker(String province, String city, String county) {
        if (BaseApplication.getApp().getAreaData().size() > 0) {
            AddressPickTaskUtils.Instance(mActivity).showAddressDialog(BaseApplication.getApp().getAreaData(), province, city, county, new OnLinkageListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    setSelectCity(province, city, county);
                }
            });
        } else {
            AddressPickTask task = new AddressPickTask(this);
            task.setHideProvince(false);
            task.setHideCounty(false);

            task.setCallback(new AddressPickTask.Callback() {
                @Override
                public void onAddressInitFailed() {
                    showToast("数据初始化失败");
                }

                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    setSelectCity(province, city, county);
                }
            });
            task.execute(province, city, county);
        }
    }


    /**
     * 选择城市
     */
    private void setSelectCity(Province province, City city, County county) {
        if (county == null) {
            //showToast(province.getAreaName() + city.getAreaName());
            _city_value = province.getAreaName() + " " + city.getAreaName();
            _province_id_value = province.getAreaId();
            _city_id_value = city.getAreaId();
            _area_id_value = "";
        } else {
            //showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
            _province_id_value = province.getAreaId();
            _city_id_value = city.getAreaId();
            _area_id_value = county.getAreaId();
            _city_value = province.getAreaName() + " " + city.getAreaName() + " " + county.getAreaName();
        }
        _city_tv.setText(_city_value);
    }

    /**
     * 生日选择
     */
    private void setYearMonthDayPicker(int year, int month, int day) {
        DatePicker picker = new DatePicker(mActivity, DatePicker.YEAR_MONTH_DAY);
        // picker.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        //picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));


        picker.setTopBackgroundColor(getResources().getColor(R.color.white));
        picker.setTopHeight(50);
        picker.setTopLineColor(getResources().getColor(R.color.linee7e7e7));
        picker.setTopLineHeight(1);
        picker.setTitleTextColor(getResources().getColor(R.color.color000000));
        picker.setTitleTextSize(16);
        picker.setCancelTextColor(getResources().getColor(R.color.color999999));
        picker.setCancelTextSize(14);
        picker.setSubmitTextColor(getResources().getColor(R.color.FF274FF3));
        picker.setSubmitTextSize(14);
        picker.setSelectedTextColor(getResources().getColor(R.color.color000000));
        picker.setUnSelectedTextColor(getResources().getColor(R.color.color999999));
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(getResources().getColor(R.color.linee7e7e7));//线颜色
        //config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setBackgroundColor(getResources().getColor(R.color.white));

        picker.setRangeStart(1900, 1, 1);
        picker.setRangeEnd(2100, 11, 31);
        picker.setSelectedItem(year, month, day);
        picker.setCanLinkage(true);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                _birthday_value = year + "-" + month + "-" + day;
                _birthday_tv.setText(_birthday_value);
            }
        });
        picker.show();
    }

    private Dialog dialog;
    private IndustryAdapter mLeftAdapter, mRightAdapter;
    private ArrayList<IndustryEntity> listData, rightData;

    /**
     * 显示行业弹出框
     */
    private void showIndustryDialog() {

        listData = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(mActivity.getAssets().open("industry.json"));
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                IndustryEntity entity = new IndustryEntity();
                entity.setIndustryData(array.optJSONObject(i).getString("data"));
                entity.setIndustryId(array.optJSONObject(i).getString("id"));
                entity.setIndustryName(array.optJSONObject(i).getString("text"));
                if (i == 0) {
                    entity.setIndustryIsShow(true);
                } else {
                    entity.setIndustryIsShow(false);
                }
                listData.add(entity);
            }

            if (listData.size() > 0) {
                JSONArray arrayRight = new JSONArray(listData.get(0).getIndustryData());
                rightData = new ArrayList<>();
                for (int i = 0; i < arrayRight.length(); i++) {
                    IndustryEntity entity = new IndustryEntity();
                    entity.setIndustryId(arrayRight.optJSONObject(i).getString("id"));
                    entity.setIndustryName(arrayRight.optJSONObject(i).getString("text"));
                    entity.setIndustryIsShow(false);
                    rightData.add(entity);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);

        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_industry_list_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle2);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, XDensityUtils.dp2px(500));
        dialog.show();

        ListView _left_list = dialog.findViewById(R.id.dialog_industry_left_list);
        final ListView _right_list = dialog.findViewById(R.id.dialog_industry_right_list);

        mLeftAdapter = new IndustryAdapter(mActivity, listData);
        _left_list.setAdapter(mLeftAdapter);
        mRightAdapter = new IndustryAdapter(mActivity, rightData);
        _right_list.setAdapter(mRightAdapter);

        _left_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    for (int i = 0; i < listData.size(); i++) {
                        if (i == position) {
                            listData.get(position).setIndustryIsShow(true);
                        } else {
                            listData.get(i).setIndustryIsShow(false);
                        }
                    }
                    mLeftAdapter.notifyDataSetChanged();

                    JSONArray array = new JSONArray(listData.get(position).getIndustryData());
                    rightData = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        IndustryEntity entity = new IndustryEntity();
                        entity.setIndustryId(array.optJSONObject(i).getString("id"));
                        entity.setIndustryName(array.optJSONObject(i).getString("text"));
                        entity.setIndustryIsShow(false);

                        rightData.add(entity);
                    }
                    mRightAdapter = new IndustryAdapter(mActivity, rightData);
                    _right_list.setAdapter(mRightAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        _right_list.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _industry_value = rightData.get(position).getIndustryName();
                _industry_tv.setText(_industry_value);
                dialog.dismiss();
            }
        });

//        dialog.findViewById(R.id.dialog_home_del).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
    }

}
