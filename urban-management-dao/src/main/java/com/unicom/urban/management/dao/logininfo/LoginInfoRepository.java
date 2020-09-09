package com.unicom.urban.management.dao.logininfo;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.LoginInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoginInfoRepository extends CustomizeRepository<LoginInfo, String> {

    Page<LoginInfo> findByUsername(String username, Pageable pageable);

}
