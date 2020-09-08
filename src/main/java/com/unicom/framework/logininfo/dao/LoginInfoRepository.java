package com.unicom.framework.logininfo.dao;

import com.unicom.framework.logininfo.entity.LoginInfo;
import com.unicom.framework.repository.CustomizeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoginInfoRepository extends CustomizeRepository<LoginInfo, String> {

    Page<LoginInfo> findByUsername(String username, Pageable pageable);

}
