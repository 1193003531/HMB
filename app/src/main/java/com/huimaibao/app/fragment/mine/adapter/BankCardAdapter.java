package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.BankCardEntity;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.XTimeUtils;

import java.util.List;

public class BankCardAdapter extends BaseAdapter {
    private Context context;
    public List<BankCardEntity> list;
    private String mType;//0-银行卡列表，1-弹出框银行卡列表


    public BankCardAdapter(Context context, List<BankCardEntity> list, String type) {
        this.context = context;
        this.list = list;
        this.mType = type;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public BankCardEntity getItem(int position) {
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
            mHolder = new ViewHolder();
            if (mType.equals("0")) {
                view = LayoutInflater.from(context).inflate(R.layout.act_mine_bank_card_item, null);
                mHolder._item_ll = view.findViewById(R.id.bank_card_item_ll);
                mHolder._item_type = view.findViewById(R.id.bank_card_item_type);
                mHolder._item_name = view.findViewById(R.id.bank_card_item_name);
                mHolder._item_num = view.findViewById(R.id.bank_card_item_num);
                mHolder._item_icon = view.findViewById(R.id.bank_card_item_icon);
                mHolder._item_r_b_icon = view.findViewById(R.id.bank_card_item_w_icon);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.dialog_bank_list_item, null);
                mHolder._item_name = view.findViewById(R.id.dialog_bank_card_item_namenum);
                mHolder._item_check = view.findViewById(R.id.dialog_bank_card_item_check);
                mHolder._item_d_icon = view.findViewById(R.id.dialog_bank_card_item_icon);
            }
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        BankCardEntity item = getItem(position);
        if (mType.equals("0")) {
            //item.getBankCardSColor(), item.getBankCardEColor()
            int[] colors = {Color.rgb(253, 104, 104), Color.rgb(249, 72, 73)};
            setBankBackground(mHolder._item_ll, colors);
            mHolder._item_type.setText(item.getBankCardType());
            mHolder._item_num.setText(XRegexUtils.cardIdHide2(item.getBankCardNum()));
            mHolder._item_name.setText(item.getBankCardName());
        } else {
            mHolder._item_name.setText(item.getBankCardName() + "(" + item.getBankCardNum().substring(item.getBankCardNum().length() - 4, item.getBankCardNum().length()) + ")");
            if (item.getBankCardCheck()) {
                mHolder._item_check.setVisibility(View.VISIBLE);
            } else {
                mHolder._item_check.setVisibility(View.GONE);
            }
        }

        return view;
    }

    static class ViewHolder {
        RelativeLayout _item_ll;
        TextView _item_type, _item_name, _item_num;
        ImageView _item_icon, _item_d_icon, _item_r_b_icon, _item_check;
    }

    /**
     * 设置银行卡背景色
     */
    private void setBankBackground(RelativeLayout layout, int[] colors) {
        //int[] colors = {0xFFfd6868, 0xFFf94849};

        //设置渐变方向,包括从上到下，从左到右，从下到上，从右到左
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        //drawable.setColors(int[] colors);//设置渐变的颜色数组
        //设置圆角，四个圆角大小相同
        drawable.setCornerRadius(XDensityUtils.dp2px(8));
        //drawable.setColor(fillColor);// 设置填充色，此时会使用设置的颜色，而不会使用渐变色
        //drawable.setStroke(strokeWidth, strokeColor);// 设置边框宽度和颜色
        //drawable.setGradientCenter(float x, float y);// 渐变中心坐标
        //设置渐变形状，包括RECTANT,OVAL,LINE,RING
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        layout.setBackground(drawable);
    }

}