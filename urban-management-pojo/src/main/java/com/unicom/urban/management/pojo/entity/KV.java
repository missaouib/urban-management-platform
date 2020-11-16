package com.unicom.urban.management.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Kv实体类
 *
 * @author 顾志杰
 * @date 2020/10/14-18:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class KV implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String tableName;

    private String fieldName;

    @Column(name = "key_")
    private String key;

    private String value;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer sts;
}
