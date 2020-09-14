package com.unicom.urban.management.dao.user;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends CustomizeRepository<User, String> {

    User findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);

    Boolean existsByUsername(String username);

    @Modifying
    @Query(value = "delete from User u where u.id in ?1")
    void deleteUserWithIds(List<String> ids);


}
