package com.safframework.rxcache4a.persistence.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.CacheEntity
 * @author: Tony Shen
 * @date: 2018-10-15 01:12
 * @version: V1.0 <描述当前版本功能>
 */
@Entity
public class CacheEntity {

    @Id(autoincrement = true)
    private Long id;

    public String key;

    public String value;

    @Generated(hash = 1506971430)
    public CacheEntity(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    @Generated(hash = 1391258017)
    public CacheEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
