package com.huimaibao.app.fragment.finds.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.finds.entity.FindsCommentsEntity;
import com.huimaibao.app.fragment.finds.entity.FindsPraiseEntity;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.NoScrollListView;

import java.util.List;

/**
 * 动态适配器
 */
public class FindsCommentsAdapter extends BaseAdapter {
    private Activity mActivity;
    public List<FindsCommentsEntity> list;

    FindsCommentAdapter commentAdapter;

    private CharSequence charSequence;
    private String content_v = "";

    private XFrameAnimation xFAPraise;

    private int[] praiseRes = {R.drawable.finds_list_praise, R.drawable.finds_list_praise_1, R.drawable.finds_list_praise_2, R.drawable.finds_list_praise_3
            , R.drawable.finds_list_praise_4, R.drawable.finds_list_praise_5, R.drawable.finds_list_praise_6, R.drawable.finds_list_praise_7
            , R.drawable.finds_list_praise_8, R.drawable.finds_list_praise_9, R.drawable.finds_list_praise_10, R.drawable.finds_list_praise_11
            , R.drawable.finds_list_praise_12, R.drawable.finds_list_praise_13, R.drawable.finds_list_praise_14, R.drawable.finds_list_praise_15
            , R.drawable.finds_list_praise_16, R.drawable.finds_list_praise_17, R.drawable.finds_list_praise_18, R.drawable.finds_list_praise_19
            , R.drawable.finds_list_praise_20, R.drawable.finds_list_praise_21, R.drawable.finds_list_praise_22, R.drawable.finds_list_praise_23
            , R.drawable.finds_list_praise_24, R.drawable.finds_list_praise_25, R.drawable.finds_list_praise_26, R.drawable.finds_list_praise_27
            , R.drawable.finds_list_praise_28, R.drawable.finds_list_praise_29};


