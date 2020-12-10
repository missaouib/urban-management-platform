package com.unicom.urban.management.dao.user;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);

    Boolean existsByUsername(String username);

    List<User> findAllByDept_IdAndSort(String deptId, Integer sort);

    List<User> findAllByUsernameAndDeleted(String username, String delete);

    Boolean existsByUsernameAndDeleted(String username, String delete);

    List<User> findAllByDept_Id(String deptId);
}
