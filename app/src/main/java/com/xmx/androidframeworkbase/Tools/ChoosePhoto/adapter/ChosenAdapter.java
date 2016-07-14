package com.xmx.androidframeworkbase.Tools.ChoosePhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.GifImageView;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.PhotoInf;
import com.xmx.androidframeworkbase.Tools.Data.BaseEntityAdapter;

import java.util.ArrayList;

public class ChosenAdapter extends BaseEntityAdapter<PhotoInf> {

    public ChosenAdapter(Context context, ArrayList<PhotoInf> photos) {
        super(context, photos);
    }

    static class ViewHolder {
        GifImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cp_chosen, null);
            holder = new ViewHolder();
            holder.iv = (GifImageView) convertView.findViewById(R.id.chosen_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setImageByPathLoader(mData.get(position).getPath());
        return convertView;
    }
}
