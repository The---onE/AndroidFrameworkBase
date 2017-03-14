package com.xmx.androidframeworkbase.core.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.core.fragments.CartFragment;
import com.xmx.androidframeworkbase.core.fragments.DataFragment;
import com.xmx.androidframeworkbase.core.fragments.HomeFragment;
import com.xmx.androidframeworkbase.core.fragments.NotificationFragment;
import com.xmx.androidframeworkbase.core.fragments.PushFragment;
import com.xmx.androidframeworkbase.core.fragments.IMFragment;
import com.xmx.androidframeworkbase.base.activity.BaseNavigationActivity;
import com.xmx.androidframeworkbase.core.PagerAdapter;
import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.common.user.UserConstants;
import com.xmx.androidframeworkbase.common.user.UserManager;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

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
        fragments.add(new CartFragment());
        fragments.add(new NotificationFragment());
        fragments.add(new IMFragment());
        fragments.add(new PushFragment());
        fragments.add(new DataFragment());

        List<String> titles = new ArrayList<>();
        titles.add("首页");
        titles.add("购物车");
        titles.add("通知");
        titles.add("IM");
        titles.add("推送");
        titles.add("数据");

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);

        vp = getViewById(R.id.view_pager);
        vp.setAdapter(adapter);
        // 设置标签页底部选项卡
        TabLayout tabLayout = getViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        NavigationView navigation = getViewById(R.id.nav_view);
        Menu menu = navigation.getMenu();
        final MenuItem login = menu.findItem(R.id.nav_logout);

        UserManager.getInstance().autoLogin(new AutoLoginCallback() {
            @Override
            public void success(final AVObject user) {
                login.setTitle(user.getString("nickname") + " 点击注销");
            }

            @Override
            public void error(AVException e) {
                ExceptionUtil.normalException(e, getBaseContext());
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        showToast("请在侧边栏中选择登录");
                        break;
                    case UserConstants.USERNAME_ERROR:
                        showToast("请在侧边栏中选择登录");
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        showToast("登录过期，请在侧边栏中重新登录");
                        break;
                }
            }
        });
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
            case R.id.nav_cart:
                vp.setCurrentItem(1);
                break;
            case R.id.nav_notification:
                vp.setCurrentItem(2);
                break;
            case R.id.nav_im:
                vp.setCurrentItem(3);
                break;
            case R.id.nav_push:
                vp.setCurrentItem(4);
                break;
            case R.id.nav_data:
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
