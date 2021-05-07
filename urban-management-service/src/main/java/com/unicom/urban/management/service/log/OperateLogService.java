package com.unicom.urban.management.service.log;

import com.unicom.urban.management.dao.log.OperateLogRepository;
import com.unicom.urban.management.mapper.LogMapper;
import com.unicom.urban.management.pojo.entity.OperateLog;
import com.unicom.urban.management.pojo.vo.OperateLogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Page<OperateLogVO> search(String username, Pageable pageable) {
        Page<OperateLog> page;
        if (StringUtils.isNotEmpty(username)) {
            page = operateLogRepository.findByUsername(username, pageable);
        } else {
            page = operateLogRepository.findAll(pageable);
        }
        List<OperateLogVO> list = LogMapper.INSTANCE.operateLogListToOperateLogVOList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

}
