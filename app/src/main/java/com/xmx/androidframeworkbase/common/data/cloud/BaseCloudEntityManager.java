package com.xmx.androidframeworkbase.common.data.cloud;

import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.common.data.callback.DelCallback;
import com.xmx.androidframeworkbase.common.data.callback.InsertCallback;
import com.xmx.androidframeworkbase.common.data.callback.SelectCallback;
import com.xmx.androidframeworkbase.common.data.callback.SelectLoginCallback;
import com.xmx.androidframeworkbase.common.data.callback.UpdateCallback;
import com.xmx.androidframeworkbase.common.data.DataConstants;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.common.user.UserConstants;
import com.xmx.androidframeworkbase.common.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class BaseCloudEntityManager<Entity extends ICloudEntity> {

    //子类构造函数中初始化下列变量！
    protected String tableName = null;
    protected Entity entityTemplate = null; //空模版，不需要数据
    protected String userField = null; //用户字段，保存当前登录用户的ObjectId，为空时不保存用户字段

    protected boolean checkDatabase() {
        return tableName != null && entityTemplate != null;
    }

    //查询全部数据
    public void selectAll(final SelectCallback<Entity> callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>(tableName);
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    List<Entity> entities = new ArrayList<>();
                    for (AVObject object : avObjects) {
                        Entity entity = (Entity) entityTemplate.convertToEntity(object);
                        entities.add(entity);
                    }
                    callback.success(entities);
                } else {
                    callback.syncError(e);
                }
            }
        });
    }

    //查询全部数据并排序, ascFlag为true升序，为false降序
    public void selectAll(final SelectCallback<Entity> callback, final String order, final boolean ascFlag) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>(tableName);
        if (order != null) {
            if (ascFlag) {
                query.orderByAscending(order);
            } else {
                query.orderByDescending(order);
            }
        }
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    List<Entity> entities = new ArrayList<>();
                    for (AVObject object : avObjects) {
                        Entity entity = (Entity) entityTemplate.convertToEntity(object);
                        entities.add(entity);
                    }
                    callback.success(entities);
                } else {
                    callback.syncError(e);
                }
            }
        });
    }

    //按条件查询数据并排序, ascFlag为true升序，为false降序
    public void selectByCondition(final Map<String, Object> conditions,
                                  final String order, final boolean ascFlag,
                                  final SelectCallback<Entity> callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>(tableName);
        if (conditions != null) {
            for (String key : conditions.keySet()) {
                query.whereEqualTo(key, conditions.get(key));
            }
        }
        if (order != null) {
            if (ascFlag) {
                query.orderByAscending(order);
            } else {
                query.orderByDescending(order);
            }
        }
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    List<Entity> entities = new ArrayList<>();
                    for (AVObject object : avObjects) {
                        Entity entity = (Entity) entityTemplate.convertToEntity(object);
                        entities.add(entity);
                    }
                    callback.success(entities);
                } else {
                    callback.syncError(e);
                }
            }
        });
    }

    //在登录状态下按条件查询数据并排序, ascFlag为true升序，为false降序
    public void selectByCondition(final Map<String, Object> conditions,
                                  final String order, final boolean ascFlag,
                                  final SelectLoginCallback<Entity> callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final UserData user) {
                AVQuery<AVObject> query = new AVQuery<>(tableName);
                if (userField != null) {
                    query.whereEqualTo(userField, user.objectId);
                }
                if (conditions != null) {
                    for (String key : conditions.keySet()) {
                        query.whereEqualTo(key, conditions.get(key));
                    }
                }
                if (order != null) {
                    if (ascFlag) {
                        query.orderByAscending(order);
                    } else {
                        query.orderByDescending(order);
                    }
                }
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        if (e == null) {
                            List<Entity> entities = new ArrayList<>();
                            for (AVObject object : avObjects) {
                                Entity entity = (Entity) entityTemplate.convertToEntity(object);
                                entities.add(entity);
                            }
                            callback.success(user, entities);
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                    case UserConstants.CANNOT_CHECK_LOGIN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }

    public void insertToCloud(final Entity entity, final InsertCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final UserData user) {
                final AVObject object = entity.getContent(tableName);
                if (userField != null) {
                    object.put(userField, user.objectId);
                }
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            callback.success(user, object.getObjectId());
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                    case UserConstants.CANNOT_CHECK_LOGIN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }

    public void deleteFromCloud(final String objectId, final DelCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final UserData user) {
                AVQuery<AVObject> query = new AVQuery<>(tableName);
                query.getInBackground(objectId, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject object, AVException e) {
                        if (e == null) {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        callback.success(user);
                                    } else {
                                        callback.syncError(e);
                                    }
                                }
                            });
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                    case UserConstants.CANNOT_CHECK_LOGIN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }

    public void updateToCloud(final String objectId, final Map<String, Object> update,
                              final UpdateCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(final UserData user) {
                AVQuery<AVObject> query = new AVQuery<>(tableName);
                query.getInBackground(objectId, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject object, AVException e) {
                        if (e == null) {
                            if (update != null) {
                                for (String key : update.keySet()) {
                                    object.put(key, update.get(key));
                                }
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            callback.success(user);
                                        } else {
                                            callback.syncError(e);
                                        }
                                    }
                                });
                            }
                        } else {
                            callback.syncError(e);
                        }
                    }
                });
            }

            @Override
            public void error(AVException e) {
                callback.syncError(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                    case UserConstants.CANNOT_CHECK_LOGIN:
                        callback.syncError(DataConstants.NOT_LOGGED_IN);
                        break;
                    case UserConstants.USERNAME_ERROR:
                        callback.syncError(DataConstants.USERNAME_ERROR);
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        callback.syncError(DataConstants.CHECKSUM_ERROR);
                        break;
                }
            }
        });
    }

    public static void defaultError(int error, Context context) {
        switch (error) {
            case DataConstants.NOT_INIT:
                Toast.makeText(context, R.string.failure, Toast.LENGTH_SHORT).show();
                break;
            case DataConstants.NOT_LOGGED_IN:
                Toast.makeText(context, R.string.not_loggedin, Toast.LENGTH_SHORT).show();
                break;
            case DataConstants.USERNAME_ERROR:
                Toast.makeText(context, R.string.username_error, Toast.LENGTH_SHORT).show();
                break;
            case DataConstants.CHECKSUM_ERROR:
                Toast.makeText(context, R.string.not_loggedin, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
