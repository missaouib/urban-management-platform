package com.unicom.urban.management.dao.log;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.OperateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author liukai
 */
public interface OperateLogRepository extends CustomizeRepository<OperateLog, String> {

    Page<OperateLog> findByUsername(String username, Pageable pageable);

}
