package com.safframework.rxcache4a.persistence.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache4a.persistence.db.room.RoomImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

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
    public void testObject() {

        RoomImpl impl = new RoomImpl(appContext);

        RxCache.config(new RxCache.Builder().persistence(impl));

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u);

        Record<User> record = rxCache.get("test", User.class);

        assertEquals(u.name, record.getData().name);
        assertEquals(u.password, record.getData().password);
    }
}
