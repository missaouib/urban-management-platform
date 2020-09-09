package com.unicom.urban.management.web.framework.logininfo.dao;

import com.unicom.urban.management.pojo.entity.LoginInfo;
import com.unicom.urban.management.web.framework.repository.CustomizeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoginInfoRepository extends CustomizeRepository<LoginInfo, String> {

    Page<LoginInfo> findByUsername(String username, Pageable pageable);

}
