package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.Delete;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部件分类
 *
 * @author 顾志杰
 * @date 2020/10/13-19:02
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventType {

    private String id;

    private String name;

    /**
     *
     */
    private String fullName;

    private String code;

    private String level;

    private List<EventType> children;

    private EventType parent;

    private Integer type;

    private Dept dept;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 创建用户
     */
    private User createBy;

    /**
     * 修改用户
     */
    private User updateBy;

    /**
     * 数据是否已被删除
     */
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public List<EventType> getChildren() {
        return children;
    }

    public void setChildren(List<EventType> children) {
        this.children = children;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public EventType getParent() {
        return parent;
    }

    public void setParent(EventType parent) {
        this.parent = parent;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    @CreatedDate
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @LastModifiedDate
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by")
    public User getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
