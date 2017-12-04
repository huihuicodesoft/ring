package cn.com.wh.ring.cache;

import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.database.bean.UserInfo;
import cn.com.wh.ring.database.dao.UserInfoDao;

/**
 * Created by Hui on 2017/12/4.
 */

public class UserCache {
    private static UserInfo userInfo;

    /**
     * 支持多线程
     * @return
     */
    public static UserInfo getUserInfo() {
        if (userInfo == null) {
            UserInfoDao userInfoDao = MainApplication.getInstance().getDaoSession().getUserInfoDao();
            userInfo = userInfoDao.queryBuilder().build().forCurrentThread().unique();
        }
        return userInfo;
    }
}
