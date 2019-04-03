package com.huimaibao.app.fragment.home.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.adapter.CardAlbumAdapter;
import com.huimaibao.app.fragment.home.server.CardLogic;
import com.huimaibao.app.fragment.library.act.ImageShowActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.JsonFormatUtils;
import com.huimaibao.app.view.ExpandableTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youth.xframe.utils.XBitmapUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.NoScrollGridView;
import com.youth.xframe.widget.RoundedImagView;
import com.youth.xframe.widget.XScrollView;
import com.youth.xframe.widget.XToast;

import java.util.ArrayList;

/**
 * 名片详情
 */
public class CardDetailActivity extends BaseActivity {

    private XScrollView mScrollView;
    private LinearLayout _top_layout, _title_ll;
    ViewGroup.LayoutParams params;
    private View _needOffsetView;

    //用户信息
    private ImageView _top_head;
    private CircleImageView _top_head_iv, _head_iv;
    private ImageView _back_btn, _share_btn;

    private String _material_id_value = "", _user_id_value = "", _browse_value = "", _focus_on_value = "";


    private TextView _card_name, _top_name_tv, _card_company, _card_phone, _card_wechat, _card_addr, _card_introduce, _card_browse, _card_focus_on;
    private String _head_value = "", _name_value = "", _jobs_value = "", _company_value = "", _phone_value = "", _wechat_value = "", _addr_value = "", _introduce_value = "";

    private WXShare mWxShare;
    private String shareDes = "";

