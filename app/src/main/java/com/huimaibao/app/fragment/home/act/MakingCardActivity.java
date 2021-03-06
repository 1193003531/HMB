package com.huimaibao.app.fragment.home.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.fragment.home.adapter.CardAlbumAdapter;
import com.huimaibao.app.fragment.home.server.CardLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.picture.LoadCallback;
import com.huimaibao.app.picture.PictureActivity;
import com.huimaibao.app.utils.AddressPickTask;
import com.huimaibao.app.utils.AddressPickTaskUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.utils.JsonFormatUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.picture.lib.PictureSelector;
import com.picture.lib.config.PictureConfig;
import com.picture.lib.entity.LocalMedia;
import com.youth.xframe.pickers.entity.City;
import com.youth.xframe.pickers.entity.County;
import com.youth.xframe.pickers.entity.Province;
import com.youth.xframe.pickers.listeners.OnLinkageListener;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.NoScrollGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 制作名片
 */
public class MakingCardActivity extends PictureActivity {

    //private TakePhoneHelper takePhoneHelper;
    //拍照或选择图片路径
    private String urlPath = "";

    private ScrollView mScrollView;

    private String mType = "";

    private String _check_style = "style3", _check_head = "头像";
    private CircleImageView _card_head;
    //private TextView _card_city;
    private EditText _card_name, _card_jobs, _card_company, _card_phone, _card_wechat, _card_city, _card_addr, _card_introduce;
    private TextView _card_name_tv, _card_jobs_tv, _card_company_tv, _card_phone_tv, _card_wechat_tv, _card_city_tv, _card_addr_tv, _card_introduce_tv;
    private ImageView _iv_name, _iv_jobs, _iv_company, _iv_phone, _iv_wechat, _iv_city, _iv_addr, _iv_introduce;
    private TextView _card_introduce_num;
    private String _city_value = "", _province_id_value = "", _city_id_value = "", _area_id_value = "";
    private String _head_value = "", _name_value = "", _jobs_value = "", _company_value = "", _phone_value = "", _wechat_value = "", _addr_value = "", _introduce_value = "";

