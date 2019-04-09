package com.huimaibao.app.fragment.finds.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;

import java.util.List;

/**
 * 动态适配器
 */
public class FindsCommentsAdapter extends BaseAdapter {
    private Activity mActivity;
    public List<FindsCommentsEntity> list;
    private CharSequence charSequence;
    private String content_v = "";

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

        ImageLoaderManager.loadImage(item.getFindsUserHead(), mHolder._item_head, R.drawable.ic_launcher);
        mHolder._item_name.setText(item.getFindsUserName());
        mHolder._item_praise_num.setText(item.getFindsPraiseNum());

//        content_v = "<font color='#333333' size='30'>" + item.getFindsContent() + "</font>" + "<font color='#a2a2a2' size='12'>" + XTimeUtils.getTimeRangeS(item.getFindsTime()) + "</font>";
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            //FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
//            //FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
//            charSequence = Html.fromHtml(content_v, Html.FROM_HTML_MODE_LEGACY);
//        } else {
//            charSequence = Html.fromHtml(content_v);
//        }

        SpannableString ssContent, ssTime;

        ssContent = new SpannableString(item.getFindsContent());
        ssContent.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, ssContent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssContent.setSpan(new AbsoluteSizeSpan(15,true), 0, ssContent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssTime = new SpannableString(XTimeUtils.getTimeRangeS(item.getFindsTime()));
        ssTime.setSpan(new ForegroundColorSpan(Color.parseColor("#a2a2a2")), 0, ssTime.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssTime.setSpan(new AbsoluteSizeSpan(12,true), 0, ssTime.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableStringBuilder multiWord = new SpannableStringBuilder();
        multiWord.append(ssContent);
        multiWord.append(ssTime);

        mHolder._item_content.setText(multiWord);

        if (item.getList().size() == 0) {
            // mHolder._item_list.setVisibility(View.GONE);
        }


        mHolder._item_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        CircleImageView _item_head;
        TextView _item_name, _item_content, _item_praise_num, _item_more;
        ImageView _item_praise_iv;
        LinearLayout _item_praise_ll;
        ListView _item_list;
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

}