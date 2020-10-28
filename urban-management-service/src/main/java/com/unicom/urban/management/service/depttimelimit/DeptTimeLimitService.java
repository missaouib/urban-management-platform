package com.unicom.urban.management.service.depttimelimit;

import com.unicom.urban.management.dao.depttimelimit.DeptTimeLimitRepository;
import com.unicom.urban.management.pojo.entity.DeptTimeLimit;
import com.unicom.urban.management.pojo.entity.KV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 专业部门时限service
 *
 * @author liubozhi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DeptTimeLimitService {

    @Autowired
    private DeptTimeLimitRepository deptTimeLimitRepository;

    public Integer findByEventType_IdAndLevel_Id(String eventTypeId, String levelId) {
        DeptTimeLimit deptTimeLimit = null;
        if (deptTimeLimit == null){
            return 0;
        }
        return deptTimeLimit.getTimeLimit();
    }
}
