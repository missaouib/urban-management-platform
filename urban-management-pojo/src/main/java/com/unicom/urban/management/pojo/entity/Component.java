package com.unicom.urban.management.pojo.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 部件
 *
 * @author 顾志杰
 * @date 2020/10/13-18:59
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Component extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne
    private ComponentType componentType;

    @OneToOne(cascade = CascadeType.ALL)
    private ComponentInfo componentInfo;

    private Integer sts;

    @ManyToOne
    private Publish publish;


}
