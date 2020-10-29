package com.unicom.urban.management.service.processtimelimit;

import com.unicom.urban.management.dao.processtimelimit.ProcessTimeLimitRepository;
import com.unicom.urban.management.pojo.entity.ProcessTimeLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 流程时限
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ProcessTimeLimitService {

    @Autowired
    private ProcessTimeLimitRepository processTimeLimitRepository;

    public ProcessTimeLimit findByTaskName(String taskName) {
        return processTimeLimitRepository.findByTaskName(taskName);
    }

}
