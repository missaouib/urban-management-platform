package com.unicom.urban.management.service.logininfo;

import com.unicom.urban.management.dao.logininfo.LoginInfoRepository;
import com.unicom.urban.management.pojo.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginInfoService {

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    public void save(LoginInfo loginInfo) {
        loginInfoRepository.save(loginInfo);
    }

    public Page<LoginInfo> search(LoginInfo loginInfo, Pageable pageable) {
        if (StringUtils.isEmpty(loginInfo.getUsername())) {
            return loginInfoRepository.findAll(pageable);
        }
        return loginInfoRepository.findByUsername(loginInfo.getUsername(), pageable);
    }


}
