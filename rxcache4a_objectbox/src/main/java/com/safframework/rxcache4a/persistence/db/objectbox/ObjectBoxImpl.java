package com.safframework.rxcache4a.persistence.db.objectbox;

import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import com.safframework.rxcache.persistence.db.DB;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.objectbox.ObjectBoxImpl
 * @author: Tony Shen
 * @date: 2018-10-19 09:03
 * @version: V1.0 <描述当前版本功能>
 */
public class ObjectBoxImpl implements DB {

    private BoxStore boxStore;
    private Converter converter;
    private Box<CacheEntity> cacheEntityBox;

    public ObjectBoxImpl(BoxStore boxStore) {

        this(boxStore,new GsonConverter());
    }

    public ObjectBoxImpl(BoxStore boxStore, Converter converter) {

        this.boxStore = boxStore;
        this.converter = converter;
        this.cacheEntityBox= boxStore.boxFor(CacheEntity.class);
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {

        CacheEntity entity = cacheEntityBox.query().equal(CacheEntity_.key, key).build().findUnique();

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
    public String getStringData(String key) {

        CacheEntity entity = cacheEntityBox.query().equal(CacheEntity_.key, key).build().findUnique();

        if (entity==null) return null;

        long timestamp = entity.timestamp;
        long expireTime = entity.expireTime;

        String json = null;

        if (expireTime<0) { // 缓存的数据从不过期

            json = entity.data;
        } else {

            if (timestamp + expireTime > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                json = entity.data;
            } else {        // 缓存的数据已经过期

                evict(key);
            }
        }

        return json;
    }

    @Override
    public <T> void save(String key, T value) {

        save(key,value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {

        CacheEntity entity = new CacheEntity();
        entity.key = key;
        entity.timestamp = System.currentTimeMillis();
        entity.expireTime = expireTime;
        entity.data = converter.toJson(value);
        cacheEntityBox.put(entity);
    }

    @Override
    public Set<String> keySet() {

        List<CacheEntity> list = cacheEntityBox.getAll();

        Set<String> result = new HashSet<>();

        for (CacheEntity entity:list) {

            result.add(entity.key);
        }

        return result;
    }

    @Override
    public boolean containsKey(String key) {

        Set<String> keys = keySet();

        return Preconditions.isNotBlank(keys) ? keys.contains(key) : false;
    }

    @Override
    public void evict(String key) {

        CacheEntity entity = cacheEntityBox.query().equal(CacheEntity_.key, key).build().findUnique();

        if (entity!=null) {

            cacheEntityBox.remove(entity);
        }
    }

    @Override
    public void evictAll() {

        cacheEntityBox.removeAll();
    }
}
