package com.unicom.urban.management.jpa;


import com.unicom.urban.management.util.SecurityUtil;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * JPA自动审批功能,从SecurityContextHolder拿出当前登录人的信息,
 * <p>
 * 通过@ {@link org.springframework.data.annotation.CreatedBy} 和@ {@link org.springframework.data.annotation.LastModifiedBy}
 * 在创建和修改实体的时候 自动保存当前登录人信息
 * </p>
 *
 * @author liukai
 * @date 2020/6/10 16:11
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of(SecurityUtil.getUser().castToUser());
    }

}
