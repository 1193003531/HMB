package com.huimaibao.app.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.home.act.MakingCardActivity;
import com.huimaibao.app.fragment.message.act.MessageActivity;
import com.huimaibao.app.fragment.mine.act.AdvertSlogansActivity;
import com.huimaibao.app.fragment.mine.act.BasicActivity;
import com.huimaibao.app.fragment.mine.act.CollectionActivity;
import com.huimaibao.app.fragment.mine.act.FeedbackActivity;
import com.huimaibao.app.fragment.mine.act.MarketingRewardActivity;
import com.huimaibao.app.fragment.mine.act.MemberActivity;
import com.huimaibao.app.fragment.mine.act.MyQRCodeActivity;
import com.huimaibao.app.fragment.mine.act.SetActivity;
import com.huimaibao.app.fragment.mine.act.WalletActivity;
import com.huimaibao.app.fragment.mine.settings.VerifyPhoneActivity;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.login.logic.LoginLogic;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XScrollView;
import com.youth.xframe.widget.XSwipeRefreshView;

/**
 * 个人中心
 */
public class MineFragment extends BaseFragment {
    private Activity mActivity = this.getActivity();

    private XSwipeRefreshView mSwipeRefreshView;

    private XScrollView mScrollView;

    private LinearLayout _top_layout;
    ViewGroup.LayoutParams params;
    private View _needOffsetView;

    //基本信息
    private LinearLayout btn_qrcode, btn_Set, btn_basic;
    //营销奖励,脉宝,会员中心,钱包,互推圈,邀请好友,名片夹,收藏文章,广告标语,意见反馈,设置
    private LinearLayout btn_MR, btn_VT, btn_MC, btn_Wallet, btn_PAEO, btn_IF, btn_Cc, btn_Coll, btn_ggby, btn_Feedback;
    //完善信息，邀请码
    private LinearLayout _perfect_ll, _yqm_ll;
    //完善信息隐藏,消息
    private ImageView _perfect_del, _msg_btn;
    //去完善,我的,关注数量
    private TextView btn_perfect, _msg_title, _card_num;
    //服务商，认证用户，会员，商家，互推达人,背景图片
    private ImageView _fws_iv, _rzyh_iv, _vip_iv, _shop_iv, _htdr_iv, _top_bg_iv;


    private CircleImageView mHeadImageView;
    //姓名，广告语，邀请码,复制邀请码,会员到期时间
    private TextView _name_tv, _motto_tv, _code_tv, btn_code_copy, _member_data;
    //营销奖励,脉宝
    private TextView _reward_tv;
    private int _vip_value = 0;


    //完善
//    ViewGroup.LayoutParams paramsPer;
//    private LinearLayout _perfect_ll;
//    private TextView _perfect_num;
//    private ProgressBar _perfect_progress;
//    int _Sw = 0;
//    int _sw = 0;

    private DialogUtils mDialogUtils;


    //复制
    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mActivity = this.getActivity();
        myClipboard = (ClipboardManager) mActivity.getSystemService(mActivity.CLIPBOARD_SERVICE);
        initView(view);
        return view;
    }

    @Override
    protected void initData() {
        //showPerfectProgress(10);
        //getUserInfo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showData();
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();


        if ((boolean) XPreferencesUtils.get("updateBasic", false)) {
            getUserInfo(true);
            XPreferencesUtils.put("updateBasic", false);
        }
        //showData();
        if ((boolean) XPreferencesUtils.get("set_payment_pwd", false)) {
            startActivity(WalletActivity.class, "钱包");
            XPreferencesUtils.put("set_payment_pwd", false);
        }

        if ((boolean) XPreferencesUtils.get("updateVip", false)) {
            showData();
            XPreferencesUtils.put("updateVip", false);
        }
        _card_num.setText("关注" + XPreferencesUtils.get("card_num", "0"));
        _motto_tv.setText("" + XPreferencesUtils.get("motto", ""));
        _member_data.setText("" + XPreferencesUtils.get("member_data", "开通会员"));

    }

    private void initView(View v) {
        mDialogUtils = new DialogUtils(mActivity);

        _needOffsetView = v.findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);

        _top_layout = v.findViewById(R.id.top_layout);
        mScrollView = v.findViewById(R.id.scrollView);
        _msg_btn = v.findViewById(R.id.mine_msg_btn);
        _msg_title = v.findViewById(R.id.mine_item_left);

        mScrollView.setOnScrollListener(new XScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > 255) {
                    scrollY = 255;
                    _msg_btn.setImageResource(R.drawable.mine_msg_b_icon);
                    _msg_title.setVisibility(View.VISIBLE);
                } else {
                    _msg_btn.setImageResource(R.drawable.mine_msg_icon);
                    _msg_title.setVisibility(View.GONE);
                }
                params = _top_layout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = XDensityUtils.dp2px(44) + XDensityUtils.getStatusBarHeight();
                _top_layout.setLayoutParams(params);
                _top_layout.setBackgroundColor(Color.argb(scrollY, 255, 255, 255));
                XStatusBar.setTranslucentForImageViewInFragment(mActivity, scrollY > 80 ? 80 : scrollY, _needOffsetView);
            }
        });


        btn_basic = v.findViewById(R.id.mine_basic_ll_btn);
        btn_MR = v.findViewById(R.id.mine_marketing_reward_btn);
        btn_MC = v.findViewById(R.id.mine_member_center_btn);
        btn_Wallet = v.findViewById(R.id.mine_wallet_btn);
        btn_PAEO = v.findViewById(R.id.mine_push_around_each_other_btn);
        btn_IF = v.findViewById(R.id.mine_invite_friends_btn);
        btn_Cc = v.findViewById(R.id.mine_card_clip_btn);
        btn_Coll = v.findViewById(R.id.mine_collection_btn);
        btn_ggby = v.findViewById(R.id.mine_ggby_btn);
        btn_Feedback = v.findViewById(R.id.mine_feedback_btn);
        btn_qrcode = v.findViewById(R.id.mine_qrcode_btn);
        btn_Set = v.findViewById(R.id.mine_set_btn);

        btn_perfect = v.findViewById(R.id.mine_basic_perfect);
        _perfect_del = v.findViewById(R.id.mine_basic_perfect_del);
        _perfect_ll = v.findViewById(R.id.mine_basic_perfect_ll);
        _yqm_ll = v.findViewById(R.id.mine_basic_code_ll);

        _card_num = v.findViewById(R.id.mine_card_clip_tv);
        _member_data = v.findViewById(R.id.mine_member_data_tv);
