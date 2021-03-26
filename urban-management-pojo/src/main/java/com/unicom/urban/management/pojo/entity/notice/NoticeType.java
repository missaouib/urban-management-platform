package com.unicom.urban.management.pojo.entity.notice;

import com.unicom.urban.management.pojo.Delete;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    private List<Notice> noticeList;

    private String deleted = Delete.NORMAL;

    /**
     * 判断此类别下是否有通知公告
     */
    @Transient
    public boolean hasNotice() {
        return CollectionUtils.isNotEmpty(this.noticeList);
    }

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

    @JoinColumn
    @OneToMany(fetch = FetchType.LAZY)
    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }
}
