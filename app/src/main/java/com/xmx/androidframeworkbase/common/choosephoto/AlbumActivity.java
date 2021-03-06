package com.xmx.androidframeworkbase.common.choosephoto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.common.choosephoto.adapter.AlbumAdapter;
import com.xmx.androidframeworkbase.common.choosephoto.entities.AlbumItem;
import com.xmx.androidframeworkbase.common.choosephoto.entities.PhotoInf;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_cp_album)
public class AlbumActivity extends BaseTempActivity {
    @ViewInject(R.id.album_listview)
    private ListView albumGV;

    private List<AlbumItem> albumList;

    //设置获取图片的字段信息
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME, //名称
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.LONGITUDE, //经度
            MediaStore.Images.Media._ID, //id
            MediaStore.Images.Media.BUCKET_ID, //dir id 目录
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME //dir name 目录名称
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle(R.string.choose_album);
    }

    @Event(value = R.id.album_listview, type = ListView.OnItemClickListener.class)
    private void onAlbumItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(AlbumActivity.this, PhotoActivity.class);
        intent.putExtra("album", albumList.get(i));
        startActivityForResult(intent, Constants.CHOOSE_PHOTO);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        albumList = getPhotoAlbum();
        albumGV.setAdapter(new AlbumAdapter(albumList, this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CHOOSE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    //按相册获取图片信息
    private List<AlbumItem> getPhotoAlbum() {
        List<AlbumItem> albumList = new ArrayList<>();
        Cursor cursor = MediaStore.Images.Media.query(getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
                null, MediaStore.Images.Media.DATE_MODIFIED);
        Map<String, AlbumItem> countMap = new LinkedHashMap<>();
        AlbumItem pa;
        cursor.moveToLast();
        cursor.moveToNext();
        while (cursor.moveToPrevious()) {
            String path = cursor.getString(1);
            String dir_id = cursor.getString(4);
            String dir = cursor.getString(5);
            if (!countMap.containsKey(dir_id)) {
                pa = new AlbumItem();
                pa.setName(dir);
                pa.getBitList().add(new PhotoInf(path));
                countMap.put(dir_id, pa);
            } else {
                pa = countMap.get(dir_id);
                pa.increaseCount();
                pa.getBitList().add(new PhotoInf(path));
            }
        }
        cursor.close();
        Iterable<String> it = countMap.keySet();
        for (String key : it) {
            albumList.add(countMap.get(key));
        }
        return albumList;
    }
}
