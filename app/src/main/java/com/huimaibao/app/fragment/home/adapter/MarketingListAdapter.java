package com.huimaibao.app.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.home.entity.MakingListEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class MarketingListAdapter extends BaseAdapter {
    private Context context;
    public List<MakingListEntity> mlList;


    public MarketingListAdapter(Context context, List<MakingListEntity> mlList) {
        this.context = context;
        this.mlList = mlList;

    }


    @Override
    public int getCount() {
        return mlList == null ? 0 : mlList.size();
    }

    @Override
    public MakingListEntity getItem(int position) {
        if (mlList != null && mlList.size() != 0) {
            return mlList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.fragment_home_marketing_item, null);
            mHolder = new ViewHolder();
            mHolder._item_image = view.findViewById(R.id.f_h_w_item_image);
            mHolder._item_num_iv = view.findViewById(R.id.f_h_w_item_iv);
            mHolder._item_num = view.findViewById(R.id.f_h_w_item_num);
            mHolder._item_title = view.findViewById(R.id.f_h_w_item_title);
            mHolder._item_browse = view.findViewById(R.id.f_h_w_item_browse);
            mHolder._item_share = view.findViewById(R.id.f_h_w_item_share);
            mHolder._item_Head = view.findViewById(R.id.f_h_w_item_head_image);
            mHolder._item_name = view.findViewById(R.id.f_h_w_item_head_name);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        MakingListEntity mlItem = getItem(position);
        switch (position) {
            case 0:
                mHolder._item_num_iv.setVisibility(View.VISIBLE);
                mHolder._item_num_iv.setImageResource(R.drawable.marketing_web_1_icon);
                mHolder._item_num.setText("");
                break;
            case 1:
                mHolder._item_num_iv.setVisibility(View.VISIBLE);
                mHolder._item_num_iv.setImageResource(R.drawable.marketing_web_2_icon);
                mHolder._item_num.setText("");
                break;
            case 2:
                mHolder._item_num_iv.setVisibility(View.VISIBLE);
                mHolder._item_num_iv.setImageResource(R.drawable.marketing_web_3_icon);
                mHolder._item_num.setText("");
                break;
            default:
                mHolder._item_num_iv.setVisibility(View.INVISIBLE);
                mHolder._item_num.setText("" + (position + 1));
                if ((position + 1) > 99) {
                    mHolder._item_num.setTextSize(11);
                } else {
                    mHolder._item_num.setTextSize(16);
                }
                break;
        }

//        if (XEmptyUtils.isSpace(mlItem.getMakingListImage())) {
//            mHolder._item_image.setImageResource(R.drawable.ic_launcher);
//        } else {
//            ImageLoaderManager.loadImage(mlItem.getMakingListImage(), mHolder._item_image, R.drawable.ic_launcher);
//        }

        ImageLoaderManager.loadImage(mlItem.getMakingListHead(), mHolder._item_Head, R.drawable.ic_launcher);

        mHolder._item_name.setText(mlItem.getMakingListName());
        mHolder._item_title.setText(mlItem.getMakingListTitle());
        mHolder._item_browse.setText(mlItem.getMakingListBrowse()+" 浏览量");
        mHolder._item_share.setText(mlItem.getMakingListInterested()+" 感兴趣");

        return view;
    }

    static class ViewHolder {
        ImageView _item_num_iv;
        RoundedImagView _item_image;
        CircleImageView _item_Head;
        TextView _item_num, _item_title, _item_browse, _item_share, _item_name;

    }

}