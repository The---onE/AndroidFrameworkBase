package com.xmx.androidframeworkbase.User;

import android.content.Context;
import android.content.SharedPreferences;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.Sync.SyncEntityManager;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.Callback.LoginCallback;
import com.xmx.androidframeworkbase.User.Callback.LogoutCallback;
import com.xmx.androidframeworkbase.User.Callback.RegisterCallback;

import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

/**
 * Created by The_onE on 2016/1/10.
 */
public class UserManager {
    private static UserManager instance;

    Context mContext;
    SharedPreferences mSP;

    static LogoutCallback logoutCallback = new LogoutCallback() {
        @Override
        public void logout(AVObject user) {
            SyncEntityManager.getInstance().getSQLManager().clearDatabase();
        }
    };

    public synchronized static UserManager getInstance() {
        if (null == instance) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setContext(Context context) {
        mContext = context;
        mSP = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
    }

    public static long getId(AVObject user) {
        String id = user.getObjectId();
        return Math.abs(id.hashCode());
    }

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

    public static String makeChecksum() {
        int checksum = new Random().nextInt();
        return "" + checksum;
    }

    public String getChecksum() {
        return mSP.getString("checksum", "");
    }

    public String getUsername() {
        return mSP.getString("username", "");
    }

    public void login(AVObject user, String un, String cs, String nn) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean("loggedin", true);
        editor.putString("username", un);
        editor.putString("checksum", cs);
        editor.putString("nickname", nn);
        editor.apply();

        saveLog(un);
    }

    public void logout(AVObject user, LogoutCallback callback) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean("loggedin", false);
        editor.putString("username", "");
        editor.putString("checksum", "");
        editor.putString("nickname", "");
        editor.apply();

        callback.logout(user);
    }

    public void logout(final LogoutCallback callback) {
        if (isLoggedIn()) {
            String username = getUsername();
            AVQuery<AVObject> query = new AVQuery<>(Constants.USER_DATA_TABLE);
            query.whereEqualTo("username", username);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (list.size() > 0) {
                            AVObject user = list.get(0);
                            logout(user, callback);
                            /*List<String> subscribing = user.getList("subscribing");
                            if (subscribing != null) {
                                for (String sub : subscribing) {
                                    PushService.unsubscribe(mContext, UserManager.getSHA(sub));
                                }
                                AVInstallation.getCurrentInstallation().saveInBackground();
                            }*/
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void saveLog(String username) {
        final AVObject post = new AVObject(Constants.LOGIN_LOG_TABLE);
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

    public boolean isLoggedIn() {
        return mSP.getBoolean("loggedin", false);
    }

    public void register(final String username, final String password, final String nickname, final RegisterCallback registerCallback) {
        final AVQuery<AVObject> query = AVQuery.getQuery(Constants.USER_INFO_TABLE);
        query.whereEqualTo("username", username);
        query.countInBackground(new CountCallback() {
            public void done(final int count, AVException e) {
                if (e == null) {
                    if (count > 0) {
                        registerCallback.usernameExist();
                    } else {
                        AVQuery<AVObject> query2 = AVQuery.getQuery(Constants.USER_DATA_TABLE);
                        query2.whereEqualTo("nickname", nickname);
                        query2.countInBackground(new CountCallback() {
                            @Override
                            public void done(int i, AVException e) {
                                if (e == null) {
                                    if (i > 0) {
                                        registerCallback.nicknameExist();
                                    } else {
                                        final AVObject post = new AVObject(Constants.USER_INFO_TABLE);
                                        post.put("username", username);
                                        post.put("password", UserManager.getSHA(password));
                                        post.put("status", 0);
                                        post.put("timestamp", System.currentTimeMillis() / 1000);

                                        final AVObject data = new AVObject(Constants.USER_DATA_TABLE);
                                        data.put("username", username);
                                        data.put("nickname", nickname);
                                        final String checksum = UserManager.makeChecksum();
                                        data.put("checksumA", UserManager.getSHA(checksum));

                                        post.put("data", data);

                                        AVACL acl = new AVACL();
                                        acl.setPublicReadAccess(true);
                                        acl.setPublicWriteAccess(false);
                                        post.setACL(acl);

                                        post.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    login(data, username, checksum, nickname);
                                                    registerCallback.success();
                                                } else {
                                                    registerCallback.errorNetwork();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    registerCallback.errorNetwork();
                                }
                            }
                        });
                    }
                } else {
                    registerCallback.errorNetwork();
                }
            }
        });
    }

    public void login(final String username, final String password, final LoginCallback loginCallback) {
        AVQuery<AVObject> query = new AVQuery<>(Constants.USER_INFO_TABLE);
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
                                        final String nickname = data.getString("nickname");
                                        final String newChecksum = makeChecksum();
                                        data.put("checksumA", getSHA(newChecksum));
                                        data.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    login(data, username, newChecksum, nickname);
                                                    loginCallback.success(user);
                                                } else {
                                                    loginCallback.errorNetwork();
                                                }
                                            }
                                        });
                                    } else {
                                        loginCallback.errorNetwork();
                                    }
                                }
                            });
                        } else {
                            loginCallback.errorPassword();
                        }
                    } else {
                        loginCallback.errorUsername();
                    }
                } else {
                    loginCallback.errorNetwork();
                }
            }
        });
    }

    public void autoLogin(final AutoLoginCallback loginCallback) {
        final String username = getUsername();
        if (!isLoggedIn() || username.equals("")) {
            loginCallback.notLoggedIn();
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>(Constants.USER_DATA_TABLE);
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        final AVObject user = list.get(0);
                        String checksum = user.getString("checksumA");
                        if (checksum.equals(getSHA(getChecksum()))) {
                            final String nickname = user.getString("nickname");
                            final String newChecksum = makeChecksum();
                            user.put("checksumA", getSHA(newChecksum));
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        login(user, username, newChecksum, nickname);
                                        loginCallback.success(user);
                                    } else {
                                        loginCallback.errorNetwork();
                                    }
                                }
                            });
                        } else {
                            logout(user, logoutCallback);
                            loginCallback.errorChecksum();
                        }
                    } else {
                        loginCallback.errorUsername();
                    }
                } else {
                    loginCallback.errorNetwork();
                }
            }
        });
    }

    public void checkLogin(final AutoLoginCallback loginCallback) {
        String username = getUsername();
        if (!isLoggedIn() || username.equals("")) {
            loginCallback.notLoggedIn();
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>(Constants.USER_DATA_TABLE);
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        AVObject user = list.get(0);
                        String checksum = user.getString("checksumA");
                        if (checksum.equals(getSHA(getChecksum()))) {
                            loginCallback.success(user);
                        } else {
                            logout(user, logoutCallback);
                            loginCallback.errorChecksum();
                        }
                    } else {
                        loginCallback.errorUsername();
                    }
                } else {
                    loginCallback.errorNetwork();
                }
            }
        });
    }
}
