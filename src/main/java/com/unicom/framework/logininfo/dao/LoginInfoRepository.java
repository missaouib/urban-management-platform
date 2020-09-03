package com.unicom.framework.logininfo.dao;

import com.unicom.framework.logininfo.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, String>, JpaSpecificationExecutor<LoginInfo> {


}
