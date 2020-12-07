package com.unicom.urban.management.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 惯用语实体类
 *
 * @author liubozhi
 */
@Setter
@Getter
@Entity
@Table(name = "idioms")
public class Idioms {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 惯用语内容
     */
    private String idiomsValue;

}
