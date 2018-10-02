package com.safframework.rxcache4a.memory.impl;

import android.support.v4.util.LruCache;

import com.safframework.rxcache.domain.CacheHolder;
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

        cache = new LruCache<String, Object>(maxSize);
    }

    @Override
    public <T> CacheHolder<T> getIfPresent(String key) {

        T result = (T) cache.get(key);
        return result != null ? new CacheHolder<>(result, timestampMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {

        cache.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
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
    }

    @Override
    public void evictAll() {

        cache.evictAll();
    }
}
