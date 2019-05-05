package com.huimaibao.app.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.appload.AppDownloadManager;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.base.BaseApplication;
import com.huimaibao.app.fragment.ElectCircleFragment;
import com.huimaibao.app.fragment.FindsFragment;
import com.huimaibao.app.fragment.HomeFragment;
import com.huimaibao.app.fragment.MessageFragment;
import com.huimaibao.app.fragment.MineFragment;
import com.huimaibao.app.fragment.home.act.LibraryActivity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.fragment.message.server.MessageLogic;
import com.huimaibao.app.fragment.mine.act.MarketingRewardActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.LoginActivity;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XAppUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XFileUtils;
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.RoundedImagView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

    private int[] homeRes = {R.drawable.home_buttom_home_1,
            R.drawable.home_buttom_home_2, R.drawable.home_buttom_home_3,
            R.drawable.home_buttom_home_4, R.drawable.home_buttom_home_5,
            R.drawable.home_buttom_home_6, R.drawable.home_buttom_home_7,
            R.drawable.home_buttom_home_8, R.drawable.home_buttom_home_9,
            R.drawable.home_buttom_home_10, R.drawable.home_buttom_home_11,
            R.drawable.home_buttom_home_12, R.drawable.home_buttom_home_13,
            R.drawable.home_buttom_home_14, R.drawable.home_buttom_home_15,
            R.drawable.home_buttom_home_16, R.drawable.home_buttom_home_17,
            R.drawable.home_buttom_home_18, R.drawable.home_buttom_home_19};

    private int[] electRes = {R.drawable.home_buttom_elect_1,
            R.drawable.home_buttom_elect_2, R.drawable.home_buttom_elect_3,
            R.drawable.home_buttom_elect_4, R.drawable.home_buttom_elect_5,
            R.drawable.home_buttom_elect_6, R.drawable.home_buttom_elect_7,
            R.drawable.home_buttom_elect_8, R.drawable.home_buttom_elect_9,
            R.drawable.home_buttom_elect_10, R.drawable.home_buttom_elect_11,
            R.drawable.home_buttom_elect_12, R.drawable.home_buttom_elect_13,
            R.drawable.home_buttom_elect_14, R.drawable.home_buttom_elect_15,
            R.drawable.home_buttom_elect_16, R.drawable.home_buttom_elect_17,
            R.drawable.home_buttom_elect_18, R.drawable.home_buttom_elect_19, R.drawable.home_buttom_elect_20};

    private int[] msgRes = {R.drawable.home_buttom_msg_1,
            R.drawable.home_buttom_msg_2, R.drawable.home_buttom_msg_3,
            R.drawable.home_buttom_msg_4, R.drawable.home_buttom_msg_5,
            R.drawable.home_buttom_msg_6, R.drawable.home_buttom_msg_7,
            R.drawable.home_buttom_msg_8, R.drawable.home_buttom_msg_9,
            R.drawable.home_buttom_msg_10, R.drawable.home_buttom_msg_11,
            R.drawable.home_buttom_msg_12, R.drawable.home_buttom_msg_13,
            R.drawable.home_buttom_msg_14, R.drawable.home_buttom_msg_15,
            R.drawable.home_buttom_msg_16, R.drawable.home_buttom_msg_17,
            R.drawable.home_buttom_msg_18, R.drawable.home_buttom_msg_19, R.drawable.home_buttom_msg_20};

    private int[] mineRes = {R.drawable.home_buttom_mine_1,
            R.drawable.home_buttom_mine_2, R.drawable.home_buttom_mine_3,
            R.drawable.home_buttom_mine_4, R.drawable.home_buttom_mine_5,
            R.drawable.home_buttom_mine_6, R.drawable.home_buttom_mine_7,
            R.drawable.home_buttom_mine_8, R.drawable.home_buttom_mine_9,
            R.drawable.home_buttom_mine_10, R.drawable.home_buttom_mine_11,
            R.drawable.home_buttom_mine_12, R.drawable.home_buttom_mine_13,
            R.drawable.home_buttom_mine_14, R.drawable.home_buttom_mine_15,
            R.drawable.home_buttom_mine_16, R.drawable.home_buttom_mine_17,
            R.drawable.home_buttom_mine_18};

    //首页，互推圈，消息，我的
    private TextView item_home, item_elect_circle, item_message, item_mine;
    private ImageView item_home_iv, item_elect_circle_iv, item_message_iv, item_mine_iv;
    private XFrameAnimation xFAHome, xFAElect, xFAMsg, xFAMine;

    private static FragmentManager fm;
    // private Fragment mCurrent;
    //private MainFragment mainFragment;
    private HomeFragment homeFragment;
    private ElectCircleFragment electCircleFragment;
    //private MessageFragment messageFragment;
    private FindsFragment findsFragment;
    private MineFragment mineFragment;


    //引导页
    //private RelativeLayout _home_no_ll, _elect_no_ll, _mine_no_ll;

    private DialogUtils mDialogUtils;
    //app更新
    private AppDownloadManager mDownloadManager;
    //活动弹出框数据
    private List<MessageEntity> ListPushData;

    //营销奖励金
    private LinearLayout _red_ll;
    private RoundedImagView _red_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initViews();

        homeFragment = new HomeFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, homeFragment);
        hideFragment(homeFragment, fragmentTransaction);
        fragmentTransaction.show(homeFragment);
        //mCurrent = mainFragment;
        fragmentTransaction.commit();

