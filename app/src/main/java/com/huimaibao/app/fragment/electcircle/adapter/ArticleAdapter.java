package com.huimaibao.app.fragment.electcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.CollectionEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

/**文章适配器*/
public class ArticleAdapter extends BaseAdapter {
    private Context context;
    public List<CollectionEntity> ilList;


    public ArticleAdapter(Context context, List<CollectionEntity> ilList) {
        this.context = context;
        this.ilList = ilList;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public CollectionEntity getItem(int position) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.act_e_m_l_article_item, null);
            mHolder = new ViewHolder();
            mHolder._item_box = view.findViewById(R.id.collection_item_check);
            mHolder._item_image = view.findViewById(R.id.collection_item_image_right);
            mHolder._item_image_1 = view.findViewById(R.id.collection_item_image_1);
            mHolder._item_image_2 = view.findViewById(R.id.collection_item_image_2);
            mHolder._item_image_3 = view.findViewById(R.id.collection_item_image_3);
            mHolder._item_browse = view.findViewById(R.id.collection_item_browse);
            mHolder._item_title = view.findViewById(R.id.collection_item_title);
            mHolder._item_name = view.findViewById(R.id.collection_item_name);
            mHolder._item_time = view.findViewById(R.id.collection_item_time);
            mHolder._item_image_ll = view.findViewById(R.id.collection_item_image_ll);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        CollectionEntity ilItem = getItem(position);


        if (ilItem.getCollectionIsCheck())
            mHolder._item_box.setImageResource(R.drawable.home_true_icon);
        else
            mHolder._item_box.setImageResource(R.drawable.home_false_icon);


        if (ilItem.getCollectionType().equals("1")) {
            mHolder._item_image.setVisibility(View.GONE);
            mHolder._item_image_ll.setVisibility(View.VISIBLE);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage1(), mHolder._item_image_1, R.drawable.ic_launcher);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage2(), mHolder._item_image_2, R.drawable.ic_launcher);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage3(), mHolder._item_image_3, R.drawable.ic_launcher);
        } else {
            mHolder._item_image.setVisibility(View.VISIBLE);
            mHolder._item_image_ll.setVisibility(View.GONE);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage(), mHolder._item_image, R.drawable.ic_launcher);
        }


        mHolder._item_title.setText(ilItem.getCollectionTitle());
        mHolder._item_time.setText(XTimeUtils.StringToYMD(ilItem.getCollectionTime()));
        mHolder._item_browse.setText(ilItem.getCollectionBrowse() + "阅读");
        mHolder._item_name.setText("" + ilItem.getCollectionName());

        mHolder._item_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemCheckListener.onItemCheckClick(position);
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView _item_box;
        RoundedImagView _item_image, _item_image_1, _item_image_2, _item_image_3;
        LinearLayout _item_image_ll;

        TextView _item_title, _item_name, _item_browse, _item_time;
    }

    /**
     * 点击选择监听接口
     */
    public interface onItemCheckListener {
        void onItemCheckClick(int position);
    }

    private onItemCheckListener mOnItemCheckListener;

    public void setOnItemCheckListener(onItemCheckListener mOnItemCheckListener) {
        this.mOnItemCheckListener = mOnItemCheckListener;
    }

}