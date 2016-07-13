package com.xmx.androidframeworkbase.Tools.ChoosePhoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.GifImageView;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.GifLoader;

import java.util.ArrayList;

public class ShowAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> paths;

    public ShowAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public String getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        GifImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cp_show, null);
            holder = new ViewHolder();
            holder.iv = (GifImageView) convertView.findViewById(R.id.show_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setImageByPathLoader(paths.get(position), GifLoader.Type.FIFO);
        return convertView;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }
}