    private NoScrollGridView _album_gv;
    private CardAlbumAdapter albumAdapter;
    private ArrayList<String> albumData, imagePthData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_making_card);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle("");
        setShowLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(true, false, true, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSaveData();
            }
        });

        //takePhoneHelper = TakePhoneHelper.of(this);
        initView();

        if (mType.equals("完善名片")) {
            getCardDetails();
        } else {
            initData();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mScrollView = findViewById(R.id.making_card_scrollview);
        _card_head = findViewById(R.id.making_card_head);
        _card_name = findViewById(R.id.making_card_name);
        _card_jobs = findViewById(R.id.making_card_jobs);
        _card_company = findViewById(R.id.making_card_company);
        _card_phone = findViewById(R.id.making_card_phone);
        _card_wechat = findViewById(R.id.making_card_wechat);
        _card_city = findViewById(R.id.making_card_city);
        _card_addr = findViewById(R.id.making_card_addr);
        _card_introduce = findViewById(R.id.making_card_introduce);
        _card_introduce_num = findViewById(R.id.making_card_introduce_num);

        //城市不可编辑
        _card_city.setFocusable(false);
        _card_city.setFocusableInTouchMode(false);


        _card_name_tv = findViewById(R.id.making_card_name_tv);
        _card_jobs_tv = findViewById(R.id.making_card_jobs_tv);
        _card_company_tv = findViewById(R.id.making_card_company_tv);
        _card_phone_tv = findViewById(R.id.making_card_phone_tv);
        _card_wechat_tv = findViewById(R.id.making_card_wechat_tv);
        _card_city_tv = findViewById(R.id.making_card_city_tv);
        _card_addr_tv = findViewById(R.id.making_card_addr_tv);
        _card_introduce_tv = findViewById(R.id.making_card_introduce_tv);

        _iv_name = findViewById(R.id.making_card_name_iv);
        _iv_jobs = findViewById(R.id.making_card_jobs_iv);
        _iv_company = findViewById(R.id.making_card_company_iv);
        _iv_phone = findViewById(R.id.making_card_phone_iv);
        _iv_wechat = findViewById(R.id.making_card_wechat_iv);
        _iv_city = findViewById(R.id.making_card_city_iv);
        _iv_addr = findViewById(R.id.making_card_addr_iv);
        _iv_introduce = findViewById(R.id.making_card_introduce_iv);


        _album_gv = findViewById(R.id.making_card_album);

        _album_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (albumData.get(position).equals("添加")) {
                    _check_head = "相册";
                    // takePhoneHelper.setTakePhone(1, false, false, 0, 0, true, 512000, 0, 0);
                    //takePhoneHelper.showTakePhoneDialog(getTakePhoto());
                    showTakePhoneDialog("相册", 1);
                } else {
                    imagePthData.remove(position);
                    albumData.clear();
                    albumData.addAll(imagePthData);
                    if (albumData.size() < 3) {
                        albumData.add("添加");
                    }
                    albumAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    /***/
    private void initData() {
        _check_style = "" + XPreferencesUtils.get("style", "style3");
        _head_value = XEmptyUtils.isSpace(XPreferencesUtils.get("portrait", "") + "") ? XPreferencesUtils.get("logo", "") + "" : XPreferencesUtils.get("portrait", "") + "";
        _name_value = "" + XPreferencesUtils.get("name", "");
        _jobs_value = "" + XPreferencesUtils.get("profession", "");
        _company_value = "" + XPreferencesUtils.get("company", "");
        _phone_value = "" + XPreferencesUtils.get("phone", "");
        _wechat_value = "" + XPreferencesUtils.get("wechat", "");
        _addr_value = "" + XPreferencesUtils.get("address_detail", "");
        _introduce_value = XPreferencesUtils.get("introduce", "").toString().trim();
        _city_value = "" + XPreferencesUtils.get("address", "");
        _province_id_value = "" + XPreferencesUtils.get("province", "");
        _city_id_value = "" + XPreferencesUtils.get("city", "");
        _area_id_value = "" + XPreferencesUtils.get("area", "");


        ImageLoaderManager.loadImage(_head_value, _card_head, R.drawable.ic_default);
        _card_name.setText(_name_value);
        _card_jobs.setText(_jobs_value);
        _card_company.setText(_company_value);
        _card_phone.setText(_phone_value);
        _card_wechat.setText(_wechat_value);
        _card_city.setText(_city_value);
        _card_addr.setText(_addr_value);
        _card_introduce.setText(XEmptyUtils.isSpace(_introduce_value) ? "" : _introduce_value);

        _card_name.setSelection(_name_value.length());
        _card_introduce_num.setText("限" + String.valueOf(50 - _card_introduce.getText().toString().length()) + "字");//此处需要进行强制类型转换

        setFocusChangeListener();
        setaddTextChangedListener();
        setAllAnimation();
//相册
        albumData = new ArrayList<>();
        imagePthData = new ArrayList<>();
        try {
            imagePthData = JsonFormatUtils.stringToList("" + XPreferencesUtils.get("album", ""));
            //albumData.addAll(imagePthData);
            for (String s : imagePthData) {
                if (!s.equals("添加")) {
                    albumData.add(s);
                }
            }
            imagePthData.clear();
            imagePthData.addAll(albumData);


            if (albumData.size() < 3) {
                albumData.add("添加");
            }
        } catch (Exception e) {
            albumData.add("添加");
        }


        albumAdapter = new CardAlbumAdapter(mActivity, albumData, true);
        _album_gv.setAdapter(albumAdapter);


        //setTopView();
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.making_card_head:
                _check_head = "头像";
                //takePhoneHelper.setTakePhone(1, true, true, 200, 200, true, 102400, 0, 0);
                //takePhoneHelper.showTakePhoneDialog(getTakePhoto());
                showTakePhoneDialog("头像", 1);
                break;
            case R.id.making_card_city:
                if (_card_city.getText().toString().length() > 0) {
                    isAnimation = false;
                } else {
                    isAnimation = true;
                }
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
        }
    }

    /**
     * 保存
     */
    private void cardSaveData() {
        _name_value = _card_name.getText().toString();
        _jobs_value = _card_jobs.getText().toString();
        _company_value = _card_company.getText().toString();
        _phone_value = _card_phone.getText().toString();
        _wechat_value = _card_wechat.getText().toString();
        _city_value = _card_city.getText().toString();
        _addr_value = _card_addr.getText().toString();
        _introduce_value = _card_introduce.getText().toString();

        if (XEmptyUtils.isSpace(_name_value)) {
            showToast("请输入姓名");
        } else if (XEmptyUtils.isSpace(_company_value)) {
            showToast("请输入公司名称");
        } else if (XEmptyUtils.isSpace(_jobs_value)) {
            showToast("请输入职业");
        } else if (!XRegexUtils.checkMobile(_phone_value)) {
            showToast("输入的手机号有误,请重新输入");
        } else {
            updateUserInfo(_name_value, _phone_value, _wechat_value, _company_value, _jobs_value, _head_value, _addr_value);
        }
    }

    /**
     * 图片返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        if (selectList.get(i).isCut()) {
                            //为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                            Log.d("PictureUrl:", selectList.get(i).getCutPath());
                            urlPath = selectList.get(i).getCutPath();
                        } else if (selectList.get(i).isCompressed()) {
                            //为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                            //如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                            Log.d("PictureUrl:", selectList.get(i).getCompressPath());
                            urlPath = selectList.get(i).getCompressPath();
                        } else {
                            //原图path
                            Log.d("PictureUrl:", selectList.get(i).getPath());
                            urlPath = selectList.get(i).getPath();
                        }
                    }


                    final String object = setImageUrl();

                    putLoadImage(object, urlPath, new LoadCallback() {
                        @Override
                        public void onSuccess(Object o, Object result) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(mActivity,url, Toast.LENGTH_LONG).show();
                                    showToast("上传成功");
                                    if (_check_head.equals("头像")) {
                                        _head_value = ServerApi.OSS_IMAGE_URL + object;
                                        ImageLoaderManager.loadImage(_head_value, _card_head);
                                    } else {

                                        imagePthData.add(ServerApi.OSS_IMAGE_URL + object);
                                        albumData.clear();
                                        albumData.addAll(imagePthData);
                                        if (albumData.size() < 3) {
                                            albumData.add("添加");
                                        }
                                        if (albumData.size() == 2) {
                                            albumAdapter = new CardAlbumAdapter(mActivity, albumData, true);
                                            _album_gv.setAdapter(albumAdapter);
                                        } else {
                                            albumAdapter.notifyDataSetChanged();
                                        }
                                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onFailure(Object o, ClientException clientException, ServiceException serviceException) {

                        }
                    });


                    break;
            }
        }
    }


    /**
     * 修改用户信息
     */
    private void updateUserInfo(String name, String phone, String wechat, String company, String jobs, String head, String addr) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));//	用户id
        map.put("user_name", name);//	用户名称
        map.put("birthday", XPreferencesUtils.get("birthday", ""));//生日
        map.put("sex", XPreferencesUtils.get("sex", "男"));//性别
        map.put("mobile_phone", phone);//手机号码
        map.put("company", company);//公司名称
        map.put("profession", jobs);//职业
        map.put("wechat_code", wechat);//微信号
        map.put("address_detail", addr);//详情地址
        map.put("portrait", head);//头像
        map.put("head_picture", head);//用户头像
        map.put("style", _check_style);//样式

        map.put("province", XEmptyUtils.isSpace(_province_id_value) ? XPreferencesUtils.get("province", "") : _province_id_value);//省id,
        map.put("city", XEmptyUtils.isSpace(_city_id_value) ? XPreferencesUtils.get("city", "") : _city_id_value);//市id
        map.put("area", XEmptyUtils.isSpace(_area_id_value) ? XPreferencesUtils.get("area", "") : _area_id_value);//区id


        JSONArray jsonArray = new JSONArray();
        for (String tag : imagePthData) {
            jsonArray.put(tag);
        }
        map.put("album", jsonArray.toString().replace("\\", ""));//相册
        map.put("introduce", _introduce_value);//个人简介

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginLogic.Instance(mActivity).updateUserInfoApi(map, "保存中...", new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {

                        try {
                            JSONObject json = new JSONObject(object.toString());
                            LogUtils.debug("json" + json);
                            if (json.getString("status").equals("0")) {
                                showToast("保存成功");
                                XPreferencesUtils.put("makingCard", true);
                                XPreferencesUtils.put("updateBasic", true);
                                finish();
                            } else {
                                LogUtils.debug("error:" + json.getString("message"));
                                showToast("保存失败," + json.getString("message"));
                            }
                        } catch (Exception e) {
                            LogUtils.debug("error:" + e);
                            showToast("保存失败");
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        LogUtils.debug("error:" + error);
                        showToast("保存失败");
                    }
                });
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error" + error);
            }
        });
    }


    private void setFocusChangeListener() {
        _card_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_name.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_name);
                } else {
                    _iv_name.setBackgroundResource(R.color.line_eaeaea);
                }
            }
        });

        _card_jobs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_jobs.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_jobs);
                } else {
                    _iv_jobs.setBackgroundResource(R.color.line_eaeaea);
                }
            }
        });

        _card_company.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_company.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_company);
                } else {
                    _iv_company.setBackgroundResource(R.color.line_eaeaea);
                }
            }
        });

        _card_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_phone.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_phone);
                } else {
                    _iv_phone.setBackgroundResource(R.color.line_eaeaea);
                }
            }
        });

        _card_wechat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_wechat.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_wechat);
                } else {
                    _iv_wechat.setBackgroundResource(R.color.line_eaeaea);
                }
            }
        });

