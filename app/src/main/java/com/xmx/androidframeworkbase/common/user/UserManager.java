package com.xmx.androidframeworkbase.common.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.xmx.androidframeworkbase.common.user.callback.AVUserCallback;
import com.xmx.androidframeworkbase.common.push.ReceiveMessageActivity;
import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.common.user.callback.LoginCallback;
import com.xmx.androidframeworkbase.common.user.callback.LogoutCallback;
import com.xmx.androidframeworkbase.common.user.callback.RegisterCallback;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

/**
 * Created by The_onE on 2016/1/10.
 * 用户管理器，单例类
 */
public class UserManager {
    private static UserManager instance;

    public synchronized static UserManager getInstance() {
        if (null == instance) {
            instance = new UserManager();
        }
        return instance;
    }

    private Context mContext;
    // 在设备中存储登录信息
    private SharedPreferences mSP;
    // 是否已登录(注册、登录或自动登录)
    private boolean loginFlag = false;

    /**
     * 设置当前上下文，在Application中调用
     */
    public void setContext(Context context) {
        mContext = context;
        mSP = context.getSharedPreferences(UserConstants.USER_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    /**
     * 默认的注销事件
     */
    private static LogoutCallback logoutCallback = new LogoutCallback() {
        @Override
        public void logout(UserData user) {
            //SyncEntityManager.getInstance().getSQLManager().clearDatabase();
        }
    };

    /**
     * 字符串SHA加密
     *
     * @param s 要加密的字符串
     * @return 经哈希加密的字符串
     */
    public static String getSHA(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(s.getBytes("UTF-8"));
            byte[] digest = md.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 生成随机校验码
     *
     * @return 随机校验码
     */
    private static String makeChecksum() {
        int checksum = new Random().nextInt();
        return "" + checksum;
    }

    /**
     * 生成用于登录AVUser的密码
     *
     * @param username 用户名
     * @param seed     用于生成密码的种子
     * @return 用于登录AVUser的密码
     */
    private String makeAVPassword(String username, String seed) {
        return getSHA(username + seed);
    }

    /**
     * 获取设备存储的校验码
     *
     * @return 设备存储的校验码
     */
    private String getChecksum() {
        return mSP.getString("checksum", "");
    }

    /**
     * 获取设备存储的用户名
     *
     * @return 设备存储的用户名
     */
    private String getUsername() {
        return mSP.getString("username", "");
    }

    /**
     * 判断是否已登录
     *
     * @return 是否已登录
     */
    public boolean isLoggedIn() {
        return mSP.getBoolean("loggedin", false);
    }

    /**
     * 登录成功后的处理
     *
     * @param user 用户数据
     * @param cs   生成的校验码
     */
    private void loginProc(UserData user, String cs) {
        loginFlag = true;
        // 将用户信息保存到设备
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean("loggedin", true);
        editor.putString("username", user.username);
        editor.putString("checksum", cs);
        editor.putString("nickname", user.nickname);
        editor.apply();
        // 将登录信息保存到云端日志
        saveLog(user.username);

        List<String> subscribing = user.subscribing;
        if (subscribing != null) {
            for (String sub : subscribing) {
                PushService.subscribe(mContext, UserManager.getSHA(sub), ReceiveMessageActivity.class);
            }
            AVInstallation.getCurrentInstallation().saveInBackground();
        }
    }

    /**
     * 注销后的处理
     *
     * @param user     用户数据
     * @param callback 自定义处理
     */
    public void logoutProc(UserData user, LogoutCallback callback) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean("loggedin", false);
        editor.putString("username", "");
        editor.putString("checksum", "");
        editor.putString("nickname", "");
        editor.apply();

        List<String> subscribing = user.subscribing;
        if (subscribing != null) {
            for (String sub : subscribing) {
                PushService.unsubscribe(mContext, UserManager.getSHA(sub));
            }
            AVInstallation.getCurrentInstallation().saveInBackground();
        }
        callback.logout(user);
    }

    /**
     * 注销接口
     *
     * @param callback 注销成功的处理，返回注销用户数据
     * @return 是否注销成功
     */
    public boolean logout(final LogoutCallback callback) {
        if (isLoggedIn()) {
            String username = getUsername();
            AVQuery<AVObject> query = new AVQuery<>(UserConstants.USER_DATA_TABLE);
            query.whereEqualTo("username", username);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (list.size() > 0) {
                            AVObject user = list.get(0);
                            UserData userData = UserData.convert(user);
                            logoutProc(userData, callback);
                        }
                    } else {
                        ExceptionUtil.normalException(e, mContext);
                    }
                }
            });
            return true;
        }
        return false;
    }

    /**
     * 在云端保存登录日志
     *
     * @param username 登录的用户名
     */
    private void saveLog(String username) {
        final AVObject post = new AVObject(UserConstants.LOGIN_LOG_TABLE);
        post.put("username", username);
        post.put("status", 0);
        post.put("timestamp", System.currentTimeMillis() / 1000);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 注册AVUser
     *
     * @param username 用户名
     * @param seed     用于生成密码的随机种子
     * @param callback 处理回调
     */
    private void registerAVUser(String username, String seed, final AVUserCallback callback) {
        AVUser user = new AVUser(); // 新建 AVUser 对象实例
        user.setUsername(username); // 设置用户名
        user.setPassword(makeAVPassword(username, seed)); // 设置密码
        user.signUpInBackground(
                new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            callback.success();
                        } else {
                            callback.error(e);
                        }
                    }
                }
        );
    }

    /**
     * 登录AVUser
     *
     * @param username 用户名
     * @param seed     用于生成密码的随机种子
     * @param callback 处理回调
     */
    private void loginAVUser(String username, String seed, final AVUserCallback callback) {
        AVUser.logInInBackground(username, makeAVPassword(username, seed),
                new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            callback.success();
                        } else {
                            callback.error(e);
                        }
                    }
                });
    }

    /**
     * 注册
     *
     * @param username         用户名
     * @param password         密码，未经加密的密码
     * @param nickname         昵称
     * @param registerCallback 回调处理
     */
    public void register(final String username, final String password, final String nickname,
                         final RegisterCallback registerCallback) {
        final AVQuery<AVObject> query = AVQuery.getQuery(UserConstants.USER_INFO_TABLE);
        query.whereEqualTo("username", username);
        query.countInBackground(new CountCallback() {
            public void done(final int count, AVException e) {
                if (e == null) {
                    if (count > 0) {
                        registerCallback.error(UserConstants.USERNAME_EXIST);
                    } else {
                        AVQuery<AVObject> query2 = AVQuery.getQuery(UserConstants.USER_DATA_TABLE);
                        query2.whereEqualTo("nickname", nickname);
                        query2.countInBackground(new CountCallback() {
                            @Override
                            public void done(int i, AVException e) {
                                if (e == null) {
                                    if (i > 0) {
                                        registerCallback.error(UserConstants.NICKNAME_EXIST);
                                    } else {
                                        final AVObject post = new AVObject(UserConstants.USER_INFO_TABLE);
                                        post.put("username", username);
                                        post.put("password", UserManager.getSHA(password));
                                        post.put("status", 0);
                                        post.put("timestamp", System.currentTimeMillis() / 1000);

                                        final AVObject data = new AVObject(UserConstants.USER_DATA_TABLE);
                                        data.put("username", username);
                                        data.put("nickname", nickname);
                                        final String checksum = UserManager.makeChecksum();
                                        data.put("checksumA", UserManager.getSHA(checksum));

                                        // 用于登录AVUser的字符串
                                        final String seed = makeChecksum();
                                        data.put("checksumAV", seed);

                                        post.put("data", data);

                                        AVACL acl = new AVACL();
                                        acl.setPublicReadAccess(true);
                                        acl.setPublicWriteAccess(false);
                                        post.setACL(acl);

                                        post.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    // 保存成功
                                                    // 注册AVUser
                                                    registerAVUser(username, seed,
                                                            new AVUserCallback() {
                                                                @Override
                                                                public void success() {
                                                                    // 以注册用户进行登录
                                                                    UserData d = UserData.convert(data);
                                                                    loginProc(d, checksum);
                                                                    registerCallback.success(d);
                                                                }

                                                                @Override
                                                                public void error(AVException e) {
                                                                    registerCallback.error(e);
                                                                }
                                                            });
                                                } else {
                                                    registerCallback.error(e);
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    registerCallback.error(e);
                                }
                            }
                        });
                    }
                } else {
                    registerCallback.error(e);
                }
            }
        });
    }

    /**
     * 登录接口
     *
     * @param username      用户名
     * @param password      密码，未经加密的密码
     * @param loginCallback 回调处理
     */
    public void login(final String username, final String password, final LoginCallback loginCallback) {
        AVQuery<AVObject> query = new AVQuery<>(UserConstants.USER_INFO_TABLE);
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    if (avObjects.size() > 0) {
                        final AVObject user = avObjects.get(0);
                        String rightPassword = user.getString("password");
                        if (rightPassword.equals(getSHA(password))) {
                            user.getAVObject("data").fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                @Override
                                public void done(final AVObject data, AVException e) {
                                    if (e == null) {
                                        // 获取用于登录AVUser的字符串
                                        final String seed = data.getString("checksumAV");
                                        final String newChecksum = makeChecksum();
                                        data.put("checksumA", getSHA(newChecksum));
                                        data.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    // 登录AVUser
                                                    loginAVUser(username, seed,
                                                            new AVUserCallback() {
                                                                @Override
                                                                public void success() {
                                                                    // 处理用户登录
                                                                    UserData d = UserData.convert(data);
                                                                    loginProc(d, newChecksum);
                                                                    loginCallback.success(d);
                                                                }

                                                                @Override
                                                                public void error(AVException e) {
                                                                    loginCallback.error(e);
                                                                }
                                                            });
                                                } else {
                                                    loginCallback.error(e);
                                                }
                                            }
                                        });
                                    } else {
                                        loginCallback.error(e);
                                    }
                                }
                            });
                        } else {
                            loginCallback.error(UserConstants.PASSWORD_ERROR);
                        }
                    } else {
                        loginCallback.error(UserConstants.USERNAME_ERROR);
                    }
                } else {
                    loginCallback.error(e);
                }
            }
        });
    }

    /**
     * 用设备保存的数据自动登录
     *
     * @param loginCallback 回调处理
     */
    public void autoLogin(final AutoLoginCallback loginCallback) {
        final String username = getUsername();
        if (!isLoggedIn() || username.equals("")) {
            loginCallback.error(UserConstants.NOT_LOGGED_IN);
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>(UserConstants.USER_DATA_TABLE);
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        final AVObject user = list.get(0);
                        String checksum = user.getString("checksumA");
                        // 获取用于登录AVUser的字符串
                        final String seed = user.getString("checksumAV");
                        if (checksum.equals(getSHA(getChecksum()))) {
                            final String newChecksum = makeChecksum();
                            user.put("checksumA", getSHA(newChecksum));
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        // 登录AVUser
                                        loginAVUser(username, seed,
                                                new AVUserCallback() {
                                                    @Override
                                                    public void success() {
                                                        // 处理用户登录
                                                        UserData d = UserData.convert(user);
                                                        loginProc(d, newChecksum);
                                                        loginCallback.success(d);
                                                    }

                                                    @Override
                                                    public void error(AVException e) {
                                                        loginCallback.error(e);
                                                    }
                                                });
                                    } else {
                                        loginCallback.error(e);
                                    }
                                }
                            });
                        } else {
                            logoutProc(UserData.convert(user), logoutCallback);
                            loginCallback.error(UserConstants.CHECKSUM_ERROR);
                        }
                    } else {
                        loginCallback.error(UserConstants.USERNAME_ERROR);
                    }
                } else {
                    loginCallback.error(e);
                }
            }
        });
    }

    /**
     * 判断是否已登录，若已登录获取用户数据
     *
     * @param loginCallback 回调处理
     */
    public void checkLogin(final AutoLoginCallback loginCallback) {
        // 若尚未通过注册、登录或自动登录完成登录，则不能校验登录
        if (!loginFlag) {
            loginCallback.error(UserConstants.CANNOT_CHECK_LOGIN);
            return;
        }
        String username = getUsername();
        AVQuery<AVObject> query = new AVQuery<>(UserConstants.USER_DATA_TABLE);
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        AVObject user = list.get(0);
                        String checksum = user.getString("checksumA");
                        if (checksum.equals(getSHA(getChecksum()))) {
                            loginCallback.success(UserData.convert(user));
                        } else {
                            logoutProc(UserData.convert(user), logoutCallback);
                            loginCallback.error(UserConstants.CHECKSUM_ERROR);
                        }
                    } else {
                        loginCallback.error(UserConstants.USERNAME_ERROR);
                    }
                } else {
                    loginCallback.error(e);
                }
            }
        });
    }
}
