package com.huimaibao.app.fragment.finds.act;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;

/**
 * 我的动态
 */
public class FindsMyActivity extends BaseActivity {

    //消息点
    private ImageView _top_msg_dian;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_list_my);
        setNeedBackGesture(true);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_msg_dian = findViewById(R.id.finds_list_top_msg_dian);
    }


    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.finds_list_top_msg_rl:
                startActivity(FindsMSGActivity.class, "消息记录");
                break;
        }
    }

}
