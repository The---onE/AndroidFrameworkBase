package com.xmx.androidframeworkbase.core.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.base.activity.BaseActivity;
import com.xmx.androidframeworkbase.common.user.IUserManager;
import com.xmx.androidframeworkbase.module.user.LoginActivity;
import com.xmx.androidframeworkbase.common.user.LoginEvent;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.common.user.callback.LogoutCallback;
import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.core.fragments.CartFragment;
import com.xmx.androidframeworkbase.core.fragments.DataFragment;
import com.xmx.androidframeworkbase.core.fragments.HomeFragment;
import com.xmx.androidframeworkbase.core.fragments.NotificationFragment;
import com.xmx.androidframeworkbase.core.fragments.PushFragment;
import com.xmx.androidframeworkbase.core.fragments.IMFragment;
import com.xmx.androidframeworkbase.core.PagerAdapter;
import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.common.user.UserConstants;
import com.xmx.androidframeworkbase.common.user.UserManager;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // 第一次点击返回键时间，在一定时间内再次点击则退出程序
    private long mExitTime = 0;
    // 分页Fragment容器
    ViewPager vp;
    // 侧滑菜单登录菜单项
    MenuItem login;

    private IUserManager userManager = UserManager.getInstance();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        // 初始化侧滑菜单
        initDrawerNavigation();

        // Fragment列表
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new CartFragment());
        fragments.add(new NotificationFragment());
        fragments.add(new IMFragment());
        fragments.add(new PushFragment());
        fragments.add(new DataFragment());

        // Fragment对应的标题
        List<String> titles = new ArrayList<>();
        titles.add("首页");
        titles.add("购物车");
        titles.add("通知");
        titles.add("IM");
        titles.add("推送");
        titles.add("数据");

        // 分页Fragment适配器
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);

        vp = getViewById(R.id.view_pager);
        vp.setAdapter(adapter);
        // 设置标签页底部选项卡
        TabLayout tabLayout = getViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vp);

        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_launcher);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_launcher);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_launcher);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_launcher);
        tabLayout.getTabAt(4).setIcon(R.mipmap.ic_launcher);
        tabLayout.getTabAt(5).setIcon(R.mipmap.ic_launcher);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 设置侧滑菜单
        NavigationView navigation = getViewById(R.id.nav_view);
        Menu menu = navigation.getMenu();
        login = menu.findItem(R.id.nav_logout);
        // 在SplashActivity中自动登录，在此校验登录
        if (userManager.isLoggedIn()) {
            checkLogin();
        }
    }

    /**
     * 第一次点击返回键弹出确认信息，再次点击则退出程序
     */
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

    /**
     * 初始化侧边栏
     */
    protected void initDrawerNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // 处理登录返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserConstants.LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            // 登录成功
            checkLogin();
        }
    }

    private void checkLogin() {
        userManager.checkLogin(new AutoLoginCallback() {
            @Override
            public void success(UserData user) {
                login.setTitle(user.nickname + " 点击注销");
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.CANNOT_CHECK_LOGIN:
                        showToast("请先登录");
                        break;
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

            @Override
            public void error(AVException e) {
                ExceptionUtil.normalException(e, getBaseContext());
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home: // 首页
                vp.setCurrentItem(0);
                break;
            case R.id.nav_cart: // 购物车
                vp.setCurrentItem(1);
                break;
            case R.id.nav_notification: // 通知
                vp.setCurrentItem(2);
                break;
            case R.id.nav_im: // IM
                vp.setCurrentItem(3);
                break;
            case R.id.nav_push: // 推送
                vp.setCurrentItem(4);
                break;
            case R.id.nav_data: // 数据
                vp.setCurrentItem(5);
                break;
            case R.id.nav_setting: // 设置
                break;
            case R.id.nav_logout: // 登录/注销
                final Intent intent = new Intent(this, LoginActivity.class);
                if (userManager.isLoggedIn()) {
                    // 注销
                    AlertDialog.Builder builder = new AlertDialog
                            .Builder(this);
                    builder.setMessage("确定要注销吗？");
                    builder.setTitle("提示");
                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 确认注销
                            userManager.logout(new LogoutCallback() {
                                @Override
                                public void logout(UserData user) {
                                    //SyncEntityManager.getInstance().getSQLManager().clearDatabase();
                                }
                            });
                            showToast("注销成功");
                            login.setTitle("登录");
//                            startActivityForResult(intent, UserConstants.LOGIN_REQUEST_CODE);
                        }
                    });
                    builder.show();
                } else {
                    // 登录
                    startActivityForResult(intent, UserConstants.LOGIN_REQUEST_CODE);
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 设置菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 设置菜单点击事件
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

    @Subscribe
    public void onEvent(LoginEvent event) {
        checkLogin();
    }
}
