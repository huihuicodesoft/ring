package cn.com.wh.ring;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import cn.com.wh.ring.database.DaoMaster;
import cn.com.wh.ring.database.DaoSession;

/**
 * Created by Hui on 2017/7/14.
 */

public class MainApplication extends Application {
    public static final int DB_VERSION = 2;
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    private static MainApplication mainApplication;

    public static MainApplication getInstance() {
        return mainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = MainApplication.this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "ring-db-encrypted" : "ring-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
    
}
