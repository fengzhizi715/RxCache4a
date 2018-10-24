package com.safframework.rxcache4a.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.safframework.rxcache.RxCache
import com.safframework.rxcache.config.Constant
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.rxcache4a.livedata.LiveDataCache.kt
 * @author: Tony Shen
 * @date: 2018-10-23 17:29
 * @version: V1.0 <描述当前版本功能>
 */
class LiveDataCache<T>(private val cache: RxCache) {

    var mediatorLiveData: MediatorLiveData<T>

    init {

        mediatorLiveData = MediatorLiveData<T>()
    }

    fun get(key: String,type:Type): LiveData<T> {

        val liveData = MutableLiveData<T>()

        liveData.postValue(cache.get<T>(key,type).data)

        mediatorLiveData.addSource(liveData){

            mediatorLiveData.postValue(it)
        }

        return mediatorLiveData
    }

    fun save(key: String, value: T): LiveData<T> = save(key,value, Constant.NEVER_EXPIRE)

    fun save(key: String, value: T, expireTime: Long): LiveData<T> {

        val liveData = MutableLiveData<T>()

        cache.save(key, value, expireTime)

        liveData.postValue(value)

        mediatorLiveData.addSource(liveData){

            mediatorLiveData.postValue(it)
        }

        return mediatorLiveData
    }

    fun remove(key: String): LiveData<T> {

        val liveData = MutableLiveData<T>()

        cache.remove(key)

        mediatorLiveData.addSource(liveData){
            mediatorLiveData.value = null
        }

        return mediatorLiveData
    }
}

fun <T> RxCache.toLiveData() = LiveDataCache<T>(this)