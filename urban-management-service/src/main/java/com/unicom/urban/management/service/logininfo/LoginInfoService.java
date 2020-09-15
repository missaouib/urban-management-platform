package com.unicom.urban.management.service.logininfo;

import com.unicom.urban.management.dao.logininfo.LoginInfoRepository;
import com.unicom.urban.management.pojo.entity.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginInfoService {

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    public void save(LoginInfo loginInfo) {
        loginInfoRepository.save(loginInfo);
    }

    public Page<LoginInfo> search(LoginInfo loginInfo, Pageable pageable) {
        return loginInfoRepository.findAll((Specification<LoginInfo>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
//            if (StringUtils.isNotEmpty(loginInfo.getCreateTime())) {
//                list.add(criteriaBuilder.equal(root.get("createTime").as(String.class), loginInfo.getCreateTime()));
//            }
            if (StringUtils.isNotEmpty(loginInfo.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), loginInfo.getUsername()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
    }


}
