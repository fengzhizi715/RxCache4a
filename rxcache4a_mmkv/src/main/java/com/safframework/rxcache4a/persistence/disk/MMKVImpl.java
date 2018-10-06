package com.safframework.rxcache4a.persistence.disk;

import com.google.gson.Gson;
import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.persistence.disk.Disk;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @FileName: com.safframework.rxcache4a.persistence.disk.MMKVImpl
 * @author: Tony Shen
 * @date: 2018-10-06 10:37
 * @version: V1.0 <描述当前版本功能>
 */
public class MMKVImpl implements Disk {

    private MMKV kv = null;
    private HashMap<String, Long> timestampMap;
    private HashMap<String, Long> expireTimeMap;
    private Gson gson;

    public MMKVImpl() {

        this.kv = MMKV.defaultMMKV();
        this.timestampMap = new HashMap<>();
        this.expireTimeMap = new HashMap<>();
        this.gson = new Gson();
    }

    public MMKVImpl(String mmkvID) {

        this.kv = MMKV.mmkvWithID(mmkvID);
        this.timestampMap = new HashMap<>();
        this.expireTimeMap = new HashMap<>();
        this.gson = new Gson();
    }

    @Override
    public int storedMB() {
        double megabytes = Math.ceil((double) kv.totalSize() / 1024 / 1024);
        return (int) megabytes;
    }

    @Override
    public <T> CacheHolder<T> retrieve(String key, Type type) {

        CacheHolder<T> result = null;

        if (expireTimeMap.get(key) < 0) { // 缓存的数据从不过期

            result = get(key, type);
        } else {

            if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                result = get(key, type);
            } else {                     // 缓存的数据已经过期

                evict(key);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> CacheHolder<T> get(String key, Type type) {
        if (type.equals(String.class)) {
            String result = kv.getString(key, "");
            return (CacheHolder<T>) new CacheHolder<>(result, timestampMap.get(key), expireTimeMap.get(key));
        } else if (type.equals(Integer.class)) {
            Integer result = kv.getInt(key, 0);
            return (CacheHolder<T>) new CacheHolder<>(result, timestampMap.get(key), expireTimeMap.get(key));
        } else if (type.equals(Boolean.class)) {
            Boolean result = kv.getBoolean(key, false);
            return (CacheHolder<T>) new CacheHolder<>(result, timestampMap.get(key), expireTimeMap.get(key));
        } else if (type.equals(Long.class)) {
            Long result = kv.getLong(key, 0);
            return (CacheHolder<T>) new CacheHolder<>(result, timestampMap.get(key), expireTimeMap.get(key));
        } else if (type.equals(Float.class)) {
            Float result = kv.getFloat(key, 0);
            return (CacheHolder<T>) new CacheHolder<>(result, timestampMap.get(key), expireTimeMap.get(key));
        } else {
            String json = kv.decodeString(key);
            T result = gson.fromJson(json, type);
            return result != null ? new CacheHolder<>(result, timestampMap.get(key), expireTimeMap.get(key)) : null;

        }
    }

    @Override
    public <T> void save(String key, T value) {
        save(key, value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {
        if (value == null) {
            return;
        }
        Class<?> clazz = value.getClass();
        if (clazz.equals(String.class)) {
            kv.encode(key, (String) value);
        } else if (clazz.equals(Integer.class)) {
            kv.encode(key, (Integer) value);
        } else if (clazz.equals(Boolean.class)) {
            kv.encode(key, (Boolean) value);
        } else if (clazz.equals(Long.class)) {
            kv.encode(key, (Long) value);
        } else if (clazz.equals(Float.class)) {
            kv.encode(key, (Float) value);
            //Set<String>用gson序列化
        } else {
            kv.encode(key, gson.toJson(value));
        }
        timestampMap.put(key, System.currentTimeMillis());
        expireTimeMap.put(key, expireTime);
    }

    @Override
    public List<String> allKeys() {

        return Arrays.asList(kv.allKeys());
    }

    @Override
    public boolean containsKey(String key) {

        return kv.containsKey(key);
    }

    @Override
    public void evict(String key) {

        kv.removeValueForKey(key);
    }

    @Override
    public void evictAll() {

        kv.clearAll();
    }
}
