package com.huimaibao.app.fragment;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.electcircle.act.AreaActivity;
import com.huimaibao.app.fragment.electcircle.act.ElectPersonalActivity;
import com.huimaibao.app.fragment.electcircle.act.MaterialLibActivity;
import com.huimaibao.app.fragment.electcircle.act.RegulationActivity;
import com.huimaibao.app.fragment.electcircle.act.ShareRecordActivity;
import com.huimaibao.app.fragment.electcircle.act.ShareTaskActivity;
import com.huimaibao.app.fragment.electcircle.server.ElectLogic;
import com.huimaibao.app.fragment.electcircle.view.TagCloudView;
import com.huimaibao.app.fragment.electcircle.view.VectorTagsAdapter;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 互推圈
 */
public class ElectCircleFragment extends BaseFragment implements SensorEventListener {

    private TextView _top_left_btn, _top_share_btn, _bottom_left_btn, _bottom_right_btn, _bottom_right_num;
    private ImageView _top_right_btn;
    private TextView _content_tv;
    private TextView _is_elect;
    private int _is_elect_value = 1;

    private LinearLayout _top_left_ll;
    private TextView _top_lib_btn, _top_addr_btn;
    private boolean isShowTopLL = false;

    private TagCloudView tagCloudView;
    private VectorTagsAdapter vectorTagsAdapter;

    private List<Map<String, Object>> headData = new ArrayList<>();

    private View _needOffsetView;


    private ImageView _elect_bg, _elect_bg_line, _elect_bg_line2;

    //弹出框
    private DialogUtils mDialogUtils;
    //分享任务次数
    private String yesterday_share_passive_num = "0";
    //摇一摇
    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效

    //记录摇动状态
    private boolean isShake = false;


