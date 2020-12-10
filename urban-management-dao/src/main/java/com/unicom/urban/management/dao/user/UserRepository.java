package com.unicom.urban.management.dao.user;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.User;

import java.util.List;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    List<User> findAllByDept_IdAndSort(String deptId, Integer sort);

    boolean existsByUsername(String username);

}
