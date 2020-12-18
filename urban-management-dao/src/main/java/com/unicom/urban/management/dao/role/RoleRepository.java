package com.unicom.urban.management.dao.role;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Role;

public interface RoleRepository extends CustomizeRepository<Role, String> {

    /**
     * 根据在同一部门下的排序字段查询是否存在重复的数据
     *
     * @param sort   排序数值
     * @param deptId 部门
     * @return 是否重复
     */
    boolean existsAllBySortAndDept_Id(Integer sort, String deptId);

}
