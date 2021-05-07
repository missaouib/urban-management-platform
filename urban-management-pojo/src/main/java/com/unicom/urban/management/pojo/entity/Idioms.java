package com.unicom.urban.management.pojo.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 惯用语实体类
 *
 * @author liubozhi
 */
@Entity
@Table(name = "idioms")
public class Idioms {

    private String id;

    private User user;

    /**
     * 惯用语内容
     */
    private String idiomsValue;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIdiomsValue() {
        return idiomsValue;
    }

    public void setIdiomsValue(String idiomsValue) {
        this.idiomsValue = idiomsValue;
    }
}
