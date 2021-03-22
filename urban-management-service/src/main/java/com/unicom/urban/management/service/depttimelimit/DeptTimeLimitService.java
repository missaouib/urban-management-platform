package com.unicom.urban.management.service.depttimelimit;

import com.unicom.urban.management.dao.depttimelimit.DeptTimeLimitRepository;
import com.unicom.urban.management.mapper.DeptTimeLimitMapper;
import com.unicom.urban.management.pojo.entity.DeptTimeLimit;
import com.unicom.urban.management.pojo.vo.DeptTimeLimitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public List<DeptTimeLimitVO> findDeptTimeLimitByCondition(String condition) {
        List<DeptTimeLimit> list = deptTimeLimitRepository.findAllByEventCondition_Id(condition);
        if (list != null) {
            return DeptTimeLimitMapper.INSTANCE.deptTimeLimitListTodeptTimeLimitVOList(list);
        }
        return null;
    }

    public DeptTimeLimitVO findDeptTimeLimit(String timeLimit) {
        Optional<DeptTimeLimit> byId = deptTimeLimitRepository.findById(timeLimit);
        return DeptTimeLimitMapper.INSTANCE.deptTimeLimitToDeptTimeLimitVO(byId.orElse(new DeptTimeLimit()));
    }

    public DeptTimeLimit findOne(String id) {
        return deptTimeLimitRepository.getOne(id);
    }
}
