package com.xmx.androidframeworkbase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.xmx.androidframeworkbase.Fragments.CloudFragment;
import com.xmx.androidframeworkbase.Fragments.HomeFragment;
import com.xmx.androidframeworkbase.Fragments.PushFragment;
import com.xmx.androidframeworkbase.Fragments.SQLFragment;
import com.xmx.androidframeworkbase.Fragments.SyncFragment;
import com.xmx.androidframeworkbase.Fragments.IMFragment;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseNavigationActivity;
import com.xmx.androidframeworkbase.Tools.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity {
    private long mExitTime = 0;

    ViewPager vp;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new IMFragment());
        fragments.add(new PushFragment());
        fragments.add(new SyncFragment());
        fragments.add(new SQLFragment());
        fragments.add(new CloudFragment());

        List<String> titles = new ArrayList<>();
        titles.add("首页");
        titles.add("IM");
        titles.add("推送");
        titles.add("Sync");
        titles.add("SQL");
        titles.add("Cloud");

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);

        vp = getViewById(R.id.pager_main);
        vp.setAdapter(adapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        super.onNavigationItemSelected(item);

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                vp.setCurrentItem(0);
                break;
            case R.id.nav_im:
                vp.setCurrentItem(1);
                break;
            case R.id.nav_push:
                vp.setCurrentItem(2);
                break;
            case R.id.nav_sync:
                vp.setCurrentItem(3);
                break;
            case R.id.nav_sql:
                vp.setCurrentItem(4);
                break;
            case R.id.nav_cloud:
                vp.setCurrentItem(5);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
