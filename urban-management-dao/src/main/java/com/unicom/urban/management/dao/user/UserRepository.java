package com.unicom.urban.management.dao.user;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);


}