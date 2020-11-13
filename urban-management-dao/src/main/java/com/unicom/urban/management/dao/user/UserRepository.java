package com.unicom.urban.management.dao.user;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);

    Boolean existsByUsername(String username);

    void deleteByIdIn(Collections ids);

}
