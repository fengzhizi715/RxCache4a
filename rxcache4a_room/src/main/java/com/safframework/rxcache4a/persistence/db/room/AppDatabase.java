package com.safframework.rxcache4a.persistence.db.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.room.AppDatabase
 * @author: Tony Shen
 * @date: 2018-10-15 16:40
 * @version: V1.0 <描述当前版本功能>
 */
@Database(entities = {CacheEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CacheEntityDao cacheEntityDao();
}
