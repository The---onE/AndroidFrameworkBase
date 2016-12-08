package com.xmx.androidframeworkbase.Tools.Data.Sync;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.Tools.Data.Callback.DelCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.InsertCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.SelectCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.SelectLoginCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.UpdateCallback;
import com.xmx.androidframeworkbase.Tools.Data.Cloud.BaseCloudEntityManager;
import com.xmx.androidframeworkbase.Tools.Data.DataConstants;
import com.xmx.androidframeworkbase.Tools.Data.SQL.BaseSQLEntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class BaseSyncEntityManager<Entity extends ISyncEntity> {
    String userId = null;

    public class SQLManager extends BaseSQLEntityManager<Entity> {
        void setTableName(String tableName) {
            this.tableName = tableName;
        }

        void setEntityTemplate(Entity entityTemplate) {
            this.entityTemplate = entityTemplate;
        }
    }

    class CloudManager extends BaseCloudEntityManager<Entity> {
        void setTableName(String tableName) {
            this.tableName = tableName;
        }

        void setEntityTemplate(Entity entityTemplate) {
            this.entityTemplate = entityTemplate;
        }

        public void setUserField(String userField) {
            this.userField = userField;
        }
    }

    SQLManager sqlManager = new SQLManager();
    CloudManager cloudManager = new CloudManager();

    //使用数据库管理器查询数据
    public SQLManager getSQLManager() {
        return sqlManager;
    }

    //子类构造函数中调用方法初始化下列变量！
    private String tableName = null;
    private Entity entityTemplate = null; //空模版，不需要数据
    private String userField = null; //用户字段，保存当前登录用户的ObjectId，为空时不保存用户字段

    //设置表名，数据库和云端表名相同
    protected void setTableName(String tableName) {
        sqlManager.setTableName(tableName);
        cloudManager.setTableName(tableName);
        this.tableName = tableName;
    }

    //设置模版，不需要数据
    public void setEntityTemplate(Entity entityTemplate) {
        sqlManager.setEntityTemplate(entityTemplate);
        cloudManager.setEntityTemplate(entityTemplate);
        this.entityTemplate = entityTemplate;
    }

    //设置用户字段，保存当前登录用户的ObjectId，不设置不保存用户字段
    public void setUserField(String userField) {
        cloudManager.setUserField(userField);
        this.userField = userField;
    }

    protected boolean checkDatabase() {
        return tableName != null && entityTemplate != null;
    }

    protected boolean switchAccount(String id) {
        if (!id.equals(userId)) {
            userId = id;
            sqlManager.openDatabase(userId);
            return true;
        }
        return false;
    }

    //将云端数据同步至数据库
    public void syncFromCloud(final Map<String, Object> conditions,
                              final SelectLoginCallback<Entity> callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        cloudManager.selectByCondition(conditions, null, false, new SelectLoginCallback<Entity>() {

            @Override
            public void success(AVObject user, List<Entity> entities) {
                switchAccount(user.getObjectId());
                for (Entity entity : entities) {
                    if (sqlManager.selectByCloudId(entity.getCloudId()) == null) {
                        sqlManager.insertData(entity);
                    }
                }
                callback.success(user, entities);
            }

            @Override
            public void syncError(int error) {
                callback.syncError(error);
            }

            @Override
            public void syncError(AVException e) {
                callback.syncError(e);
            }
        });
    }

    //向云端添加数据，成功后添加至数据库
    public void insertData(final Entity entity, final InsertCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        cloudManager.insertToCloud(entity, new InsertCallback() {
            @Override
            public void success(AVObject user, String objectId) {
                switchAccount(user.getObjectId());
                entity.setCloudId(objectId);
                sqlManager.insertData(entity);
                callback.success(user, objectId);
            }

            @Override
            public void syncError(int error) {
                callback.syncError(error);
            }

            @Override
            public void syncError(AVException e) {
                callback.syncError(e);
            }
        });
    }

    //从云端删除一条记录，成功后删除数据库中对应记录
    public void deleteData(final String objectId, final DelCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        cloudManager.deleteFromCloud(objectId, new DelCallback() {
            @Override
            public void success(AVObject user) {
                switchAccount(user.getObjectId());
                sqlManager.deleteByCloudId(objectId);
                callback.success(user);
            }

            @Override
            public void syncError(int error) {
                callback.syncError(error);
            }

            @Override
            public void syncError(AVException e) {
                callback.syncError(e);
            }
        });
    }

    //在云端更新一条记录，成功后更新数据库中对应记录
    public void updateData(final String objectId, final Map<String, Object> update,
                           final UpdateCallback callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        cloudManager.updateToCloud(objectId, update, new UpdateCallback() {
            @Override
            public void success(AVObject user) {
                switchAccount(user.getObjectId());
                List<String> strings = new ArrayList<>();
                for (String key : update.keySet()) {
                    Object value = update.get(key);
                    String string;
                    if (value instanceof Date) {
                        string = key + " = " + ((Date) value).getTime();
                    } else if (value instanceof String) {
                        string = key + " = '" + value + "'";
                    } else {
                        string = key + " = " + value.toString();
                    }
                    strings.add(string);
                }
                sqlManager.updateData(objectId, strings.toArray(new String[strings.size()]));
                callback.success(user);
            }

            @Override
            public void syncError(int error) {
                callback.syncError(error);
            }

            @Override
            public void syncError(AVException e) {
                callback.syncError(e);
            }
        });
    }
}
