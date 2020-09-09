package com.unicom.urban.management.web.project.user.dao;

import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.web.framework.repository.CustomizeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);


}
