package com.xmx.androidframeworkbase.common.choosephoto;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.common.choosephoto.adapter.ChosenAdapter;
import com.xmx.androidframeworkbase.common.choosephoto.adapter.PhotoAdapter;
import com.xmx.androidframeworkbase.common.choosephoto.entities.AlbumItem;
import com.xmx.androidframeworkbase.common.choosephoto.entities.PhotoInf;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_cp_photo)
public class PhotoActivity extends BaseTempActivity {

    @ViewInject(R.id.photo_path)
    TextView photoPath;

    @ViewInject(R.id.chosen_gridview)
    private GridView chosen_gv;

    @ViewInject(R.id.photo_gridview)
    private GridView gv;

    @ViewInject(R.id.btn_sure)
    private Button btnSure;

    private AlbumItem album;
    private PhotoAdapter adapter;

    private ArrayList<PhotoInf> chosen = new ArrayList<>();
    private ArrayList<String> paths = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {

        setTitle(R.string.choose_photo);

        album = (AlbumItem) getIntent().getExtras().get("album");
        if (album != null) {
            String p = album.getBitList().get(0).getPath();
            int end = p.lastIndexOf("/");
            if (end != -1) {
                photoPath.setText(p.substring(0, end));
            } else {
                photoPath.setText(null);
            }

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                gv.setNumColumns(6);
            } else {
                gv.setNumColumns(4);
            }
        }
    }

    @Event(value = R.id.photo_gridview, type = GridView.OnItemClickListener.class)
    private void onPhotoItemClick(AdapterView<?> parent, View view, int position, long id) {
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

    @Event(value = R.id.chosen_gridview, type = GridView.OnItemClickListener.class)
    private void onChosenItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(PhotoActivity.this, BigPhotoActivity.class);
        intent.putExtra("paths", paths);
        intent.putExtra("index", position);
        startActivity(intent);
    }

    @Event(value = R.id.btn_sure)
    private void onSureClick(View v) {
        if (!paths.isEmpty()) {
            Intent i = new Intent(PhotoActivity.this, AlbumActivity.class);
            i.putExtra("paths", paths);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        adapter = new PhotoAdapter(this, album);
        gv.setAdapter(adapter);

        ChosenAdapter chosen_adapter = new ChosenAdapter(this, chosen);
        chosen_gv.setAdapter(chosen_adapter);
    }

    private void processChosen(boolean isSelect) {
        int size = chosen.size();
        if (isSelect) {
            btnSure.setText("确定(" + size + ")");
        } else {
            btnSure.setText("确定(" + size + ")");
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) ((100 + 5) * density * size - 10);
        int itemWidth = (int) (100 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        chosen_gv.setLayoutParams(params);
        chosen_gv.setColumnWidth(itemWidth);
        chosen_gv.setNumColumns(size);
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