    private NoScrollGridView _album_gv;
    private CardAlbumAdapter albumAdapter;
    private ArrayList<String> albumData;
    private String _album_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_card_detail);
        setNeedBackGesture(true);

        mWxShare = WXShare.Instance(this);
        shareDes = XPreferencesUtils.get("", "").toString();
        initView();

        getCardDetails();
    }

    private void initView() {
        _needOffsetView = findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, 0, _needOffsetView);


        //top变化
        _top_layout = findViewById(R.id.top_layout);
        _top_head_iv = findViewById(R.id.card_title_head);
        _top_name_tv = findViewById(R.id.card_title_name);
        _title_ll = findViewById(R.id.card_title_ll);
        _back_btn = findViewById(R.id.back_btn);
        _share_btn = findViewById(R.id.share_btn);

        mScrollView = findViewById(R.id.scrollView);
        mScrollView.setOnScrollListener(new XScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > 255) {
                    scrollY = 255;
                    _title_ll.setVisibility(View.VISIBLE);
                    _back_btn.setImageResource(R.drawable.ico_arrow_left);
                    _share_btn.setImageResource(R.drawable.title_share_b_icon);
                } else {
                    _title_ll.setVisibility(View.INVISIBLE);
                    _back_btn.setImageResource(R.drawable.ico_arrow_left_w);
                    _share_btn.setImageResource(R.drawable.title_share_w_icon);
                }
                params = _top_layout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
                _top_layout.setLayoutParams(params);
                _top_layout.setBackgroundColor(Color.argb(scrollY, 255, 255, 255));
                XStatusBar.setTranslucentForImageViewInFragment(mActivity, scrollY > 80 ? 80 : scrollY, _needOffsetView);
            }
        });

        _top_head = findViewById(R.id.card_detail_top_head);
        _head_iv = findViewById(R.id.card_detail_head);
        _card_name = findViewById(R.id.card_detail_name_tv);
        _card_company = findViewById(R.id.card_detail_company_tv);
        _card_phone = findViewById(R.id.card_detail_phone_tv);
        _card_wechat = findViewById(R.id.card_detail_wechat_tv);
        _card_addr = findViewById(R.id.card_detail_addr_tv);
        _card_introduce = findViewById(R.id.card_detail_introduce_tv);

        _card_browse = findViewById(R.id.card_detail_rq);
        _card_focus_on = findViewById(R.id.card_detail_gz);


        _album_gv = findViewById(R.id.card_detail_album);
        _album_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mActivity, ImageShowActivity.class);
                intent.putStringArrayListExtra("infos", albumData);
                intent.putExtra("position", position);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.share_btn:
                setShare();
                break;
            case R.id.card_detail_update_btn:
                startActivity(MakingCardActivity.class, "编辑名片");
                break;
            case R.id.card_detail_generate_btn:
                startActivity(CardShareActivity.class, "生成图片");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("makingCard", false)) {
            getCardDetails();
            XPreferencesUtils.put("makingCard", false);
        }
    }

    /**
     * 获取信息
     */
    private void getCardDetails() {
        CardLogic.Instance(mActivity).getCardForIdApi(new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                //_style_value = "" + XPreferencesUtils.get("style", "style3");
                _head_value = XEmptyUtils.isSpace(XPreferencesUtils.get("logo", "") + "") ? XPreferencesUtils.get("portrait", "") + "" : XPreferencesUtils.get("logo", "") + "";
                _name_value = "" + XPreferencesUtils.get("name", "");
                _jobs_value = "" + XPreferencesUtils.get("profession", "");
                _company_value = "" + XPreferencesUtils.get("company", "");
                _phone_value = "" + XPreferencesUtils.get("phone", "");
                _wechat_value = "" + XPreferencesUtils.get("wechat", "");
                _addr_value = "" + XPreferencesUtils.get("address_detail", "");
                _introduce_value = "" + XPreferencesUtils.get("introduce", "");

                _browse_value = "0";//+ XPreferencesUtils.get("introduce", "");
                _focus_on_value = "0";//+ XPreferencesUtils.get("introduce", "");

                setShowAll();

            }

            @Override
            public void onFailed(String error) {
                //XLog.d("error" + error);
            }
        });
    }

    /**
     * 显示数据
     */
    private void setShowAll() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                ImageLoaderManager.loadImage(_head_value, _top_head_iv);
                ImageLoaderManager.loadImage(_head_value, _head_iv);
                try {
                    _top_head.setImageBitmap(XBitmapUtils.doBlur(ImageLoader.getInstance().loadImageSync(_head_value),5,10));
                } catch (Exception e) {

                }


                _top_name_tv.setText(_name_value);
                _card_name.setText(_name_value);
                _card_company.setText(_company_value + "/" + _jobs_value);
                _card_browse.setText("人气 " + _browse_value);
                _card_focus_on.setText("关注 " + _focus_on_value);
                _card_phone.setText(_phone_value);
                _card_wechat.setText(_wechat_value);
                _card_addr.setText(_addr_value);
                _card_introduce.setText(XEmptyUtils.isSpace(_introduce_value) ? "" : _introduce_value);

                albumData = new ArrayList<>();
                _album_value = "" + XPreferencesUtils.get("album", "");
                albumData = JsonFormatUtils.stringToList(_album_value);


                albumAdapter = new CardAlbumAdapter(mActivity, albumData, false);
                _album_gv.setAdapter(albumAdapter);

                mScrollView.smoothScrollTo(0, 0);
            }
        });
    }


    /**
     * 分享
     */
    private void setShare() {

        mWxShare.showShareDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //好友
                getShare(0);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //朋友圈
                getShare(1);
            }
        });
    }

    private void getShare(final int sceneFlag) {
        ImageSize imageSize = new ImageSize(120, 120);
        Bitmap thumb;
        try {
            thumb = ImageLoader.getInstance().loadImageSync(_head_value, imageSize);
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.layer_share_icon);
        }

        mWxShare.WXShareWeb("我是" + _name_value + " " + "邀请您与我共享千万营销人名片", XEmptyUtils.isSpace(shareDes) ? "汇脉宝--共享流量，专注营销" : shareDes, ServerApi.CARD_URL + XPreferencesUtils.get("user_id", ""), thumb, sceneFlag, new OnResponseListener() {
            @Override
            public void onSuccess() {
                XToast.normal("分享成功");
            }

            @Override
            public void onCancel() {
                XToast.normal("分享取消");
            }

            @Override
            public void onFail(String message) {
                XToast.normal("分享失败");
            }
        });

        mWxShare.setDismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWxShare.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWxShare.unregister();
    }

}
