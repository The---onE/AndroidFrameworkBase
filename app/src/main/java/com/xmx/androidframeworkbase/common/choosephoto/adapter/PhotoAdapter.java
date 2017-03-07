package com.xmx.androidframeworkbase.common.choosephoto.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.xmx.androidframeworkbase.common.choosephoto.entities.AlbumItem;
import com.xmx.androidframeworkbase.common.choosephoto.entities.PhotoInf;
import com.xmx.androidframeworkbase.common.choosephoto.entities.PhotoItem;

public class PhotoAdapter extends BaseAdapter {
    private Context context;
    private AlbumItem album;

    public PhotoAdapter(Context context, AlbumItem album) {
        this.context = context;
        this.album = album;
    }

    @Override
    public int getCount() {
        return album.getBitList().size();
    }

    @Override
    public PhotoInf getItem(int position) {
        return album.getBitList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoItem item;
        if (convertView == null) {
            item = new PhotoItem(context);
            item.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        } else {
            item = (PhotoItem) convertView;
        }
        item.setPath(album.getBitList().get(position).getPath());
        boolean flag = album.getBitList().get(position).isSelect();
        item.setChecked(flag);
        return item;
    }
}
