package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.VeinTreasureEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;

import java.util.List;

public class VeinTreasureAdapter extends BaseAdapter {
    private Context context;
    public List<VeinTreasureEntity> ilList;


    public VeinTreasureAdapter(Context context, List<VeinTreasureEntity> ilList) {
        this.context = context;
        this.ilList = ilList;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public VeinTreasureEntity getItem(int position) {
        if (ilList != null && ilList.size() != 0) {
            return ilList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.act_mine_vein_treasure_item, null);
            mHolder = new ViewHolder();
            mHolder._item_image = view.findViewById(R.id.vein_treasure_item_image);
            mHolder._item_type = view.findViewById(R.id.vein_treasure_item_type);
            mHolder._item_money = view.findViewById(R.id.vein_treasure_item_money);
            mHolder._item_name = view.findViewById(R.id.vein_treasure_item_name);
            mHolder._item_name2 = view.findViewById(R.id.vein_treasure_item_name2);
            mHolder._item_time = view.findViewById(R.id.vein_treasure_item_time);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        VeinTreasureEntity ilItem = getItem(position);
        if (ilItem.getVeinTreasureState().equals("1")) {
            mHolder._item_name.setVisibility(View.VISIBLE);
            mHolder._item_name2.setVisibility(View.GONE);
            mHolder._item_name.setText(ilItem.getVeinTreasureName());
        } else {
            mHolder._item_name.setVisibility(View.GONE);
            mHolder._item_name2.setVisibility(View.VISIBLE);
            mHolder._item_name2.setText(ilItem.getVeinTreasureName());
        }
        if (ilItem.getVeinTreasureType().equals("全民推广")) {
            mHolder._item_name.setVisibility(View.GONE);
            mHolder._item_name2.setVisibility(View.GONE);
            mHolder._item_image.setImageResource(R.drawable.vein_list_icon);
        } else {
            ImageLoaderManager.loadImage(ilItem.getVeinTreasureImage(), mHolder._item_image, R.drawable.ic_default);
        }

        mHolder._item_type.setText(ilItem.getVeinTreasureType());
        mHolder._item_time.setText(XTimeUtils.getTimeRange(ilItem.getVeinTreasureTime()));
        if (XStringUtils.m1(ilItem.getVeinTreasureMoney()) > 0) {
            mHolder._item_money.setText("+" + ilItem.getVeinTreasureMoney());
        } else {
            mHolder._item_money.setText("-" + ilItem.getVeinTreasureMoney());
        }


        return view;
    }

    static class ViewHolder {
        CircleImageView _item_image;
        TextView _item_type, _item_money, _item_time, _item_name, _item_name2;
    }

}