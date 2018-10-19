package com.safframework.rxcache4a.persistence.db.objectbox;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.objectbox.BoxStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @FileName: com.safframework.rxcache4a.ObjectBoxImplTest
 * @author: Tony Shen
 * @date: 2018-10-15 18:51
 * @version: V1.0 <描述当前版本功能>
 */
@RunWith(AndroidJUnit4.class)
public class ObjectBoxImplTest {

    Context appContext;
    BoxStore boxStore;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getTargetContext();
        boxStore = MyObjectBox.builder().androidContext(appContext).build();

    }

    @Test
    public void testWithObject() {


        ObjectBoxImpl impl = new ObjectBoxImpl(boxStore);
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
}
