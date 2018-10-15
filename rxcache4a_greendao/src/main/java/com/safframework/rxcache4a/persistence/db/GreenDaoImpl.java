package com.safframework.rxcache4a.persistence.db;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import com.safframework.rxcache.persistence.db.DB;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.GreenDaoImpl
 * @author: Tony Shen
 * @date: 2018-10-15 11:50
 * @version: V1.0 <描述当前版本功能>
 */
public class GreenDaoImpl implements DB {

    private CacheEntityDao dao;
    private Converter converter;

    public GreenDaoImpl(CacheEntityDao dao) {

        this.dao = dao;
        this.converter = new GsonConverter();
    }

    public GreenDaoImpl(CacheEntityDao dao, Converter converter) {

        this.dao = dao;
        this.converter = converter;
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {
        return null;
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
        return keys.contains(key);
    }

    @Override
    public void evict(String key) {

        List<CacheEntity> list = dao.loadAll();

        for (CacheEntity entity:list) {

            if (key.equals(entity.key)) {

                dao.delete(entity);
                break;
            }
        }
    }

    @Override
    public void evictAll() {

        dao.deleteAll();
    }
}
