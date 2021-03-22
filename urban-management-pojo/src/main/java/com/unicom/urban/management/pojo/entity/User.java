package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.common.util.AESUtil;
import com.unicom.urban.management.pojo.Delete;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 用户
 *
 * @author liukai
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_user")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update sys_user set deleted = " + Delete.DELETE + " where id = ?")
public class User {

    public static final Integer ENABLE = 0;

    public static final Integer DISABLED = 1;

    public final static String ADMIN_USER_ID = "1";

    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 登录账号
     */
    private String username;

    private String password;

    /**
     * 手机号码
     */
    private String phone;

    private String officePhone;

    /**
     * 邮箱
     */
    private String email;

    private String sex;

    /**
     * 职务
     */
    private String post;

    /**
     * 出生日期
     */
    private LocalDate birth;

    /**
     * 激活状态 0激活 1未激活
     */
    private Integer sts;

    /**
     * 头像图片URL
     */
    private String profilePhotoUrl;

    private List<Role> roleList;

    private Dept dept;

    private List<Grid> gridList;

    private Integer sort;

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


    public User() {
    }

    public User(String id) {
        this.id = id;
    }


    /**
     * 此人是否配置了部门
     */
    @Transient
    public boolean hasDept() {
        Dept dept = this.getDept();
        return dept != null;
    }

    @Transient
    public boolean noDept() {
        Dept dept = this.getDept();
        return dept == null;
    }

    /**
     * 激活
     */
    @Transient
    public void activation() {
        this.setSts((null == this.getSts() || this.getSts() == 0) ? 1 : 0);
    }

    /**
     * 是否为超级管理员
     */
    @Transient
    public boolean isAdmin() {
        return ADMIN_USER_ID.equals(id);
    }

    /**
     * 获取未加密的电话号码
     */
    @Transient
    public String getRawPhone() {
        if (StringUtils.isNotEmpty(phone)) {
            return AESUtil.decrypt(phone);
        }
        return phone;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public Integer getSts() {
        return sts;
    }

    public void setSts(Integer sts) {
        this.sts = sts;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id")
    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_grid", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "grid_id"))
    public List<Grid> getGridList() {
        return gridList;
    }

    public void setGridList(List<Grid> gridList) {
        this.gridList = gridList;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
