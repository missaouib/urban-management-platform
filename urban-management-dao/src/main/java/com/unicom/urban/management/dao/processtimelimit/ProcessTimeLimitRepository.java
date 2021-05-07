package com.unicom.urban.management.dao.processtimelimit;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.ProcessTimeLimit;

/**
 * 网格
 *
 * @author jiangwen
 */
public interface ProcessTimeLimitRepository extends CustomizeRepository<ProcessTimeLimit, String> {

    /**
     * 根据任务环节名称查询
     *
     * @param taskName 任务环节名称
     * @return 流程时限
     */
    ProcessTimeLimit findByTaskName(String taskName);

    /**
     * 根据任务环节名称 和 时限分类查询
     *
     * @param taskName 任务环节名称
     * @param kvId     时限分类
     * @return 流程时限
     */
    ProcessTimeLimit findByTaskNameAndLevel_Id(String taskName, String kvId);

}
