package com.huimaibao.app.fragment.finds.act;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsCommentsAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsCommentEntity;
import com.huimaibao.app.fragment.finds.entity.FindsCommentsEntity;
import com.huimaibao.app.fragment.finds.server.FindsLogic;
import com.huimaibao.app.fragment.home.act.ReportActivity;
import com.huimaibao.app.fragment.mine.act.FeedbackActivity;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.utils.ToastUtils;
import com.huimaibao.app.view.FriendsCircleImageLayout;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.GifView;
import com.youth.xframe.widget.NoScrollListView;
import com.youth.xframe.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 动态
 */
public class FindsCommentsActivity extends BaseActivity {

    private String mType = "动态";

    //top
    private ImageView _top_focus_iv;
    private LinearLayout _top_user_ll;
    private CircleImageView _top_head;
    private TextView _top_name, _top_title, _top_comment_num;

    //button 添加评论
    private EditText _add_comment;
    private TextView _add_comment_btn;
    private String _add_comment_value = "", _comment_id_value = "";
    //评论类型(主-0，子-1)
    private String _comment_type = "0";
    //评论上级id;0-评论动态
    private String _superior_id_value = "0";
    //评论或回复id;0-评论动态
    private String _comment_id_r_value = "0";
    //子级评论或回复
    private String _c_type_value = "0", _c_name_value = "";

    //
    private int _group_position = 0, _child_position = 0;

    //dy
    private CircleImageView _item_head;
    private TextView _item_name, _item_content, _item_time, _item_praise_num, _item_comments_num;
    private ImageView _item_focus_iv, _item_praise_iv;
    private FriendsCircleImageLayout _item_images;
    private LinearLayout _item_ll;
    private RelativeLayout _item_comment_ll;
    private CircleImageView _item_comment_iv_1, _item_comment_iv_2, _item_comment_iv_3, _item_comment_iv_4, _item_comment_iv_more;

    private String _dynamic_id_value = "", _dy_cardid_value = "", _dy_userid_value = "", _userid_value = "", _dy_head_value = "", _dy_name_value = "", _dy_content_value = "", _dy_images_value = "", _dy_time_value = "", _dy_comments_num_value = "0", _dy_isfocus_value = "0", _dy_ispraise_value = "0", _dy_praisenum_value = "0", _dy_comment_head_value = "";
    //评论头像
    private List<String> commentHeadList;


    private XScrollView mScrollView;
    private NoScrollListView mListView;
    private FindsCommentsAdapter mAdapter;
    private List<FindsCommentsEntity> listData;//评论集合
    private List<String> listImage;//动态图片集合
    private List<String> listCommentIDData;//已有子评论id集合

    //加载更多
    private View mFooterView;
    private GifView _footer_gif;
    private boolean isLoading = false;
    private int countPage = 1, totalPage = 1, countPageChild = 1, totalPageChild = 1, praise_num = 0;


    private DialogUtils mDialogUtils;
    //复制
    private ClipboardManager myClipboard;
    private ClipData myClip;

    private XFrameAnimation xFAFocus, xFAPraise;

    private int[] focusRes = {R.drawable.finds_list_top_focus, R.drawable.finds_list_top_focus_1, R.drawable.finds_list_top_focus_2,
            R.drawable.finds_list_top_focus_3, R.drawable.finds_list_top_focus_4, R.drawable.finds_list_top_focus_5
            , R.drawable.finds_list_top_focus_6, R.drawable.finds_list_top_focus_7, R.drawable.finds_list_top_focus_8
            , R.drawable.finds_list_top_focus_9, R.drawable.finds_list_top_focus_10, R.drawable.finds_list_top_focus_11, R.drawable.finds_list_top_focus_12
            , R.drawable.finds_list_top_focus_13, R.drawable.finds_list_top_focus_14, R.drawable.finds_list_top_focus_15
            , R.drawable.finds_list_top_focus_16, R.drawable.finds_list_top_focus_17, R.drawable.finds_list_top_focus_18
            , R.drawable.finds_list_top_focus_19, R.drawable.finds_list_top_focus_20, R.drawable.finds_list_top_focus_22, R.drawable.finds_list_top_focus_23
            , R.drawable.finds_list_top_focus_24, R.drawable.finds_list_top_focus_25, R.drawable.finds_list_top_focus_26};

    private int[] praiseRes = {R.drawable.finds_list_praise, R.drawable.finds_list_praise_1, R.drawable.finds_list_praise_2, R.drawable.finds_list_praise_3
            , R.drawable.finds_list_praise_4, R.drawable.finds_list_praise_5, R.drawable.finds_list_praise_6, R.drawable.finds_list_praise_7
            , R.drawable.finds_list_praise_8, R.drawable.finds_list_praise_9, R.drawable.finds_list_praise_10, R.drawable.finds_list_praise_11
            , R.drawable.finds_list_praise_12, R.drawable.finds_list_praise_13, R.drawable.finds_list_praise_14, R.drawable.finds_list_praise_15
            , R.drawable.finds_list_praise_16, R.drawable.finds_list_praise_17, R.drawable.finds_list_praise_18, R.drawable.finds_list_praise_19
            , R.drawable.finds_list_praise_20, R.drawable.finds_list_praise_21, R.drawable.finds_list_praise_22, R.drawable.finds_list_praise_23
            , R.drawable.finds_list_praise_24, R.drawable.finds_list_praise_25, R.drawable.finds_list_praise_26, R.drawable.finds_list_praise_27
            , R.drawable.finds_list_praise_28, R.drawable.finds_list_praise_29, R.drawable.finds_list_praise_30, R.drawable.finds_list_praise_31,
            R.drawable.finds_list_praise_32, R.drawable.finds_list_praise_33};

