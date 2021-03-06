package cn.com.wh.ring.database;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

import cn.com.wh.ring.database.bean.Address;
import cn.com.wh.ring.database.bean.PostPublish;
import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.database.dao.AddressDao;
import cn.com.wh.ring.database.dao.PostPublishDao;
import cn.com.wh.ring.database.dao.UserInfoDao;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig postPublishDaoConfig;
    private final DaoConfig addressDaoConfig;
    private final DaoConfig userInfoDaoConfig;

    private final PostPublishDao postPublishDao;
    private final AddressDao addressDao;
    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        postPublishDaoConfig = daoConfigMap.get(PostPublishDao.class).clone();
        postPublishDaoConfig.initIdentityScope(type);
        addressDaoConfig = daoConfigMap.get(AddressDao.class).clone();
        addressDaoConfig.initIdentityScope(type);
        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        postPublishDao = new PostPublishDao(postPublishDaoConfig, this);
        addressDao = new AddressDao(addressDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(PostPublish.class, postPublishDao);
        registerDao(Address.class, addressDao);
        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        postPublishDaoConfig.clearIdentityScope();
        addressDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
    }

    public PostPublishDao getPostPublishDao() {
        return postPublishDao;
    }

    public AddressDao getAddressDao() {
        return addressDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }
}
