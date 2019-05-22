package com.huimaibao.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.main.MainActivity;
import com.huimaibao.app.picture.LoadCallback;
import com.huimaibao.app.picture.PictureActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.picture.lib.PictureSelector;
import com.picture.lib.config.PictureConfig;
import com.picture.lib.entity.LocalMedia;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 完善信息
 */
public class PerfectBActivity extends PictureActivity {

    private String mType = "";

    //拍照或选择图片路径
    private String urlPath = "";


    //姓名，公司，职位
    //private EditText _name_et, _company_et, _jobs_et;
    private String _head_value = "", _name_value = "", _company_value = "", _jobs_value = "";
    private ImageView _head_iv;//, _ed_name_clear, _ed_company_clear, _ed_jobs_clear;

    private EditText _card_name, _card_jobs, _card_company;
    private TextView _card_name_tv, _card_jobs_tv, _card_company_tv;
    private ImageView _iv_name, _iv_jobs, _iv_company;


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

        // takePhoneHelper = TakePhoneHelper.of(this);

        initView();
        initData();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        _head_iv = findViewById(R.id.login_perfect_image);

//        _name_et = findViewById(R.id.login_perfect_name);
//        _company_et = findViewById(R.id.login_perfect_company);
//        _jobs_et = findViewById(R.id.login_perfect_jobs);
//
//        _ed_name_clear = findViewById(R.id.login_perfect_name_del);
//        _ed_company_clear = findViewById(R.id.login_perfect_company_del);
//        _ed_jobs_clear = findViewById(R.id.login_perfect_jobs_del);


        _card_name = findViewById(R.id.making_card_name);
        _card_jobs = findViewById(R.id.making_card_jobs);
        _card_company = findViewById(R.id.making_card_company);

        _card_name_tv = findViewById(R.id.making_card_name_tv);
        _card_jobs_tv = findViewById(R.id.making_card_jobs_tv);
        _card_company_tv = findViewById(R.id.making_card_company_tv);

        _iv_name = findViewById(R.id.making_card_name_iv);
        _iv_jobs = findViewById(R.id.making_card_jobs_iv);
        _iv_company = findViewById(R.id.making_card_company_iv);

    }

    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.login_perfect_image:
                //takePhoneHelper.setTakePhone(1, true, true, 200, 200, true, 102400, 0, 0);
                //takePhoneHelper.showTakePhoneDialog(getTakePhoto());
                showTakePhoneDialog("头像",1);
                break;
            //保存
            case R.id.login_perfect_save:
                //_head_value = "";
                _name_value = _card_name.getText().toString();
                _company_value = _card_company.getText().toString();
                _jobs_value = _card_jobs.getText().toString();

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
                        if (XPreferencesUtils.get("sex", "1").equals("1"))
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
        if (XEmptyUtils.isSpace(mType)) {
            mType = "";
        }
        if (mType.equals("微信")) {
            _head_value = XPreferencesUtils.get("headimgurl", "").toString();
            _name_value = XPreferencesUtils.get("nickname", "").toString();
            // XCache.get(this).getAsString("headimgurl");
            ImageLoaderManager.loadImage(_head_value, _head_iv, R.drawable.ic_default);
            _card_name.setText(_name_value);
            _card_name.setSelection(_name_value.length());
        }


        setFocusChangeListener();
        setaddTextChangedListener();
        setAllAnimation();


    }

    private void setFocusChangeListener() {
        _card_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    _iv_name.setBackgroundResource(R.color.main_color);
                    setAnimationED(_card_name, _card_name_tv);
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
                    setAnimationED(_card_jobs, _card_jobs_tv);
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
                    setAnimationED(_card_company, _card_company_tv);
                } else {
                    _iv_company.setBackgroundResource(R.color.line_eaeaea);
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
    }

    /***/
    private boolean isAnimation = false;

    private void setAnimationED(EditText editText, TextView textView) {
        if (editText.getText().toString().length() > 0) {
            isAnimation = false;
        } else {
            isAnimation = true;
        }
    }

    private void setEdAnimation(String value, final TextView textView) {
        if (value.length() == 0) {
            AnimationSet animationSet = new AnimationSet(true);
            Animation translateAnimation = new TranslateAnimation(0, 0, 0, 60);//平移动画  从0,0,平移到0,60
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
                Animation translateAnimation = new TranslateAnimation(0, 0, 60, 0);//平移动画  从0,0,平移到0,60
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
    }


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


                    break;
            }
        }
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
                LogUtils.debug("error:" + error);
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
        map.put("portrait", head);//头像
        map.put("profession", jobs);//职业
        map.put("birthday", "1990-01-01");//生日
        map.put("sex", sex);//性别
        map.put("mobile_phone", XPreferencesUtils.get("phone", ""));//手机号码
        map.put("company", company);//公司名称
        map.put("head_picture", head);//用户头像
//        map.put("telephone", "");//公司电话
//        map.put("industry", "");//行业
//        map.put("wechat_code", "");//微信号
//        map.put("qq", "");//QQ
//        map.put("email", "");//邮箱
//        map.put("website", "");//网页
//        map.put("province", "");//省id
//        map.put("city", "");//市id
//        map.put("area", "");//区id
//        map.put("address_detail", "");//详情地址
//        map.put("motto", "");//自我描述

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
                                toMainView();
                            } else {
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        XPreferencesUtils.put("token", "");
//        LogUtils.debug("json:tokentokentokentokentoken");
//    }

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
