package cn.com.wh.ring.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.com.wh.ring.database.DaoSession;
import cn.com.wh.ring.database.bean.PostPublish;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "POST_PUBLISH".
*/
public class PostPublishDao extends AbstractDao<PostPublish, Long> {

    public static final String TABLENAME = "POST_PUBLISH";

    /**
     * Properties of entity PostPublish.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Token = new Property(1, String.class, "token", false, "TOKEN");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property MediaContent = new Property(3, String.class, "mediaContent", false, "MEDIA_CONTENT");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
        public final static Property Anonymous = new Property(5, Boolean.class, "anonymous", false, "ANONYMOUS");
        public final static Property State = new Property(6, Integer.class, "state", false, "STATE");
        public final static Property Region = new Property(7, String.class, "region", false, "REGION");
        public final static Property Lng = new Property(8, Double.class, "lng", false, "LNG");
        public final static Property Lat = new Property(9, Double.class, "lat", false, "LAT");
        public final static Property Time = new Property(10, long.class, "time", false, "TIME");
    }


    public PostPublishDao(DaoConfig config) {
        super(config);
    }
    
    public PostPublishDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"POST_PUBLISH\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TOKEN\" TEXT NOT NULL ," + // 1: token
                "\"CONTENT\" TEXT," + // 2: content
                "\"MEDIA_CONTENT\" TEXT," + // 3: mediaContent
                "\"TYPE\" TEXT NOT NULL ," + // 4: type
                "\"ANONYMOUS\" INTEGER," + // 5: anonymous
                "\"STATE\" INTEGER," + // 6: state
                "\"REGION\" TEXT," + // 7: region
                "\"LNG\" REAL," + // 8: lng
                "\"LAT\" REAL," + // 9: lat
                "\"TIME\" INTEGER NOT NULL );"); // 10: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"POST_PUBLISH\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PostPublish entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getToken());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String mediaContent = entity.getMediaContent();
        if (mediaContent != null) {
            stmt.bindString(4, mediaContent);
        }
        stmt.bindString(5, entity.getType());
 
        Boolean anonymous = entity.getAnonymous();
        if (anonymous != null) {
            stmt.bindLong(6, anonymous ? 1L: 0L);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(7, state);
        }
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(8, region);
        }
 
        Double lng = entity.getLng();
        if (lng != null) {
            stmt.bindDouble(9, lng);
        }
 
        Double lat = entity.getLat();
        if (lat != null) {
            stmt.bindDouble(10, lat);
        }
        stmt.bindLong(11, entity.getTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PostPublish entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getToken());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String mediaContent = entity.getMediaContent();
        if (mediaContent != null) {
            stmt.bindString(4, mediaContent);
        }
        stmt.bindString(5, entity.getType());
 
        Boolean anonymous = entity.getAnonymous();
        if (anonymous != null) {
            stmt.bindLong(6, anonymous ? 1L: 0L);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(7, state);
        }
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(8, region);
        }
 
        Double lng = entity.getLng();
        if (lng != null) {
            stmt.bindDouble(9, lng);
        }
 
        Double lat = entity.getLat();
        if (lat != null) {
            stmt.bindDouble(10, lat);
        }
        stmt.bindLong(11, entity.getTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PostPublish readEntity(Cursor cursor, int offset) {
        PostPublish entity = new PostPublish( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // token
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // mediaContent
            cursor.getString(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // anonymous
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // state
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // region
            cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8), // lng
            cursor.isNull(offset + 9) ? null : cursor.getDouble(offset + 9), // lat
            cursor.getLong(offset + 10) // time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PostPublish entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setToken(cursor.getString(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMediaContent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.getString(offset + 4));
        entity.setAnonymous(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setState(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setRegion(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLng(cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8));
        entity.setLat(cursor.isNull(offset + 9) ? null : cursor.getDouble(offset + 9));
        entity.setTime(cursor.getLong(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PostPublish entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PostPublish entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PostPublish entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
