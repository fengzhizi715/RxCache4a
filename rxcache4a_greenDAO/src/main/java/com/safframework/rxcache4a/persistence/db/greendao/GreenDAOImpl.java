package com.safframework.rxcache4a.persistence.db.greendao;

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

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.greendao.GreenDAOImpl
 * @author: Tony Shen
 * @date: 2018-10-15 11:50
 * @version: V1.0 <描述当前版本功能>
 */
public class GreenDAOImpl implements DB {

    private CacheEntityDao dao;
    private Converter converter;

    public GreenDAOImpl(CacheEntityDao dao) {

        this(dao,new GsonConverter());
    }

    public GreenDAOImpl(CacheEntityDao dao, Converter converter) {

        this.dao = dao;
        this.converter = converter;
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {

        CacheEntity entity = dao.queryBuilder().where(CacheEntityDao.Properties.Key.eq(key)).unique();

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

        CacheEntity entity = dao.queryBuilder().where(CacheEntityDao.Properties.Key.eq(key)).unique();

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
        entity.setKey(key);
        entity.setTimestamp(System.currentTimeMillis());
        entity.setExpireTime(expireTime);
        entity.setData(converter.toJson(value));
        dao.save(entity);
    }

    @Override
    public Set<String> keySet() {

        List<CacheEntity> list = dao.loadAll();

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

        CacheEntity entity = dao.queryBuilder().where(CacheEntityDao.Properties.Key.eq(key)).unique();

        if (entity!=null) {

            dao.delete(entity);
        }

    }

    @Override
    public void evictAll() {

        dao.deleteAll();
    }
}
