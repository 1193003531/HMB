package com.huimaibao.app.fragment.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.huimaibao.app.view.SlidingDeleteView;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;

import java.util.List;

public class MessageDelAdapter extends BaseAdapter {
    private Context context;
    public List<MessageEntity> messageList;
    private String type;


    public MessageDelAdapter(Context context, List<MessageEntity> messageList, String type) {
        this.context = context;
        this.messageList = messageList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public MessageEntity getItem(int position) {
        if (messageList != null && messageList.size() != 0) {
            return messageList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.fragment_message_del_item, null);
            mHolder = new ViewHolder();
            mHolder.slidingDeleteView = view.findViewById(R.id.slidingview);
            mHolder.tvDelete = view.findViewById(R.id.tv_delete);
            mHolder._item_image = view.findViewById(R.id.list_message_item_image);
            mHolder._item_name = view.findViewById(R.id.list_message_item_name);
            mHolder._item_content = view.findViewById(R.id.list_message_item_content);
            mHolder._item_time = view.findViewById(R.id.list_message_item_time);
            mHolder._item_dian = view.findViewById(R.id.list_message_item_dian);
            mHolder._item_ll = view.findViewById(R.id.list_message_item_ll);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        LinearLayout containerView = mHolder.slidingDeleteView.findViewById(R.id.lay_container);
        containerView.getLayoutParams().width = XDensityUtils.getScreenWidth();
        mHolder.slidingDeleteView.setEnable(true);

        MessageEntity msg = getItem(position);

        mHolder._item_dian.setVisibility(View.GONE);
        if (type.equals("人气") || type.equals("感兴趣")) {
            if (msg.getMessageId().equals("0")) {
                mHolder._item_image.setImageResource(R.drawable.msg_xt_3_icon);
            } else {
                ImageLoaderManager.loadImage(msg.getMessageImage(),mHolder._item_image,R.drawable.ic_default);
            }

            if (XEmptyUtils.isSpace(msg.getMessageName())) {
                mHolder._item_name.setText("游客");
            } else {
                mHolder._item_name.setText(msg.getMessageName());
            }

            mHolder._item_time.setText(XTimeUtils.getTimeRange(msg.getMessageTime()));
            if (XEmptyUtils.isSpace(msg.getMessagePhone())) {
                mHolder._item_ll.setVisibility(View.GONE);
            } else {
                mHolder._item_ll.setVisibility(View.VISIBLE);
                mHolder._item_content.setText("手机:" + msg.getMessagePhone());
            }
        } else {
            mHolder._item_ll.setVisibility(View.VISIBLE);
            ImageLoaderManager.loadImage(msg.getMessageImage(),mHolder._item_image,R.drawable.ic_default);
            mHolder._item_name.setText(msg.getMessageName());
            mHolder._item_content.setText(msg.getMessageContent());
            mHolder._item_time.setText(XTimeUtils.getTimeRange(msg.getMessageTime()));
        }
        containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position);
            }
        });

        mHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });

        return view;
    }

    /**
     * 点击监听接口
     */
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int position);
    }

    private onItemDeleteListener mOnItemDeleteListener;
    private onItemClickListener mOnItemClickListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    public void setOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static class ViewHolder {
        SlidingDeleteView slidingDeleteView;
        CircleImageView _item_image;
        ImageView _item_dian;
        TextView _item_name, _item_content, _item_time;
        LinearLayout _item_ll;
        TextView tvDelete;
    }

}