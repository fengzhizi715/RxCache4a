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
    lateinit var handler: Handler

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getTargetContext()
        Looper.prepare()
        handler = Handler()
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

        val livedata1 = rxCache.toLiveData<User>().get("user", User::class.java)

        handler.post{

            val u2 = livedata1.blockingObserve()!!

            assertEquals(u.name, u2.name)
        }

        val livedata2 = rxCache.toLiveData<Address>().save("address",address)

        handler.post{

            val address2 = livedata2.blockingObserve()!!

            assertEquals(address.city, address2.city)
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