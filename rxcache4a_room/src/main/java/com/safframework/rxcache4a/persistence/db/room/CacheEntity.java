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

    public String data;

    public Long timestamp;

    public Long expireTime;
}
