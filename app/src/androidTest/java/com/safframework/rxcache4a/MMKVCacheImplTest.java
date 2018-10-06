package com.safframework.rxcache4a;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache4a.demo.User;
import com.safframework.rxcache4a.persistence.disk.MMKVImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MMKVCacheImplTest {

    Context appContext;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

//        assertEquals("com.safframework.rxcache4a", appContext.getPackageName());
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testAll() {
        testObject();
        testString();
        testBoolean();
        testInt();
        testLong();
        testFloat();

    }

    @Test
    public void testObject() {
        MMKVImpl mmkv = new MMKVImpl();
        mmkv.evictAll();

        assertFalse(mmkv.containsKey("object"));

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        mmkv.save("object", u);

        assertTrue(mmkv.containsKey("object"));

        Record<User> record = mmkv.retrieve("object", User.class);
        assertEquals(u.name, record.getData().name);
        //清除所有数据
        mmkv.evictAll();
        assertFalse(mmkv.containsKey("object"));
    }

    @Test
    public void testString() {

        MMKVImpl mmkv = new MMKVImpl();
        mmkv.evictAll();

        //没有test数据
        assertFalse(mmkv.containsKey("string"));
        mmkv.save("string", "我是test的string！！！");

        //有test数据
        assertTrue(mmkv.containsKey("string"));

        Record<String> record= mmkv.retrieve("string", String.class);
        assertEquals("我是test的string！！！", record.getData());

        //清除test数据
        mmkv.evict("string");
        assertFalse(mmkv.containsKey("string"));

    }

    @Test
    public void testBoolean() {

        MMKVImpl mmkv = new MMKVImpl();
        mmkv.evictAll();

        //没有test数据
        assertFalse(mmkv.containsKey("bool"));
        mmkv.save("bool", true);

        //有test数据
        assertTrue(mmkv.containsKey("bool"));

        Record<Boolean> record = mmkv.retrieve("bool", Boolean.class);
        assertEquals(true, record.getData());

        //清除test数据
        mmkv.evict("bool");
        assertFalse(mmkv.containsKey("bool"));

    }

    @Test
    public void testInt() {

        MMKVImpl mmkv = new MMKVImpl();
        mmkv.evictAll();

        //没有test数据
        assertFalse(mmkv.containsKey("int"));
        mmkv.save("int", 0);

        //有test数据
        assertTrue(mmkv.containsKey("int"));

        Record<Integer> record = mmkv.retrieve("int", Integer.class);
        assertEquals((Integer) 0, record.getData());

        //清除test数据
        mmkv.evict("int");
        assertFalse(mmkv.containsKey("int"));

    }

    @Test
    public void testLong() {

        MMKVImpl mmkv = new MMKVImpl();
        mmkv.evictAll();

        //没有test数据
        assertFalse(mmkv.containsKey("long"));
        mmkv.save("long", 0L);

        //有test数据
        assertTrue(mmkv.containsKey("long"));

        Record<Long> record = mmkv.retrieve("long", Long.class);
        assertEquals((Long) 0L, record.getData());

        //清除test数据
        mmkv.evict("long");
        assertFalse(mmkv.containsKey("long"));

    }

    @Test
    public void testFloat() {

        MMKVImpl mmkv = new MMKVImpl();
        mmkv.evictAll();

        //没有test数据
        assertFalse(mmkv.containsKey("float"));
        mmkv.save("float", 1.0F);

        //有test数据
        assertTrue(mmkv.containsKey("float"));

        Record<Float> record = mmkv.retrieve("float", Float.class);
        assertEquals((Float) 1.0F, record.getData());

        //清除test数据
        mmkv.evict("float");
        assertFalse(mmkv.containsKey("float"));

    }
}
