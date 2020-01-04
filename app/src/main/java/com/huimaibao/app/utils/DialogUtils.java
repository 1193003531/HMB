package com.huimaibao.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.login.LoginActivity;
import com.huimaibao.app.view.PayPsdInputView;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XOutdatedUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.GifView;
import com.youth.xframe.widget.RoundedImagView;
import com.youth.xframe.widget.XColorDrawable;


/**
 *
 */
public class DialogUtils {
    public Activity mActivity;
    public Dialog dialog;

    public static DialogUtils of(Activity activity) {
        return new DialogUtils(activity);
    }

    public DialogUtils(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 显示个人微网，营销网页弹出框
     */
    public void showListDialog(final String type, View.OnClickListener cloneListener, View.OnClickListener amendListener, View.OnClickListener titleListener, View.OnClickListener defaultListener, View.OnClickListener delListener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_personal_web_list_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        //设置,修改,默认,删除
        TextView _set_btn, _amend_btn, _title_btn, _default_btn, _del_btn;
        _set_btn = dialog.findViewById(R.id.dialog_p_w_l_set);
        _amend_btn = dialog.findViewById(R.id.dialog_p_w_l_amend);
        _title_btn = dialog.findViewById(R.id.dialog_p_w_l_title);
        _default_btn = dialog.findViewById(R.id.dialog_p_w_l_default);
        _del_btn = dialog.findViewById(R.id.dialog_p_w_l_del);

        if (type.equals("营销")) {
            _set_btn.setVisibility(View.GONE);
            _default_btn.setVisibility(View.GONE);
            _title_btn.setVisibility(View.GONE);
        }

        //克隆设置
        _set_btn.setOnClickListener(cloneListener);
        //修改
        _amend_btn.setOnClickListener(amendListener);
        //设为默认
        _default_btn.setOnClickListener(defaultListener);
        //删除
        _del_btn.setOnClickListener(delListener);
        //重设title
        _title_btn.setOnClickListener(titleListener);
        dialog.findViewById(R.id.dialog_p_w_l_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void showTitleDialog(final onItemTitleListener mOnItemTitleListener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_personal_web_title_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        //window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        //title
        TextView _sure_btn;
        final EditText _title;
        _title = dialog.findViewById(R.id.dialog_page_web_title);
        _sure_btn = dialog.findViewById(R.id.dialog_page_web_title_sure);

        _sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemTitleListener.onItemTitleClick(_title.getText().toString());
            }
        });

        dialog.findViewById(R.id.dialog_page_web_title_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    /**
     * title监听接口
     */
    public interface onItemTitleListener {
        void onItemTitleClick(String title);
    }


    /**
     * 显示克隆弹出框
     */
    boolean isTrue = true;

    public void showCloneDialog(final onItemToCloneListener mOnItemToCloneListener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_personal_web_kl_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        //希望克隆，金额，不希望克隆，取消，确定
        final TextView _true_btn, _false_btn, _cancel_btn, _sure_btn;
        final EditText _money;
        // final CheckBox _true_btn, _false_btn;
        _true_btn = dialog.findViewById(R.id.dialog_toclone_true);
        _money = dialog.findViewById(R.id.dialog_toclone_money);
        _false_btn = dialog.findViewById(R.id.dialog_toclone_false);
        _cancel_btn = dialog.findViewById(R.id.dialog_toclone_cancel);
        _sure_btn = dialog.findViewById(R.id.dialog_toclone_sure);

        XKeyboardUtils.openKeyboard(mActivity);//payPwd
        _money.requestFocus();
        _money.setSelection(_money.getText().toString().length());


        _true_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTrue = true;
                _true_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.home_true_icon), null, null, null);
                _false_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.home_false_icon), null, null, null);
            }
        });

        _false_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTrue = false;
                _true_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.home_false_icon), null, null, null);
                _false_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(mActivity.getResources().getDrawable(R.drawable.home_true_icon), null, null, null);
            }
        });

        _cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //
        _sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemToCloneListener.onItemToCloneClick(isTrue, _money.getText().toString());
            }
        });
    }

    /**
     * 克隆监听接口
     */
    public interface onItemToCloneListener {
        void onItemToCloneClick(boolean isClone, String money);
    }

    //private onItemToCloneListener mOnItemToCloneListener;


    /**
     * 互推圈首次弹出框
     */
    public void showElectOnlyOneDialog() {


        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_elect_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.elect_dialog_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XPreferencesUtils.put("isFirstElect", false);
                dialog.dismiss();
            }
        });


    }


    /**
     * 无title弹出框
     */
    public void showNoTitleDialog(String msg, String cancel, String sure, View.OnClickListener listener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_no_title_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView _msg_tv = dialog.findViewById(R.id.dialog_no_title_msg);
        TextView _cancel_btn = dialog.findViewById(R.id.dialog_no_title_cancel);
        TextView _sure_btn = dialog.findViewById(R.id.dialog_no_title_sure);
        _msg_tv.setText(msg);
        _cancel_btn.setText(cancel);
        _sure_btn.setText(sure);

        _sure_btn.setOnClickListener(listener);
        _cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 无sure弹出框
     */
    public void showNoSureDialog(boolean cancelable, String msg, String cancel, View.OnClickListener listener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_no_sure_layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView _msg_tv = dialog.findViewById(R.id.dialog_no_sure_msg);
        TextView _cancel_btn = dialog.findViewById(R.id.dialog_no_sure_cancel);
        _msg_tv.setText(msg);
        _cancel_btn.setText(cancel);
        _cancel_btn.setOnClickListener(listener);

    }

    /**
     * 无sure弹出框
     */
    public void showNoSureDialog(String msg, String cancel) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_no_sure_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView _msg_tv = dialog.findViewById(R.id.dialog_no_sure_msg);
        TextView _cancel_btn = dialog.findViewById(R.id.dialog_no_sure_cancel);
        _msg_tv.setText(msg);
        _cancel_btn.setText(cancel);
        _cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    /**
     * token失效弹出框
     */
    public void showNoTokenDialog() {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_no_sure_layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView _msg_tv = dialog.findViewById(R.id.dialog_no_sure_msg);
        TextView _cancel_btn = dialog.findViewById(R.id.dialog_no_sure_cancel);
        _msg_tv.setText("登录凭证已失效,请重新登录");
        _cancel_btn.setText("确定");
        _cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XPreferencesUtils.put("token", "");
                XPreferencesUtils.put("isLoginOut", true);
                //startActivity(LoginActivity.class, "");

                XActivityStack.getInstance().finishiAll();
                Intent intent = new Intent();
                intent.setClass(mActivity, LoginActivity.class);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                dialog.dismiss();
                mActivity.finish();
            }
        });

    }


    /**
     * 更新弹出框
     */
    public void showAppDialog(String version, String description, boolean force, View.OnClickListener listener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.update_dialog_view, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();
        TextView _version_tv = dialog.findViewById(R.id.dialog_app_version);
        TextView _description_tv = dialog.findViewById(R.id.dialog_app_description);
        TextView _cancel_btn = dialog.findViewById(R.id.dialog_app_cancel);
        ImageView _line_iv = dialog.findViewById(R.id.dialog_app_line);

        _version_tv.setText("V" + version);
        String des = description.replace(";", "\n");
        _description_tv.setText(des);
        if (force) {
            _cancel_btn.setVisibility(View.GONE);
            _line_iv.setVisibility(View.GONE);
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
            _cancel_btn.setVisibility(View.VISIBLE);
            _line_iv.setVisibility(View.VISIBLE);
        }


        dialog.findViewById(R.id.dialog_app_sure).setOnClickListener(listener);
        _cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    /**
     * 加载弹出框
     */
    // private XColorDrawable drawable;
    public void showLoadingDialog(String msg) {
        XColorDrawable drawable = new XColorDrawable();
        TextView loadingMessage;
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    loadingMessage = dialog.findViewById(R.id.xframe_loading_message);
                    loadingMessage.setText(msg);
                } catch (Exception e) {
                }

                return;
            }
        }
        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_loading_view, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();

        GifView gif = dialog.findViewById(R.id.load_gif_view);
        // 设置背景gif图片资源
        gif.setMovieResource(R.raw.load_gif_icon);

        loadingMessage = dialog.findViewById(R.id.xframe_loading_message);
        // ProgressBar progressBar =  dialog.findViewById(com.youth.xframe.R.id.xframe_loading_progressbar);
        LinearLayout loadingView = dialog.findViewById(R.id.xframe_loading_view);
        loadingMessage.setPadding(15, 0, 0, 0);
        drawable.setColor(Color.WHITE);
        XOutdatedUtils.setBackground(loadingView, drawable);
        loadingMessage.setText(msg);
    }


    /**
     * 银行卡解绑弹出框
     */
    public void showBankDelDialog(View.OnClickListener listener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_bank_del_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();


        dialog.findViewById(R.id.dialog_bank_detail_del).setOnClickListener(listener);

        dialog.findViewById(R.id.dialog_bank_detail_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 银行卡支付密码弹出框
     */
    public void showBankPayPwdDialog(PayPsdInputView.onPasswordListener onPasswordListener, View.OnClickListener listener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_bank_pay_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
//        window.setGravity(Gravity.BOTTOM);
//        //设置弹出动画
//        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final PayPsdInputView payPwd = dialog.findViewById(R.id.dialog_bank_card_pay_pwd);
        payPwd.setComparePassword(onPasswordListener);
        XKeyboardUtils.openKeyboard(mActivity);//payPwd
        payPwd.requestFocus();
        payPwd.setSelection(payPwd.getText().toString().length());
        dialog.findViewById(R.id.dialog_bank_card_pay_pwd_forgot).setOnClickListener(listener);

        dialog.findViewById(R.id.dialog_bank_card_pay_pwd_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示举报弹出框
     */
    public void showGeneralDialog(View.OnClickListener listener1, View.OnClickListener listener2) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_general_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.dialog_general_1_btn).setOnClickListener(listener1);
        dialog.findViewById(R.id.dialog_general_2_btn).setOnClickListener(listener2);

        dialog.findViewById(R.id.dialog_general_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 显示首页广告弹出框
     */
    public void showHomeDialog(String url, View.OnClickListener listener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_home_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

        RoundedImagView _dialog_iv = dialog.findViewById(R.id.dialog_home_image);
        ImageLoaderManager.loadImage(url, _dialog_iv);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        _dialog_iv.setLayoutParams(layoutParams);

        _dialog_iv.setOnClickListener(listener);

        dialog.findViewById(R.id.dialog_home_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 显示首页营销奖励1元弹出框
     */
    public void showHomeRedDialog(View.OnClickListener listener) {

        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_home_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

        RoundedImagView _dialog_iv = dialog.findViewById(R.id.dialog_home_image);
        _dialog_iv.setImageResource(R.drawable.home_dialog_red_icon);

//        LinearLayout.LayoutParams layoutParams =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        _dialog_iv.setLayoutParams(layoutParams);

        _dialog_iv.setOnClickListener(listener);

        dialog.findViewById(R.id.dialog_home_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 发现动态评论弹出框
     */
    public void showFindsDYCommentDialog(String type, View.OnClickListener replyListener, View.OnClickListener copyListener, View.OnClickListener delListener) {

        //1、使用Dialog、设置style
        dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity, R.layout.dialog_comment_layout, null);
        dialog.setContentView(view);
        //dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        //设置弹出位置
        //window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        // window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

        TextView _dialog_copy, _dialog_del;

        _dialog_copy = dialog.findViewById(R.id.dialog_comment_copy);
        _dialog_del = dialog.findViewById(R.id.dialog_comment_del);

        if (type.equals("0")) {
            _dialog_copy.setBackgroundResource(R.drawable.btn_duf_button);
            _dialog_del.setVisibility(View.GONE);
        }

        dialog.findViewById(R.id.dialog_comment_reply).setOnClickListener(replyListener);
        _dialog_copy.setOnClickListener(copyListener);
        _dialog_del.setOnClickListener(delListener);
        dialog.findViewById(R.id.dialog_comment_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public void dismissDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            // dialog.dismiss();
        }
    }

}
