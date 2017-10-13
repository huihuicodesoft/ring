package cn.com.wh.ring.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.com.wh.ring.database.DaoSession;
import cn.com.wh.ring.database.bean.UserInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_INFO".
*/
public class UserInfoDao extends AbstractDao<UserInfo, Long> {

    public static final String TABLENAME = "USER_INFO";

    /**
     * Properties of entity UserInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, Long.class, "userId", false, "USER_ID");
        public final static Property InfoId = new Property(2, Long.class, "infoId", false, "INFO_ID");
        public final static Property Nickname = new Property(3, String.class, "nickname", false, "NICKNAME");
        public final static Property Birthday = new Property(4, Long.class, "birthday", false, "BIRTHDAY");
        public final static Property Sex = new Property(5, Integer.class, "sex", false, "SEX");
        public final static Property Avatar = new Property(6, String.class, "avatar", false, "AVATAR");
        public final static Property Signature = new Property(7, String.class, "signature", false, "SIGNATURE");
        public final static Property AddressCode = new Property(8, String.class, "addressCode", false, "ADDRESS_CODE");
        public final static Property LastModifiedTime = new Property(9, Long.class, "lastModifiedTime", false, "LAST_MODIFIED_TIME");
    }


    public UserInfoDao(DaoConfig config) {
        super(config);
    }

    public UserInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER UNIQUE ," + // 1: userId
                "\"INFO_ID\" INTEGER," + // 2: infoId
                "\"NICKNAME\" TEXT," + // 3: nickname
                "\"BIRTHDAY\" INTEGER," + // 4: birthday
                "\"SEX\" INTEGER," + // 5: sex
                "\"AVATAR\" TEXT," + // 6: avatar
                "\"SIGNATURE\" TEXT," + // 7: signature
                "\"ADDRESS_CODE\" TEXT," + // 8: addressCode
                "\"LAST_MODIFIED_TIME\" INTEGER);"); // 9: lastModifiedTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserInfo entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }

        Long infoId = entity.getInfoId();
        if (infoId != null) {
            stmt.bindLong(3, infoId);
        }

        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }

        Long birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindLong(5, birthday);
        }

        Integer sex = entity.getSex();
        if (sex != null) {
            stmt.bindLong(6, sex);
        }

        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(7, avatar);
        }

        String signature = entity.getSignature();
        if (signature != null) {
            stmt.bindString(8, signature);
        }

        String addressCode = entity.getAddressCode();
        if (addressCode != null) {
            stmt.bindString(9, addressCode);
        }

        Long lastModifiedTime = entity.getLastModifiedTime();
        if (lastModifiedTime != null) {
            stmt.bindLong(10, lastModifiedTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserInfo entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }

        Long infoId = entity.getInfoId();
        if (infoId != null) {
            stmt.bindLong(3, infoId);
        }

        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }

        Long birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindLong(5, birthday);
        }

        Integer sex = entity.getSex();
        if (sex != null) {
            stmt.bindLong(6, sex);
        }

        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(7, avatar);
        }

        String signature = entity.getSignature();
        if (signature != null) {
            stmt.bindString(8, signature);
        }

        String addressCode = entity.getAddressCode();
        if (addressCode != null) {
            stmt.bindString(9, addressCode);
        }

        Long lastModifiedTime = entity.getLastModifiedTime();
        if (lastModifiedTime != null) {
            stmt.bindLong(10, lastModifiedTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public UserInfo readEntity(Cursor cursor, int offset) {
        UserInfo entity = new UserInfo( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // userId
                cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // infoId
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nickname
                cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // birthday
                cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // sex
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // avatar
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // signature
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // addressCode
                cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9) // lastModifiedTime
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, UserInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setInfoId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setNickname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBirthday(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setSex(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setAvatar(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSignature(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAddressCode(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLastModifiedTime(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
    }

    @Override
    protected final Long updateKeyAfterInsert(UserInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(UserInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}