package com.external.smswizard.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import roboguice.util.Ln;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "smswizard.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Message, String> simpleDao;
    private RuntimeExceptionDao<Message, String> simpleRuntimeDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public Dao<Message, String> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(Message.class);
        }
        return simpleDao;
    }

    public RuntimeExceptionDao<Message, String> getSimpleDataDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(Message.class);
        }
        return simpleRuntimeDao;
    }

    //method for list of person
    public List<Message> GetData() {
        DatabaseHelper helper = new DatabaseHelper(context);
        RuntimeExceptionDao<Message, String> simpleDao = helper.getSimpleDataDao();
        List<Message> list = simpleDao.queryForAll();
        return list;
    }

    //method for insert data
    public int addData(Message message) {
        RuntimeExceptionDao<Message, String> dao = getSimpleDataDao();
        return dao.create(message);
    }

    //method for delete all rows
    public void deleteAll() {
        RuntimeExceptionDao<Message, String> dao = getSimpleDataDao();
        List<Message> list = dao.queryForAll();
        dao.delete(list);
    }

    @Override
    public void close() {
        super.close();
        simpleRuntimeDao = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Ln.i(DatabaseHelper.class.getName());
            TableUtils.createTable(connectionSource, Message.class);
        } catch (SQLException e) {
            Ln.e(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Ln.i(DatabaseHelper.class.getName());
            TableUtils.dropTable(connectionSource, Message.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}