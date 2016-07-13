package com.xmx.androidframeworkbase.Tools.ChoosePhoto;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.adapter.ChosenAdapter;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.adapter.PhotoAdapter;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.AlbumItem;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.entities.PhotoInf;

public class PhotoActivity extends Activity {
    private GridView chosen_gridview;
    private AlbumItem album;
    private PhotoAdapter adapter;
    private GridView gv;

    private ArrayList<PhotoInf> chosen = new ArrayList<>();

    private ArrayList<String> paths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_photo);

        album = (AlbumItem) getIntent().getExtras().get("album");
        if (album != null) {
            TextView tv = (TextView) findViewById(R.id.photo_path);
            String p = album.getBitList().get(0).getPath();
            int end = p.lastIndexOf("/");
            if (end != -1) {
                tv.setText(p.substring(0, end));
            } else {
                tv.setText(null);
            }

            gv = (GridView) findViewById(R.id.photo_gridview);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                gv.setNumColumns(6);
            } else {
                gv.setNumColumns(4);
            }
            adapter = new PhotoAdapter(this, album);
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PhotoInf gridItem = album.getBitList().get(position);
                    if (gridItem.isSelect()) {
                        gridItem.setSelect(false);
                        paths.remove(gridItem.getPath());
                        chosen.remove(gridItem);
                        processChosen(false);
                    } else {
                        gridItem.setSelect(true);
                        paths.add(gridItem.getPath());
                        chosen.add(gridItem);
                        processChosen(true);
                    }
                    adapter.notifyDataSetChanged();
                }
            });

            chosen_gridview = (GridView) findViewById(R.id.chosen_gridview);
            ChosenAdapter chosen_adapter = new ChosenAdapter(this, chosen);
            chosen_gridview.setAdapter(chosen_adapter);
            chosen_gridview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(PhotoActivity.this, BigPhotoActivity.class);
                    intent.putExtra("paths", paths);
                    intent.putExtra("index", position);
                    startActivity(intent);
                }
            });

            findViewById(R.id.btn_sure).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!paths.isEmpty()) {
                        Intent i = new Intent(PhotoActivity.this, AlbumActivity.class);
                        i.putExtra("paths", paths);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            });
        }
    }

    private void processChosen(boolean isSelect) {
        int size = chosen.size();
        Button btn_sure = (Button) findViewById(R.id.btn_sure);
        if (isSelect) {
            btn_sure.setText("确定(" + size + ")");
        } else {
            btn_sure.setText("确定(" + size + ")");
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) ((100 + 5) * density * size - 10);
        int itemWidth = (int) (100 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        chosen_gridview.setLayoutParams(params);
        chosen_gridview.setColumnWidth(itemWidth);
        chosen_gridview.setNumColumns(size);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gv.setNumColumns(6);
        } else {
            gv.setNumColumns(4);
        }
    }
}