    //动态图片集合
    private String[] mUrls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_comments);
        setNeedBackGesture(true);

        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }


        initView();
        initData();
    }

    /***/
    private void initView() {
        mDialogUtils = new DialogUtils(mActivity);
        myClipboard = (ClipboardManager) mActivity.getSystemService(mActivity.CLIPBOARD_SERVICE);
        _dy_isfocus_value = XPreferencesUtils.get("concern", "0").toString();
        _dynamic_id_value = XPreferencesUtils.get("dynamic_id", "").toString();
        _userid_value = XPreferencesUtils.get("user_id", "").toString();

        _top_focus_iv = findViewById(R.id.finds_list_top_foucs);
        _top_user_ll = findViewById(R.id.finds_list_top_user_ll);
        _top_head = findViewById(R.id.finds_list_top_user_head);
        _top_name = findViewById(R.id.finds_list_top_user_name);
        _top_title = findViewById(R.id.finds_list_title);
        _top_comment_num = findViewById(R.id.finds_list_comments_num);

        _add_comment = findViewById(R.id.finds_add_comments);
        _add_comment_btn = findViewById(R.id.finds_add_comments_btn);
        _add_comment.addTextChangedListener(addCommentWatcher);
        _add_comment_btn.setEnabled(false);
        _add_comment.setFocusable(false);
        _add_comment.setFocusableInTouchMode(false);

//        if (mType.equals("动态")) {
//            _add_comment.setFocusable(false);
//            _add_comment.setFocusableInTouchMode(false);
//        } else {
//            _comment_type = "0";
//            _add_comment.setFocusable(true);
//            _add_comment.setFocusableInTouchMode(true);
//            XKeyboardUtils.openKeyboard(mActivity, _add_comment);
//        }

        XKeyboardUtils.observeSoftKeyboard(mActivity, new XKeyboardUtils.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeyboardHeight, boolean visible) {
                if (!visible) {
                    _superior_id_value = "0";
                    _comment_type = "0";
                    _add_comment.setText("");
                    _add_comment.setHint("发表一下看法");
                }
            }
        });

        _add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _comment_type = "0";
                _add_comment.setFocusable(true);
                _add_comment.setFocusableInTouchMode(true);
                XKeyboardUtils.openKeyboard(mActivity, _add_comment);
            }
        });


        _item_ll = findViewById(R.id.finds_list_top_ll);
        _item_head = findViewById(R.id.finds_list_top_head);
        _item_name = findViewById(R.id.finds_list_top_name);
        _item_content = findViewById(R.id.finds_list_top_content);
        _item_time = findViewById(R.id.finds_list_top_time);
        _item_comments_num = findViewById(R.id.finds_list_top_comments_num);
        _item_focus_iv = findViewById(R.id.finds_list_top_foucs_btn);
        _item_praise_num = findViewById(R.id.finds_list_top_praise_num);
        _item_praise_iv = findViewById(R.id.finds_list_top_praise_iv);
        _item_images = findViewById(R.id.finds_list_item_comments_grid);
        _item_comment_ll = findViewById(R.id.finds_list_top_iv_ll);
        _item_comment_iv_1 = findViewById(R.id.finds_list_top_iv_1);
        _item_comment_iv_2 = findViewById(R.id.finds_list_top_iv_2);
        _item_comment_iv_3 = findViewById(R.id.finds_list_top_iv_3);
        _item_comment_iv_4 = findViewById(R.id.finds_list_top_iv_4);
        _item_comment_iv_more = findViewById(R.id.finds_list_top_iv_more);


        mScrollView = findViewById(R.id.scrollView);
        mListView = findViewById(R.id.finds_comments_list);
        //mListView.setFocusable(false);


        mFooterView = View.inflate(mActivity, R.layout.view_footer, null);
        _footer_gif = mFooterView.findViewById(R.id.load_gif);
        _footer_gif.setMovieResource(com.youth.xframe.R.raw.load_gif_icon);


        mScrollView.setOnScrollListener(new XScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                LogUtils.debug("scrollY:" + scrollY);
                if (scrollY > 166) {
                    if (_dy_isfocus_value.equals("0")) {
                        _top_focus_iv.setVisibility(View.VISIBLE);
                    } else {
                        _top_focus_iv.setVisibility(View.GONE);
                    }
                    _top_user_ll.setVisibility(View.VISIBLE);
                    _top_title.setVisibility(View.INVISIBLE);
                } else {
                    _top_user_ll.setVisibility(View.GONE);
                    _top_focus_iv.setVisibility(View.GONE);
                    _top_title.setVisibility(View.VISIBLE);
                }

                if (scrollY > _item_ll.getHeight() - XDensityUtils.dp2px(32)) {
                    _top_comment_num.setVisibility(View.VISIBLE);
                } else {
                    _top_comment_num.setVisibility(View.GONE);
                }

            }
        });


        mScrollView.setScanScrollChangedListener(new XScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                if (countPage >= totalPage) {
                    //showToast("没有数据了");
                    return;
                }
                if (!isLoading) {
                    isLoading = true;
                    mListView.removeFooterView(mFooterView);
                    mListView.addFooterView(mFooterView);
                    loadMoreData();
                }

            }

            @Override
            public void onScrolledToTop() {

            }
        });

    }


    /**
     * 显示动态数据
     */
    private void setDynamicData() {

        //动态图片
        listImage = new ArrayList<>();
        try {
            if (!XEmptyUtils.isSpace(_dy_images_value)) {
                mUrls = new String[]{};
                mUrls = _dy_images_value.split(",");
                for (int j = 0; j < mUrls.length; j++) {
                    listImage.add(mUrls[j]);
                }
            }

        } catch (Exception e) {

        }


        ImageLoaderManager.loadImage(_dy_head_value, _top_head, R.drawable.ic_default);
        ImageLoaderManager.loadImage(_dy_head_value, _item_head, R.drawable.ic_default);


        _top_name.setText(_dy_name_value.length() > 5 ? _dy_name_value.substring(0, 5) : _dy_name_value);
        _item_name.setText(_dy_name_value);
        if (XEmptyUtils.isSpace(_dy_content_value)) {
            _item_content.setVisibility(View.GONE);
        } else {
            _item_content.setVisibility(View.VISIBLE);
            _item_content.setText(XStringUtils.ToDBC(_dy_content_value));
        }

        _item_time.setText(XTimeUtils.getTimeRangeS(_dy_time_value));
        _item_comments_num.setText(_dy_comments_num_value + "条评论");
        _top_comment_num.setText(_dy_comments_num_value + "条评论");
        _item_praise_num.setText(_dy_praisenum_value);
        //_item_images.setSpacing(4);
        _item_images.init(mActivity);
        _item_images.setImageUrls(listImage);

        if (_dy_ispraise_value.equals("0")) {
            _item_praise_iv.setImageResource(R.drawable.finds_list_praise);
        } else {
            _item_praise_iv.setImageResource(R.drawable.finds_list_praise_29);
        }

        if (_dy_userid_value.equals(_userid_value)) {
            _item_focus_iv.setVisibility(View.GONE);
            _dy_isfocus_value = "1";
        } else {
            if (_dy_isfocus_value.equals("0")) {
                _item_focus_iv.setVisibility(View.VISIBLE);
            } else {
                _item_focus_iv.setVisibility(View.GONE);
            }
        }


        commentHeadList = new ArrayList<>();
        if (!XEmptyUtils.isSpace(_dy_comment_head_value) && _dy_comment_head_value.length() > 5) {
            String[] head = _dy_comment_head_value.split(",");
            for (int i = 0; i < head.length; i++) {
                commentHeadList.add(head[i]);
            }
        }


        setShowCommentHead();
        mScrollView.scrollTo(0, 0);
    }


    /**
     * 评论头像显示
     */
    private void setShowCommentHead() {
        if (commentHeadList.size() == 0) {
            _item_comment_ll.setVisibility(View.INVISIBLE);
        } else {
            _item_comment_ll.setVisibility(View.VISIBLE);
            switch (commentHeadList.size()) {
                case 1:
                    _item_comment_iv_1.setVisibility(View.VISIBLE);
                    _item_comment_iv_2.setVisibility(View.GONE);
                    _item_comment_iv_3.setVisibility(View.GONE);
                    _item_comment_iv_4.setVisibility(View.GONE);
                    _item_comment_iv_more.setVisibility(View.GONE);
                    ImageLoaderManager.loadImage(commentHeadList.get(0), _item_comment_iv_1, R.drawable.finds_comments_more_n_icon);
                    break;
                case 2:
                    _item_comment_iv_1.setVisibility(View.VISIBLE);
                    _item_comment_iv_2.setVisibility(View.VISIBLE);
                    _item_comment_iv_3.setVisibility(View.GONE);
                    _item_comment_iv_4.setVisibility(View.GONE);
                    _item_comment_iv_more.setVisibility(View.GONE);
                    ImageLoaderManager.loadImage(commentHeadList.get(0), _item_comment_iv_1, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(1), _item_comment_iv_2, R.drawable.finds_comments_more_n_icon);
                    break;
                case 3:
                    _item_comment_iv_1.setVisibility(View.VISIBLE);
                    _item_comment_iv_2.setVisibility(View.VISIBLE);
                    _item_comment_iv_3.setVisibility(View.VISIBLE);
                    _item_comment_iv_4.setVisibility(View.GONE);
                    _item_comment_iv_more.setVisibility(View.GONE);
                    ImageLoaderManager.loadImage(commentHeadList.get(0), _item_comment_iv_1, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(1), _item_comment_iv_2, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(2), _item_comment_iv_3, R.drawable.finds_comments_more_n_icon);
                    break;
                case 4:
                    _item_comment_iv_1.setVisibility(View.VISIBLE);
                    _item_comment_iv_2.setVisibility(View.VISIBLE);
                    _item_comment_iv_3.setVisibility(View.VISIBLE);
                    _item_comment_iv_4.setVisibility(View.VISIBLE);
                    _item_comment_iv_more.setVisibility(View.GONE);
                    ImageLoaderManager.loadImage(commentHeadList.get(0), _item_comment_iv_1, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(1), _item_comment_iv_2, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(2), _item_comment_iv_3, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(3), _item_comment_iv_3, R.drawable.finds_comments_more_n_icon);
                    break;
                case 5:
                    _item_comment_iv_1.setVisibility(View.VISIBLE);
                    _item_comment_iv_2.setVisibility(View.VISIBLE);
                    _item_comment_iv_3.setVisibility(View.VISIBLE);
                    _item_comment_iv_4.setVisibility(View.VISIBLE);
                    _item_comment_iv_more.setVisibility(View.VISIBLE);
                    ImageLoaderManager.loadImage(commentHeadList.get(0), _item_comment_iv_1, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(1), _item_comment_iv_2, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(2), _item_comment_iv_3, R.drawable.finds_comments_more_n_icon);
                    ImageLoaderManager.loadImage(commentHeadList.get(3), _item_comment_iv_3, R.drawable.finds_comments_more_n_icon);
                    break;
            }
        }
    }


    /***/
    private void initData() {
        getDetailsData();
        countPage = 1;
        getDYCommentData(countPage, true);
    }


    private void loadMoreData() {
        countPage++;
        getDYCommentData(countPage, false);
    }


    /**
     * 加载完成
     */
    private void loadOver() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                _footer_pro.setVisibility(View.GONE);
