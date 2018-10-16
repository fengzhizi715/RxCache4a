package com.safframework.rxcache4a.persistence.db.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.greendao.CacheEntity
 * @author: Tony Shen
 * @date: 2018-10-15 01:12
 * @version: V1.0 <描述当前版本功能>
 */
@Entity
public class CacheEntity {

    @Id(autoincrement = true)
    private Long id;

    public String key;

    public String data;// 对象转换的 json 字符串

    public Long timestamp;

    public Long expireTime;

    @Generated(hash = 67720095)
    public CacheEntity(Long id, String key, String data, Long timestamp,
            Long expireTime) {
        this.id = id;
        this.key = key;
        this.data = data;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
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

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}
