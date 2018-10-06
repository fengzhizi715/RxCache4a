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
import java.util.List;

/**
 * @FileName: com.safframework.rxcache4a.persistence.disk.MMKVImpl
 * @author: Tony Shen
 * @date: 2018-10-06 10:37
 * @version: V1.0 <描述当前版本功能>
 */
public class MMKVImpl implements Disk {

    private String rootDir;
    private MMKV kv = null;
    private HashMap<String, Long> timestampMap;
    private HashMap<String, Long> expireTimeMap;
    private Gson gson;

    public MMKVImpl(String rootDir) {

        this.rootDir = rootDir;
        this.kv = MMKV.defaultMMKV();
        this.timestampMap = new HashMap<>();
        this.expireTimeMap = new HashMap<>();
        this.gson = new Gson();
    }

    public MMKVImpl(String rootDir,String mmkvID) {

        this.rootDir = rootDir;
        this.kv = MMKV.mmkvWithID(mmkvID);
        this.timestampMap = new HashMap<>();
        this.expireTimeMap = new HashMap<>();
        this.gson = new Gson();
    }

    @Override
    public int storedMB() {

        long bytes = 0;

        File cacheDirectory = new File(rootDir);

        final File[] files = cacheDirectory.listFiles();
        if (files == null) return 0;

        for (File file : files) {
            bytes += file.length();
        }

        double megabytes = Math.ceil((double) bytes / 1024 / 1024);
        return (int) megabytes;
    }

    @Override
    public <T> CacheHolder<T> retrieve(String key, Type type) {

        T result = null;

        if (expireTimeMap.get(key)<0) { // 缓存的数据从不过期

            String json = kv.decodeString(key);
            result = gson.fromJson(json, type);
        } else {

            if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                String json = kv.decodeString(key);
                result = gson.fromJson(json, type);
            } else {                     // 缓存的数据已经过期

                evict(key);
            }
        }

        return result != null ? new CacheHolder<>(result, timestampMap.get(key), expireTimeMap.get(key)) : null;
    }

    @Override
    public <T> void save(String key, T value) {

        save(key,value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {

        kv.encode(key,gson.toJson(value));
        timestampMap.put(key,System.currentTimeMillis());
        expireTimeMap.put(key,expireTime);
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
