package com.safframework.rxcache4a.persistence.db.greendao;

import android.content.Context;

/**
 * Created by Tony Shen on 2017/7/25.
 */

public class DBService {

    private static final String DB_NAME = "cache.db";
    private static volatile DBService defaultInstance;
    private DaoSession daoSession;

    private DBService(Context context) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());

        daoSession = daoMaster.newSession();
    }

    public static DBService getInstance(Context context) {
        if (defaultInstance == null) {
            synchronized (DBService.class) {
                if (defaultInstance == null) {
                    defaultInstance = new DBService(context.getApplicationContext());
                }
            }
        }
        return defaultInstance;
    }

    public CacheEntityDao getCacheEntityDao(){
        return daoSession.getCacheEntityDao();
    }

}
