package com.xmx.androidframeworkbase.common.user;

/**
 * Created by The_onE on 2016/7/3.
 */
public class UserConstants {
    public static final String USER_INFO_TABLE = "UserInfo"; // 云端用户信息表名
    public static final String USER_DATA_TABLE = "UserData"; // 云端用户数据表名
    public static final String LOGIN_LOG_TABLE = "LoginLog"; // 云端用户日志表名

    public static final String USER_SHARED_PREFERENCE = "USER"; // SharedPreference用户表名

    public static final int USERNAME_EXIST = -1; // 用户名已存在
    public static final int NICKNAME_EXIST = -2; // 昵称已存在
    public static final int PASSWORD_ERROR = -3; // 密码错误
    public static final int USERNAME_ERROR = -4; // 用户名不存在
    public static final int NOT_LOGGED_IN = -5; // 尚未登录
    public static final int CHECKSUM_ERROR = -6; // 校验码错误
    public static final int CANNOT_CHECK_LOGIN = -10; // 尚未通过注册、登录或自动登录完成登录，不能校验登录

    public static final int LOGIN_SUCCESS = 1; // 登录成功

    public static final int LOGIN_REQUEST_CODE = 1000; // 登录请求代码
    public static final int REGISTER_REQUEST_CODE = 1001; // 登录请求代码
}