//        _card_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    _iv_city.setBackgroundResource(R.color.main_color);
//                    setAnimationED(_card_city);
//                } else {
//                    _iv_city.setBackgroundResource(R.color.line_eaeaea);
//                }
//            }
//        });

        _card_addr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_addr.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_addr);
                } else {
                    _iv_addr.setBackgroundResource(R.color.line_eaeaea);
                }
            }
        });

        _card_introduce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_introduce.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_introduce);
                } else {
                    _iv_introduce.setBackgroundResource(R.color.line_eaeaea);
                }
            }
        });
    }


    private void setaddTextChangedListener() {

        _card_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                _name_value = _card_name.getText().toString();
                setEdAnimation(_name_value, _card_name_tv);
            }
        });

        _card_jobs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                _jobs_value = _card_jobs.getText().toString();
                setEdAnimation(_jobs_value, _card_jobs_tv);
            }
        });

        _card_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                _company_value = _card_company.getText().toString();
                setEdAnimation(_company_value, _card_company_tv);
            }
        });

        _card_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                _phone_value = _card_phone.getText().toString();
                setEdAnimation(_phone_value, _card_phone_tv);
            }
        });

        _card_wechat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                _wechat_value = _card_wechat.getText().toString();
                setEdAnimation(_wechat_value, _card_wechat_tv);
            }
        });

        _card_addr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                _addr_value = _card_addr.getText().toString();
                setEdAnimation(_addr_value, _card_addr_tv);
            }
        });
        _card_introduce.addTextChangedListener(new TextWatcher() {
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
                editStart = _card_introduce.getSelectionStart();

                editEnd = _card_introduce.getSelectionEnd();

                _card_introduce_num.setText("限" + String.valueOf(50 - temp.length()) + "字");//此处需要进行强制类型转换

                if (temp.length() >= 50) {//条件判断可以实现其他功能
                    ToastUtils.showCenter("你输入的字数已达上限");
//                    s.delete(editStart - 1, editEnd);
//
//                    int tempSelection = editStart;
//
//                    _card_introduce.setText(s);
//
//                    _card_introduce.setSelection(tempSelection);
//                    ToastUtils.showCenter("你输入的字数已达上限");
                }

                _introduce_value = _card_introduce.getText().toString();
                setEdAnimation(_introduce_value, _card_introduce_tv);

            }
        });
    }


    /***/
    private boolean isAnimation = false;

    private void setAnimationED(EditText editText) {
        if (editText.getText().toString().length() > 0) {
            isAnimation = false;
        } else {
            isAnimation = true;
        }
    }

    private void setEdAnimation(String value, final TextView textView) {
        if (value.length() == 0) {
            AnimationSet animationSet = new AnimationSet(true);
            Animation translateAnimation = new TranslateAnimation(0, 0, 0, XDensityUtils.dp2px(15));//平移动画  从0,0,平移到0,45
            translateAnimation.setDuration(100);//动画持续的时间为0.1s
            animationSet.addAnimation(translateAnimation);
            animationSet.setFillAfter(true);
            translateAnimation.setAnimationListener(
                    new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {//开始时

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {//结束时
                            textView.setTextColor(getResources().getColor(R.color.color999999));
                            textView.setTextSize(15);
                            isAnimation = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {//进行时

                        }
                    });
            textView.startAnimation(animationSet);
        } else {
            if (isAnimation) {
                AnimationSet animationSet = new AnimationSet(true);
                Animation translateAnimation = new TranslateAnimation(0, 0, XDensityUtils.dp2px(15), 0);//平移动画  从0,0,平移到0,45
                translateAnimation.setDuration(100);//动画持续的时间为0.1s
                animationSet.addAnimation(translateAnimation);
                animationSet.setFillAfter(true);
                translateAnimation.setAnimationListener(
                        new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {//开始时

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {//结束时
                                textView.setTextColor(getResources().getColor(R.color.main_color));
                                textView.setTextSize(11);
                                isAnimation = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {//进行时

                            }
                        });

                textView.startAnimation(animationSet);
            }
        }
    }

    private void setAllAnimation() {
        setEdAnimation(_name_value, _card_name_tv);
        setEdAnimation(_jobs_value, _card_jobs_tv);
        setEdAnimation(_company_value, _card_company_tv);
        setEdAnimation(_phone_value, _card_phone_tv);
        setEdAnimation(_wechat_value, _card_wechat_tv);
        setEdAnimation(_city_value, _card_city_tv);
        setEdAnimation(_addr_value, _card_addr_tv);
        setEdAnimation(XEmptyUtils.isSpace(_introduce_value) ? "" : _introduce_value, _card_introduce_tv);
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
        _card_city.setText(_city_value);
        setEdAnimation(_city_value, _card_city_tv);
    }

}
