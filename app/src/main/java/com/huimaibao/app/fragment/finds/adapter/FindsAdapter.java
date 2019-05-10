package com.huimaibao.app.fragment.finds.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.finds.act.FindsCommentsActivity;
import com.huimaibao.app.fragment.finds.entity.FindsEntity;
import com.huimaibao.app.fragment.home.act.ReportActivity;
import com.huimaibao.app.fragment.library.act.ImageShowActivity;
import com.huimaibao.app.fragment.mine.act.FeedbackActivity;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.view.FriendsCircleImageLayout;
import com.huimaibao.app.view.NineGridViewLayout;
import com.huimaibao.app.view.RatioImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态适配器
 */
public class FindsAdapter extends BaseAdapter {
    private Activity mActivity;
    private String mType = "发现";
    public List<FindsEntity> list;
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
            , R.drawable.finds_list_praise_28, R.drawable.finds_list_praise_29,R.drawable.finds_list_praise_30,R.drawable.finds_list_praise_31,
            R.drawable.finds_list_praise_32,R.drawable.finds_list_praise_33};


    public FindsAdapter(Activity activity, String type, List<FindsEntity> list) {
        this.mActivity = activity;
        this.mType = type;
        this.list = list;
        mDialogUtils = new DialogUtils(mActivity);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public FindsEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.act_finds_list_item, null);
            mHolder = new ViewHolder();
            mHolder._item_head = convertView.findViewById(R.id.finds_list_item_head);
            mHolder._item_name = convertView.findViewById(R.id.finds_list_item_name);
            mHolder._item_content = convertView.findViewById(R.id.finds_list_item_content);
            mHolder._item_time = convertView.findViewById(R.id.finds_list_item_time);
            mHolder._item_praise_num = convertView.findViewById(R.id.finds_list_item_praise_num);
            mHolder._item_comments_num = convertView.findViewById(R.id.finds_list_item_comments_num);
            mHolder._item_focus_iv = convertView.findViewById(R.id.finds_list_item_focus);
            mHolder._item_praise_iv = convertView.findViewById(R.id.finds_list_item_praise_iv);
            mHolder._item_praise_ll = convertView.findViewById(R.id.finds_list_item_praise_ll);
            mHolder._item_feedback_iv = convertView.findViewById(R.id.finds_list_item_feedback);
            mHolder._item_one_image = convertView.findViewById(R.id.finds_list_item_grid_one);
            mHolder._item_images = convertView.findViewById(R.id.finds_list_item_grid);
            mHolder._item_del = convertView.findViewById(R.id.finds_list_item_praise_del);

            if (mType.equals("发现")) {
                mHolder._item_del.setVisibility(View.GONE);
            } else {
                mHolder._item_feedback_iv.setVisibility(View.GONE);
            }
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final FindsEntity item = getItem(position);

        if (item.getFindsUserHead().equals(mHolder._item_head.getTag())) {

        } else {
            ImageLoaderManager.loadImage(item.getFindsUserHead(), mHolder._item_head);
            mHolder._item_head.setTag(item.getFindsUserHead());
        }


        mHolder._item_name.setText(item.getFindsUserName());
        if (XEmptyUtils.isSpace(item.getFindsContent())) {
            mHolder._item_content.setVisibility(View.GONE);
        } else {
            mHolder._item_content.setVisibility(View.VISIBLE);
            mHolder._item_content.setText(XStringUtils.ToDBC(item.getFindsContent()));
        }
        mHolder._item_time.setText(XTimeUtils.getTimeRangeS(item.getFindsTime()));
        mHolder._item_praise_num.setText(item.getFindsPraiseNum());
        mHolder._item_comments_num.setText(item.getFindsCommentsNum());
        mHolder._item_one_image.setVisibility(View.GONE);
        //mHolder._item_images.setSpacing(4);

//        if (item.getFindsImageList().size() == 1) {
//            mHolder._item_images.setItemAspectRatio(ImageLoaderManager.getAspectRatio(item.getFindsImageList().get(0).trim()));
//        } else {
//            mHolder._item_images.setItemAspectRatio(1);
//        }
//
        mHolder._item_images.init(mActivity);
        if (item.getFindsImageList().equals(mHolder._item_images.getTag())) {

        } else {
            mHolder._item_images.setImageUrls(item.getFindsImageList());
            mHolder._item_images.setTag(item.getFindsImageList());
        }


        //  mHolder._item_images.setUrlList(item.getFindsImageList());