//        if ((boolean) XPreferencesUtils.get("isFirstHome", true))
//            _home_no_ll.setVisibility(View.VISIBLE);
//        else
//            _home_no_ll.setVisibility(View.GONE);

        mDialogUtils = new DialogUtils(mActivity);
        mDownloadManager = new AppDownloadManager(mActivity);

        //XPreferencesUtils.put("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGkuNTFodWltYWliYW8uY25cL2Zyb250XC9yZWZyZXNoIiwiaWF0IjoxNTQ3ODExNjM1LCJleHAiOjE1NTA0MDM4MjcsIm5iZiI6MTU0NzgxMTgyNywianRpIjoiSWo5UlA0QngyQVV4cjhjcSIsInN1YiI6Nzc4LCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.3iFuE7ysD0MbflwB_ffxQbWE_M97vweDtGldZRk8lmw");

        //LogUtils.debug("token" + XPreferencesUtils.get("token", ""));
        //
        LoginLogic.Instance(mActivity).refreshTokenApi();


        //LogUtils.debug("token" + XPreferencesUtils.get("token", ""));
    }


    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();
        upDataAPP();
        getPushData(false);
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //XStatusBar.setColor(this, getResources().getColor(R.color.colorPrimary));
        XStatusBar.setTranslucentForImageViewInFragment(MainActivity.this, null);
    }


    /**
     * 隐藏Fragment方法
     *
     * @param fragment
     * @param ft
     */
    private static void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
        //提示
        _red_ll = findViewById(R.id.dialog_home_ll);
        _red_iv = findViewById(R.id.dialog_home_image);
        _red_ll.setBackgroundResource(R.color.transparent5);
        _red_iv.setImageResource(R.drawable.home_dialog_red_icon);
        _red_ll.setVisibility(View.GONE);

        //_home_no_ll = findViewById(R.id.home_no_ll);
        //_elect_no_ll = findViewById(R.id.elect_no_ll);
        //_mine_no_ll = findViewById(R.id.mine_no_ll);
        //底部
        item_home = findViewById(R.id.home_buttom_home_tv);
        item_elect_circle = findViewById(R.id.home_buttom_elect_tv);
        item_message = findViewById(R.id.home_buttom_msg_tv);
        item_mine = findViewById(R.id.home_buttom_mine_tv);

        item_home_iv = findViewById(R.id.home_buttom_home_iv);
        item_elect_circle_iv = findViewById(R.id.home_buttom_elect_iv);
        item_message_iv = findViewById(R.id.home_buttom_msg_iv);
        item_mine_iv = findViewById(R.id.home_buttom_mine_iv);

    }

    public void onAction(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (v.getId()) {
            //首页
            case R.id.home_buttom_home_btn:
                //隐藏其他的Fragment
                hideFragment(electCircleFragment, fragmentTransaction);
                hideFragment(findsFragment, fragmentTransaction);
                hideFragment(mineFragment, fragmentTransaction);
                //将我们的homeFragment显示出来
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content_layout, homeFragment);
                    hideFragment(homeFragment, fragmentTransaction);
                    fragmentTransaction.show(homeFragment);
                } else {
                    hideFragment(homeFragment, fragmentTransaction);
                    fragmentTransaction.show(homeFragment);
                }
                //mCurrent = homeFragment;
                changeTextColor(0);
                fragmentTransaction.commit();
                break;
            //互推圈
            case R.id.home_buttom_elect_btn:
                //隐藏其他的Fragment
                hideFragment(homeFragment, fragmentTransaction);
                hideFragment(findsFragment, fragmentTransaction);
                hideFragment(mineFragment, fragmentTransaction);
                //XLog.d("electCircleFragment:" + electCircleFragment);
                //将我们的twoFragment显示出来
                if (electCircleFragment == null) {
                    electCircleFragment = new ElectCircleFragment();
                    fragmentTransaction.add(R.id.content_layout, electCircleFragment);
                    hideFragment(electCircleFragment, fragmentTransaction);
                    fragmentTransaction.show(electCircleFragment);
                } else {
                    hideFragment(electCircleFragment, fragmentTransaction);
                    fragmentTransaction.show(electCircleFragment);
                }
                //mCurrent = electCircleFragment;
                changeTextColor(1);
                fragmentTransaction.commit();
