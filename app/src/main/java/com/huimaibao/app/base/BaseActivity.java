package com.huimaibao.app.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.youth.xframe.base.XActivity;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.XToast;

/**
 * 基本Activity，供其他Activity继承，实现了回调及title设置、返回按键等
 * <p>
 * 有 Activity 的父类, 统一的初始化页面的方法, 获取当前前台 Activity 的方法.
 * * 原先的代码是继承 ActionBarActivity, 后此类过时, 所以改为 AppCompatActivity, 效果一样
 */
public class BaseActivity extends XActivity {

    public Activity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        XActivityStack.getInstance().addActivity(this);
        mActivity = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        XStatusBar.setColor(this, getResources().getColor(R.color.colorPrimary));
        //XStatusBar.setTranslucent(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * 顶部左边设置
     *
     * @param leftShow
     * @param imageShow
     * @param textShow
     * @param title
     */
    protected void setTopLeft(boolean leftShow, boolean imageShow, boolean textShow, Object title) {
        try {
            LinearLayout leftLL = findViewById(R.id.back_btn);
            ImageView leftImage = findViewById(R.id.iv_title_left);
            TextView leftText = findViewById(R.id.tv_title_left);
            if (leftShow) {
                leftLL.setVisibility(View.VISIBLE);
            } else {
                leftLL.setVisibility(View.INVISIBLE);
            }
            if (imageShow) {
                leftImage.setVisibility(View.VISIBLE);
            } else {
                leftImage.setVisibility(View.GONE);
            }

            if (textShow) {
                leftText.setVisibility(View.VISIBLE);
                leftText.setText((String) title);
            } else {
                leftText.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param title 标题
     */
    protected void setTopTitle(Object title) {
        try {
            TextView titleText = findViewById(R.id.navication_text);
            titleText.setText((String) title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 顶部右边设置
     *
     * @param rightShow
     * @param imageShow
     * @param textShow
     * @param title
     */
    protected void setTopRight(boolean rightShow, boolean imageShow, boolean textShow, Object title, View.OnClickListener listener) {
        try {
            LinearLayout rigthLL = findViewById(R.id.ll_title_right);
            ImageView rightImage = findViewById(R.id.iv_title_right);
            TextView rightText = findViewById(R.id.tv_title_right);

            if (rightShow) {
                rigthLL.setVisibility(View.VISIBLE);
            } else {
                rigthLL.setVisibility(View.INVISIBLE);
            }
            if (imageShow) {
                rightImage.setVisibility(View.VISIBLE);
                rightImage.setOnClickListener(listener);
            } else {
                rightImage.setVisibility(View.GONE);
            }
            if (textShow) {
                rightText.setVisibility(View.VISIBLE);
                rightText.setText((String) title);
                rightText.setOnClickListener(listener);
            } else {
                rightText.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***/
    protected void setTopRight(Object title) {
        try {
            TextView rightText = findViewById(R.id.tv_title_right);
            rightText.setText((String) title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getTopRight() {
        try {
            TextView rightText = findViewById(R.id.tv_title_right);
            return rightText.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 是否隐title,默认显示
     *
     * @param isshowline
     */
    protected void setShoweTitle(boolean isshowline) {
        try {
            LinearLayout titlell = findViewById(R.id.stub_title_bar_ll);
            if (isshowline) {
                titlell.setVisibility(View.VISIBLE);
            } else {
                titlell.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否隐藏线,默认显示
     *
     * @param isshowline
     */
    protected void setShoweLine(boolean isshowline) {
        try {
            ImageView titleLine = findViewById(R.id.stub_title_bar_line);
            if (isshowline) {
                titleLine.setVisibility(View.VISIBLE);
            } else {
                titleLine.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        XActivityStack.getInstance().removeActivity(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    /**
     * 打开activity
     *
     * @param classs
     * @param type
     */
    public void startActivity(Class<?> classs, String type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, classs);
        intent.setAction("one");
        intent.putExtra("vType", type);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startActivity(Class<?> classs, String type, String url) {
        Intent intent = new Intent();
        intent.setClass(mActivity, classs);
        intent.setAction("one");
        intent.putExtra("vType", type);
        intent.putExtra("vUrl", url);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startActivity(Class<?> classs, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mActivity, classs);
        intent.setAction("two");
        intent.putExtras(bundle);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 返回
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
//            case R.id.ll_title_right:
//                onBackPressed();
//                break;
        }

    }

    /***/
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
            }
        });
    }


}
