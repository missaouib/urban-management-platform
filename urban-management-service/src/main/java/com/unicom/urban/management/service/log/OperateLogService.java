package com.unicom.urban.management.service.log;

import com.unicom.urban.management.dao.log.OperateLogRepository;
import com.unicom.urban.management.pojo.entity.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作日志
 */
@Service
public class OperateLogService {

    @Autowired
    private OperateLogRepository operateLogRepository;


    @Transactional(rollbackFor = Exception.class)
    public OperateLog save(OperateLog operateLog) {
        return operateLogRepository.save(operateLog);
    }


}
