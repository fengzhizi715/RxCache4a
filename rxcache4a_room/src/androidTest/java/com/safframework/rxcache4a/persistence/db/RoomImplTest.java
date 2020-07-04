package com.safframework.rxcache4a.persistence.db;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache4a.persistence.db.room.RoomImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.RoomImplTest
 * @author: Tony Shen
 * @date: 2018-10-15 19:31
 * @version: V1.0 <描述当前版本功能>
 */
@RunWith(AndroidJUnit4.class)
public class RoomImplTest {

    Context appContext;


    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testWithObject() {

        RoomImpl impl = new RoomImpl(appContext);
        impl.evictAll();

        RxCache.config(new RxCache.Builder().persistence(impl));

        RxCache rxCache = RxCache.getRxCache();

        Address address = new Address();
        address.province = "Jiangsu";
        address.city = "Suzhou";
        address.area = "Gusu";
        address.street = "ren ming road";

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        u.address = address;

        rxCache.save("user",u);

        Record<User> record = rxCache.get("user", User.class);

        assertEquals(u.name, record.getData().name);
        assertEquals(u.password, record.getData().password);
        assertEquals(address.city, record.getData().address.city);

        rxCache.save("address",address);

        Record<Address> record2 = rxCache.get("address", Address.class);
        assertEquals(address.city, record2.getData().city);
    }

    @Test
    public void testWithExpireTime() {

        RoomImpl impl = new RoomImpl(appContext);
        impl.evictAll();

        RxCache.config(new RxCache.Builder().persistence(impl));

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u,2000);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Record<User> record = rxCache.get("test", User.class);

        assertNull(record);
    }
}
