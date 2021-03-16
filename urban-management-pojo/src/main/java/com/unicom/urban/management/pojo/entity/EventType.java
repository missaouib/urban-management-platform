package com.unicom.urban.management.pojo.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 部件分类
 *
 * @author 顾志杰
 * @date 2020/10/13-19:02
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventType extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String code;

    private String level;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<EventType> children;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventType parent;

    private Integer type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Dept dept;
}