//                if ((boolean) XPreferencesUtils.get("isFirstElect", true))
//                    _elect_no_ll.setVisibility(View.VISIBLE);
//                else
//                    _elect_no_ll.setVisibility(View.GONE);
                break;
            //消息
            case R.id.home_buttom_msg_btn:
                //隐藏其他的Fragment
                hideFragment(homeFragment, fragmentTransaction);
                hideFragment(electCircleFragment, fragmentTransaction);
                hideFragment(mineFragment, fragmentTransaction);
                //XLog.d("findsFragment:" + findsFragment);
                //将我们的ThreeFragment显示出来
                if (findsFragment == null) {
                    findsFragment = new FindsFragment();
                    fragmentTransaction.add(R.id.content_layout, findsFragment);
                    hideFragment(findsFragment, fragmentTransaction);
                    fragmentTransaction.show(findsFragment);
                } else {
                    hideFragment(findsFragment, fragmentTransaction);
                    fragmentTransaction.show(findsFragment);
                }
                //mCurrent = findsFragment;
                changeTextColor(2);
                fragmentTransaction.commit();

                break;
            //我的
            case R.id.home_buttom_mine_btn:
                //隐藏其他的Fragment
                hideFragment(homeFragment, fragmentTransaction);
                hideFragment(electCircleFragment, fragmentTransaction);
                hideFragment(findsFragment, fragmentTransaction);
                //XLog.d("mineFragment:" + mineFragment);
                //将我们的FouthFragment显示出来
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout, mineFragment);
                    hideFragment(mineFragment, fragmentTransaction);
                    fragmentTransaction.show(mineFragment);
                } else {
                    hideFragment(mineFragment, fragmentTransaction);
                    fragmentTransaction.show(mineFragment);
                }

                //mCurrent = mineFragment;
                changeTextColor(3);
                fragmentTransaction.commit();

