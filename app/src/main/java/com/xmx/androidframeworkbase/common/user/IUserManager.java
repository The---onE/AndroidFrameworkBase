package com.xmx.androidframeworkbase.common.user;

import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.common.user.callback.LoginCallback;
import com.xmx.androidframeworkbase.common.user.callback.LogoutCallback;
import com.xmx.androidframeworkbase.common.user.callback.RegisterCallback;

/**
 * Created by The_onE on 2017/4/22.
 * 用户管理器接口，通过该接口提供的服务进行注册登录等操作
 * 登录后在设备保留校验信息，之后可通过校验信息自动登录
 * 自动登录时比对校验信息，确认无误则成功登录并更新校验信息
 * 用户在其他设备登录会导致本设备校验信息失效，需重新登录
 * 在登录或自动登录成功后才可校验登录获取数据，用于确认是否在其他设备登录或登录过期
 */

public interface IUserManager {
    /**
     * 判断是否已登录
     *
     * @return 是否已登录
     */
    public boolean isLoggedIn();

    /**
     * 注销接口
     *
     * @param callback 注销成功的处理，返回注销用户数据
     * @return 是否注销成功
     */
    public boolean logout(final LogoutCallback callback);


    /**
     * 注册
     *
     * @param username         用户名
     * @param password         密码，未经加密的密码
     * @param nickname         昵称
     * @param registerCallback 回调处理
     */
    public void register(final String username, final String password, final String nickname,
                         final RegisterCallback registerCallback);

    /**
     * 登录接口
     *
     * @param username      用户名
     * @param password      密码，未经加密的密码
     * @param loginCallback 回调处理
     */
    public void login(final String username, final String password,
                      final LoginCallback loginCallback);

    /**
     * 用设备保存的数据自动登录
     *
     * @param loginCallback 回调处理
     */
    public void autoLogin(final AutoLoginCallback loginCallback);

    /**
     * 判断是否已登录，若已登录获取用户数据
     *
     * @param loginCallback 回调处理
     */
    public void checkLogin(final AutoLoginCallback loginCallback);
}
