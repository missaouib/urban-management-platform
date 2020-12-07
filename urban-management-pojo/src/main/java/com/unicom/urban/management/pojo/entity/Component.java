package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "update component set deleted = " + Delete.DELETE + " where id = ?")
@Where(clause = "deleted = " + Delete.NORMAL)
public class Component extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventType eventType;

    @OneToOne(cascade = CascadeType.ALL)
    private ComponentInfo componentInfo;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer sts;

    @ManyToOne(fetch = FetchType.LAZY)
    private Publish publish;

    @OneToOne
    private Record record;


}
