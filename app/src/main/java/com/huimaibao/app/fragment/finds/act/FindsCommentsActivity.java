package com.huimaibao.app.fragment.finds.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;

/**
 * 关于我们
 */
public class FindsCommentsActivity extends BaseActivity {

    private String mType = "";
    // private List<Map<String, String>> listData = null;
    private String[] name = new String[]{"我要投稿", "商务合作", "市场合作", "品牌广告合作", "城市合作"};

    //private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_about_us);
        // mListView = findViewById(R.id.about_us_list);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.about_us_item_1:
            case R.id.about_us_item_2:
            case R.id.about_us_item_3:
            case R.id.about_us_item_4:
            case R.id.about_us_item_5:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:023-67539176"));
                startActivity(intent);//调用上面这个intent实现拨号
                break;
        }
    }

}
