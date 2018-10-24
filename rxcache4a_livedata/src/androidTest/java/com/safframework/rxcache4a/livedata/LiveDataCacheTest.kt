package com.safframework.rxcache4a.livedata

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.safframework.rxcache.RxCache
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 *
 * @FileName:
 *          com.safframework.rxcache4a.livedata.LiveDataCacheTest
 * @author: Tony Shen
 * @date: 2018-10-24 14:13
 * @version: V1.0 <描述当前版本功能>
 */
@RunWith(AndroidJUnit4::class)
class LiveDataCacheTest {

    lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun testWithObject() {

        RxCache.config(RxCache.Builder())

        val rxCache = RxCache.getRxCache()

        val address = Address()
        address.province = "Jiangsu"
        address.city = "Suzhou"
        address.area = "Gusu"
        address.street = "ren ming road"

        val u = User()
        u.name = "tony"
        u.password = "123456"
        u.address = address

        rxCache.save("user", u)

        val livedata = rxCache.toLiveData<User>().get("user", User::class.java)

        Looper.prepare()
        Handler().post{

            val u2 = livedata.blockingObserve()!!

            assertEquals(u.name, u2.name)
        }

    }

    private fun <T> LiveData<T>.blockingObserve(): T? {

        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

              (observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }

}