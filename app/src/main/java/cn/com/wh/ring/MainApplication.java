package cn.com.wh.ring;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import cn.com.wh.ring.database.DaoMaster;
import cn.com.wh.ring.database.DaoSession;

/**
 * Created by Hui on 2017/7/14.
 */

public class MainApplication extends Application {
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
