package cn.com.wh.ring.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import cn.com.wh.ring.database.DaoMaster;
import cn.com.wh.ring.database.dao.PostPublishDao;

/**
 * Created by Hui on 2017/8/17.
 */

public class SQLiteOpenHelper extends DaoMaster.OpenHelper {
    public SQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //数据迁移模块
        MigrationHelper.getInstance().migrate(db, PostPublishDao.class);
    }
}