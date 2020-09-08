package com.unicom.project.user.dao;

import com.unicom.framework.repository.CustomizeRepository;
import com.unicom.project.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);


}