//        if (item.getFindsImageList().size() == 1) {
//            mHolder._item_one_image.setVisibility(View.VISIBLE);
//            // mHolder._item_images.setVisibility(View.GONE);
//            if (item.getFindsImageList().get(0).equals(mHolder._item_one_image.getTag())) {
//
//            } else {
//                showOneImageData(mHolder._item_one_image, item.getFindsImageList().get(0));
//                mHolder._item_one_image.setTag(item.getFindsImageList().get(0));
//            }
//        } else {
//            mHolder._item_one_image.setVisibility(View.GONE);
//            //mHolder._item_images.setVisibility(View.VISIBLE);
//            mHolder._item_images.setIsShowAll(false);
//            if (item.getFindsImageList().equals(mHolder._item_images.getTag())) {
//
//            } else {
//                mHolder._item_images.setUrlList(item.getFindsImageList());
//                mHolder._item_images.setTag(item.getFindsImageList());
//            }
//
//        }


        if (item.getFindsUserId().equals(XPreferencesUtils.get("user_id", ""))) {
            mHolder._item_focus_iv.setVisibility(View.GONE);
        } else {
            if (item.getFindsIsFocus().equals("0")) {
                mHolder._item_focus_iv.setImageResource(R.drawable.finds_list_top_focus);
                mHolder._item_focus_iv.setVisibility(View.VISIBLE);
            } else {
                mHolder._item_focus_iv.setVisibility(View.GONE);
            }
        }


        if (item.getFindsIsPraise().equals("0")) {
            mHolder._item_praise_iv.setImageResource(R.drawable.finds_list_praise);
        } else {
            mHolder._item_praise_iv.setImageResource(R.drawable.finds_list_praise_29);
        }

        mHolder._item_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mOnItemHeadClickListener.onItemHeadClick(position);
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL + item.getFindsUserId() + ServerApi.HOME_PAGE_WEB_TOKEN);
            }
        });
        mHolder._item_focus_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mOnItemHeadClickListener.onItemHeadClick(position);
                xFAFocus = new XFrameAnimation(mHolder._item_focus_iv, focusRes, 20, false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        mOnItemFocusClickListener.onItemFocusClick(position);
                    }
                }, 540);

            }
        });


        mHolder._item_comments_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mOnItemHeadClickListener.onItemHeadClick(position);
                XPreferencesUtils.put("concern", item.getFindsIsFocus());
                XPreferencesUtils.put("dynamic_id", item.getFindsId());
                XPreferencesUtils.put("comment_num_pos", position);
                XPreferencesUtils.put("comment_id", "");
                startActivity(FindsCommentsActivity.class, "评论");
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

        mHolder._item_feedback_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.showGeneralDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(FeedbackActivity.class, "意见反馈");
                        mDialogUtils.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ReportActivity.class, "动态", item.getFindsId());
                        mDialogUtils.dismissDialog();
                    }
                });
            }
        });

        mHolder._item_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mOnItemHeadClickListener.onItemHeadClick(position);
                XPreferencesUtils.put("concern", item.getFindsIsFocus());
                XPreferencesUtils.put("dynamic_id", item.getFindsId());
                XPreferencesUtils.put("comment_num_pos", position);
                XPreferencesUtils.put("comment_id", "");
                startActivity(FindsCommentsActivity.class, "动态");
            }
        });

        mHolder._item_one_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage(0, "", (ArrayList<String>) item.getFindsImageList());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        CircleImageView _item_head;
        TextView _item_name, _item_content, _item_time, _item_praise_num, _item_comments_num, _item_del;
        ImageView _item_focus_iv, _item_praise_iv, _item_feedback_iv;
        RatioImageView _item_one_image;
        //NineGridViewLayout _item_images;
        FriendsCircleImageLayout _item_images;
        LinearLayout _item_praise_ll;
    }

    private onItemDeleteListener mOnItemDeleteListener;
    // private onItemClickListener mOnItemClickListener;
    private onItemFocusClickListener mOnItemFocusClickListener;
    private onItemPraiseClickListener mOnItemPraiseClickListener;


    /**
     * 关注点击监听接口
     */
    public interface onItemFocusClickListener {
        void onItemFocusClick(int position);
    }


    /**
     * 赞点击监听接口
     */
    public interface onItemPraiseClickListener {
        void onItemPraiseClick(int position);
    }

    /**
     * 点击监听接口
     */
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    /**
     * 删除反馈按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int position);
    }


    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }


    public void setOnItemFocusClickListener(onItemFocusClickListener mOnItemFocusClickListener) {
        this.mOnItemFocusClickListener = mOnItemFocusClickListener;
    }


    public void setOnItemPraiseClickListener(onItemPraiseClickListener mOnItemPraiseClickListener) {
        this.mOnItemPraiseClickListener = mOnItemPraiseClickListener;
    }

//    public void setOnItemClickListener(onItemClickListener mOnItemClickListener) {
//        this.mOnItemClickListener = mOnItemClickListener;
//    }

    private void startActivity(Class<?> classs, String type) {
        Intent intent = new Intent();
        intent.setClass(mActivity, classs);
        intent.setAction("one");
        intent.putExtra("vType", type);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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


    private void onClickImage(int i, String url, ArrayList<String> urlList) {
        // Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(mActivity, ImageShowActivity.class);
        intent.putStringArrayListExtra("infos", urlList);
        intent.putExtra("position", i);
        mActivity.startActivity(intent);
        // mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void showOneImageData(final RatioImageView imageView, String url) {
        final int parentWidth = XDensityUtils.getScreenWidth() - XDensityUtils.dp2px(84);
        LogUtils.debug("mTotalWidth:" + parentWidth);
        //这里是只显示一张图片的情况，显示图片的宽高可以根据实际图片大小自由定制，parentWidth 为该layout的宽度
        ImageLoaderManager.displayImage(mActivity, imageView, url, ImageLoaderManager.getPhotoImageOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int newW;
                int newH;
                if (h > w * 3) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    protected void setOneImageLayoutParams(RatioImageView imageView, int width, int height) {
        imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        imageView.layout(0, 0, width, height);
    }

}