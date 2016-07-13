package com.xmx.androidframeworkbase.Tools.ChoosePhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.AlbumItem;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.GifImageView;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {
    private List<AlbumItem> albumList;
    private Context context;

    public AlbumAdapter(List<AlbumItem> list, Context context) {
        this.albumList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cp_album, null);
            holder = new ViewHolder();
            holder.iv = (GifImageView) convertView.findViewById(R.id.album_item_image);
            holder.iv2 = (GifImageView) convertView.findViewById(R.id.album_item_image2);
            holder.tv = (TextView) convertView.findViewById(R.id.album_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setImageResource(R.drawable.pic_loading);
        holder.iv.setImageByPathLoader(albumList.get(position).getBitList().get(0).getPath());
        if (holder.iv2 != null) {
            holder.iv2.setImageResource(R.drawable.pic_loading);
            if (albumList.get(position).getBitList().size() > 1) {
                holder.iv2.setImageByPathLoader(albumList.get(position).getBitList().get(1).getPath());
            } else {
                holder.iv2.setImageBitmap(null);
            }
        }
        holder.tv.setText(albumList.get(position).getName() + "(" + albumList.get(position).getCount() + ")");
        return convertView;
    }

}
