package com.huimaibao.app.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huimaibao.app.R;

/**
 * 自定义输入框
 */

public class SixEditView extends LinearLayout {
    private Context context;
    private SixEditListener listener;
    private EditText e1, e2, e3, e4, e5, e6;
    private ImageView i1, i2, i3, i4, i5, i6;
    private String s_e1, s_e2, s_e3, s_e4, s_e5, s_e6;
    private onKeyListeners onkeylistener;


    public SixEditView(Context context) {
        super(context);
        this.context = context;
    }


    public SixEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }


    public void clear_edit() {
        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");
    }

    /**
     * 取消e6的焦点
     */
    public void clearLastFouse() {
        e6.setFocusable(false);
        e6.setFocusableInTouchMode(false);
        e6.clearFocus();
    }


    public void getLastFouse() {
        e6.setFocusable(true);
        e6.setFocusableInTouchMode(true);
        e6.requestFocus();
        e6.findFocus();
    }


    //初始化view
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.act_login_v_code, this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.height = (int) context.getResources().getDimension(R.dimen.dp52);
        view.setLayoutParams(params);
        e1 = view.findViewById(R.id.e1);
        e2 = view.findViewById(R.id.e2);
        e3 = view.findViewById(R.id.e3);
        e4 = view.findViewById(R.id.e4);
        e5 = view.findViewById(R.id.e5);
        e6 = view.findViewById(R.id.e6);

        i1 = view.findViewById(R.id.i1);
        i2 = view.findViewById(R.id.i2);
        i3 = view.findViewById(R.id.i3);
        i4 = view.findViewById(R.id.i4);
        i5 = view.findViewById(R.id.i5);
        i6 = view.findViewById(R.id.i6);


        onkeylistener = new onKeyListeners();
        e1.setOnKeyListener(onkeylistener);
        e2.setOnKeyListener(onkeylistener);
        e3.setOnKeyListener(onkeylistener);
        e4.setOnKeyListener(onkeylistener);
        e5.setOnKeyListener(onkeylistener);
        e6.setOnKeyListener(onkeylistener);


