package com.huimaibao.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.main.MainActivity;
import com.huimaibao.app.takePhone.LoadCallback;
import com.huimaibao.app.takePhone.TakePhoneHelper;
import com.huimaibao.app.takePhone.TakePhotoActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.takephoto.model.TResult;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 完善信息
 */
public class PerfectBActivity extends TakePhotoActivity {

    private String mType = "";

    private TakePhoneHelper takePhoneHelper;

    //姓名，公司，职位
    private EditText _name_et, _company_et, _jobs_et;
    private String _head_value = "", _name_value = "", _company_value = "", _jobs_value = "";
    private ImageView _head_iv, _ed_name_clear, _ed_company_clear, _ed_jobs_clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_perfect_b);

        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle("完善信息");
        setTopLeft(false, false, false, "");
        setTopRight(false, false, true, "跳过", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainView();
            }
        });

        takePhoneHelper = TakePhoneHelper.of(this);

        initView();
        initData();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        _head_iv = findViewById(R.id.login_perfect_image);
        _name_et = findViewById(R.id.login_perfect_name);
        _company_et = findViewById(R.id.login_perfect_company);
        _jobs_et = findViewById(R.id.login_perfect_jobs);

        _ed_name_clear = findViewById(R.id.login_perfect_name_del);
        _ed_company_clear = findViewById(R.id.login_perfect_company_del);
        _ed_jobs_clear = findViewById(R.id.login_perfect_jobs_del);


    }

    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.login_perfect_image:
                takePhoneHelper.setTakePhone(1, true, true, 100, 100, true, 102400, 0, 0);
                takePhoneHelper.showTakePhoneDialog(getTakePhoto());
                break;
            //保存
            case R.id.login_perfect_save:
                //_head_value = "";
                _name_value = _name_et.getText().toString();
                _company_value = _company_et.getText().toString();
                _jobs_value = _jobs_et.getText().toString();

                if (XEmptyUtils.isSpace(_name_value)) {
                    showToast("请输入姓名");
                } else if (XEmptyUtils.isSpace(_company_value)) {
                    showToast("请输入公司名称");
                } else if (XEmptyUtils.isSpace(_jobs_value)) {
                    showToast("请输入职位");
                } else if (XEmptyUtils.isSpace(_head_value)) {
                    showToast("请上传头像");
                } else {
                    if (mType.equals("微信")) {
                        String sex;
                        if ( XPreferencesUtils.get("sex", "1").equals("1"))
                            sex = "男";
                        else
                            sex = "女";
                        getUserInfo(_name_value, sex, _company_value, _jobs_value, _head_value);
                        // updateUserInfo(_name_value, sex, _company_value, _jobs_value, _head_value);
                    } else {
                        //updateUserInfo(_name_value, "男", _company_value, _jobs_value, _head_value);
                        getUserInfo(_name_value, "男", _company_value, _jobs_value, _head_value);
                    }


                }
                break;
        }

    }


    /**
     * 进入主界面
     */
    private void toMainView() {
        Intent intent = new Intent();
        intent.setClass(mActivity, MainActivity.class);
        startActivity(intent);
        intent = new Intent("com.huimaibao.app.CHOOSE_LOGIN_FINISH");
        mActivity.sendBroadcast(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 初始化事件
     */
    private void initData() {

        if (mType.equals("微信")) {
            _head_value = XPreferencesUtils.get("headimgurl", "").toString();
            // XCache.get(this).getAsString("headimgurl");
            ImageLoaderManager.loadImage(_head_value, _head_iv, R.drawable.ic_launcher);
        }

        _name_et.addTextChangedListener(nameWatcher);
        _company_et.addTextChangedListener(companyWatcher);
        _jobs_et.addTextChangedListener(jobsWatcher);

        _ed_name_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ed_name_clear.setVisibility(View.GONE);
                _name_et.setText("");
            }
        });
        _ed_company_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ed_company_clear.setVisibility(View.GONE);
                _jobs_et.setText("");
            }
        });
        _ed_jobs_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ed_jobs_clear.setVisibility(View.GONE);
                _jobs_et.setText("");
            }
        });

        _name_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (_name_et.getText().toString().length() > 0)
                        _ed_name_clear.setVisibility(View.VISIBLE);
                    else
                        _ed_name_clear.setVisibility(View.GONE);
                } else {
                    _ed_name_clear.setVisibility(View.GONE);
                }
            }
        });
        _company_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (_company_et.getText().toString().length() > 0)
                        _ed_company_clear.setVisibility(View.VISIBLE);
                    else
                        _ed_company_clear.setVisibility(View.GONE);
                } else {
                    _ed_company_clear.setVisibility(View.GONE);
                }
            }
        });
        _jobs_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (_jobs_et.getText().toString().length() > 0)
                        _ed_jobs_clear.setVisibility(View.VISIBLE);
                    else
                        _ed_jobs_clear.setVisibility(View.GONE);
                } else {
                    _ed_jobs_clear.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 名称输入监听
     */
    private TextWatcher nameWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _ed_name_clear.setVisibility(View.VISIBLE);
            } else {
                _ed_name_clear.setVisibility(View.GONE);
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
     * 公司输入监听
     */
    private TextWatcher companyWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _ed_company_clear.setVisibility(View.VISIBLE);
            } else {
                _ed_company_clear.setVisibility(View.GONE);
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
     * 职位输入监听
     */
    private TextWatcher jobsWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _ed_jobs_clear.setVisibility(View.VISIBLE);
            } else {
                _ed_jobs_clear.setVisibility(View.GONE);
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
     * 获取用户信息
     */
    private void getUserInfo(final String name, final String sex, final String company, final String jobs, final String head) {
        LoginLogic.Instance(this).getUserInfoApi("", true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                updateUserInfo(name, sex, company, jobs, head);
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
                showToast("保存失败");
            }
        });


    }

    /**
     * 修改用户信息
     */
    private void updateUserInfo(String name, String sex, String company, String jobs, String head) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));//	用户id
        map.put("user_name", name);//	用户名称
        map.put("birthday", "1990-01-01");//生日
        map.put("sex", sex);//性别
        map.put("mobile_phone", XPreferencesUtils.get("phone", ""));//手机号码
        map.put("head_picture", "");//用户头像
        map.put("telephone", "");//公司电话
        map.put("company", company);//公司名称
        map.put("industry", "");//行业
        map.put("profession", jobs);//职业
        map.put("wechat_code", "");//微信号
        map.put("qq", "");//QQ
        map.put("email", "");//邮箱
        map.put("website", "");//网页
        map.put("province", "");//省id
        map.put("city", "");//市id
        map.put("area", "");//区id
        map.put("address_detail", "");//详情地址
        map.put("portrait", head);//头像
        map.put("motto", "");//自我描述

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginLogic.Instance(mActivity).updateUserInfoApi(map, "保存中...", new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {

                        try {
                            JSONObject json = new JSONObject(object.toString());
                            // XLog.d("json" + json);
                            if (json.getString("status").equals("0")) {
                                showToast("保存成功");
                                toMainView();
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

    //物理返回退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            XPreferencesUtils.put("token", "");
            return true;
        }

        //拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