//                _footer_tv.setText("点击加载更多");
                isLoading = false;
                mListView.removeFooterView(mFooterView);
                //XScrollViewUtils.setListViewHeightBasedOnChildren(mListView);
            }
        });
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            //主页
            case R.id.finds_list_top_head:
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + _dy_userid_value + ServerApi.HOME_PAGE_WEB_TOKEN);
                break;
            case R.id.finds_list_top_qbfk_btn:
                mDialogUtils.showGeneralDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(FeedbackActivity.class, "意见反馈");
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ReportActivity.class, "动态", _dynamic_id_value);
                        mDialogUtils.dismissDialog();
                    }
                });
                break;
            //关注
            case R.id.finds_list_top_foucs:
            case R.id.finds_list_top_foucs_btn:
                xFAFocus = new XFrameAnimation(_top_focus_iv, focusRes, 20, false);
                xFAFocus = new XFrameAnimation(_item_focus_iv, focusRes, 20, false);

                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        getCardAdd();
                    }
                }, 540);

                break;
            //点赞
            case R.id.finds_list_top_praise_ll:
                if (_dy_ispraise_value.equals("0")) {
                    xFAPraise = new XFrameAnimation(_item_praise_iv, praiseRes, 30, false);
                }
                getDYPraiseData();
                break;
            //
            case R.id.finds_list_top_iv_ll:
                startActivity(FindsPraiseActivity.class, "点赞的人");
                break;
            //评论
            case R.id.finds_add_comments_btn:
                _add_comment_value = _add_comment.getText().toString().trim();
                if (XEmptyUtils.isSpace(_add_comment_value)) {
                    ToastUtils.showCenter("请输入评论内容");
                } else {
                    XKeyboardUtils.closeKeyboard(mActivity);
                    if (_comment_type.equals("0")) {
                        getCommentAddData("");
                    } else {
                        getCommentAddData("child");
                    }
                }
                _superior_id_value = "0";
                _comment_type = "0";
                break;
        }
    }


    /**
     * 添加评论输入监听
     */
    private TextWatcher addCommentWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                _add_comment_btn.setEnabled(true);
                _add_comment_btn.setBackgroundResource(R.drawable.btn_blue_r20_bg);
            } else {
                _add_comment_btn.setEnabled(false);
                _add_comment_btn.setBackgroundResource(R.drawable.btn_blue_r20_q_bg);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 主评论添加删除回复
     */
    private void setCommentReply(String type, final int position) {
        mDialogUtils.showFindsDYCommentDialog(type,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        _add_comment.setFocusable(true);
                        _add_comment.setFocusableInTouchMode(true);
                        XKeyboardUtils.openKeyboard(mActivity, _add_comment);
                        //回复
                        _add_comment.setHint("回复" + listData.get(position).getFindsUserName() + ":");
                        mDialogUtils.dismissDialog();
                        _group_position = position;
                        _comment_type = "1";
                        _comment_id_r_value = listData.get(position).getFindsCommentId();
                        _c_type_value = "0";
                        _c_name_value = "";

                        // _add_comment.requestFocus();
                        // _add_comment.setSelection(_add_comment.getText().toString().length());
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //复制
                        myClip = ClipData.newPlainText("text", "" + listData.get(position).getFindsContent());
                        myClipboard.setPrimaryClip(myClip);
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        mDialogUtils.dismissDialog();
                        getCommentDelData(listData.get(position).getFindsCommentId(), "", position, -1);
                    }
                });
    }

    /**
     * 子评论添加删除回复
     */
    private void setChildCommentReply(String type, final int groupPosition, final int childPosition) {
        mDialogUtils.showFindsDYCommentDialog(type,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _add_comment.setFocusable(true);
                        _add_comment.setFocusableInTouchMode(true);
                        XKeyboardUtils.openKeyboard(mActivity, _add_comment);


                        //回复
                        _add_comment.setHint("回复" + listData.get(groupPosition).getList().get(childPosition).getFindsUserName() + ":");
                        mDialogUtils.dismissDialog();
                        _comment_type = "1";
                        _comment_id_r_value = listData.get(groupPosition).getList().get(childPosition).getFindsCommentId();
                        _group_position = groupPosition;
                        _child_position = childPosition;
                        _c_type_value = "1";
                        _c_name_value = listData.get(groupPosition).getList().get(childPosition).getFindsUserName();


                        // _add_comment.requestFocus();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //复制
                        myClip = ClipData.newPlainText("text", "" + listData.get(groupPosition).getList().get(childPosition).getFindsContent());
                        myClipboard.setPrimaryClip(myClip);
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        mDialogUtils.dismissDialog();
                        getCommentDelData(listData.get(groupPosition).getList().get(childPosition).getFindsCommentId(), "child", groupPosition, childPosition);
                    }
                });
    }

    /**
     * 主评论
     */
    private void setComment() {
        if (listData == null) {
            listData = new ArrayList<>();
        }
        List<FindsCommentsEntity> listS = new ArrayList<>();
        FindsCommentsEntity entity = new FindsCommentsEntity();
        entity.setFindsUserId(XPreferencesUtils.get("user_id", "") + "");
        entity.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
        entity.setFindsUserName(XPreferencesUtils.get("name", "") + "");
        entity.setFindsCommentId(_comment_id_value);
        entity.setFindsContent(_add_comment_value);
        entity.setFindsIsPraise("0");
        entity.setFindsPraiseNum("0");
        entity.setFindsTime(XTimeUtils.getTimes());

        //子评论
        List<FindsCommentEntity> list = new ArrayList<>();
        entity.setFindsChildCommentNum(list.size() + "");
        entity.setList(list);

        listS.add(entity);
        listS.addAll(listData);

        listData.clear();
        listData.addAll(listS);

        XKeyboardUtils.closeKeyboard(mActivity);
        _superior_id_value = "0";
        _add_comment.setText("");
        _add_comment.setHint("发表一下看法");
    }

    /**
     * 子评论
     */
    private void setChildComment() {
        if (listData == null) {
            listData = new ArrayList<>();
        }

        //子评论
        List<FindsCommentEntity> list = new ArrayList<>();
        FindsCommentEntity entity2 = new FindsCommentEntity();
        entity2.setFindsUserId(XPreferencesUtils.get("user_id", "") + "");
        entity2.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
        entity2.setFindsUserName(XPreferencesUtils.get("name", "") + "");
        entity2.setFindsCommentId(_comment_id_value);
        entity2.setFindsContent(_add_comment_value);
        entity2.setFindsIsPraise("0");
        entity2.setFindsPraiseNum("0");
        entity2.setFindsTime(XTimeUtils.getTimes());
        entity2.setFindsToUserName(_c_name_value);
        entity2.setFindsType(_c_type_value);

        list.add(entity2);
        list.addAll(listData.get(_group_position).getList());

        listData.get(_group_position).setFindsChildCommentNum(list.size() + "");
        listData.get(_group_position).setList(list);


        XKeyboardUtils.closeKeyboard(mActivity);
        _superior_id_value = "0";
        _comment_type = "0";
        _add_comment.setText("");
        _add_comment.setHint("发表一下看法");
    }


    /**
     * 动态详情
     */
    private void getDetailsData() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", _dynamic_id_value);
        map.put("concern", _dy_isfocus_value);
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        LogUtils.debug("finds:" + map);
        FindsLogic.Instance(mActivity).getDYDetailsApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        if (XEmptyUtils.isSpace(json.optString("data", "")) || json.optString("data", "").length() < 5) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDialogUtils.showNoTitleDialog("该动态已删除", "取消", "确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialogUtils.dismissDialog();
                                            finish();
                                        }
                                    });
                                }
                            });
                        } else {
                            JSONObject data = new JSONObject(json.optString("data", ""));
                            _dynamic_id_value = data.optString("dynamic_id");
                            _dy_userid_value = data.optString("user_id");
                            _dy_cardid_value = data.optString("cards_id");
                            _dy_head_value = data.optString("head_picture");
                            _dy_name_value = data.optString("user_name");
                            _dy_content_value = data.optString("content");
                            _dy_images_value = data.optString("image_path");
                            _dy_time_value = data.optString("created_at");
                            _dy_comments_num_value = data.optString("comment_times");
                            _dy_isfocus_value = data.optString("concern");
                            _dy_ispraise_value = data.optString("praise");
                            _dy_praisenum_value = data.optString("praise_number");
                            _dy_comment_head_value = data.optString("userHeadPraise");

                            //final String is_delete = data.optString("is_delete", "0");

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setDynamicData();
                                }
                            });
                        }
                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.debug("finds:" + e.toString());
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                LogUtils.debug("finds:" + error);
            }
        });
    }

    /**
     * 添加关注
     */
    private void getCardAdd() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("card_id", _dy_cardid_value);
        CardClipLogic.Instance(mActivity).getCardClipAddApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new org.json.JSONObject(object.toString());
                    String msg = json.getString("message");
                    LogUtils.debug("finds:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("关注成功");
                                _item_focus_iv.setVisibility(View.GONE);
                                _top_focus_iv.setVisibility(View.GONE);
                                _dy_isfocus_value = "1";
                            }
                        });
                    } else {
                        setFocusError("关注失败," + msg);
                    }
                } catch (Exception e) {
                    setFocusError("关注失败");
                }
            }

            @Override
            public void onFailed(String error) {
                // showToast("关注失败");
                LogUtils.debug("finds:" + error);
                setFocusError("关注失败");
            }
        });
    }


    /**
     * 点赞动态
     */
    private void getDYPraiseData() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", _dynamic_id_value);
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        FindsLogic.Instance(mActivity).getDYPraiseApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    // String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (_dy_ispraise_value.equals("0")) {
                                    _dy_ispraise_value = "1";
                                    _dy_praisenum_value = "" + (Integer.parseInt(_dy_praisenum_value) + 1);
                                    _item_praise_num.setText(_dy_praisenum_value);
                                    if (commentHeadList.size() < 5) {
                                        List<String> list = new ArrayList<>();
                                        list.add(XPreferencesUtils.get("portrait", "") + "");
                                        list.addAll(commentHeadList);
                                        commentHeadList.clear();
                                        commentHeadList.addAll(list);
                                        setShowCommentHead();
                                    }
                                } else {
                                    _item_praise_iv.setImageResource(R.drawable.finds_list_praise);
                                    _dy_ispraise_value = "0";
                                    _dy_praisenum_value = (Integer.parseInt(_dy_praisenum_value) - 1) < 0 ? "0" : (Integer.parseInt(_dy_praisenum_value) - 1) + "";
                                    _item_praise_num.setText(_dy_praisenum_value);

                                    for (int i = 0; i < commentHeadList.size(); i++) {
                                        if (commentHeadList.get(i).equals(XPreferencesUtils.get("portrait", "") + "")) {
                                            commentHeadList.remove(i);
                                            break;
                                        }
                                    }
                                    setShowCommentHead();
                                }

                                if (_dy_ispraise_value.equals("0")) {
                                    _item_praise_iv.setImageResource(R.drawable.finds_list_praise);
                                } else {
                                    _item_praise_iv.setImageResource(R.drawable.finds_list_praise_29);
                                }
                            }
                        });

                    } else {
                        setPraiseError();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setPraiseError();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                setPraiseError();
            }
        });
    }


    /**
     * 关注失败
     */
    private void setFocusError(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
                _item_focus_iv.setImageResource(R.drawable.finds_list_top_focus);
                _top_focus_iv.setImageResource(R.drawable.finds_list_top_focus);
                _dy_isfocus_value = "0";
            }
        });
    }

    /**
     * 点赞失败
     */
    private void setPraiseError() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_dy_ispraise_value.equals("0")) {
                    _item_praise_iv.setImageResource(R.drawable.finds_list_praise);
                } else {
                    _item_praise_iv.setImageResource(R.drawable.finds_list_praise_29);
                }
            }
        });
    }


    /**
     * 获取动态评论
     */
    private void getDYCommentData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", XPreferencesUtils.get("dynamic_id", ""));
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("comment_id", XPreferencesUtils.get("comment_id", ""));
        map.put("page", page);
        map.put("pageSize", "10");
        FindsLogic.Instance(mActivity).getDYCommentApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        totalPage = json.getJSONObject("data").optInt("total", 0);


                        String dynamicComment, children;
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("list"));

                        dynamicComment = json.getJSONObject("data").optString("dynamicComment", "");
                        if (page == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        if (!XEmptyUtils.isSpace(dynamicComment) && dynamicComment.length() > 5) {
                            JSONObject jsonC = new JSONObject(dynamicComment);
                            FindsCommentsEntity entity = new FindsCommentsEntity();
                            entity.setFindsTime(jsonC.optString("created_at"));
                            entity.setFindsUserId(jsonC.optString("user_id"));
                            entity.setFindsUserHead(jsonC.optString("head_picture"));
                            entity.setFindsUserName(jsonC.optString("user_name"));
                            entity.setFindsCommentId(jsonC.optString("comment_id"));
                            entity.setFindsContent(jsonC.optString("content"));
                            entity.setFindsIsPraise(jsonC.optString("praise"));
                            entity.setFindsPraiseNum(jsonC.optString("praise_number"));
                            entity.setFindsChildCommentNum("0");
                            entity.setFindsIsMore(false);


                            //子评论
                            List<FindsCommentEntity> list = new ArrayList<>();
                            children = jsonC.optString("children");
                            if (!XEmptyUtils.isSpace(children) && children.length() > 5) {
                                JSONObject childJson = new JSONObject(children);
                                FindsCommentEntity entity2 = new FindsCommentEntity();
                                entity2.setFindsIsNewMsg(true);
                                entity2.setFindsUserId(childJson.optString("user_id"));
                                entity2.setFindsUserHead(childJson.optString("head_picture"));
                                entity2.setFindsUserName(childJson.optString("user_name"));
                                entity2.setFindsCommentId(childJson.optString("comment_id"));
                                entity2.setFindsContent(childJson.optString("content"));
                                entity2.setFindsIsPraise(childJson.optString("praise"));
                                entity2.setFindsPraiseNum(childJson.optString("praise_number"));
                                entity2.setFindsTime(childJson.optString("created_at"));
                                entity2.setFindsToUserName("");
                                entity2.setFindsType("0");
                                list.add(entity2);
                            }

                            entity.setList(list);
                            entity.setFindsChildCommentPage(1);
                            entity.setFindsIsNewMsg(true);
                            listData.add(entity);
                        }


                        for (int i = 0; i < array.length(); i++) {
                            FindsCommentsEntity entity = new FindsCommentsEntity();
                            entity.setFindsTime(array.getJSONObject(i).optString("created_at"));
                            entity.setFindsUserId(array.getJSONObject(i).optString("user_id"));
                            entity.setFindsUserHead(array.getJSONObject(i).optString("head_picture"));
                            entity.setFindsUserName(array.getJSONObject(i).optString("user_name"));
                            entity.setFindsCommentId(array.getJSONObject(i).optString("comment_id"));
                            entity.setFindsContent(array.getJSONObject(i).optString("content"));
                            entity.setFindsIsPraise(array.getJSONObject(i).optString("praise"));
                            entity.setFindsPraiseNum(array.getJSONObject(i).optString("praise_number"));
                            entity.setFindsChildCommentNum(array.getJSONObject(i).optString("childrenCount", "0"));

                            try {
                                if (array.getJSONObject(i).optInt("childrenCount", 0) > 1) {
                                    entity.setFindsIsMore(true);
                                } else {
                                    entity.setFindsIsMore(false);
                                }
                            } catch (Exception e) {
                                entity.setFindsIsMore(false);
                            }


                            //子评论
                            List<FindsCommentEntity> list = new ArrayList<>();
                            children = array.getJSONObject(i).optString("children");
                            if (!XEmptyUtils.isSpace(children) && children.length() > 5) {
                                JSONArray childArray = new JSONArray(children);
                                for (int j = 0; j < childArray.length(); j++) {
                                    FindsCommentEntity entity2 = new FindsCommentEntity();
                                    entity2.setFindsIsNewMsg(false);
                                    entity2.setFindsUserId(childArray.getJSONObject(j).optString("user_id"));
                                    entity2.setFindsUserHead(childArray.getJSONObject(j).optString("head_picture"));
                                    entity2.setFindsUserName(childArray.getJSONObject(j).optString("user_name"));
                                    entity2.setFindsCommentId(childArray.getJSONObject(j).optString("comment_id"));
                                    entity2.setFindsContent(childArray.getJSONObject(j).optString("content"));
                                    entity2.setFindsIsPraise(childArray.getJSONObject(j).optString("praise"));
                                    entity2.setFindsPraiseNum(childArray.getJSONObject(j).optString("praise_number"));
                                    entity2.setFindsTime(childArray.getJSONObject(j).optString("created_at"));
                                    entity2.setFindsToUserName(childArray.getJSONObject(j).optString("to_user_name", ""));
                                    entity2.setFindsType(childArray.getJSONObject(j).optString("type", "0"));
                                    list.add(entity2);
                                }
                            }

                            entity.setList(list);
                            entity.setFindsChildCommentPage(1);
                            entity.setFindsIsNewMsg(false);
                            listData.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (page == 1) {
                                    mAdapter = new FindsCommentsAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);
                                    //点赞
                                    mAdapter.setOnItemPraiseClickListener(new FindsCommentsAdapter.onItemPraiseClickListener() {
                                        @Override
                                        public void onItemPraiseClick(int position) {
                                            getCommentPraiseData(listData.get(position).getFindsCommentId(), "", position, -1);
                                        }
                                    });
                                    //子点赞
                                    mAdapter.setOnChildItemPraiseClickListener(new FindsCommentsAdapter.onChildItemPraiseClickListener() {
                                        @Override
                                        public void onChildItemPraiseClick(int groupPosition, int childPosition) {
                                            getCommentPraiseData(listData.get(groupPosition).getList().get(childPosition).getFindsCommentId(), "child", groupPosition, childPosition);
                                        }
                                    });

                                    //评论
                                    mAdapter.setOnItemReplyClickListener(new FindsCommentsAdapter.onItemReplyClickListener() {
                                        @Override
                                        public void onItemReplyClick(final int position) {
                                            _superior_id_value = listData.get(position).getFindsCommentId();
                                            if (listData.get(position).getFindsUserId().equals(_userid_value)) {
                                                setCommentReply("", position);
                                            } else {
                                                setCommentReply("0", position);
                                            }
                                        }
                                    });

                                    //子评论
                                    mAdapter.setOnChildItemReplyClickListener(new FindsCommentsAdapter.onChildItemReplyClickListener() {
                                        @Override
                                        public void onChildItemReplyClick(int groupPosition, int childPosition) {
                                            _superior_id_value = listData.get(groupPosition).getFindsCommentId();
                                            if (listData.get(groupPosition).getList().get(childPosition).getFindsUserId().equals(_userid_value)) {
                                                setChildCommentReply("", groupPosition, childPosition);
                                            } else {
                                                setChildCommentReply("0", groupPosition, childPosition);
                                            }
                                        }
                                    });
                                    //更多子评论
                                    mAdapter.setOnItemReplyMoreClickListener(new FindsCommentsAdapter.onItemReplyMoreClickListener() {
                                        @Override
                                        public void onItemReplyMoreClick(int position) {
                                            _superior_id_value = listData.get(position).getFindsCommentId();
                                            getCommentMoreData(listData.get(position).getFindsChildCommentPage(), _superior_id_value, position);
                                            //countPageChild++;
                                        }
                                    });
                                    loadOver();
                                } else {

                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        mAdapter = new FindsCommentsAdapter(mActivity, listData);
                                        mListView.setAdapter(mAdapter);
                                    }
                                    loadOver();
                                }
                            }
                        });

                    } else {
                        showToast(msg);
                        loadOver();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.debug("finds:" + e.toString());
                    loadOver();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("finds:" + error);
                loadOver();
            }
        });
    }


    /***/
    private void setDataList() {
    }


    /**
     * 点赞评论
     */
    private void getCommentPraiseData(String comment_id, final String type, final int gposition, final int cposition) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment_id", comment_id);
        map.put("dynamic_id", XPreferencesUtils.get("dynamic_id", ""));
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        FindsLogic.Instance(mActivity).getCommentPraiseApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    //String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //showToast("点赞成功");
                                if (type.equals("child")) {
                                    if (listData.get(gposition).getList().get(cposition).getFindsIsPraise().equals("0")) {
                                        praise_num = Integer.parseInt(listData.get(gposition).getList().get(cposition).getFindsPraiseNum()) + 1;
                                        listData.get(gposition).getList().get(cposition).setFindsIsPraise("1");
                                        listData.get(gposition).getList().get(cposition).setFindsPraiseNum(praise_num + "");
                                    } else {
                                        praise_num = Integer.parseInt(listData.get(gposition).getList().get(cposition).getFindsPraiseNum()) - 1;
                                        listData.get(gposition).getList().get(cposition).setFindsIsPraise("0");
                                        listData.get(gposition).getList().get(cposition).setFindsPraiseNum(praise_num < 0 ? "0" : praise_num + "");
                                    }
                                } else {
                                    if (listData.get(gposition).getFindsIsPraise().equals("0")) {
                                        praise_num = Integer.parseInt(listData.get(gposition).getFindsPraiseNum()) + 1;
                                        listData.get(gposition).setFindsIsPraise("1");
                                        listData.get(gposition).setFindsPraiseNum(praise_num + "");
                                    } else {
                                        praise_num = Integer.parseInt(listData.get(gposition).getFindsPraiseNum()) - 1;
                                        listData.get(gposition).setFindsIsPraise("0");
                                        listData.get(gposition).setFindsPraiseNum(praise_num < 0 ? "0" : praise_num + "");
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        setPraiseError("点赞失败", type, gposition, cposition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setPraiseError("点赞失败", type, gposition, cposition);
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                setPraiseError("点赞失败", type, gposition, cposition);
            }
        });
    }

    /**
     * 点赞失败
     */
    private void setPraiseError(final String msg, final String type, final int position, final int cposition) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
                if (type.equals("child")) {
                    if (listData.get(position).getList().get(cposition).getFindsIsPraise().equals("0")) {
                        listData.get(position).getList().get(cposition).setFindsIsPraise("1");
                    } else {
                        listData.get(position).getList().get(cposition).setFindsIsPraise("0");
                    }
                } else {
                    if (listData.get(position).getFindsIsPraise().equals("0")) {
                        listData.get(position).setFindsIsPraise("1");
                    } else {
                        listData.get(position).setFindsIsPraise("0");
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 添加评论
     */
    private void getCommentAddData(final String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", _add_comment_value);
        map.put("superior_id", _superior_id_value);
        map.put("dynamic_id", XPreferencesUtils.get("dynamic_id", ""));
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("comment_id", _comment_id_r_value);
        FindsLogic.Instance(mActivity).getCommentAddApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    //String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONObject data = new JSONObject(json.getString("data"));
                        _comment_id_value = data.optString("commentId", "");
                        _dy_comments_num_value = data.optString("commentCount", _dy_comments_num_value);
                        XPreferencesUtils.put("commentCount", _dy_comments_num_value);
                        XPreferencesUtils.put("is_comment_num", true);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _item_comments_num.setText(_dy_comments_num_value + "条评论");
                                _top_comment_num.setText(_dy_comments_num_value + "条评论");

                                if (type.equals("child")) {
                                    setChildComment();
                                } else {
                                    setComment();
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        showToast("评论失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("评论失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showToast("评论失败");
            }
        });
    }

    /**
     * 删除评论
     */
    private void getCommentDelData(String comment_id, final String type, final int groupPosition, final int childPosition) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment_id", comment_id);
        FindsLogic.Instance(mActivity).getCommentDelApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    //String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONObject data = new JSONObject(json.getString("data"));
                        _dy_comments_num_value = data.optString("commentCount", _dy_comments_num_value);
                        XPreferencesUtils.put("commentCount", _dy_comments_num_value);
                        XPreferencesUtils.put("is_comment_num", true);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _item_comments_num.setText(_dy_comments_num_value + "条评论");
                                _top_comment_num.setText(_dy_comments_num_value + "条评论");

                                if (type.equals("child")) {
                                    if (listData.size() > 0 && listData.get(groupPosition).getList().size() > 0) {
                                        listData.get(groupPosition).getList().remove(childPosition);
                                    }
                                } else {
                                    if (listData.size() > 0) {
                                        listData.remove(groupPosition);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        showToast("删除评论失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("删除评论失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                showToast("删除评论失败");
            }
        });
    }


    /**
     * 更多评论
     */
    private void getCommentMoreData(final int page, String superior_id, final int position) {
        listCommentIDData = new ArrayList<>();
        for (int i = 0; i < listData.get(position).getList().size(); i++) {
            listCommentIDData.add(listData.get(position).getList().get(i).getFindsCommentId().trim());
        }


        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", _dynamic_id_value);
        //map.put("page", page);
        map.put("pageSize", 5);
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("superior_id", superior_id);
        map.put("comment_id", listCommentIDData.toString().replace("[", "").replace("]", "").trim());
        LogUtils.debug("finds:" + map);
        FindsLogic.Instance(mActivity).getCommentChildAllApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    //String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        totalPageChild = json.getJSONObject("data").optInt("total", 0);
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("list"));
                        List<FindsCommentEntity> list = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            FindsCommentEntity entity = new FindsCommentEntity();
                            entity.setFindsTime(array.getJSONObject(i).optString("created_at"));
                            entity.setFindsUserId(array.getJSONObject(i).optString("user_id"));
                            entity.setFindsUserHead(array.getJSONObject(i).optString("head_picture"));
                            entity.setFindsUserName(array.getJSONObject(i).optString("user_name"));
                            entity.setFindsCommentId(array.getJSONObject(i).optString("comment_id"));
                            entity.setFindsContent(array.getJSONObject(i).optString("content"));
                            entity.setFindsIsPraise(array.getJSONObject(i).optString("praise"));
                            entity.setFindsPraiseNum(array.getJSONObject(i).optString("praise_number"));
                            entity.setFindsType(array.getJSONObject(i).optString("type", "0"));
                            entity.setFindsToUserName(array.getJSONObject(i).optString("to_user_name"));
                            list.add(entity);
                        }
                        listData.get(position).getList().addAll(list);
                        listData.get(position).setFindsChildCommentPage(listData.get(position).getFindsChildCommentPage() + 1);
                        if (totalPageChild == 1) {
                            listData.get(position).setFindsIsMore(false);
                        }

//                        if (page == 1) {
//                            listData.get(position).getList().clear();
//                            listData.get(position).setList(list);
//                        } else {
//                            listData.get(position).getList().addAll(list);
//                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    mAdapter = new FindsCommentsAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
            }
        });
    }

}
