package com.safframework.rxcache4a.persistence.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

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

}