    public FindsCommentsAdapter(Activity activity, List<FindsCommentsEntity> list) {
        this.mActivity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public FindsCommentsEntity getItem(int position) {
        if (list != null && list.size() != 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.act_finds_list_comments_item, null);
            mHolder = new ViewHolder();
            mHolder._item_head = convertView.findViewById(R.id.finds_list_comments_item_head);
            mHolder._item_name = convertView.findViewById(R.id.finds_list_comments_item_name);
            mHolder._item_content = convertView.findViewById(R.id.finds_list_comments_item_content);
            mHolder._item_praise_num = convertView.findViewById(R.id.finds_list_comments_item_praise_num);
            mHolder._item_more = convertView.findViewById(R.id.finds_list_comments_item_more);
            mHolder._item_praise_iv = convertView.findViewById(R.id.finds_list_comments_item_praise_iv);
            mHolder._item_praise_ll = convertView.findViewById(R.id.finds_list_comments_item_praise_ll);
            mHolder._item_list = convertView.findViewById(R.id.finds_list_comments_item_list);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final FindsCommentsEntity item = getItem(position);

        if (!item.getFindsUserHead().equals(mHolder._item_head.getTag())) {
            ImageLoaderManager.loadImage(item.getFindsUserHead(), mHolder._item_head);
            mHolder._item_head.setTag(item.getFindsUserHead());
        }

        mHolder._item_name.setText(item.getFindsUserName());
        mHolder._item_praise_num.setText(item.getFindsPraiseNum());

        SpannableString ssContent, ssTime;

        ssContent = new SpannableString(item.getFindsContent() + "  ");
        ssContent.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, ssContent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssContent.setSpan(new AbsoluteSizeSpan(15, true), 0, ssContent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssTime = new SpannableString(XTimeUtils.getTimeRangeS(item.getFindsTime()));
        ssTime.setSpan(new ForegroundColorSpan(Color.parseColor("#a2a2a2")), 0, ssTime.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssTime.setSpan(new AbsoluteSizeSpan(12, true), 0, ssTime.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableStringBuilder multiWord = new SpannableStringBuilder();
        multiWord.append(ssContent);
        multiWord.append(ssTime);

        mHolder._item_content.setText(multiWord);

        if (item.getList().size() == 0) {
            mHolder._item_list.setVisibility(View.GONE);
            mHolder._item_more.setVisibility(View.GONE);
        } else {
            if (Integer.parseInt(item.getFindsChildCommentNum()) > 1) {
                mHolder._item_more.setVisibility(View.VISIBLE);
            } else {
                mHolder._item_more.setVisibility(View.GONE);
            }
            mHolder._item_list.setVisibility(View.VISIBLE);
            commentAdapter = new FindsCommentAdapter(mActivity, item.getList());
            mHolder._item_list.setAdapter(commentAdapter);
            commentAdapter.setOnItemPraiseClickListener(new FindsCommentAdapter.onItemPraiseClickListener() {
                @Override
                public void onItemPraiseClick(int childPosition) {
                    mOnChildItemPraiseClickListener.onChildItemPraiseClick(position, childPosition);
                }
            });
            commentAdapter.setOnItemReplyClickListener(new FindsCommentAdapter.onItemReplyClickListener() {
                @Override
                public void onItemReplyClick(int childPosition) {
                    mOnChildItemReplyClickListener.onChildItemReplyClick(position, childPosition);
                }
            });
        }


        if (item.getFindsIsPraise().equals("0")) {
            mHolder._item_praise_iv.setImageResource(R.drawable.finds_list_praise);
        } else {
            mHolder._item_praise_iv.setImageResource(R.drawable.finds_list_praise_29);
        }

        mHolder._item_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL);
            }
        });
        mHolder._item_praise_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getFindsIsPraise().equals("0")) {
                    xFAPraise = new XFrameAnimation(mHolder._item_praise_iv, praiseRes, 30, false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (item.getFindsIsPraise().equals("0")) {
                                mOnItemPraiseClickListener.onItemPraiseClick(position);
                            }
                        }
                    }, 900);
                } else {
                    mOnItemPraiseClickListener.onItemPraiseClick(position);
                }

            }
        });
        mHolder._item_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemReplyClickListener.onItemReplyClick(position);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        CircleImageView _item_head;
        TextView _item_name, _item_content, _item_praise_num, _item_more;
        ImageView _item_praise_iv;
        LinearLayout _item_praise_ll;
        NoScrollListView _item_list;
    }


    private void startActivity(Class<?> classs, String type, String url) {
        Intent intent = new Intent();
        intent.setClass(mActivity, classs);
        intent.setAction("one");
        intent.putExtra("vType", type);
        intent.putExtra("vUrl", url);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    /**
     * 赞点击监听接口
     */
    private onItemPraiseClickListener mOnItemPraiseClickListener;

    public interface onItemPraiseClickListener {
        void onItemPraiseClick(int position);
    }

    public void setOnItemPraiseClickListener(onItemPraiseClickListener mOnItemPraiseClickListener) {
        this.mOnItemPraiseClickListener = mOnItemPraiseClickListener;
    }


    /**
     * 子赞点击监听接口
     */
    private onChildItemPraiseClickListener mOnChildItemPraiseClickListener;

    public interface onChildItemPraiseClickListener {
        void onChildItemPraiseClick(int groupPosition, int childPosition);
    }

    public void setOnChildItemPraiseClickListener(onChildItemPraiseClickListener mOnChildItemPraiseClickListener) {
        this.mOnChildItemPraiseClickListener = mOnChildItemPraiseClickListener;
    }

    /**
     * 回复点击监听接口
     */
    private onItemReplyClickListener mOnItemReplyClickListener;

    public interface onItemReplyClickListener {
        void onItemReplyClick(int position);
    }

    public void setOnItemReplyClickListener(onItemReplyClickListener mOnItemReplyClickListener) {
        this.mOnItemReplyClickListener = mOnItemReplyClickListener;
    }

    /**
     * 子回复点击监听接口
     */
    private onChildItemReplyClickListener mOnChildItemReplyClickListener;

    public interface onChildItemReplyClickListener {
        void onChildItemReplyClick(int groupPosition, int childPosition);
    }

    public void setOnChildItemReplyClickListener(onChildItemReplyClickListener mOnChildItemReplyClickListener) {
        this.mOnChildItemReplyClickListener = mOnChildItemReplyClickListener;
    }

}