//        _perfect_progress = v.findViewById(R.id.mine_basic_progressbar);

        mHeadImageView = v.findViewById(R.id.mine_basic_image);
        _name_tv = v.findViewById(R.id.mine_basic_name);
        _motto_tv = v.findViewById(R.id.mine_basic_motto);
        _code_tv = v.findViewById(R.id.mine_basic_code);
        btn_code_copy = v.findViewById(R.id.mine_basic_code_copy);
        _reward_tv = v.findViewById(R.id.mine_marketing_reward_tv);
        _top_bg_iv = v.findViewById(R.id.mine_top_bg_iv);


        _fws_iv = v.findViewById(R.id.mine_basic_fws_iv);
        _rzyh_iv = v.findViewById(R.id.mine_basic_rzyh_iv);
        _vip_iv = v.findViewById(R.id.mine_basic_vip_iv);
        _shop_iv = v.findViewById(R.id.mine_basic_shop_iv);
        _htdr_iv = v.findViewById(R.id.mine_basic_ht_iv);
        _fws_iv.setVisibility(View.GONE);
        _rzyh_iv.setVisibility(View.GONE);
        _vip_iv.setVisibility(View.GONE);
        _shop_iv.setVisibility(View.GONE);
        _htdr_iv.setVisibility(View.GONE);


        mSwipeRefreshView = v.findViewById(R.id.swipeRefreshView);
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);
        mSwipeRefreshView.setProgressViewEndTarget(true, 210);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserInfo(false);

            }
        });

        if (mScrollView != null) {
            mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (mSwipeRefreshView != null) {
                        mSwipeRefreshView.setEnabled(mScrollView.getScrollY() == 0);
                    }
                }
            });
        }


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);
    }

    /**
     * 完善信息进度显示
     */
