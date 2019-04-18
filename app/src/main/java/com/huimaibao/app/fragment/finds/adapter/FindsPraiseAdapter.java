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
import com.huimaibao.app.fragment.finds.entity.FindsPraiseEntity;
import com.huimaibao.app.fragment.web.HomePageWebActivity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.widget.CircleImageView;

import java.util.List;

/**
 * 动态适配器
 */
public class FindsPraiseAdapter extends BaseAdapter {
    private Activity mActivity;
    public List<FindsPraiseEntity> list;

    public FindsPraiseAdapter(Activity activity, List<FindsPraiseEntity> list) {
        this.mActivity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public FindsPraiseEntity getItem(int position) {
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.act_finds_list_like_item, null);
            mHolder = new ViewHolder();
            mHolder._item_head = convertView.findViewById(R.id.finds_list_like_item_head);
            mHolder._item_name = convertView.findViewById(R.id.finds_list_like_item_name);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final FindsPraiseEntity item = getItem(position);


        ImageLoaderManager.loadImage(item.getFindsUserHead(), mHolder._item_head, R.drawable.ic_launcher);
        mHolder._item_name.setText(item.getFindsUserName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomePageWebActivity.class, "", ServerApi.HOME_PAGE_WEB_URL+item.getFindsUserId()+ServerApi.HOME_PAGE_WEB_TOKEN);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        CircleImageView _item_head;
        TextView _item_name;
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