package com.unicom.urban.management.dao.user;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.User;

import java.util.List;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    User findByUsernameAndDeleted(String username, String deleted);

    List<User> findAllByDept_IdAndSortAndDeleted(String deptId, Integer sort, String deleted);

    boolean existsByUsernameAndDeleted(String username, String deleted);

}
