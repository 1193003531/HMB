package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.MarketRewardEntity;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XStringUtils;

import java.util.List;

public class MarketRewardAdapter extends BaseAdapter {
    private Context context;
    public List<MarketRewardEntity> ilList;


    public MarketRewardAdapter(Context context, List<MarketRewardEntity> ilList) {
        this.context = context;
        this.ilList = ilList;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public MarketRewardEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_mine_marketing_reward_item, null);
            mHolder = new ViewHolder();
            mHolder._item_type = view.findViewById(R.id.marketing_reward_item_type);
            mHolder._item_money = view.findViewById(R.id.marketing_reward_item_money);
            mHolder._item_name = view.findViewById(R.id.marketing_reward_item_name);
            mHolder._item_time = view.findViewById(R.id.marketing_reward_item_time);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        MarketRewardEntity ilItem = getItem(position);


        //XTimeUtils.getTimeRange(Long.valueOf(ilItem.getMarketRewardTime()))
        mHolder._item_time.setText(XEmptyUtils.isSpace(ilItem.getMarketRewardTime()) ? "" : ilItem.getMarketRewardTime());
        if (ilItem.getMarketRewardIncomeType().equals("0")) {
            mHolder._item_money.setText("-￥" + ilItem.getMarketRewardMoney());
        } else {
            mHolder._item_money.setText("+￥" + ilItem.getMarketRewardMoney());
        }

        if (ilItem.getMarketRewardType().equals("1")) {
            mHolder._item_type.setText("你的收益来自");
            mHolder._item_name.setText(ilItem.getMarketRewardName());
        } else if (ilItem.getMarketRewardType().equals("2")) {
            mHolder._item_type.setText("邀请好友");
            mHolder._item_name.setText(ilItem.getMarketRewardName());
        } else if (ilItem.getMarketRewardType().equals("4")) {
            mHolder._item_type.setText("注册奖励");
            mHolder._item_name.setText("");
        } else {
            mHolder._item_type.setText("转入余额");
            mHolder._item_name.setText("");
        }


        return view;
    }

    static class ViewHolder {
        TextView _item_type, _item_money, _item_time, _item_name;
    }

}