package com.unicom.urban.management.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 顾志杰
 * @date 2020/10/14-18:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class KV {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String tableName;

    private String fieldName;

    private String value;

    private Integer sts;
}