package com.huimaibao.app.fragment.finds.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.fragment.finds.act.FindsCommentsActivity;
import com.huimaibao.app.fragment.finds.entity.FindsMSGEntity;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

/**
 * 动态适配器
 */
public class FindsMSGAdapter extends BaseAdapter {
    private Activity mActivity;
    public List<FindsMSGEntity> list;

    public FindsMSGAdapter(Activity activity, List<FindsMSGEntity> list) {
        this.mActivity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public FindsMSGEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.act_finds_list_msg_item, null);
            mHolder = new ViewHolder();
            mHolder._item_head = convertView.findViewById(R.id.finds_list_msg_item_head);
            mHolder._item_name = convertView.findViewById(R.id.finds_list_msg_item_name);
            mHolder._item_content = convertView.findViewById(R.id.finds_list_msg_item_content);
            mHolder._item_time = convertView.findViewById(R.id.finds_list_msg_item_time);
            mHolder._item_image = convertView.findViewById(R.id.finds_list_msg_item_image);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final FindsMSGEntity item = getItem(position);


        if (XEmptyUtils.isSpace(item.getFindsImage())) {
            mHolder._item_image.setVisibility(View.GONE);
        } else {
            mHolder._item_image.setVisibility(View.VISIBLE);
            ImageLoaderManager.loadImage(item.getFindsImage(), mHolder._item_image, R.drawable.ic_default);
        }

        ImageLoaderManager.loadImage(item.getFindsUserHead(), mHolder._item_head, R.drawable.ic_default);
        mHolder._item_name.setText(item.getFindsUserName());
        mHolder._item_time.setText(XTimeUtils.getTimeRangeS(item.getFindsTime()));

        if (item.getFindsDYType().equals("2")) {
            mHolder._item_content.setText("点赞了我");
        } else if (item.getFindsDYType().equals("3")) {
            mHolder._item_content.setText("" + item.getFindsContent());
        } else {
            mHolder._item_content.setText("回复我：" + item.getFindsContent());
        }


        mHolder._item_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mOnItemHeadClickListener.onItemHeadClick(position);
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL+item.getFindsUserId()+ServerApi.HOME_PAGE_WEB_TOKEN);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mOnItemHeadClickListener.onItemHeadClick(position);
                XPreferencesUtils.put("concern", "0");
                XPreferencesUtils.put("dynamic_id", item.getFindsId());
                XPreferencesUtils.put("comment_id", item.getFindsCommentId());
                startActivity(FindsCommentsActivity.class, "动态");
            }
        });


        return convertView;
    }

    static class ViewHolder {
        CircleImageView _item_head;
        TextView _item_name, _item_content, _item_time;
        RoundedImagView _item_image;
    }


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

}