package com.huimaibao.app.fragment.library.act;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.library.adapter.ImagePagerAdapter;
import com.youth.xframe.utils.imageshow.ImageShowViewPager;

import java.util.ArrayList;


/**
 * 图片展示
 */
public class ImageShowActivity extends BaseActivity {
    /**
     * 图片展示
     */
    private ImageShowViewPager image_pager;
    private TextView page_number;
    private int count = 0;
    /**
     * 图片下载按钮
     */
    private ImageView download;
    /**
     * 图片列表
     */
    private ArrayList<String> imgsUrl;
    /**
     * PagerAdapter
     */
    private ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_lib_imageshow);

        //setShowTitle(false);
//        setTopTitle("图片展示");
//        setTopLeft(true, true, false, "");
//        setTopRight(false, true, false, "", null);

        initView();
        initData();
        initViewPager();
    }

    private void initData() {
        imgsUrl = getIntent().getStringArrayListExtra("infos");
        count = getIntent().getIntExtra("position", 0);
        page_number.setText("1" + "/" + imgsUrl.size());
    }


    public void initView() {
        image_pager = findViewById(R.id.image_pager);
        page_number = findViewById(R.id.page_number);
        download = findViewById(R.id.download);

        image_pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                page_number.setText((arg0 + 1) + "/" + imgsUrl.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initViewPager() {
        if (imgsUrl != null && imgsUrl.size() != 0) {
            mAdapter = new ImagePagerAdapter(mActivity, imgsUrl);
            image_pager.setAdapter(mAdapter);
            image_pager.setCurrentItem(count);
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
