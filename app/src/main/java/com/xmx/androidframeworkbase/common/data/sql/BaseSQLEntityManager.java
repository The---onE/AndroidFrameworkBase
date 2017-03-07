package com.xmx.androidframeworkbase.common.data.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSQLEntityManager<Entity extends ISQLEntity> {
    protected SQLiteDatabase database = null;
    long version = System.currentTimeMillis();
    protected boolean openFlag = false;

    //子类构造函数中初始化下列变量！初始化后再调用openDatabase方法
    protected String tableName = null;
    protected Entity entityTemplate = null; //空模版，不需要数据

    //数据变更时会更新版本
    public long getVersion() {
        return version;
    }

    private void openSQLFile() {
        String d = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_DIR;
        File dir = new File(d);
        boolean flag = dir.exists() || dir.mkdirs();

        if (flag) {
            String sqlFile = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_FILE;
            File file = new File(sqlFile);
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            if (database == null) {
                Log.e("DatabaseError", "创建文件失败");
            }
            version++;
        } else {
            Log.e("DatabaseError", "创建目录失败");
        }
    }

    private void openSQLFile(String filename) {
        String d = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_DIR;
        File dir = new File(d);
        boolean flag = dir.exists() || dir.mkdirs();

        if (flag) {
            String sqlFile = android.os.Environment.getExternalStorageDirectory() +
                    Constants.DATABASE_DIR + "/" + filename + ".db";
            File file = new File(sqlFile);
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            if (database == null) {
                Log.e("DatabaseError", "创建文件失败");
            }
            version++;
        } else {
            Log.e("DatabaseError", "创建目录失败");
        }
    }

    protected boolean openDatabase() {
        openSQLFile();
        if (database != null && tableName != null && entityTemplate != null) {
            String createSQL = "create table if not exists " + tableName + "("
                    + entityTemplate.tableFields() + ")";
            database.execSQL(createSQL);
            openFlag = true;
        } else {
            openFlag = false;
        }
        return openFlag;
    }

    public boolean openDatabase(String filename) {
        openSQLFile(filename);
        if (database != null && tableName != null && entityTemplate != null) {
            String createSQL = "create table if not exists " + tableName + "("
                    + entityTemplate.tableFields() + ")";
            database.execSQL(createSQL);
            openFlag = true;
        } else {
            openFlag = false;
        }
        return openFlag;
    }

    public void closeDatabase() {
        openFlag = false;
    }

    protected boolean checkDatabase() {
        return openFlag || openDatabase();
    }

    //清空数据表
    public void clearDatabase() {
        if (!checkDatabase()) {
            return;
        }
        String clear = "delete from " + tableName;
        database.execSQL(clear);
        String zero = "delete from sqlite_sequence where NAME = '" + tableName + "'";
        database.execSQL(zero);

        version++;
    }

    //插入实体数据
    public long insertData(Entity entity) {
        if (!checkDatabase()) {
            return -1;
        }
        version++;
        return database.insert(tableName, null, entity.getContent());
    }

    public boolean operationInTransaction(TransactionCallback callback) {
        if (!checkDatabase()) {
            return false;
        }
        boolean flag = false;
        database.beginTransaction();
        try {
            int total =  callback.operation();
            database.setTransactionSuccessful();
            callback.success(total);
            flag = true;
            version++;
        } catch (Exception e) {
            callback.error(e);
        } finally {
            database.endTransaction();
        }
        return flag;
    }

    //插入实体数据
    public boolean insertData(final List<Entity> entities) {
        return operationInTransaction(new TransactionCallback() {
            @Override
            public int operation() throws Exception {
                for (Entity entity : entities) {
                    database.insert(tableName, null, entity.getContent());
                }
                return entities.size();
            }

            @Override
            public void success(int total) {

            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.filterException(e);
            }
        });
    }

    //插入实体数据
    public boolean insertData(final List<Entity> entities, final InsertCallback callback) {
        return operationInTransaction(new TransactionCallback() {
            @Override
            public int operation() throws Exception {
                int index = 0;
                for (Entity entity : entities) {
                    database.insert(tableName, null, entity.getContent());
                    index++;
                    callback.proceeding(index);
                }
                return index;
            }

            @Override
            public void success(int total) {
                callback.success(total);
            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.filterException(e);
            }
        });
    }

    //删除数据
    public int deleteById(long id) {
        if (!checkDatabase()) {
            return 0;
        }
        int res = database.delete(tableName, "ID = ?", new String[]{String.valueOf(id)});

        version++;
        return res;
    }

    //删除数据
    public boolean deleteByIds(final List<Long> ids) {
        return operationInTransaction(new TransactionCallback() {
            @Override
            public int operation() throws Exception {
                for (Long id : ids) {
                    database.delete(tableName, "ID = ?", new String[]{String.valueOf(id)});
                }
                return ids.size();
            }

            @Override
            public void success(int total) {

            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.filterException(e);
            }
        });
    }

    //删除数据
    public boolean deleteByIds(final List<Long> ids, final DeleteCallback callback) {
        return operationInTransaction(new TransactionCallback() {
            @Override
            public int operation() throws Exception {
                int index = 0;
                for (Long id : ids) {
                    database.delete(tableName, "ID = ?", new String[]{String.valueOf(id)});
                    index++;
                    callback.proceeding(index);
                }
                return index;
            }

            @Override
            public void success(int total) {
                callback.success(total);
            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.filterException(e);
            }
        });
    }

    //删除数据
    public void deleteByCloudId(String cloudId) {
        if (!checkDatabase()) {
            return;
        }
        database.delete(tableName, "CLOUD_ID = ?", new String[]{cloudId});

        version++;
    }

    //更新数据 updateData(id, "KEY1=Value1", "KEY2=Value2")
    public void updateData(long id, String... strings) {
        if (!checkDatabase()) {
            return;
        }
        String content;
        if (strings.length > 0) {
            content = "set ";
            int i = 0;
            content += strings[i] + " ";
            for (i++; i < strings.length; ++i) {
                content += ", " + strings[i] + " ";
            }
        } else {
            content = "";
        }
        String update = "update " + tableName + " " + content + " where ID = " + id;
        database.execSQL(update);

        version++;
    }

    //更新数据 updateData(cloudId, "KEY1=Value1", "KEY2=Value2")
    public void updateData(String cloudId, String... strings) {
        if (!checkDatabase()) {
            return;
        }
        String content;
        if (strings.length > 0) {
            content = "set ";
            int i = 0;
            content += strings[i] + " ";
            for (i++; i < strings.length; ++i) {
                content += ", " + strings[i] + " ";
            }
        } else {
            content = "";
        }
        String update = "update " + tableName + " " + content + " where CLOUD_ID = '" + cloudId + "'";
        database.execSQL(update);

        version++;
    }

    protected List<Entity> convertToEntities(Cursor cursor) {
        if (!checkDatabase()) {
            return null;
        }
        List<Entity> entities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                entities.add((Entity) entityTemplate.convertToEntity(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entities;
    }

    private Entity convertToEntity(Cursor cursor) {
        if (!checkDatabase()) {
            return null;
        }
        if (cursor.moveToFirst()) {
            return (Entity) entityTemplate.convertToEntity(cursor);
        } else {
            return null;
        }
    }

    //查询全部数据
    public List<Entity> selectAll() {
        if (!checkDatabase()) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from " + tableName, null);
        return convertToEntities(cursor);
    }

    //查询全部数据并排序, ascFlag为true升序，为false降序
    public List<Entity> selectAll(String order, boolean ascFlag) {
        if (!checkDatabase()) {
            return null;
        }
        String asc;
        if (ascFlag) {
            asc = "asc";
        } else {
            asc = "desc";
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " order by " + order + " " + asc, null);
        return convertToEntities(cursor);
    }

    //通过ID查询数据，需要ID字段
    public Entity selectById(long id) {
        if (!checkDatabase()) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " where ID=" + id, null);
        return convertToEntity(cursor);
    }

    //通过云ID查询数据，需要CLOUD_ID字段
    public Entity selectByCloudId(String cloudId) {
        if (!checkDatabase()) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from " + tableName
                + " where CLOUD_ID='" + cloudId + "'", null);
        return convertToEntity(cursor);
    }

    //查询值在范围内的数据
    public List<Entity> selectAmount(String data, String min, String max) {
        if (!checkDatabase()) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " where " + data +
                " between " + min + " and " + max, null);
        return convertToEntities(cursor);
    }

    //查询最新数据 selectLatest("TIME", true为升序/false为降序, "KEY1=Value1", "KEY2!=Value2")
    public Entity selectLatest(String order, boolean ascFlag, String... strings) {
        if (!checkDatabase()) {
            return null;
        }
        String content;
        if (strings.length > 0) {
            content = "where ";
            int i = 0;
            content += strings[i] + " ";
            for (i++; i < strings.length; ++i) {
                content += "and " + strings[i] + " ";
            }
        } else {
            content = "";
        }
        String asc;
        if (ascFlag) {
            asc = "asc";
        } else {
            asc = "desc";
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " " + content + " order by " + order + " " + asc + " limit " + 1, null);
        return convertToEntity(cursor);
    }

    //根据条件查询数据 selectByCondition("TIME", true为升序/false为降序, "KEY1=Value1", "KEY2!=Value2")
    public List<Entity> selectByCondition(String order, boolean ascFlag, String... strings) {
        if (!checkDatabase()) {
            return null;
        }
        String asc;
        if (ascFlag) {
            asc = "asc";
        } else {
            asc = "desc";
        }
        String content;
        if (strings.length > 0) {
            content = "where ";
            int i = 0;
            content += strings[i] + " ";
            for (i++; i < strings.length; ++i) {
                content += "and " + strings[i] + " ";
            }
        } else {
            content = "";
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " " + content + " order by " + order + " " + asc, null);
        return convertToEntities(cursor);
    }

    //获取数据总条数
    public int getCount() {
        Cursor cursor = database.rawQuery("select COUNT(*) from " + tableName, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}