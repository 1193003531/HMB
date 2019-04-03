package com.huimaibao.app.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.home.entity.MakingListEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class MakingListAdapter extends BaseAdapter {
    private Context context;
    public List<MakingListEntity> mlList;


    public MakingListAdapter(Context context, List<MakingListEntity> mlList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.act_home_making_list_item, null);
            mHolder = new ViewHolder();
            mHolder._item_duf = view.findViewById(R.id.act_p_web_item_image_duf);
            mHolder._item_image = view.findViewById(R.id.act_p_web_item_image);
            mHolder._item_time = view.findViewById(R.id.act_p_web_item_time);
            mHolder._item_title = view.findViewById(R.id.act_p_web_item_title);
            mHolder._item_browse = view.findViewById(R.id.act_p_web_item_browse);
            mHolder._item_share = view.findViewById(R.id.act_p_web_item_share);
            mHolder._item_sy = view.findViewById(R.id.act_p_web_item_sy);
            mHolder._item_ljzy_btn = view.findViewById(R.id.act_p_web_item_ljsy);
            mHolder._item_share_btn = view.findViewById(R.id.act_p_web_item_share_btn);
            mHolder._item_set_btn = view.findViewById(R.id.act_p_web_item_set_btn);

            mHolder._item_bs_ll = view.findViewById(R.id.act_p_web_item_bs_ll);
            mHolder._item_ss_rl = view.findViewById(R.id.act_p_web_item_ss_rl);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        MakingListEntity mlItem = getItem(position);

        //模板
        if (mlItem.getMakingListType().equals("0")) {
            mHolder._item_duf.setVisibility(View.GONE);
            mHolder._item_bs_ll.setVisibility(View.GONE);
            mHolder._item_time.setVisibility(View.GONE);
            mHolder._item_ss_rl.setVisibility(View.GONE);

            mHolder._item_sy.setVisibility(View.VISIBLE);
            mHolder._item_ljzy_btn.setVisibility(View.VISIBLE);

        } else {
            //是否是默认
            if (mlItem.getMakingListIsDuf().equals("1")) {
                mHolder._item_duf.setVisibility(View.VISIBLE);
            } else {
                mHolder._item_duf.setVisibility(View.GONE);
            }

            mHolder._item_bs_ll.setVisibility(View.VISIBLE);
            mHolder._item_time.setVisibility(View.VISIBLE);
            mHolder._item_ss_rl.setVisibility(View.VISIBLE);

            mHolder._item_sy.setVisibility(View.GONE);
            mHolder._item_ljzy_btn.setVisibility(View.GONE);
        }

        ImageLoaderManager.loadImage(mlItem.getMakingListImage(), mHolder._item_image, R.drawable.ic_launcher);
        mHolder._item_title.setText(mlItem.getMakingListTitle());
        mHolder._item_browse.setText(mlItem.getMakingListBrowse());
        mHolder._item_share.setText(mlItem.getMakingListShare());
        mHolder._item_time.setText(XTimeUtils.StringToYMD(mlItem.getMakingListTime()));
        mHolder._item_sy.setText(mlItem.getMakingListBrowse() + "人使用");


        mHolder._item_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemShareListener.onItemShareClick(position);
            }
        });
        mHolder._item_set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemSetListener.onItemSetClick(position);
            }
        });

        mHolder._item_ljzy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemImmediateUseListener.onItemImmediateUseClick(position);
            }
        });

        return view;
    }

    static class ViewHolder {
        RoundedImagView _item_image;
        ImageView _item_duf;
        TextView _item_time, _item_title, _item_browse, _item_share, _item_sy;
        TextView _item_share_btn, _item_set_btn, _item_ljzy_btn;
        LinearLayout _item_bs_ll;
        RelativeLayout _item_ss_rl;
    }

    /**
     * 点击分享监听接口
     */
    public interface onItemShareListener {
        void onItemShareClick(int position);
    }

    /**
     * 点击设置监听接口
     */
    public interface onItemSetListener {
        void onItemSetClick(int position);
    }

    /**
     * 点击立即使用监听接口
     */
    public interface onItemImmediateUseListener {
        void onItemImmediateUseClick(int position);
    }

    private onItemShareListener mOnItemShareListener;
    private onItemSetListener mOnItemSetListener;
    private onItemImmediateUseListener mOnItemImmediateUseListener;

    public void setOnItemShareListener(onItemShareListener mOnItemShareListener) {
        this.mOnItemShareListener = mOnItemShareListener;
    }

    public void setOnItemSetListener(onItemSetListener mOnItemSetListener) {
        this.mOnItemSetListener = mOnItemSetListener;
    }

    public void setOnImmediateUseListener(onItemImmediateUseListener mOnItemImmediateUseListener) {
        this.mOnItemImmediateUseListener = mOnItemImmediateUseListener;
    }


}