package com.safframework.rxcache4a.persistence.db.room;

import android.content.Context;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import com.safframework.rxcache.persistence.db.DB;
import com.safframework.tony.common.utils.Preconditions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.room.RoomImpl
 * @author: Tony Shen
 * @date: 2018-10-15 16:46
 * @version: V1.0 <描述当前版本功能>
 */
public class RoomImpl implements DB {

    private AppDatabase db;
    private Converter converter;
    private static final String DB_NAME = "cache";

    public RoomImpl(Context context) {

        this(context,new GsonConverter());
    }

    public RoomImpl(Context context, Converter converter) {

        this.db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
        this.converter = converter;
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {

        CacheEntity entity = db.cacheEntityDao().findByKey(key);

        if (entity==null) return null;

        long timestamp = entity.timestamp;
        long expireTime = entity.expireTime;
        T result = null;

        if (expireTime<0) { // 缓存的数据从不过期

            String json = entity.data;

            result = converter.fromJson(json,type);
        } else {

            if (timestamp + expireTime > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                String json = entity.data;

                result = converter.fromJson(json,type);
            } else {        // 缓存的数据已经过期

                evict(key);
            }
        }

        return result != null ? new Record<>(Source.PERSISTENCE, key, result, timestamp, expireTime) : null;
    }

    @Override
    public <T> void save(String key, T value) {

        save(key,value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {

        if (Preconditions.isNotBlanks(key,value)) {

            CacheEntity entity = new CacheEntity();
            entity.setKey(key);
            entity.setTimestamp(System.currentTimeMillis());
            entity.setExpireTime(expireTime);
            entity.setData(converter.toJson(value));
            db.cacheEntityDao().insert(entity);
        }
    }

    @Override
    public List<String> allKeys() {

        List<CacheEntity> list = db.cacheEntityDao().getAll();

        List<String> result = new ArrayList<>();

        for (CacheEntity entity:list) {

            result.add(entity.key);
        }

        return result;
    }

    @Override
    public boolean containsKey(String key) {

        List<String> keys = allKeys();

        return Preconditions.isNotBlank(keys) ? keys.contains(key) : false;
    }

    @Override
    public void evict(String key) {

        CacheEntity entity = db.cacheEntityDao().findByKey(key);

        if (entity!=null) {

            db.cacheEntityDao().delete(entity);
        }
    }

    @Override
    public void evictAll() {

        db.cacheEntityDao().deleteAll();
    }
}
