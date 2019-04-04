package com.huimaibao.app.fragment.finds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.finds.entity.FindsEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.view.NineGridLayout;
import com.huimaibao.app.view.NineGridViewLayout;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;

import java.util.List;

/**
 * 动态适配器
 */
public class FindsAdapter extends BaseAdapter {
    private Context context;
    public List<FindsEntity> list;

    public FindsAdapter(Context context, List<FindsEntity> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.act_finds_list_item, null);
            mHolder = new ViewHolder();
            mHolder._item_head = view.findViewById(R.id.finds_list_item_head);
            mHolder._item_name = view.findViewById(R.id.finds_list_item_name);
            mHolder._item_content = view.findViewById(R.id.finds_list_item_content);
            mHolder._item_time = view.findViewById(R.id.finds_list_item_time);
            mHolder._item_praise_num = view.findViewById(R.id.finds_list_item_praise_num);
            mHolder._item_comments_num = view.findViewById(R.id.finds_list_item_comments_num);
            mHolder._item_focus_iv = view.findViewById(R.id.finds_list_item_focus);
            mHolder._item_praise_iv = view.findViewById(R.id.finds_list_item_praise_iv);
            mHolder._item_feedback_iv = view.findViewById(R.id.finds_list_item_feedback);
            mHolder._item_images = view.findViewById(R.id.finds_list_item_grid);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        FindsEntity item = getItem(position);

        ImageLoaderManager.loadImage(item.getFindsUserHead(), mHolder._item_head, R.drawable.ic_launcher);

        mHolder._item_name.setText(item.getFindsUserName());
        mHolder._item_content.setText(item.getFindsContent());
        mHolder._item_time.setText(XTimeUtils.getTimeRange(item.getFindsTime()));
        mHolder._item_praise_num.setText(item.getFindsPraiseNum());
        mHolder._item_comments_num.setText(item.getFindsCommentsNum());

        mHolder._item_images.setUrlList(item.FindsImageList);
        return view;
    }

    static class ViewHolder {
        CircleImageView _item_head;
        TextView _item_name, _item_content, _item_time, _item_praise_num, _item_comments_num;
        ImageView _item_focus_iv, _item_praise_iv, _item_feedback_iv;
        NineGridViewLayout _item_images;
    }

}