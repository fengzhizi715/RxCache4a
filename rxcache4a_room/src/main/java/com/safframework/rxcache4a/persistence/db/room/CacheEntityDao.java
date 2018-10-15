package com.safframework.rxcache4a.persistence.db.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.room.CacheEntityDao
 * @author: Tony Shen
 * @date: 2018-10-15 16:44
 * @version: V1.0 <描述当前版本功能>
 */
@Dao
public interface CacheEntityDao {

    @Query("SELECT * FROM cacheentity")
    List<CacheEntity> getAll();

    @Query("SELECT * FROM cacheentity WHERE `key` = :key LIMIT 0,1")
    CacheEntity findByKey(String key);

    @Insert(onConflict = IGNORE)
    void insert(CacheEntity entity);

    @Delete
    void delete(CacheEntity entity);

    @Query("DELETE FROM cacheentity")
    void deleteAll();
}
