package com.safframework.rxcache4a.persistence.db.greendao;

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

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.greendao.GreenDaoImpl
 * @author: Tony Shen
 * @date: 2018-10-15 11:50
 * @version: V1.0 <描述当前版本功能>
 */
public class GreenDaoImpl implements DB {

    private CacheEntityDao dao;
    private Converter converter;

    public GreenDaoImpl(CacheEntityDao dao) {

        this(dao,new GsonConverter());
    }

    public GreenDaoImpl(CacheEntityDao dao, Converter converter) {

        this.dao = dao;
        this.converter = converter;
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {

        CacheEntity entity = dao.queryBuilder().where(CacheEntityDao.Properties.Key.eq(key)).unique();

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

        CacheEntity entity = new CacheEntity();
        entity.setKey(key);
        entity.setTimestamp(System.currentTimeMillis());
        entity.setExpireTime(expireTime);
        entity.setData(converter.toJson(value));
    }

    @Override
    public List<String> allKeys() {

        List<CacheEntity> list = dao.loadAll();

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
