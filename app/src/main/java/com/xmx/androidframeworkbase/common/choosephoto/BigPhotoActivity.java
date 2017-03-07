package com.xmx.androidframeworkbase.common.choosephoto;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseActivity;
import com.xmx.androidframeworkbase.common.choosephoto.entities.BigGifImageView;

import java.util.ArrayList;

public class BigPhotoActivity extends BaseActivity {
    LinearLayout layout;
    //JazzyViewPager vp;
    ViewPager vp;
    String path;
    ArrayList<String> paths;
    int index;
    boolean flipFlag;

    private RelativeLayout setPhoto(RelativeLayout l, String path, int sum, int index) {

        TextView tv = (TextView) l.findViewById(R.id.big_photo_index);
        if (sum <= 0) {
            l.removeView(tv);
        } else {
            tv.setText("" + index + "/" + sum);
        }

        final BigGifImageView iv = (BigGifImageView) l.findViewById(R.id.big_photo);
        boolean flag = iv.setImageByPathLoader(path);

        LinearLayout buttonLayout = (LinearLayout) l.findViewById(R.id.big_photo_button);
        if (!flag) {
            l.removeView(buttonLayout);
        } else {
            Button upend = (Button) buttonLayout.findViewById(R.id.big_photo_upend);
            upend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv.upend();
                }
            });

            Button pause = (Button) buttonLayout.findViewById(R.id.big_photo_pause);
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv.pause();
                }
            });

            Button play = (Button) buttonLayout.findViewById(R.id.big_photo_play);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv.play();
                }
            });
        }
        return l;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cp_big_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layout = (LinearLayout) (findViewById(R.id.photo_layout));

        index = getIntent().getIntExtra("index", -1);

        if (index == -1) {
            flipFlag = false;
            path = getIntent().getStringExtra("path");
            if (path == null) {
                path = getIntent().getData().toString();
                if (path != null) {
                    Uri uri = getIntent().getData();
                    path = uri.getPath();
                }
            }

            if (path != null) {
                RelativeLayout l = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_cp_big_photo, null);
                setPhoto(l, path, 0, 0);
                layout.addView(l);
            }
        } else {
            flipFlag = true;
            paths = getIntent().getStringArrayListExtra("paths");

            //vp = new JazzyViewPager(this);
            vp = new ViewPager(this);
            vp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            //vp.setTransitionEffect(JazzyViewPager.TransitionEffect.Accordion);
            vp.setAdapter(new PagerAdapter() {
                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    RelativeLayout l = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_cp_big_photo, null);
                    setPhoto(l, paths.get(position), paths.size(), position + 1);

                    container.addView(l);
                    l.setTag("layout" + position);
                    //vp.setObjectForPosition(l, position);
                    return l;
                }

                @Override
                public int getCount() {
                    return paths.size();
                }
            });
            layout.addView(vp);
            vp.setCurrentItem(index);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        for (int i = 0; i < vp.getChildCount(); i++) {
            RelativeLayout layout = (RelativeLayout) vp.getChildAt(i);
            BigGifImageView iv = (BigGifImageView) layout.findViewById(R.id.big_photo);
            iv.setImageByPathLoader(iv.getPath());
        }
    }
}
