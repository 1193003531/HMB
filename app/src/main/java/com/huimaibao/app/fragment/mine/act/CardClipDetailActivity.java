package com.huimaibao.app.fragment.mine.act;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.home.act.ReportActivity;
import com.huimaibao.app.fragment.home.adapter.CardAlbumAdapter;
import com.huimaibao.app.fragment.home.server.CardLogic;
import com.huimaibao.app.fragment.home.server.HomeLogic;
import com.huimaibao.app.fragment.library.act.ImageShowActivity;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.JsonFormatUtils;
import com.huimaibao.app.view.ExpandableTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.NoScrollGridView;
import com.youth.xframe.widget.RoundedImagView;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 名片夹详情
 */
public class CardClipDetailActivity extends BaseActivity {

    private String mType = "";

    private String _style_value = "";

    private LinearLayout mTopView;
    private View _TopView, _top_ll;
    private RoundedImagView _top_head;
    private CircleImageView _bottom_head;
    private ImageView _top_head_btn;
    private TextView _top_name, _top_jobs, _top_company, _top_phone, _top_wechat, _top_addr;
    private TextView _card_name, _card_jobs, _card_company, _card_phone, _card_wechat, _card_city, _card_addr, _card_share, _card_collect;
    private String _card_id_value = "", _user_id_value = "", _head_value = "", _name_value = "", _jobs_value = "", _company_value = "", _phone_value = "", _tel_value = "", _wechat_value = "", _city_value = "", _addr_value = "", _introduce_value = "", _share_count_value = "0";
    private CheckBox _is_collect_cb;
    private boolean is_collect = false;
    //个人介绍
    private ExpandableTextView _card_introduce;

    private WXShare mWxShare;
    private String shareDes = "";

    private NoScrollGridView _album_gv;
    private CardAlbumAdapter albumAdapter;
    private ArrayList<String> albumData;
    private String _album_value = "";

    private ScrollView mScrollView;

