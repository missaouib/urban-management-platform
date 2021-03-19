package com.unicom.urban.management.pojo.entity.time;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 计时管理 日历记录实体类
 *
 * @author liukai
 */
@Entity
@Table(name = "time_calendar")
public class Calendar {

    private String id;

    private Type type;


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Enumerated(EnumType.ORDINAL)
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {

        WORKDAY("工作日"),
        NONWORKDAY("非工作日");

        Type(String type) {
            this.type = type;
        }

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }


}
