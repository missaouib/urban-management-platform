package com.unicom.urban.management.dao.depttimelimit;


import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.DeptTimeLimit;

/**
 * 专业部门时限
 *
 * @author liubozhi
 */
public interface DeptTimeLimitRepository extends CustomizeRepository<DeptTimeLimit, String> {
    DeptTimeLimit findByEventType_IdAndLevel_Id(String eventTypeId,String levelId);
}
