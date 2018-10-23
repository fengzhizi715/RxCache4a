package com.safframework.rxcache4a.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.safframework.rxcache.RxCache
import com.safframework.rxcache.config.Constant
import com.safframework.rxcache.domain.Record
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.rxcache4a.livedata.LiveDataCache.kt
 * @author: Tony Shen
 * @date: 2018-10-23 17:29
 * @version: V1.0 <描述当前版本功能>
 */
class LiveDataCache(private val cache: RxCache) {

    fun get(key: String,type:Type): LiveData<Record<Any>> {

        val liveData = MutableLiveData<Record<Any>>()

        liveData.postValue(cache.get(key,type))

        return liveData
    }

    fun save(key: String, value: Any): LiveData<Unit> = save(key,value, Constant.NEVER_EXPIRE)

    fun save(key: String, value: Any, expireTime: Long): LiveData<Unit> {

        val liveData = MutableLiveData<Unit>()

        liveData.postValue(cache.save(key, value, expireTime))

        return liveData
    }

    fun remove(key: String): LiveData<Unit> {

        val liveData = MutableLiveData<Unit>()

        liveData.postValue(cache.remove(key))

        return liveData
    }
}

fun RxCache.toLiveData() = LiveDataCache(this)