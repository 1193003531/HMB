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
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.widget.CircleImageView;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    public List<MessageEntity> list;

    public UserAdapter(Context context, List<MessageEntity> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.act_message_item_user, null);
            mHolder = new ViewHolder();
            mHolder._item_image = view.findViewById(R.id.msg_user_item_head);
            mHolder._item_content = view.findViewById(R.id.msg_user_item_content);
            mHolder._item_time = view.findViewById(R.id.msg_user_item_time);
            mHolder._item_ll = view.findViewById(R.id.msg_user_item_ll);

            mHolder._item_consent = view.findViewById(R.id.msg_user_item_consent);
            mHolder._item_refuse = view.findViewById(R.id.msg_user_item_refuse);
            mHolder._item_toview = view.findViewById(R.id.msg_user_item_toview);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        MessageEntity msg = getItem(position);
        ImageLoaderManager.loadImage(msg.getMessageImage(),mHolder._item_image,R.drawable.ic_default);
        if (msg.getMessageType().equals("0")) {
            mHolder._item_ll.setVisibility(View.VISIBLE);
            mHolder._item_toview.setVisibility(View.GONE);
        } else if (msg.getMessageType().equals("2")) {
            mHolder._item_toview.setVisibility(View.VISIBLE);
            mHolder._item_ll.setVisibility(View.GONE);
            mHolder._item_toview.setText("去分享");
        } else {
            mHolder._item_toview.setVisibility(View.VISIBLE);
            mHolder._item_ll.setVisibility(View.GONE);
            mHolder._item_toview.setText("立即开通");
        }
        mHolder._item_content.setText(msg.getMessageContent());
        mHolder._item_time.setText(msg.getMessageTime());

        mHolder._item_consent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemConsentListener.onItemConsentClick(position);
            }
        });

        mHolder._item_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemRefuseListener.onItemRefuseClick(position);
            }
        });

        return view;
    }

    static class ViewHolder {
        CircleImageView _item_image;
        TextView _item_content, _item_time;
        LinearLayout _item_ll;
        TextView _item_consent, _item_refuse, _item_toview;
    }

    /**
     * 点击同意监听接口
     */
    public interface onItemConsentListener {
        void onItemConsentClick(int position);
    }

    private onItemConsentListener mOnItemConsentListener;

    public void setOnItemConsentListener(onItemConsentListener mOnItemConsentListener) {
        this.mOnItemConsentListener = mOnItemConsentListener;
    }

    /**
     * 点击拒绝监听接口
     */
    public interface onItemRefuseListener {
        void onItemRefuseClick(int position);
    }

    private onItemRefuseListener mOnItemRefuseListener;

    public void setOnItemRefuseListener(onItemRefuseListener mOnItemRefuseListener) {
        this.mOnItemRefuseListener = mOnItemRefuseListener;
    }

}