package com.unicom.framework.dao;

import com.unicom.framework.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, String>, JpaSpecificationExecutor<LoginInfo> {


}
