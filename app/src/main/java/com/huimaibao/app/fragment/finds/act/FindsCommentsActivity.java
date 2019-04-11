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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsCommentsAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsCommentEntity;
import com.huimaibao.app.fragment.finds.entity.FindsCommentsEntity;
import com.huimaibao.app.fragment.home.act.ReportActivity;
import com.huimaibao.app.fragment.mine.act.FeedbackActivity;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.utils.ToastUtils;
import com.huimaibao.app.view.NineGridViewLayout;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.GifView;
import com.youth.xframe.widget.XScrollView;

import java.util.ArrayList;
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
    //
    private int _group_position = 0, _child_position = 0;

    //dy
    private CircleImageView _item_head;
    private TextView _item_name, _item_content, _item_time, _item_praise_num, _item_comments_num;
    private ImageView _item_focus_iv, _item_praise_iv;
    private NineGridViewLayout _item_images;
    private LinearLayout _item_ll;
    private RelativeLayout _item_comment_ll;
    private CircleImageView _item_comment_iv_1, _item_comment_iv_2, _item_comment_iv_3, _item_comment_iv_4, _item_comment_iv_more;

    private String _dy_userid_value = "", _dy_head_value = "", _dy_name_value = "", _dy_isfocus_value = "0", _dy_ispraise_value = "0", _dy_praisenum_value = "0", _dy_comment_head_value = "";
    //评论头像
    private List<String> commentHeadList;


    private XScrollView mScrollView;
    private ListView mListView;
    private FindsCommentsAdapter mAdapter;
    private List<FindsCommentsEntity> listData;
    private List<String> listImage;

    //加载更多
    private View mFooterView;
    private GifView _footer_gif;
    private boolean isLoading = false;
    private int countPage = 1, praise_num = 0;


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
            , R.drawable.finds_list_praise_28, R.drawable.finds_list_praise_29};


    private String[] mUrls = new String[]{
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg",
            "http://img3.fengniao.com/forum/attachpics/537/165/21472986.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3251359643,4211266111&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201506/11/20150611000809_yFe5Z.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_comments);
        setNeedBackGesture(true);

        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }


        mDialogUtils = new DialogUtils(mActivity);
        myClipboard = (ClipboardManager) mActivity.getSystemService(mActivity.CLIPBOARD_SERVICE);
        initView();
        setDynamicData();
        initData();
    }

    /***/
    private void initView() {
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
        if (mType.equals("动态")) {
            _add_comment.setFocusable(false);
            _add_comment.setFocusableInTouchMode(false);
        } else {
            _comment_type = "0";
            _add_comment.setFocusable(true);
            _add_comment.setFocusableInTouchMode(true);
            XKeyboardUtils.openKeyboard(mActivity, _add_comment);
        }


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
        mListView.setFocusable(false);


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
        _dy_userid_value = XPreferencesUtils.get("user_id", "").toString();
        _dy_head_value = XPreferencesUtils.get("portrait", "").toString();
        _dy_name_value = XPreferencesUtils.get("name", "").toString();

        listImage = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            listImage.add(mUrls[i]);
        }


        ImageLoaderManager.loadImage(_dy_head_value, _top_head, R.drawable.ic_launcher);
        ImageLoaderManager.loadImage(_dy_head_value, _item_head, R.drawable.ic_launcher);


        _top_name.setText(_dy_name_value.length() > 5 ? _dy_name_value.substring(0, 5) : _dy_name_value);
        _item_name.setText(_dy_name_value);
        _item_content.setText("土耳其真是霸道：埃尔多安就这么决定了，俄罗斯能接受吗?");
        _item_time.setText(XTimeUtils.getTimeRangeS("2019-04-03 16:30:00"));
        _item_comments_num.setText("10条评论");
        _top_comment_num.setText("10条评论");
        _item_praise_num.setText(_dy_praisenum_value);
        _item_images.setUrlList(listImage);

        if (_dy_ispraise_value.equals("0")) {
            _item_praise_iv.setImageResource(R.drawable.finds_list_praise);
        } else {
            _item_praise_iv.setImageResource(R.drawable.finds_list_praise_29);
        }

        if (_dy_isfocus_value.equals("0")) {
            _item_focus_iv.setVisibility(View.VISIBLE);
        } else {
            _item_focus_iv.setVisibility(View.GONE);
        }

        commentHeadList = new ArrayList<>();
        if (!XEmptyUtils.isSpace(_dy_comment_head_value)) {
            String[] head = _dy_comment_head_value.split(",");
            for (int i = 0; i < head.length; i++) {
                commentHeadList.add(head[i]);
            }
        }


        setShowCommentHead();
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
        countPage = 1;
        listData = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            FindsCommentsEntity entity = new FindsCommentsEntity();
            entity.setFindsUserId(XPreferencesUtils.get("user_id", "") + "");
            entity.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
            entity.setFindsUserName(XPreferencesUtils.get("name", "") + "");
            entity.setFindsCommentId("" + i);
            entity.setFindsContent("这是真的吗?这是真的吗?" + i);
            entity.setFindsIsPraise("0");
            entity.setFindsPraiseNum("" + i);
            entity.setFindsTime("2018-11-15 11:01:00");

            //子评论
            List<FindsCommentEntity> list = new ArrayList<>();
            if (i % 3 == 0) {
                FindsCommentEntity entity2 = new FindsCommentEntity();
                entity2.setFindsUserId(XPreferencesUtils.get("user_id", "") + "");
                entity2.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
                entity2.setFindsUserName(XPreferencesUtils.get("name", "") + "");
                entity2.setFindsCommentId("" + i);
                entity2.setFindsContent("这是真的吗?这是真的吗?" + i);
                entity2.setFindsIsPraise("0");
                entity2.setFindsPraiseNum("" + i);
                entity2.setFindsTime("2019-04-03 1" + i + ":30:00");
                list.add(entity2);
            }

            entity.setFindsChildCommentNum(list.size() + "");
            entity.setList(list);

            listData.add(entity);
        }
        mAdapter = new FindsCommentsAdapter(mActivity, listData);
        mListView.setAdapter(mAdapter);

        //点赞
        mAdapter.setOnItemPraiseClickListener(new FindsCommentsAdapter.onItemPraiseClickListener() {
            @Override
            public void onItemPraiseClick(int position) {
                if (listData.get(position).getFindsIsPraise().equals("0")) {
                    praise_num = Integer.parseInt(listData.get(position).getFindsPraiseNum()) + 1;
                    listData.get(position).setFindsIsPraise("1");
                    listData.get(position).setFindsPraiseNum(praise_num + "");
                } else {
                    praise_num = Integer.parseInt(listData.get(position).getFindsPraiseNum()) - 1;
                    listData.get(position).setFindsIsPraise("0");
                    listData.get(position).setFindsPraiseNum(praise_num + "");
                }

                mAdapter.notifyDataSetChanged();
            }
        });
        //子点赞
        mAdapter.setOnChildItemPraiseClickListener(new FindsCommentsAdapter.onChildItemPraiseClickListener() {
            @Override
            public void onChildItemPraiseClick(int groupPosition, int childPosition) {
                if (listData.get(groupPosition).getList().get(childPosition).getFindsIsPraise().equals("0")) {
                    praise_num = Integer.parseInt(listData.get(groupPosition).getList().get(childPosition).getFindsPraiseNum()) + 1;
                    listData.get(groupPosition).getList().get(childPosition).setFindsIsPraise("1");
                    listData.get(groupPosition).getList().get(childPosition).setFindsPraiseNum(praise_num + "");
                } else {
                    praise_num = Integer.parseInt(listData.get(groupPosition).getList().get(childPosition).getFindsPraiseNum()) - 1;
                    listData.get(groupPosition).getList().get(childPosition).setFindsIsPraise("0");
                    listData.get(groupPosition).getList().get(childPosition).setFindsPraiseNum(praise_num + "");
                }

                mAdapter.notifyDataSetChanged();
            }
        });

        //评论
        mAdapter.setOnItemReplyClickListener(new FindsCommentsAdapter.onItemReplyClickListener() {
            @Override
            public void onItemReplyClick(final int position) {
                if (listData.get(position).getFindsUserId().equals(_dy_userid_value)) {
                    setCommentReply("0", position);
                } else {
                    setCommentReply("", position);
                }
            }
        });

        //子评论
        mAdapter.setOnChildItemReplyClickListener(new FindsCommentsAdapter.onChildItemReplyClickListener() {
            @Override
            public void onChildItemReplyClick(int groupPosition, int childPosition) {
                if (listData.get(groupPosition).getList().get(childPosition).getFindsUserId().equals(_dy_userid_value)) {
                    setChildCommentReply("0", groupPosition, childPosition);
                } else {
                    setChildCommentReply("", groupPosition, childPosition);
                }
            }
        });

    }


    private void loadMoreData() {
        //_footer_pro.setVisibility(View.VISIBLE);
        // _footer_tv.setText("正在加载中...");
        countPage++;
        for (int i = 1; i < 10; i++) {
            FindsCommentsEntity entity = new FindsCommentsEntity();
            entity.setFindsUserId(XPreferencesUtils.get("user_id", "") + "");
            entity.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
            entity.setFindsUserName(XPreferencesUtils.get("name", "") + "");
            entity.setFindsCommentId("" + i);
            entity.setFindsContent("这是真的吗?这是真的吗?" + i);
            entity.setFindsIsPraise("0");
            entity.setFindsPraiseNum("" + i);
            entity.setFindsTime("2019-04-03 1" + i + ":30:00");

            //子评论
            List<FindsCommentEntity> list = new ArrayList<>();
            if (i % 3 == 0) {
                FindsCommentEntity entity2 = new FindsCommentEntity();
                entity2.setFindsUserId(XPreferencesUtils.get("user_id", "") + "");
                entity2.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
                entity2.setFindsUserName(XPreferencesUtils.get("name", "") + "");
                entity2.setFindsCommentId("" + i);
                entity2.setFindsContent("这是真的吗?这是真的吗?" + i);
                entity2.setFindsIsPraise("0");
                entity2.setFindsPraiseNum("" + i);
                entity2.setFindsTime("2019-04-03 1" + i + ":30:00");
                list.add(entity2);
            }

            entity.setList(list);

            entity.setFindsChildCommentNum(list.size() + "");
            listData.add(entity);
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new FindsCommentsAdapter(mActivity, listData);
            mListView.setAdapter(mAdapter);
        }
        loadOver();
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
            case R.id.finds_list_top_qbfk_btn:
                mDialogUtils.showGeneralDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(FeedbackActivity.class, "意见反馈", _dy_userid_value);
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ReportActivity.class, "名片", _dy_userid_value);
                        mDialogUtils.dismissDialog();
                    }
                });
                break;
            case R.id.finds_list_top_foucs:
            case R.id.finds_list_top_foucs_btn:
                xFAFocus = new XFrameAnimation(_top_focus_iv, focusRes, 30, false);
                xFAFocus = new XFrameAnimation(_item_focus_iv, focusRes, 30, false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        _item_focus_iv.setVisibility(View.GONE);
                        _top_focus_iv.setVisibility(View.GONE);
                        _dy_isfocus_value = "1";
                    }
                }, 810);
                break;
            case R.id.finds_list_top_praise_ll:
                if (_dy_ispraise_value.equals("0")) {
                    xFAPraise = new XFrameAnimation(_item_praise_iv, praiseRes, 30, false);
                    _dy_ispraise_value = "1";
                    _dy_praisenum_value = "" + (Integer.parseInt(_dy_praisenum_value) + 1);
                    _item_praise_num.setText(_dy_praisenum_value);
                    if (commentHeadList.size() < 5) {
                        commentHeadList.add(_dy_head_value);
                        setShowCommentHead();
                    }
                } else {
                    _item_praise_iv.setImageResource(R.drawable.finds_list_praise);
                    _dy_ispraise_value = "0";
                    _dy_praisenum_value = "" + (Integer.parseInt(_dy_praisenum_value) - 1);
                    _item_praise_num.setText(_dy_praisenum_value);

                    for (int i = 0; i < commentHeadList.size(); i++) {
                        if (commentHeadList.get(i).equals(_dy_head_value)) {
                            commentHeadList.remove(i);
                            break;
                        }
                    }
                    setShowCommentHead();
                }

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (_dy_ispraise_value.equals("0")) {
                            _item_praise_iv.setImageResource(R.drawable.finds_list_praise);
                        } else {
                            _item_praise_iv.setImageResource(R.drawable.finds_list_praise_29);
                        }
                    }
                }, 900);
                break;
            case R.id.finds_list_top_iv_ll:
                startActivity(FindsPraiseActivity.class, "点赞的人");
                break;
            case R.id.finds_add_comments_btn:
                _add_comment_value = _add_comment.getText().toString().trim();
                if (XEmptyUtils.isSpace(_add_comment_value)) {
                    ToastUtils.showCenter("请输入评论内容");
                } else {
                    if (_comment_type.equals("0")) {
                        setComment();
                    } else {
                        setChildComment();
                    }
                }
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
                        //回复
                        _add_comment.setHint("回复" + listData.get(position).getFindsUserName() + ":");
                        mDialogUtils.dismissDialog();
                        _group_position = position;
                        _comment_type = "1";

                        _add_comment.setFocusable(true);
                        _add_comment.setFocusableInTouchMode(true);
                        XKeyboardUtils.openKeyboard(mActivity, _add_comment);
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
                        if (listData.size() > 0) {
                            listData.remove(position);
                            mAdapter.notifyDataSetChanged();
                        }
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
                        //回复
                        _add_comment.setHint("回复" + listData.get(groupPosition).getList().get(childPosition).getFindsUserName() + ":");
                        mDialogUtils.dismissDialog();
                        _comment_type = "1";
                        _group_position = groupPosition;
                        _child_position = childPosition;

                        _add_comment.setFocusable(true);
                        _add_comment.setFocusableInTouchMode(true);
                        XKeyboardUtils.openKeyboard(mActivity, _add_comment);
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
                        //删除
                        mDialogUtils.dismissDialog();
                        if (listData.size() > 0 && listData.get(groupPosition).getList().size() > 0) {
                            listData.get(groupPosition).getList().remove(childPosition);
                            mAdapter.notifyDataSetChanged();
                        }
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
        list.add(entity2);
        list.addAll(listData.get(_group_position).getList());

        listData.get(_group_position).setFindsChildCommentNum(list.size() + "");
        listData.get(_group_position).setList(list);


        XKeyboardUtils.closeKeyboard(mActivity);
        _add_comment.setText("");
        _add_comment.setHint("发表一下看法");
    }


}
