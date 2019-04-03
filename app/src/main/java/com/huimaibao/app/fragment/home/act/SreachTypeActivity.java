package com.huimaibao.app.fragment.home.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;


/**
 * 搜索类型
 */
public class SreachTypeActivity extends BaseActivity {

    private String mType = "";
    private Context mContext;

    private TextView editText;

    private ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sreach_type);
        mContext = this;
        setNeedBackGesture(true);
        editText = findViewById(R.id.et_type_search);
        Intent intent = getIntent();
        if (intent != null) {
            editText.setText(intent.getStringExtra("value"));
        }
        image=findViewById(R.id.et_type_search_image);

    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.et_type_search:
                Intent intent = new Intent(this, SreachActivity.class);
                intent.putExtra("vType", "sreachType");
                startActivityForResult(intent, 10003);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10003) {
            Bundle bundle = data.getExtras();
            editText.setText(bundle.getString("sreach_value"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

}
