package com.xmx.androidframeworkbase.Tools.ChoosePhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.AlbumItem;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.GifImageView;
import com.xmx.androidframeworkbase.Tools.Data.BaseEntityAdapter;

import java.util.List;

public class AlbumAdapter extends BaseEntityAdapter<AlbumItem> {

    public AlbumAdapter(List<AlbumItem> list, Context context) {
        super(context, list);
    }

    static class ViewHolder {
        GifImageView iv;
        GifImageView iv2;
        TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cp_album, null);
            holder = new ViewHolder();
            holder.iv = (GifImageView) convertView.findViewById(R.id.album_item_image);
            holder.iv2 = (GifImageView) convertView.findViewById(R.id.album_item_image2);
            holder.tv = (TextView) convertView.findViewById(R.id.album_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setImageResource(R.drawable.pic_loading);
        holder.iv.setImageByPathLoader(mData.get(position).getBitList().get(0).getPath());
        if (holder.iv2 != null) {
            holder.iv2.setImageResource(R.drawable.pic_loading);
            if (mData.get(position).getBitList().size() > 1) {
                holder.iv2.setImageByPathLoader(mData.get(position).getBitList().get(1).getPath());
            } else {
                holder.iv2.setImageBitmap(null);
            }
        }
        holder.tv.setText(mData.get(position).getName() + "(" + mData.get(position).getCount() + ")");
        return convertView;
    }

}
