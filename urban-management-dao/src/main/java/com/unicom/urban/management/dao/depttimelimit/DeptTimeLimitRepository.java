package com.unicom.urban.management.dao.depttimelimit;


import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.DeptTimeLimit;

import java.util.List;

/**
 * 专业部门时限
 *
 * @author liubozhi
 */
public interface DeptTimeLimitRepository extends CustomizeRepository<DeptTimeLimit, String> {

    List<DeptTimeLimit> findAllByEventCondition_Id(String eventConditionId);
}
