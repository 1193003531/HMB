package com.huimaibao.app.fragment.finds.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.utils.ToastUtils;

/**
 * 新消息，消息记录
 */
public class FindsMSGActivity extends BaseActivity {

    private String mType = "";
    //title,clear
    private TextView _top_title, _top_clear;
    private RelativeLayout _top_msg_rl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_list_my);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_title = findViewById(R.id.finds_list_title);
        _top_clear = findViewById(R.id.finds_clear_btn);
        _top_msg_rl = findViewById(R.id.finds_list_top_msg_rl);
    }


    /***/
    private void initData() {
        _top_msg_rl.setVisibility(View.GONE);
        _top_title.setText(mType);
        if (mType.equals("新消息")) {
            _top_clear.setVisibility(View.INVISIBLE);
        } else {
            _top_clear.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.finds_clear_btn:
                //清空
                ToastUtils.showCenter("清空");
                break;
        }
    }


}