//                if ((boolean) XPreferencesUtils.get("isFirstMine", true))
//                    _mine_no_ll.setVisibility(View.VISIBLE);
//                else
//                    _mine_no_ll.setVisibility(View.GONE);
                break;
            //首页首次提示
            //case R.id.home_no_ll_btn:
            //_home_no_ll.setVisibility(View.GONE);
            // XPreferencesUtils.put("isFirstHome", false);
            //break;
            //互推圈首次提示
            //case R.id.elect_no_ll_btn:
            //_elect_no_ll.setVisibility(View.GONE);
            //XPreferencesUtils.put("isFirstElect", false);
            //break;
            //我的首次提示
            //case R.id.mine_no_ll_btn:
            //_mine_no_ll.setVisibility(View.GONE);
            //XPreferencesUtils.put("isFirstMine", false);
            //break;
            //隐藏
            case R.id.dialog_home_ll:
            case R.id.dialog_home_del:
                _red_ll.setVisibility(View.GONE);
                break;
            //领取奖励金
            case R.id.dialog_home_image:
                getReceiveAward();
                break;
        }


    }


    /**
     * 由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            xFAHome = new XFrameAnimation(item_home_iv, homeRes, 30, false);
            if (xFAElect != null) {
                xFAElect.release();
            }
            if (xFAMsg != null) {
                xFAMsg.release();
            }
            if (xFAMine != null) {
                xFAMine.release();
            }
            item_home_iv.setImageResource(R.drawable.home_buttom_home_1);
            item_elect_circle_iv.setImageResource(R.drawable.home_buttom_elect_0);
            item_message_iv.setImageResource(R.drawable.home_buttom_msg_0);
            item_mine_iv.setImageResource(R.drawable.home_buttom_mine_0);
            item_home.setTextColor(mActivity.getResources().getColor(R.color.main_color));
            item_elect_circle.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_message.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_mine.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));

        } else if (position == 1) {
            xFAElect = new XFrameAnimation(item_elect_circle_iv, electRes, 20, false);
            if (xFAHome != null) {
                xFAHome.release();
            }
            if (xFAMsg != null) {
                xFAMsg.release();
            }
            if (xFAMine != null) {
                xFAMine.release();
            }

            item_home_iv.setImageResource(R.drawable.home_buttom_home_0);
            item_elect_circle_iv.setImageResource(R.drawable.home_buttom_elect_20);
            item_message_iv.setImageResource(R.drawable.home_buttom_msg_0);
            item_mine_iv.setImageResource(R.drawable.home_buttom_mine_0);
            item_elect_circle.setTextColor(mActivity.getResources().getColor(R.color.main_color));
            item_home.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_message.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_mine.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
        } else if (position == 2) {
            xFAMsg = new XFrameAnimation(item_message_iv, msgRes, 30, false);
            if (xFAHome != null) {
                xFAHome.release();
            }
            if (xFAElect != null) {
                xFAElect.release();
            }
            if (xFAMine != null) {
                xFAMine.release();
            }

            item_home_iv.setImageResource(R.drawable.home_buttom_home_0);
            item_elect_circle_iv.setImageResource(R.drawable.home_buttom_elect_0);
            item_message_iv.setImageResource(R.drawable.home_buttom_msg_1);
            item_mine_iv.setImageResource(R.drawable.home_buttom_mine_0);
            item_message.setTextColor(mActivity.getResources().getColor(R.color.main_color));
            item_home.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_elect_circle.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_mine.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));


        } else if (position == 3) {
            xFAMine = new XFrameAnimation(item_mine_iv, mineRes, 30, false);
            if (xFAHome != null) {
                xFAHome.release();
            }
            if (xFAElect != null) {
                xFAElect.release();
            }
            if (xFAMsg != null) {
                xFAMsg.release();
            }

            item_home_iv.setImageResource(R.drawable.home_buttom_home_0);
            item_elect_circle_iv.setImageResource(R.drawable.home_buttom_elect_0);
            item_message_iv.setImageResource(R.drawable.home_buttom_msg_0);
            item_mine_iv.setImageResource(R.drawable.home_buttom_mine_1);
            item_mine.setTextColor(mActivity.getResources().getColor(R.color.main_color));
            item_home.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_elect_circle.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
            item_message.setTextColor(mActivity.getResources().getColor(R.color.ff8e8e93));
        }
    }

    /**
     * 打开互推圈
     */
    public void setElectView() {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //隐藏其他的Fragment
        hideFragment(homeFragment, fragmentTransaction);
        hideFragment(findsFragment, fragmentTransaction);
        hideFragment(mineFragment, fragmentTransaction);

        //将我们的twoFragment显示出来
        if (electCircleFragment == null) {
            electCircleFragment = new ElectCircleFragment();
            fragmentTransaction.add(R.id.content_layout, electCircleFragment);
            hideFragment(electCircleFragment, fragmentTransaction);
            fragmentTransaction.show(electCircleFragment);
        } else {
            hideFragment(electCircleFragment, fragmentTransaction);
            fragmentTransaction.show(electCircleFragment);
        }
        //mCurrent = electCircleFragment;
        changeTextColor(1);
//        if ((boolean) XPreferencesUtils.get("isFirstElect", true))
//            _elect_no_ll.setVisibility(View.VISIBLE);
//        else
//            _elect_no_ll.setVisibility(View.GONE);
        fragmentTransaction.commit();
    }

    /**
     * 打开文库
     */
