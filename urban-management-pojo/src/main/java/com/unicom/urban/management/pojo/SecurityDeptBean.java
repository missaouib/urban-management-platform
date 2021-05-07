package com.unicom.urban.management.pojo;

import com.unicom.urban.management.pojo.entity.Dept;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存放在SpringSecurity中的实体部门对象
 *
 * @author liukai
 */
@Data
@ToString
public class SecurityDeptBean implements Serializable {

    private String id;

    private String deptName;

    public SecurityDeptBean() {
    }


    public SecurityDeptBean(Dept dept) {
        this.id = dept.getId();
        this.deptName = dept.getDeptName();
    }

}
