package com.huimaibao.app.fragment.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.message.entity.MessageEntity;

import java.util.List;

public class XMBAdapter extends BaseAdapter {
    private Context context;
    public List<MessageEntity> list;

    public XMBAdapter(Context context, List<MessageEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public MessageEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_message_item_xmb, null);
            mHolder = new ViewHolder();
            mHolder._item_tx_ll = view.findViewById(R.id.msg_xmb_item_tx_ll);
            mHolder._item_other_ll = view.findViewById(R.id.msg_xmb_item_other_ll);
            mHolder._item_time = view.findViewById(R.id.msg_xmb_item_time);

            mHolder._item_tx_title = view.findViewById(R.id.msg_xmb_item_tx_title);
            mHolder._item_tx_content = view.findViewById(R.id.msg_xmb_item_tx_content);
            mHolder._item_tx_money = view.findViewById(R.id.msg_xmb_item_tx_money);

            mHolder._item_other_title = view.findViewById(R.id.msg_xmb_item_other_title);
            mHolder._item_other_content = view.findViewById(R.id.msg_xmb_item_other_content);
            mHolder._item_other_money = view.findViewById(R.id.msg_xmb_item_other_money);


            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        MessageEntity msg = getItem(position);

        if (msg.getMessageType().equals("1") || msg.getMessageType().equals("2")) {
            mHolder._item_tx_ll.setVisibility(View.VISIBLE);
            mHolder._item_other_ll.setVisibility(View.GONE);
            if (msg.getMessageType().equals("1"))
                mHolder._item_tx_title.setTextColor(context.getResources().getColor(R.color.FF49be03));
            else
                mHolder._item_tx_title.setTextColor(context.getResources().getColor(R.color.FFf81e1e));
            mHolder._item_tx_title.setText(msg.getMessageName());
            mHolder._item_tx_content.setText(msg.getMessageContent());
            mHolder._item_tx_money.setText("￥" + msg.getMessageMoney());
        } else {
            mHolder._item_tx_ll.setVisibility(View.GONE);
            mHolder._item_other_ll.setVisibility(View.VISIBLE);

            mHolder._item_other_title.setText(msg.getMessageName());
            mHolder._item_other_content.setText(msg.getMessageContent());
            mHolder._item_other_money.setText("+￥" + msg.getMessageMoney());
        }
        mHolder._item_time.setText(msg.getMessageTime());

        return view;
    }

    static class ViewHolder {
        TextView _item_tx_title, _item_tx_content, _item_tx_money, _item_other_title, _item_other_content, _item_other_money, _item_time;
        //提现，其他
        LinearLayout _item_tx_ll, _item_other_ll;

    }

}