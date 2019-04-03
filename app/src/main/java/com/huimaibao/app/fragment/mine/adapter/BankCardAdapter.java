package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.BankCardEntity;
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
                mHolder._item_type = view.findViewById(R.id.bank_card_item_type);
                mHolder._item_name = view.findViewById(R.id.bank_card_item_name);
                mHolder._item_num = view.findViewById(R.id.bank_card_item_num);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.dialog_bank_list_item, null);
                mHolder._item_name = view.findViewById(R.id.dialog_bank_card_item_namenum);
                mHolder._item_Check = view.findViewById(R.id.dialog_bank_card_item_check);
            }
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        BankCardEntity item = getItem(position);
        if (mType.equals("0")) {
            mHolder._item_type.setText(item.getBankCardType());
            mHolder._item_num.setText(XRegexUtils.cardIdHide2(item.getBankCardNum()));
            mHolder._item_name.setText(item.getBankCardName());
        } else {
            mHolder._item_name.setText(item.getBankCardName() + "(" + item.getBankCardNum().substring(item.getBankCardNum().length() - 4, item.getBankCardNum().length()) + ")");
            if (item.getBankCardCheck()) {
                mHolder._item_Check.setVisibility(View.VISIBLE);
            } else {
                mHolder._item_Check.setVisibility(View.GONE);
            }
        }

        return view;
    }

    static class ViewHolder {
        TextView _item_type, _item_name, _item_num;
        ImageView _item_Check;
    }

}