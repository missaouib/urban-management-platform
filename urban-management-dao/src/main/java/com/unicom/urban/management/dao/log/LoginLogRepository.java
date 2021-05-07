package com.unicom.urban.management.dao.log;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoginLogRepository extends CustomizeRepository<LoginLog, String> {

    Page<LoginLog> findByUsername(String username, Pageable pageable);

}
