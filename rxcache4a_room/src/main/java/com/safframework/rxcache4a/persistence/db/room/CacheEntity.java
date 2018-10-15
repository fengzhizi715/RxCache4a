package com.safframework.rxcache4a.persistence.db.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.room.CacheEntity
 * @author: Tony Shen
 * @date: 2018-10-15 16:26
 * @version: V1.0 <描述当前版本功能>
 */
@Entity
public class CacheEntity {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    public String key;

    public String data;// 对象转换的 json 字符串

    public Long timestamp;

    public Long expireTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}
