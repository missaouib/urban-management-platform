package com.unicom.urban.management.pojo.entity.notice;

import com.unicom.urban.management.pojo.Delete;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 通知公告类型实体
 *
 * @author liukai
 */
@Entity
@Table(name = "message_notice_type")
public class NoticeType {

    private String id;

    private String name;

    private String deleted = Delete.NORMAL;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