    private MyHandler mHandler;
    private int mWeiChatAudio;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //设置只竖屏
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_elect_circle, container, false);
        mDialogUtils = new DialogUtils(mActivity);

        mHandler = new MyHandler();

        //初始化SoundPool
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(mActivity, R.raw.beep, 1);

        //获取Vibrator震动服务
        mVibrator = (Vibrator) mActivity.getSystemService(mActivity.VIBRATOR_SERVICE);

        //mDialogUtils = new DialogUtils(mActivity);
        boolean isFirstElect = (boolean) XPreferencesUtils.get("isFirstElect", true);
        if (isFirstElect) {
            DialogUtils.of(mActivity).showElectOnlyOneDialog();
        }
        initView(view);
        return view;
    }

    /**
     * 判断Fragment是否可见
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //true为不可见
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
        if (hidden) {
            if (mSensorManager != null) {
                mSensorManager.unregisterListener(this);
            }
        } else {
            getElectUsers(false);
            getElectUser(true);
            //获取 SensorManager 负责管理传感器
            mSensorManager = ((SensorManager) mActivity.getSystemService(mActivity.SENSOR_SERVICE));
            if (mSensorManager != null) {
                //获取加速度传感器
                mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                if (mAccelerometerSensor != null) {
                    mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
                }
            }
        }
    }


    @Override
    protected void initData() {
//        getElectUsers(false);
//        getElectUser(true);
    }


    private void initView(View v) {
        _needOffsetView = v.findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);

        _elect_bg = v.findViewById(R.id.elect_circle_bg);
        _elect_bg_line = v.findViewById(R.id.elect_circle_bg_line);
        _elect_bg_line2 = v.findViewById(R.id.elect_circle_bg_line_2);
        // 加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(mActivity,
                R.anim.elect_load_animation);
        // 显示动画
        _elect_bg.startAnimation(animation);

        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(mActivity,
                R.anim.elect_line_animation);
        _elect_bg_line.startAnimation(translateAnimation);
        TranslateAnimation translateAnimation2 = (TranslateAnimation) AnimationUtils.loadAnimation(mActivity,
                R.anim.elect_line2_animation);
        _elect_bg_line2.startAnimation(translateAnimation2);

        _is_elect = v.findViewById(R.id.elect_circle_check);
        _top_left_btn = v.findViewById(R.id.elect_circle_top_left);
        _top_share_btn = v.findViewById(R.id.elect_circle_top_share);
        _top_right_btn = v.findViewById(R.id.elect_circle_top_right);

        _top_left_ll = v.findViewById(R.id.elect_circle_lib_ll);
        _top_lib_btn = v.findViewById(R.id.elect_circle_lib_sc);
        _top_addr_btn = v.findViewById(R.id.elect_circle_lib_addr);

        _bottom_left_btn = v.findViewById(R.id.elect_circle_bottom_left);
        _bottom_right_btn = v.findViewById(R.id.elect_circle_bottom_right);
        _bottom_right_num = v.findViewById(R.id.elect_circle_bottom_right_num);
        _content_tv = v.findViewById(R.id.elect_circle_content);
        tagCloudView = v.findViewById(R.id.elect_circle_tag_cloud);

        tagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                //  XToast.normal("Tag " + headData.get(position).get("id") + " clicked.");
                if (XStringUtils.m1(yesterday_share_passive_num) > 0) {
                    mDialogUtils.showNoTitleDialog("您还有分享任务未完成,去完成", "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(ShareTaskActivity.class, "分享任务");
                            mDialogUtils.dismissDialog();
                        }
                    });
                } else {
                    if (XEmptyUtils.isEmpty(XPreferencesUtils.get("material_id", ""))) {
                        mDialogUtils.showNoTitleDialog("您还有选择素材,请选择素材", "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(MaterialLibActivity.class, "");
                                mDialogUtils.dismissDialog();
                            }
                        });
                    } else {
                        startActivity(ElectPersonalActivity.class, headData.get(position).get("id").toString());
                    }
                }
            }
        });


        _is_elect_value = (int) XPreferencesUtils.get("_is_elect_value", 1);

        if (_is_elect_value == 1) {
            _is_elect.setText("允许匹配");
            _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_true_icon), null, null, null);
            XPreferencesUtils.put("_is_elect_value", _is_elect_value);
        } else {
            _is_elect.setText("禁止匹配");
            _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_false_icon), null, null, null);
            XPreferencesUtils.put("_is_elect_value", _is_elect_value);
        }



        //是否允许匹配
        _is_elect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_is_elect_value == 1) {
                    getElectUser(0);
                } else {
                    getElectUser(1);

                }
            }
        });


//        shake.setShakeListener(new ShakeManager.ShakeListener() {
//            @Override
//            public void onShake() {
//                //摇一摇监听开始
//                shake.start();
//                getElectUsers(true);
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isMaterial", false)) {
            getElectUsers(false);
            getElectUser(true);
            //_content_tv.setText("【" + XPreferencesUtils.get("areaName", "全国") + "】" + XPreferencesUtils.get("elect_lib_name", ""));
            XPreferencesUtils.put("isMaterial", false);
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //final Intent intent = new Intent();
        _top_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowTopLL) {
                    setHideLib();
                } else {
                    setShowLib();
                }
            }
        });
        _top_left_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHideLib();
            }
        });
        _top_lib_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MaterialLibActivity.class, "");
                setHideLib();
            }
        });
        _top_addr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AreaActivity.class, "推广区域");
                setHideLib();
            }
        });

        _top_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageWebActivity.class, "邀请好友", ServerApi.INVITATION_URL );
            }
        });
        _top_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegulationActivity.class, "规则");
            }
        });
        _bottom_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShareRecordActivity.class, "分享记录");
            }
        });
        _bottom_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShareTaskActivity.class, "分享任务");
            }
        });
    }

    /**
     * 显示素材库
     */
    private void setShowLib() {
        isShowTopLL = true;
        _top_left_ll.setVisibility(View.VISIBLE);
        _top_left_btn.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.elect_up_icon), null,
                null, null);
    }

    /**
     * 隐藏素材库
     */
    private void setHideLib() {
        isShowTopLL = false;
        _top_left_ll.setVisibility(View.GONE);
        _top_left_btn.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.elect_down_icon), null,
                null, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XPreferencesUtils.put("material_style", "");
        XPreferencesUtils.put("material_id", "");
        XPreferencesUtils.put("elect_lib_name", "");
        XPreferencesUtils.put("isMaterial", false);
    }


    /**
     * 获取互推人
     */
    private void getElectUsers(boolean isShow) {
        ElectLogic.Instance(mActivity).getUsersApi(isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    //hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getString("data"));
                        headData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", array.optJSONObject(i).optString("id"));
                            if (XEmptyUtils.isSpace(array.optJSONObject(i).optString("head_portrait"))) {
                                map.put("headImage", array.optJSONObject(i).optString("head_picture"));
                            } else {
                                map.put("headImage", array.optJSONObject(i).optString("head_portrait"));
                            }

                            headData.add(map);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vectorTagsAdapter = new VectorTagsAdapter(mActivity, headData);
                                tagCloudView.setAdapter(vectorTagsAdapter);
                                if (headData.size() == 0) {
                                    mDialogUtils.showNoTitleDialog("该区域暂无互推人,请选择其他区域", "取消", "确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(AreaActivity.class, "推广区域");
                                            mDialogUtils.dismissDialog();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        showToast(json.getString("message"));
                    }
                } catch (Exception e) {
                    //XLog.e("e:" + e);
                    //  hideDialog();
                    //showToast(""+e);
                }
            }

            @Override
            public void onFailed(String error) {
                // XLog.e("error:" + error);
                // hideDialog();
                //showToast(""+error);
            }
        });
    }

    /**
     * 获取登录用户互推信息
     */
    private void getElectUser(boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", XPreferencesUtils.get("user_id", ""));
        ElectLogic.Instance(mActivity).getElectUserApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("jsonpush:" + json);
                    if (json.getString("status").equals("0")) {
                        final int num = json.optJSONObject("data").optInt("share_passive_num", 0);
                        //我为他人带来浏览次数
                        XPreferencesUtils.put("others_browse_num", json.optJSONObject("data").optString("others_browse_num", "0"));
                        //他人为我带来浏览次数
                        XPreferencesUtils.put("forme_browse_num", json.optJSONObject("data").optString("forme_brows_num", "0"));
                        XPreferencesUtils.put("today_share_num", json.optJSONObject("data").optString("today_share_num", "2"));
                        XPreferencesUtils.put("areaName", json.optJSONObject("data").optString("province", "全国"));
                        XPreferencesUtils.put("areaId", json.optJSONObject("data").optString("province_id", "0"));


                        XPreferencesUtils.put("material_style", json.optJSONObject("data").optString("material_style", "4"));
                        XPreferencesUtils.put("material_id", json.optJSONObject("data").optString("material_id", ""));

                        if (!XEmptyUtils.isSpace(json.optJSONObject("data").optString("interPushInfoMaterial", ""))) {
                            JSONObject titleData = new JSONObject(json.optJSONObject("data").optString("interPushInfoMaterial", ""));
                            if (XPreferencesUtils.get("material_style", "4").equals("4")) {
                                XPreferencesUtils.put("elect_lib_name", titleData.optString("user_name"));
                            } else {
                                XPreferencesUtils.put("elect_lib_name", titleData.optString("title"));
                            }
                        }
                        //分享任务
                        yesterday_share_passive_num = json.optJSONObject("data").optString("yesterday_share_passive_num");
                        _is_elect_value = json.optJSONObject("data").optInt("is_enabled", 1);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (num > 0) {
                                    _bottom_right_num.setVisibility(View.VISIBLE);
                                    _bottom_right_num.setText("" + num);
                                } else {
                                    _bottom_right_num.setVisibility(View.GONE);
                                }
                                XPreferencesUtils.put("_is_elect_value", _is_elect_value);
                                if (_is_elect_value == 1) {
                                    _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_true_icon), null, null, null);
                                } else {
                                    _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_false_icon), null, null, null);
                                }
                                _content_tv.setText("【" + XPreferencesUtils.get("areaName", "全国") + "】" + XPreferencesUtils.get("elect_lib_name", ""));

                            }
                        });
                    }
                } catch (Exception e) {
                    LogUtils.debug("e:" + e);
                    //  hideDialog();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
                // hideDialog();
            }
        });
    }


    /**
     * 是否互推
     */
    private void getElectUser(final int enabled) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("is_enabled", enabled);
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
                                if (enabled == 1) {
                                    _is_elect_value = 1;
                                    _is_elect.setText("允许匹配");
                                    _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_true_icon), null, null, null);
                                    XPreferencesUtils.put("_is_elect_value", _is_elect_value);
                                } else {
                                    _is_elect_value = 0;
                                    _is_elect.setText("禁止匹配");
                                    _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_false_icon), null, null, null);
                                    XPreferencesUtils.put("_is_elect_value", _is_elect_value);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    LogUtils.debug("e:" + e);
                    showToast("设置失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
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
                if (_is_elect_value == 1) {
                    _is_elect.setText("允许匹配");
                    _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_true_icon), null, null, null);
                } else {
                    _is_elect.setText("禁止匹配");
                    _is_elect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.elect_false_icon), null, null, null);
                }
                XPreferencesUtils.put("material_style", "");
                XPreferencesUtils.put("material_id", "");
                XPreferencesUtils.put("elect_lib_name", "");
                XPreferencesUtils.put("isMaterial", false);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) mActivity.getSystemService(mActivity.SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }

    }


    @Override
    public void onPause() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    private long lastUpdateTime;

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            long currentUpdateTime = System.currentTimeMillis();
            long timeInterval = currentUpdateTime - lastUpdateTime;
            if (timeInterval < 50)
                return;
            lastUpdateTime = currentUpdateTime;

            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            double speed = Math.sqrt(x * x + y * y + z * z) / timeInterval * 1000;
            if (speed >= 500 && !isShake) {
                isShake = true;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            //Log.d(TAG, "onSensorChanged: 摇动");

                            //开始震动 发出提示音 展示动画效果
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            //再来一次震动提示
                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

//
//            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
//                    .abs(z) > 17) && !isShake) {
//
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    mVibrator.vibrate(300);
                    //发出提示音
                    mSoundPool.play(mWeiChatAudio, 1, 1, 0, 0, 1);
                    tagCloudView.setScrollSpeed(10);
                    break;
                case AGAIN_SHAKE:
                    mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
                    isShake = false;
                    // 展示上下两种图片回来的效果
                    //electCircleFragment.startAnimation(true);
                    tagCloudView.setScrollSpeed(1);
                    getElectUsers(true);
                    break;
            }
        }
    }

}
