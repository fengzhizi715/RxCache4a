package com.safframework.rxcache4a.memory.impl;

import android.support.v4.util.LruCache;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;

import java.util.Set;

/**
 * @FileName: com.safframework.rxcache4a.memory.impl.LRUCacheImpl
 * @author: Tony Shen
 * @date: 2018-10-03 00:17
 * @version: V1.0 <描述当前版本功能>
 */
public class LRUCacheImpl extends AbstractMemoryImpl {

    private LruCache<String, Object> cache;

    public LRUCacheImpl(int maxSize) {

        super(maxSize);

        int memCacheSize = (int) (Runtime.getRuntime().maxMemory() / (1024*8) );
        cache = new LruCache<String, Object>(memCacheSize);
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {

        T result = null;

        if (expireTimeMap.get(key)<0) { // 缓存的数据从不过期

            result = (T) cache.get(key);
        } else {

            if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                result = (T) cache.get(key);
            } else {                     // 缓存的数据已经过期

                evict(key);
            }
        }

        return result != null ? new Record<>(Source.MEMORY, key, result, timestampMap.get(key), expireTimeMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {

        put(key, value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {

        cache.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
        expireTimeMap.put(key,expireTime);
    }

    @Override
    public Set<String> keySet() {

        return cache.snapshot().keySet();
    }

    @Override
    public boolean containsKey(String key) {

        return cache.snapshot().containsKey(key);
    }

    @Override
    public void evict(String key) {

        cache.remove(key);

        timestampMap.remove(key);
        expireTimeMap.remove(key);
    }

    @Override
    public void evictAll() {

        cache.evictAll();

        timestampMap.clear();
        expireTimeMap.clear();
    }
}
