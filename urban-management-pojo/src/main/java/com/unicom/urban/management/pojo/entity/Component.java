package com.unicom.urban.management.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 部件
 *
 * @author 顾志杰
 * @date 2020/10/13-18:59
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Component extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String layerId;

    private String componentName;

    @ManyToOne
    private ComponentType componentType;

    @OneToOne
    private ComponentInfo componentInfo;

    private String url;

    private String sts;

}