//    private void showPerfectProgress(int progress) {
//        _perfect_progress.setProgress(progress);
//        _perfect_num.setText(progress + "%");
//
//        paramsPer = _perfect_progress.getLayoutParams();
//        // paramsPer.width = _perfect_progress.getWidth();
//
//        // XLog.d("getWidth:" + _perfect_progress.getWidth());
//
//        //_Sw = XDensityUtils.getScreenWidth() - XDensityUtils.dp2px(153);
//        _Sw = _perfect_progress.getWidth();
//        _sw = _Sw / 100;
//        // XLog.d("_Sw:" + _Sw + " _sw:" + _sw);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.leftMargin = _sw * progress - _perfect_num.getWidth() / 2 + 15;// + _Sw % 100;
//        _perfect_num.setLayoutParams(layoutParams);
//    }


    /**
     * 点击事件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BasicActivity
                // startActivity(BasicActivity.class, "基本信息");
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + XPreferencesUtils.get("user_id", "") + ServerApi.HOME_PAGE_WEB_TOKEN);
            }
        });
        btn_perfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BasicActivity
                startActivity(MakingCardActivity.class, "完善名片");
                //startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL);
            }
        });

        //隐藏完善信息
        _perfect_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _perfect_ll.setVisibility(View.GONE);
            }
        });

        //消息
        _msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BasicActivity
                startActivity(MessageActivity.class, "消息");
            }
        });

        btn_MR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XPreferencesUtils.get("type", "4").equals("3")) {
                    mDialogUtils.showNoSureDialog("服务商请到后台管理系统查看数据", "确定");
                } else {
                    startActivity(MarketingRewardActivity.class, "营销奖励");
                }

            }
        });
        //复制邀请码
        btn_code_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制邀请码
                myClip = ClipData.newPlainText("text", "" + XPreferencesUtils.get("invitation_code", ""));
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.showCenter("复制成功");
            }
        });
        btn_MC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MemberActivity
                startActivity(MemberActivity.class, "会员中心");
            }
        });
        btn_Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) XPreferencesUtils.get("payment_pwd", false)) {
                    startActivity(WalletActivity.class, "钱包");
                } else {
                    mDialogUtils.showNoTitleDialog("您还没有设置支付密码,马上去设置?", "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(VerifyPhoneActivity.class, "设置支付密码");
                            mDialogUtils.dismissDialog();
                        }
                    });
                }
            }
        });
        btn_PAEO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageWebActivity.class, "招商合作", ServerApi.MERCHANTS_URL);
            }
        });
        btn_IF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageWebActivity.class, "邀请好友", ServerApi.INVITATION_URL);
            }
        });
        btn_Cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageActivity.class, "关注");
            }
        });
        btn_Coll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CollectionActivity.class, "收藏文章");
            }
        });
        btn_ggby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AdvertSlogansActivity.class, "广告标语");
            }
        });
        btn_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FeedbackActivity.class, "意见反馈");
            }
        });
        /**我的二维码*/
        btn_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head = "" + XPreferencesUtils.get("portrait", "");
                String name = "" + XPreferencesUtils.get("name", "");

                if (XEmptyUtils.isSpace(head) && XEmptyUtils.isSpace(name)) {
                    mDialogUtils.showNoTitleDialog("请完善头像、姓名、手机号", "取消", "立即完善", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(BasicActivity.class, "基本信息");
                            mDialogUtils.dismissDialog();
                        }
                    });
                } else {
                    startActivity(MyQRCodeActivity.class, "二维码名片");
                }
            }
        });
        /**设置*/
        btn_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SetActivity.class, "设置");
            }
        });
    }


    /**
     * 获取用户信息
     */
    private void getUserInfo(boolean isShow) {
        LoginLogic.Instance(mActivity).getUserInfoApi("", isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                showData();
            }

            @Override
            public void onFailed(String error) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        if (mSwipeRefreshView.isRefreshing()) {
                            mSwipeRefreshView.setRefreshing(false);
                        }
                    }
                });
            }
        });
    }

    /**
     * 显示数据
     */
    private void showData() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageLoaderManager.loadImage(XPreferencesUtils.get("background", "").toString(), _top_bg_iv, R.drawable.mine_top_bg);
                    ImageLoaderManager.loadImage(XPreferencesUtils.get("portrait", "").toString(), mHeadImageView, R.drawable.ic_launcher);
                    String name = "" + XPreferencesUtils.get("name", "");
                    String phone = "" + XPreferencesUtils.get("phone", "");
                    //3-服务商，4-普通
                    if (XPreferencesUtils.get("type", "4").equals("3")) {
                        _yqm_ll.setVisibility(View.VISIBLE);
                        _fws_iv.setVisibility(View.VISIBLE);
                        _name_tv.setText(XEmptyUtils.isSpace(name) ? phone : name);
                        _code_tv.setText("" + XPreferencesUtils.get("invitation_code", ""));

                    } else {
                        _name_tv.setText(XEmptyUtils.isSpace(name) ? phone : name);
                        _yqm_ll.setVisibility(View.GONE);
                        _fws_iv.setVisibility(View.GONE);
                    }

                    _card_num.setText("关注" + XPreferencesUtils.get("card_num", "0"));
                    _motto_tv.setText("" + XPreferencesUtils.get("motto", ""));
                    _reward_tv.setText("" + XPreferencesUtils.get("reward", "0") + "元");
                    // _vein_treasure_tv.setText("" + XPreferencesUtils.get("maibao", "0") + "个");
                    try {
                        _vip_value = Integer.parseInt(XPreferencesUtils.get("vip_level", "0") + "");
                    } catch (Exception e) {
                        _vip_value = 0;
                    }

                    switch (_vip_value) {
                        case 0:
                            _vip_iv.setVisibility(View.GONE);
                            break;
                        case 1:
                            _vip_iv.setVisibility(View.VISIBLE);
                            _vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
                            break;
                        case 2:
                            _vip_iv.setVisibility(View.VISIBLE);
                            _vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
                            break;
                        case 3:
                            _vip_iv.setVisibility(View.VISIBLE);
                            _vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
                            break;
                        case 4:
                            _vip_iv.setVisibility(View.VISIBLE);
                            _vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
                            break;
                    }


                    int progress;
                    try {
                        progress = (int) (Double.parseDouble(XPreferencesUtils.get("is_perfect", "0").toString().trim()) * 100);
                    } catch (Exception e) {
                        progress = 30;
                    }
                    if (progress == 100) {
                        _perfect_ll.setVisibility(View.GONE);
                    } else {
                        _perfect_ll.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    //XLog.d("error2:" + e.toString());
                }
// 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        });
    }
}
