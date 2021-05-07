package com.unicom.urban.management.dao.dept;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Dept;

import java.util.List;

public interface DeptRepository extends CustomizeRepository<Dept, String> {

    List<Dept> findAllByParent_Id(String parentId);

    List<Dept> findAllByGrid_id(String gridId);

    List<Dept> findAllByOrderBySortDesc();
}