//    public void setArticleView() {
//        /*选择首页*/
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        //隐藏其他的Fragment
//        hideFragment(electCircleFragment, fragmentTransaction);
//        hideFragment(findsFragment, fragmentTransaction);
//        hideFragment(mineFragment, fragmentTransaction);
//        //将我们的homeFragment显示出来
//        if (homeFragment == null) {
//            homeFragment = new HomeFragment();
//            fragmentTransaction.add(R.id.content_layout, homeFragment);
//            hideFragment(homeFragment, fragmentTransaction);
//            fragmentTransaction.show(homeFragment);
//        } else {
//            hideFragment(homeFragment, fragmentTransaction);
//            fragmentTransaction.show(homeFragment);
//        }
//        //mCurrent = homeFragment;
//        changeTextColor(0);
//        fragmentTransaction.commit();
//        //打开文库
//        //homeFragment.setCurrentArticle();
//
//    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mDownloadManager != null) {
            mDownloadManager.resume();
        }

        // 打开互推圈
        if ((boolean) XPreferencesUtils.get("isStrategyElect", false)) {
            setElectView();
            XPreferencesUtils.put("isStrategyElect", false);
        }
        // 打开文库
//        if ((boolean) XPreferencesUtils.get("isStrategyArticle", false)) {
//            XPreferencesUtils.put("isStrategyArticle", false);
//            startActivity(LibraryActivity.class, "文库");
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDownloadManager != null) {
            mDownloadManager.onPause();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fm = null;
        homeFragment = null;
        electCircleFragment = null;
        findsFragment = null;
        mineFragment = null;
    }


    /**
     * "汇脉宝APP版本更新"
     */
    private void upDataAPP() {

        LoginLogic.Instance(mActivity).appVersionApi("", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("app=s=" + json);
                    String status = json.optString("status");
                    //String message = json.getString("message");
                    String data = json.optString("data");
                    if (status.equals("0")) {
                        if (XEmptyUtils.isSpace(data)) {
                            return;
                        }
                        JSONObject jsonD = new JSONObject(data);
                        if (jsonD.optString("type").equals("1")) {//1.android,2.ios
                            if (jsonD.optString("is_allow").equals("1")) {//是否生效:1.是,2.否
//                                try {
//                                    int number = Integer.parseInt(jsonD.getString("number").replace(".", "").trim());
//                                    XLog.d("number" + number);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                                XPreferencesUtils.put("version", jsonD.optString("number", "" + XAppUtils.getVersionCode(mActivity)).replace(".", "").trim());
                                if (XStringUtils.m1(jsonD.optString("number").replace(".", "").trim()) > XAppUtils.getVersionCode(mActivity)) {
                                    if (jsonD.optString("force").equals("1")) {//是否强制更新：1 是， 0否
                                        //jsonD.getString("url")
                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), true);
                                    } else {
                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), false);
                                    }
                                }

//                                if (!jsonD.getString("number").equals(XAppUtils.getVersionName(mActivity))) {
//
//                                    XPreferencesUtils.put("version", jsonD.optString("number"));
//                                    if (jsonD.optString("force").equals("1")) {//是否强制更新：1 是， 0否
//                                        //jsonD.getString("url")
//                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), true);
//                                    } else {
//                                        showAppDialog(jsonD.optString("number"), jsonD.optString("description"), jsonD.optString("url"), false);
//                                    }
//                                }

                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error:" + error);
            }
        });

    }


    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        LoginLogic.Instance(mActivity).getUserInfoApi("", false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!(boolean) XPreferencesUtils.get("is_activity", false)) {
                            if ((boolean) XPreferencesUtils.get("isHomeRedPush", true)) {
                                _red_ll.setVisibility(View.VISIBLE);
                            } else {
                                _red_ll.setVisibility(View.GONE);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("json:" + error);
                if (error.equals("401")) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getUserInfo();
                        }
                    });
                }
            }
        });
    }


    /**
     * 显示弹出框
     */
    private void showAppDialog(final String version, final String des, final String url, final boolean isfocrce) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //UpdateAPPUtils.Instance(mActivity).checkUpdateInfo(version, des, url, isfocrce);
                mDialogUtils.showAppDialog(version, des, isfocrce, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // mDownloadManager = new AppDownloadManager(mActivity);
                        if (mDownloadManager == null) {
                            mDownloadManager = new AppDownloadManager(mActivity);
                        }
                        mDownloadManager.downloadApk(url, "汇脉宝", "版本更新");
                        ToastUtils.showCenter("已在通知栏下载更新");
                        mDialogUtils.dismissDialog();
                    }
                });
            }
        });
    }

    /**
     * 获取推送信息
     */
    private void getPushData(boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "1");
        map.put("limit", "1");
        MessageLogic.Instance(mActivity).getPushApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("pus:" + json);
                    //String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getJSONObject("list").getString("data"));

                        ListPushData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            MessageEntity item = new MessageEntity();
                            item.setMessageId(array.getJSONObject(i).getString("id"));
                            item.setMessageImage(array.getJSONObject(i).getString("icon"));
                            item.setMessageName(array.getJSONObject(i).getString("title"));
                            item.setMessageContent(array.getJSONObject(i).getString("content"));
                            item.setMessageTime(array.getJSONObject(i).getString("created_at"));
                            item.setMessageType(array.getJSONObject(i).getString("type"));
                            item.setMessageUrl(array.getJSONObject(i).getString("link"));
                            ListPushData.add(item);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ListPushData.size() > 0) {
                                    if (XPreferencesUtils.get("homePushId", ListPushData.get(0).getMessageId()).equals(ListPushData.get(0).getMessageId())) {
                                        if (!(boolean) XPreferencesUtils.get("isHomePush", false)) {
                                            showPushDialog();
                                        }
                                    } else {
                                        showPushDialog();
                                    }
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
            }
        });
    }


    /**
     * 显示活动弹出框
     */
    private void showPushDialog() {
        mDialogUtils.showHomeDialog(ListPushData.get(0).getMessageContent(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XPreferencesUtils.put("homePushId", ListPushData.get(0).getMessageId());
                XPreferencesUtils.put("isHomePush", true);
                startActivity(MessageWebActivity.class, "活动", ListPushData.get(0).getMessageUrl());
                mDialogUtils.dismissDialog();
            }
        });
    }

    /**
     * 领取奖励
     */
    private void getReceiveAward() {
        HomeLogic.Instance(mActivity).getReceiveAwardApi(new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("app=s=" + json);
                    String status = json.getString("status");
                    //String message = json.getString("message");
                    //String data = json.getString("data");
                    if (status.equals("0")) {
                        showToast("领取成功");
                        XPreferencesUtils.put("isHomeRedPush", false);
                        startActivity(MarketingRewardActivity.class, "营销奖励");
                        hideDialog();
                    } else {
                        showToast(json.getString("message"));
                        hideDialog();
                    }
                } catch (Exception e) {
                    hideDialog();
                }
            }

            @Override
            public void onFailed(String error) {
                hideDialog();
            }
        });
    }


    /**
     * 隐藏弹出框
     */
    private void hideDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _red_ll.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 退出
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                XToast.normal("在按一次退出");
                mExitTime = System.currentTimeMillis();
            } else {
                //XFileUtils.deleteDirs(BaseApplication.getApp().getFilePath());
                finish();
                XActivityStack.getInstance().appExit();
            }
            return true;
        }

        //拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
