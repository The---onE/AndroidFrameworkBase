package com.xmx.androidframeworkbase.Tools.ChoosePhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.GifImageView;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.GifLoader;
import com.xmx.androidframeworkbase.Tools.Data.BaseEntityAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowAdapter extends BaseEntityAdapter<String> {

    public ShowAdapter(Context context, ArrayList<String> paths) {
        super(context, paths);
    }

    static class ViewHolder {
        GifImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cp_show, null);
            holder = new ViewHolder();
            holder.iv = (GifImageView) convertView.findViewById(R.id.show_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setImageByPathLoader(mData.get(position), GifLoader.Type.FIFO);
        return convertView;
    }

    public List<String> getPaths() {
        return mData;
    }
}
