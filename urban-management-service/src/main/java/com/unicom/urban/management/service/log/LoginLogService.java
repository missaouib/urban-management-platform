package com.unicom.urban.management.service.log;

import com.unicom.urban.management.dao.log.LoginLogRepository;
import com.unicom.urban.management.mapper.LoginLogMapper;
import com.unicom.urban.management.pojo.entity.LoginLog;
import com.unicom.urban.management.pojo.dto.LoginLogDTO;
import com.unicom.urban.management.pojo.vo.LoginLogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    public void save(LoginLog loginInfo) {
        loginLogRepository.save(loginInfo);
    }

    public Page<LoginLogVO> search(LoginLogDTO loginLogDTO, Pageable pageable) {
        Page<LoginLog> page = loginLogRepository.findAll((Specification<LoginLog>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
//            if (StringUtils.isNotEmpty(loginLog.getCreateTime())) {
//                list.add(criteriaBuilder.equal(root.get("createTime").as(String.class), loginLog.getCreateTime()));
//            }
            if (StringUtils.isNotEmpty(loginLogDTO.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), loginLogDTO.getUsername()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<LoginLogVO> list = LoginLogMapper.INSTANCE.loginLogListToLoginLogVOList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }


}
