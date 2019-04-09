package com.huimaibao.app.fragment.finds.act;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsCommentsAdapter;
import com.huimaibao.app.fragment.finds.adapter.FindsPraiseAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsCommentEntity;
import com.huimaibao.app.fragment.finds.entity.FindsCommentsEntity;
import com.huimaibao.app.fragment.finds.entity.FindsPraiseEntity;
import com.huimaibao.app.fragment.home.act.ReportActivity;
import com.huimaibao.app.fragment.mine.act.FeedbackActivity;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.utils.ToastUtils;
import com.huimaibao.app.view.NineGridViewLayout;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.GifView;
import com.youth.xframe.widget.NoScrollListView;
import com.youth.xframe.widget.XScrollView;
import com.youth.xframe.widget.XSwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态
 */
public class FindsCommentsActivity extends BaseActivity {

    //top
    private ImageView _top_focus_iv;
    private LinearLayout _top_user_ll;
    private CircleImageView _top_head;
    private TextView _top_name, _top_title, _top_comment_num;

    //dy
    private CircleImageView _item_head;
    private TextView _item_name, _item_content, _item_time, _item_praise_num, _item_comments_num;
    private ImageView _item_focus_iv, _item_praise_iv;
    private NineGridViewLayout _item_images;
    private LinearLayout _item_ll, _item_praise_ll;
    private RelativeLayout _item_comment_ll;
    private ImageView _item_comment_iv_1, _item_comment_iv_2, _item_comment_iv_3, _item_comment_iv_4, _item_comment_iv_more;

    private String _dy_userid_value = "", _dy_head_value = "", _dy_name_value = "";

    private XScrollView mScrollView;
    private NoScrollListView mListView;

    private FindsCommentsAdapter mAdapter;
    private List<FindsCommentsEntity> listData;
    private List<String> listImage;

    //加载更多
    private View mFooterView;
    private GifView _footer_gif;
    private boolean isLoading = false;
    private int countPage = 1;


    private DialogUtils mDialogUtils;

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

        mDialogUtils = new DialogUtils(mActivity);

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

        _item_ll = findViewById(R.id.finds_list_top_ll);
        _item_head = findViewById(R.id.finds_list_top_head);
        _item_name = findViewById(R.id.finds_list_top_name);
        _item_content = findViewById(R.id.finds_list_top_content);
        _item_time = findViewById(R.id.finds_list_top_time);
        _item_comments_num = findViewById(R.id.finds_list_top_comments_num);
        _item_focus_iv = findViewById(R.id.finds_list_top_foucs_btn);
        _item_praise_num = findViewById(R.id.finds_list_top_praise_num);
        _item_praise_iv = findViewById(R.id.finds_list_top_praise_iv);
        _item_praise_ll = findViewById(R.id.finds_list_top_praise_ll);
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
                    _top_user_ll.setVisibility(View.VISIBLE);
                    _top_focus_iv.setVisibility(View.VISIBLE);
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

//                params = _top_layout.getLayoutParams();
//                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                params.height = XDensityUtils.dp2px(45) + XDensityUtils.getStatusBarHeight();
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


    /***/
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
        _item_praise_num.setText("0");
        _item_images.setUrlList(listImage);


//        _item_comment_ll = findViewById(R.id.finds_list_top_iv_ll);
//        _item_comment_iv_1 = findViewById(R.id.finds_list_top_iv_1);
//        _item_comment_iv_2 = findViewById(R.id.finds_list_top_iv_2);
//        _item_comment_iv_3 = findViewById(R.id.finds_list_top_iv_3);
//        _item_comment_iv_4 = findViewById(R.id.finds_list_top_iv_4);
//        _item_comment_iv_more = findViewById(R.id.finds_list_top_iv_more);
//        _item_focus_iv = findViewById(R.id.finds_list_top_foucs_btn);
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
            entity.setFindsTime("2019-04-03 1" + i + ":30:00");

            List<FindsCommentEntity> list = new ArrayList<>();

            if (i % 4 == 0) {
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

            listData.add(entity);
        }
        mAdapter = new FindsCommentsAdapter(mActivity, listData);
        mListView.setAdapter(mAdapter);
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

            List<FindsCommentEntity> list = new ArrayList<>();

            if (i % 4 == 0) {
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
                break;
            case R.id.finds_list_top_praise_ll:
                xFAPraise = new XFrameAnimation(_item_praise_iv, praiseRes, 30, false);
                break;
        }
    }

}
