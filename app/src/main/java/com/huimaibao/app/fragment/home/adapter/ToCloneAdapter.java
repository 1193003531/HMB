package com.huimaibao.app.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.home.entity.ToCloneEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class ToCloneAdapter extends BaseAdapter {
    private Context context;
    public List<ToCloneEntity> TCList;


    public ToCloneAdapter(Context context, List<ToCloneEntity> TCList) {
        this.context = context;
        this.TCList = TCList;
    }

    @Override
    public int getCount() {
        return TCList == null ? 0 : TCList.size();
    }

    @Override
    public ToCloneEntity getItem(int position) {
        if (TCList != null && TCList.size() != 0) {
            return TCList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.act_home_to_clone_item, null);
            mHolder = new ViewHolder();
            mHolder._item_image = view.findViewById(R.id.act_h_toclone_item_image);
            mHolder._item_num = view.findViewById(R.id.act_h_toclone_item_num);
            mHolder._item_title = view.findViewById(R.id.act_h_toclone_item_title);
            mHolder._item_browse = view.findViewById(R.id.act_h_toclone_item_browse);
            mHolder._item_Head = view.findViewById(R.id.act_h_toclone_item_head_image);
            mHolder._item_name = view.findViewById(R.id.act_h_toclone_item_head_name);
            mHolder._item_free_btn = view.findViewById(R.id.act_h_toclone_item_free);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        ToCloneEntity tcItem = getItem(position);

        if (!tcItem.getToCloneHead().equals(mHolder._item_Head.getTag())) {
            ImageLoaderManager.loadImage(tcItem.getToCloneHead(), mHolder._item_Head, R.drawable.ic_default);
            mHolder._item_Head.setTag(tcItem.getToCloneHead());
        }

        if (!tcItem.getToCloneImage().equals(mHolder._item_image.getTag())) {
            ImageLoaderManager.loadImage(tcItem.getToCloneImage(), mHolder._item_image, R.drawable.ic_default);
            mHolder._item_image.setTag(tcItem.getToCloneImage());
        }


        mHolder._item_num.setText("NO." + (position + 1));
        mHolder._item_browse.setText(tcItem.getToCloneBrowse() + "人使用");
        mHolder._item_name.setText(tcItem.getToCloneName());
        mHolder._item_title.setText(tcItem.getToCloneTitle());
        if (XStringUtils.m1(tcItem.getToCloneMoney()) > 0) {
            mHolder._item_free_btn.setText(tcItem.getToCloneMoney() + "元克隆");
        } else {
            mHolder._item_free_btn.setText("免费克隆");
        }

        mHolder._item_free_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemCloneListener.onItemCloneClick(position);
            }
        });
        return view;
    }

    static class ViewHolder {
        RoundedImagView _item_image;
        CircleImageView _item_Head;
        TextView _item_num, _item_title, _item_browse, _item_name;
        TextView _item_free_btn;
    }

    /**
     * 点击分享监听接口
     */
    public interface onItemCloneListener {
        void onItemCloneClick(int position);
    }

    private onItemCloneListener mOnItemCloneListener;

    public void setOnItemCloneListener(onItemCloneListener mOnItemCloneListener) {
        this.mOnItemCloneListener = mOnItemCloneListener;
    }

}