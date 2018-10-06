package com.safframework.rxcache4a.persistence;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.persistence.disk.Disk;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @FileName: com.safframework.rxcache4a.persistence.MMKVImpl
 * @author: Tony Shen
 * @date: 2018-10-06 10:37
 * @version: V1.0 <描述当前版本功能>
 */
public class MMKVImpl implements Disk {

    private String rootDir;
    private MMKV kv = null;

    public MMKVImpl(String rootDir) {

        this.rootDir = rootDir;
        this.kv = MMKV.defaultMMKV();
    }

    public MMKVImpl(String rootDir,String mmkvID) {

        this.rootDir = rootDir;
        this.kv = MMKV.mmkvWithID(mmkvID);
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



        return null;
    }

    @Override
    public <T> void save(String key, T value) {

        save(key,value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {

        
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

        kv.remove(key);
    }

    @Override
    public void evictAll() {

        kv.clearAll();
    }
}