//        e1.setCursorVisible(false);
//        e2.setCursorVisible(false);
//        e3.setCursorVisible(false);
//        e4.setCursorVisible(false);
//        e5.setCursorVisible(false);
//        e6.setCursorVisible(false);
        clear_focuse();
        setaddTextChangedListener();
    }


    private void setaddTextChangedListener() {
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (e1.getText().toString().equals("")) {
                    if (e1.isFocused()) {
                        i1.setBackgroundResource(R.color.coloreff7e27);
                    }else{
                        i1.setBackgroundResource(R.color.colored2d2d2);
                    }
                } else {
                    s_e1 = e1.getText().toString();
                    i1.setBackgroundResource(R.color.color333333);
                    e2_focuse();
                }
            }
        });


        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (e2.getText().toString().equals("")) {
                    if (e2.isFocused()) {
                        i2.setBackgroundResource(R.color.coloreff7e27);
                    }else{
                        i2.setBackgroundResource(R.color.colored2d2d2);
                    }
                } else {
                    s_e2 = e2.getText().toString();
                    i2.setBackgroundResource(R.color.color333333);
                    e3_focuse();
                }
            }
        });


        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (e3.getText().toString().equals("")) {
                    if (e3.isFocused()) {
                        i3.setBackgroundResource(R.color.coloreff7e27);
                    }else{
                        i3.setBackgroundResource(R.color.colored2d2d2);
                    }
                } else {
                    s_e3 = e3.getText().toString();
                    i3.setBackgroundResource(R.color.color333333);
                    e4_focuse();
                }
            }
        });


        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (e4.getText().toString().equals("")) {
                    if (e4.isFocused()) {
                        i4.setBackgroundResource(R.color.coloreff7e27);
                    }else{
                        i4.setBackgroundResource(R.color.colored2d2d2);
                    }
                } else {
                    s_e4 = e4.getText().toString();
                    i4.setBackgroundResource(R.color.color333333);
                    e5_focuse();
                }
            }
        });


        e5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (e5.getText().toString().equals("")) {
                    if (e5.isFocused()) {
                        i5.setBackgroundResource(R.color.coloreff7e27);
                    }else{
                        i5.setBackgroundResource(R.color.colored2d2d2);
                    }
                } else {
                    s_e5 = e5.getText().toString();
                    i5.setBackgroundResource(R.color.color333333);
                    last_focuse();
                }
            }
        });


        e6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (e6.getText().toString().equals("")) {
                    if (e6.isFocused()) {
                        i6.setBackgroundResource(R.color.coloreff7e27);
                    }else{
                        i6.setBackgroundResource(R.color.colored2d2d2);
                    }
                } else {
                    e6.setFocusable(false);
                    s_e6 = e6.getText().toString();
                    i6.setBackgroundResource(R.color.color333333);
                    setLastFocuse();
                    isfirstallwrite();
                }
            }
        });
    }

    public void setLastFocuse() {
        e6.setFocusable(true);
        e6.setFocusableInTouchMode(true);
        e6.requestFocus();
        e6.findFocus();
    }


    public void getLockTime() {
        e1.setFocusable(false);
        e2.setFocusable(false);
        e3.setFocusable(false);
        e4.setFocusable(false);
        e5.setFocusable(false);
        e6.setFocusable(false);
    }


    String eString = "";

    private void isfirstallwrite() {
        if (!TextUtils.isEmpty(s_e1) && !TextUtils.isEmpty(s_e2) && !TextUtils.isEmpty(s_e3) && !TextUtils.isEmpty(s_e4)
                && !TextUtils.isEmpty(s_e5) && !TextUtils.isEmpty(s_e6)) {
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            eString = s_e1 + s_e2 + s_e3 + s_e4 + s_e5 + s_e6;
            if (listener != null) {
                listener.onInputComplete(eString);
            }
        }
    }

    /**
     * 点击删除按钮监听
     *
     * @author
     */
    class onKeyListeners implements android.view.View.OnKeyListener {


        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (event.getAction() != KeyEvent.ACTION_UP) {
                    return true;
                }
                if (e6.isFocused()) {
                    if (!TextUtils.isEmpty(e6.getText())) {
                        e6.setText("");
                    } else {
                        e6.clearFocus();
                        i6.setBackgroundResource(R.color.colored2d2d2);
                        e5_focuse();
                        e5.setText("");
                    }
                } else if (e5.isFocused()) {
                    if (!TextUtils.isEmpty(e5.getText())) {
                        e5.setText("");
                    } else {
                        e5.clearFocus();
                        i5.setBackgroundResource(R.color.colored2d2d2);
                        e4_focuse();
                        e4.setText("");
                    }
                } else if (e4.isFocused()) {
                    if (!TextUtils.isEmpty(e4.getText())) {
                        e4.setText("");
                    } else {
                        e4.clearFocus();
                        i4.setBackgroundResource(R.color.colored2d2d2);
                        e3_focuse();
                        e3.setText("");
                    }
                } else if (e3.isFocused()) {
                    if (!TextUtils.isEmpty(e3.getText())) {
                        e3.setText("");
                    } else {
                        e3.clearFocus();
                        i3.setBackgroundResource(R.color.colored2d2d2);
                        e2_focuse();
                        e2.setText("");
                    }
                } else if (e2.isFocused()) {
                    if (!TextUtils.isEmpty(e2.getText())) {
                        e2.setText("");
                    } else {
                        e2.clearFocus();
                        i2.setBackgroundResource(R.color.colored2d2d2);
                        clear_focuse();
                        e1.setText("");
                    }
                }
                return true;
            }
            return false;
        }
    }


    private void e2_focuse() {
        e2.setFocusable(true);
        e2.setFocusableInTouchMode(true);
        e2.requestFocus();
        e2.findFocus();
        i2.setBackgroundResource(R.color.coloreff7e27);

        e1.setFocusable(false);
        e3.setFocusable(false);
        e4.setFocusable(false);
        e5.setFocusable(false);
        e6.setFocusable(false);
    }


    public void clear_focuse() {
        e1.setFocusable(true);
        e1.setFocusableInTouchMode(true);
        e1.requestFocus();
        e1.findFocus();

        InputMethodManager inputManager = (InputMethodManager) e1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(e1, 0);

        e2.setFocusable(false);
        e3.setFocusable(false);
        e4.setFocusable(false);
        e5.setFocusable(false);
        e6.setFocusable(false);
    }


    private void e3_focuse() {
        e3.setFocusable(true);
        e3.setFocusableInTouchMode(true);
        e3.requestFocus();
        e3.findFocus();
        i3.setBackgroundResource(R.color.coloreff7e27);

        e2.setFocusable(false);
        e1.setFocusable(false);
        e4.setFocusable(false);
        e5.setFocusable(false);
        e6.setFocusable(false);
    }


    private void e4_focuse() {
        e4.setFocusable(true);
        e4.setFocusableInTouchMode(true);
        e4.requestFocus();
        e4.findFocus();
        i4.setBackgroundResource(R.color.coloreff7e27);

        e2.setFocusable(false);
        e3.setFocusable(false);
        e1.setFocusable(false);
        e5.setFocusable(false);
        e6.setFocusable(false);
    }


    private void e5_focuse() {
        e5.setFocusable(true);
        e5.setFocusableInTouchMode(true);
        e5.requestFocus();
        e5.findFocus();
        i5.setBackgroundResource(R.color.coloreff7e27);

        e2.setFocusable(false);
        e3.setFocusable(false);
        e4.setFocusable(false);
        e1.setFocusable(false);
        e6.setFocusable(false);
    }


    public void last_focuse() {
        e6.setFocusable(true);
        e6.setFocusableInTouchMode(true);
        e6.requestFocus();
        e6.findFocus();
        i6.setBackgroundResource(R.color.coloreff7e27);

        e2.setFocusable(false);
        e3.setFocusable(false);
        e4.setFocusable(false);
        e5.setFocusable(false);
        e1.setFocusable(false);
    }

    public void setListener(SixEditListener listener) {
        this.listener = listener;
    }

    public interface SixEditListener {
        void onInputComplete(String passWord);
    }
}
