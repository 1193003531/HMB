package com.huimaibao.app.fragment.mine.act;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;

import org.json.JSONObject;

/**
 * 广告标语
 */
public class AdvertSlogansActivity extends BaseActivity {

    //内容
    private EditText _as_content;
    private TextView _as_num;
    private String _as_content_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_advertising_slogans);
        setNeedBackGesture(true);

        setTopTitle("广告标语");
        setShowLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(true, false, true, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _as_content_value = _as_content.getText().toString().trim();
                if (XEmptyUtils.isSpace(_as_content_value)) {
                    ToastUtils.showCenter("请输入广告标语");
                } else {
                    getMotto(_as_content_value);
                }
            }
        });

        initView();
        initData();
    }

    /***/
    private void initView() {
        _as_content = findViewById(R.id.advertising_slogans_content);
        _as_num = findViewById(R.id.advertising_slogans_num);
    }

    /***/
    private void initData() {
        _as_content.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;

            private int editStart;

            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = _as_content.getSelectionStart();

                editEnd = _as_content.getSelectionEnd();

                _as_num.setText("还可输入" + String.valueOf(30 - temp.length()) + "字");//此处需要进行强制类型转换

                if (temp.length() > 30) {//条件判断可以实现其他功能

                    s.delete(editStart - 1, editEnd);

                    int tempSelection = editStart;

                    _as_content.setText(s);

                    _as_content.setSelection(tempSelection);
                    ToastUtils.showCenter("你输入的字数已经超过了");
                }
            }
        });
    }

    /***/
    private void getMotto(final String motto) {
        HomeLogic.Instance(mActivity).getUpdateMottoApi(motto, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        showToast("设置成功");
                        XPreferencesUtils.put("motto", motto);
                        finish();
                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("设置失败" + e.toString());
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("设置失败" + error);
            }
        });
    }

}
