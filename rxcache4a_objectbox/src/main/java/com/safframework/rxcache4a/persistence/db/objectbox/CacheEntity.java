package com.safframework.rxcache4a.persistence.db.objectbox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @FileName: com.safframework.rxcache4a.persistence.db.objectbox.CacheEntity
 * @author: Tony Shen
 * @date: 2018-10-19 08:54
 * @version: V1.0 <描述当前版本功能>
 */
@Entity
public class CacheEntity {

    @Id
    public long id;

    public String key;

    public String data;// 对象转换的 json 字符串

    public Long timestamp;

    public Long expireTime;
}
