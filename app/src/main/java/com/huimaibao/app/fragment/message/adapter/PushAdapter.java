package com.huimaibao.app.fragment.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class PushAdapter extends BaseAdapter {
    private Context context;
    public List<MessageEntity> list;

    public PushAdapter(Context context, List<MessageEntity> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_message_item_push, null);
            mHolder = new ViewHolder();
            mHolder._item_image = view.findViewById(R.id.msg_push_item_iv);
            mHolder._item_title = view.findViewById(R.id.msg_push_item_title);
            mHolder._item_content = view.findViewById(R.id.msg_push_item_content);
            mHolder._item_time = view.findViewById(R.id.msg_push_item_time);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        MessageEntity msg = getItem(position);

        ImageLoaderManager.loadImage(msg.getMessageImage(),mHolder._item_image,R.drawable.ic_launcher);
        mHolder._item_time.setText(msg.getMessageTime());
        mHolder._item_title.setText(msg.getMessageName());
        mHolder._item_content.setText(msg.getMessageContent());
        return view;
    }

    static class ViewHolder {
        RoundedImagView _item_image;
        TextView _item_title, _item_content, _item_time;
    }

}