    private DialogUtils mDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_card_clip_details);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            _user_id_value = mType = intent.getStringExtra("vType");
        }
        mDialogUtils = new DialogUtils(mActivity);
        setTopTitle("名片详情");
        setShoweLine(false);
        setTopLeft(true, true, false, "");
        setTopRight(true, true, false, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.showGeneralDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(FeedbackActivity.class, "意见反馈", _user_id_value);
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ReportActivity.class, "名片", _user_id_value);
                        mDialogUtils.dismissDialog();
                    }
                });
            }
        });

        mWxShare = WXShare.Instance(this);
        shareDes = XPreferencesUtils.get("", "").toString();
        initView();

        getCardDetails();
    }

    private void initView() {
        mTopView = findViewById(R.id.card_detail_top);

        _card_name = findViewById(R.id.card_detail_name_tv);
        _card_jobs = findViewById(R.id.card_detail_jobs_tv);
        _card_company = findViewById(R.id.card_detail_company_tv);
        _card_phone = findViewById(R.id.card_detail_phone_tv);
        _card_wechat = findViewById(R.id.card_detail_wechat_tv);
        _card_addr = findViewById(R.id.card_detail_addr_tv);
        _card_city = findViewById(R.id.card_detail_city_tv);
        _card_introduce = findViewById(R.id.card_detail_introduce_tv);
        _bottom_head = findViewById(R.id.card_detail_head);
        _is_collect_cb = findViewById(R.id.card_detail_collect_cb);
        _card_share = findViewById(R.id.card_detail_share_tv);
        _card_collect = findViewById(R.id.card_detail_collect_tv);

        _album_gv = findViewById(R.id.card_detail_album);

//        albumData = new ArrayList<>();
//        _album_value = "" + XPreferencesUtils.get("album", "");
//        albumData = JsonFormatUtils.stringToList(_album_value);
//
//
//        albumAdapter = new CardAlbumAdapter(mActivity, albumData,false);
//        _album_gv.setAdapter(albumAdapter);
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

        mScrollView = findViewById(R.id.card_detail_sc);

    }


    /**
     * 顶部名片
     */
    private void setTopView() {
        if (_style_value.equals("style4")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_2_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style5")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_4_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style6")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_3_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style7")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_11_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.white);
        } else if (_style_value.equals("style9")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_12_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style10")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_5_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style11")) {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_6_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style12")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_9_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style13")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_13_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style14")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_10_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style15")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_7_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style16")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_14_view);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style17")) {
            initTopView(R.layout.act_home_card_2_view, R.drawable.card_8_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else {
            initTopView(R.layout.act_home_card_1_view, R.drawable.card_1_view);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        }

        ImageLoaderManager.loadImage(_head_value, _bottom_head, R.drawable.ic_launcher);
        ImageLoaderManager.loadImage(_head_value, _top_head, R.drawable.ic_launcher);
        _top_name.setText(_name_value);
        _top_jobs.setText(_jobs_value);
        _top_company.setText(_company_value);
        _top_phone.setText("电话：" + _phone_value);
        if (XEmptyUtils.isSpace(_wechat_value)) {
            _top_wechat.setVisibility(View.GONE);
        } else {
            _top_wechat.setVisibility(View.VISIBLE);
            _top_wechat.setText("微信：" + _wechat_value);
        }

        if (XEmptyUtils.isSpace(_addr_value)) {
            _top_addr.setVisibility(View.GONE);
        } else {
            _top_addr.setVisibility(View.VISIBLE);
            _top_addr.setText("地址：" + _addr_value);
        }
    }

    /**
     * 初始化顶部控件
     */
    private void initTopView(int resource, int id) {
        mTopView.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        _TopView = getLayoutInflater().inflate(resource, null);

        _top_ll = _TopView.findViewById(R.id.card_top_ll);
        _top_head = _TopView.findViewById(R.id.card_top_head);
        _top_head_btn = _TopView.findViewById(R.id.card_top_head_btn);
        _top_head_btn.setVisibility(View.GONE);
        _top_name = _TopView.findViewById(R.id.card_top_name);
        _top_jobs = _TopView.findViewById(R.id.card_top_jobs);
        _top_company = _TopView.findViewById(R.id.card_top_company);
        _top_phone = _TopView.findViewById(R.id.card_top_phone);
        _top_wechat = _TopView.findViewById(R.id.card_top_wechat);
        _top_addr = _TopView.findViewById(R.id.card_top_addr);

        mTopView.addView(_TopView, params);

        _top_ll.setBackground(getResources().getDrawable(id));


    }

    /**
     * 设置顶部字体颜色
     */
    private void setTopTextColor(int nid, int jid, int oid) {
        _top_name.setTextColor(getResources().getColor(nid));
        _top_jobs.setTextColor(getResources().getColor(jid));
        _top_company.setTextColor(getResources().getColor(oid));
        _top_phone.setTextColor(getResources().getColor(oid));
        _top_wechat.setTextColor(getResources().getColor(oid));
        _top_addr.setTextColor(getResources().getColor(oid));
    }

//    private void setTopHeadImage(int id) {
//        _top_head_btn.setBackground(getResources().getDrawable(id));
//    }


    public void onAction(View v) {
        switch (v.getId()) {
            //个人
            case R.id.card_detail_head:
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + mType + ServerApi.HOME_PAGE_WEB_TOKEN);
                // startActivity(PersonalActivity.class, mType);
                break;
            //加入通讯录
            case R.id.card_detail_phone_add:
                if (Build.VERSION.SDK_INT >= 23) {//判断当前系统的版本

                    XPermission.requestPermissions(mActivity, 1008, new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS
                    }, new XPermission.OnPermissionListener() {
                        //权限申请成功时调用
                        @Override
                        public void onPermissionGranted() {
                            addContact(_name_value, _tel_value);
                        }

                        //权限被用户禁止时调用
                        @Override
                        public void onPermissionDenied() {
                            //给出友好提示，并且提示启动当前应用设置页面打开权限
                            XPermission.showTipsDialog(mActivity);
                        }
                    });
                } else {
                    addContact(_name_value, _tel_value);
                }

                break;
            //拨号
            case R.id.card_detail_tell_btn:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _tel_value));
                startActivity(intent);//调用上面这个intent实现拨号
                break;
            //收藏
            case R.id.card_detail_collect_cb_btn:
                if (is_collect) {
                    getCardDel();
                } else {
                    getCardAdd();
                }
                break;
            //分享
            case R.id.card_detail_share_btn:
                setShare();
                break;
        }
    }


    /**
     * 获取信息
     */
    private void getCardDetails() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", mType);
        CardLogic.Instance(mActivity).getCardClipApi(map, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(object.toString());
                    LogUtils.debug("card:" + json);
                    if (json.getString("status").equals("0")) {
                        JSONObject jsonD = new JSONObject(json.getString("data"));
//                        XPreferencesUtils.put("card_id", jsonD.getString("card_id"));//名片id
//                        XPreferencesUtils.put("has_holder", jsonD.getString("has_holder"));//是否收藏改名片
//                        XPreferencesUtils.put("phone", jsonD.getString("phone"));//手机号
//                        XPreferencesUtils.put("name", jsonD.getString("name"));//名称
//                        XPreferencesUtils.put("portrait", jsonD.getString("portrait"));//头像
//                        XPreferencesUtils.put("industry", jsonD.getString("industry"));//行业
//                        XPreferencesUtils.put("profession", jsonD.getString("profession"));//职业
//                        XPreferencesUtils.put("company", jsonD.getString("company"));//公司
//                        XPreferencesUtils.put("wechat", jsonD.getString("wechat"));//
//                        XPreferencesUtils.put("website", jsonD.getString("website"));//
//                        XPreferencesUtils.put("address", jsonD.getString("address"));//
//                        XPreferencesUtils.put("address_detail", jsonD.getString("address_detail"));//
//                        XPreferencesUtils.put("self_introduction", jsonD.getString("self_introduction"));//是否制作营销网页
//                        XPreferencesUtils.put("open_phone", jsonD.getString("open_phone"));//是否公开手机号
//                        XPreferencesUtils.put("qq", jsonD.getString("qq"));
//                        XPreferencesUtils.put("email", jsonD.getString("email"));
//                        XPreferencesUtils.put("introduce", jsonD.getString("introduce"));//个人介绍
//                        XPreferencesUtils.put("style", jsonD.getString("style"));//样式
//                        XPreferencesUtils.put("album", jsonD.getString("album"));//相册
//                        XPreferencesUtils.put("motto", jsonD.getString("motto"));//广告语
                        _card_id_value = jsonD.optString("card_id");
                        is_collect = jsonD.optBoolean("has_holder");
                        _share_count_value = jsonD.optString("share_count");
                        _style_value = jsonD.optString("style");
                        _head_value = jsonD.optString("portrait");
                        _name_value = jsonD.optString("name");
                        _jobs_value = jsonD.optString("profession");
                        _company_value = jsonD.optString("company");
                        _tel_value = _phone_value = jsonD.optString("phone");
                        _wechat_value = jsonD.optString("wechat");
                        _addr_value = jsonD.optString("address_detail");
                        _city_value = jsonD.optString("address");
                        _introduce_value = jsonD.optString("introduce");
                        _album_value = jsonD.optString("album");

                        //手机号是否公开
                        if (!jsonD.optString("open_phone").equals("1")) {
                            if (!XEmptyUtils.isSpace(_phone_value)) {
                                _phone_value = XRegexUtils.phoneNoHide(_phone_value);
                            }
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (is_collect) {
                                    _card_collect.setText("取消收藏");
                                } else {
                                    _card_collect.setText("收藏名片");
                                }
                                _is_collect_cb.setChecked(is_collect);
                                _card_name.setText(_name_value);
                                _card_jobs.setText(_jobs_value);
                                _card_company.setText(_company_value);
                                _card_phone.setText(_phone_value);
                                _card_wechat.setText(_wechat_value);
                                _card_addr.setText(_addr_value);
                                _card_city.setText(_city_value);
                                _card_introduce.setText(XEmptyUtils.isSpace(_introduce_value) ? "" : _introduce_value);

                                _card_share.setText((XEmptyUtils.isSpace(_share_count_value) ? "0" : _share_count_value));

                                albumData = new ArrayList<>();
                                albumData = JsonFormatUtils.stringToList(_album_value);

                                albumAdapter = new CardAlbumAdapter(mActivity, albumData, false);
                                _album_gv.setAdapter(albumAdapter);

                                setTopView();
                                mScrollView.smoothScrollTo(0, 0);
                            }
                        });
                    } else {
                        showToast("查询失败," + json.getString("message"));
                    }
                } catch (Exception e) {
                    LogUtils.debug("card:" + e);
                    showToast("查询失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("card:" + error);

                showToast("查询失败");
            }
        });
    }


    /**
     * 添加收藏
     */
    private void getCardAdd() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("card_id", _card_id_value);
        CardClipLogic.Instance(mActivity).getCardClipAddApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(object.toString());
                    //XLog.d("card:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("收藏成功");
                                is_collect = true;
                                _is_collect_cb.setChecked(is_collect);
                                if (is_collect) {
                                    _card_collect.setText("取消收藏");
                                } else {
                                    _card_collect.setText("收藏名片");
                                }
                                XPreferencesUtils.put("is_collect_card", true);
                            }
                        });
                    } else {
                        showToast("收藏失败");
                    }
                } catch (Exception e) {
                    showToast("收藏失败");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("收藏失败");
            }
        });
    }

    /**
     * 取消收藏
     */
    private void getCardDel() {
        List<String> list = new ArrayList<>();
        list.add(_card_id_value);
        CardClipLogic.Instance(mActivity).getCardClipDelApi(list, true, "取消收藏中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    //XLog.d("card:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("取消收藏成功");
                                is_collect = false;
                                _is_collect_cb.setChecked(is_collect);
                                if (is_collect) {
                                    _card_collect.setText("取消收藏");
                                } else {
                                    _card_collect.setText("收藏名片");
                                }
                                XPreferencesUtils.put("is_collect_card", true);
                            }
                        });
                    } else {
                        showToast("取消收藏失败");
                    }
                } catch (Exception e) {
                    showToast("取消收藏失败");
                }
            }

            @Override
            public void onFailed(String error) {
                showToast("取消收藏失败");
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
        mWxShare.WXShareWeb("我是" + _name_value + " " + "邀请您与我共享千万营销人名片", XEmptyUtils.isSpace(shareDes) ? "汇脉宝--共享流量，专注营销" : shareDes, ServerApi.CARD_URL + _user_id_value, thumb, sceneFlag, new OnResponseListener() {
            @Override
            public void onSuccess() {
                XToast.normal("分享成功");
                HomeLogic.Instance(mActivity).getAddShareApi(_card_id_value, "3", new ResultBack() {
                    @Override
                    public void onSuccess(Object object) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getCardDetails();
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });
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

    //一步一步添加数据
    // 一个添加联系人信息的例子
    public void addContact(String name, String phoneNumber) {
        mDialogUtils.showLoadingDialog("添加中...");
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = mActivity.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        mActivity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        mActivity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

//        values.put(Data.RAW_CONTACT_ID, rawContactId);
//        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
//        // 联系人的Email地址
//        values.put(Email.DATA, "zhangphil@xxx.com");
//        // 电子邮件的类型
//        values.put(Email.TYPE, Email.TYPE_WORK);
//        // 向联系人Email URI添加Email数据
//        getContentResolver().insert(Data.CONTENT_URI, values);

        XToast.normal("联系人数据添加成功");
        mDialogUtils.dismissDialog();
    }

}
