package com.unicom.urban.management.service.user;

import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.service.password.PasswordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;


    public Page<User> search(User user, Pageable pageable) {
        return userRepository.findAll((Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(user.getName())) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), user.getName()));
            }
            if (StringUtils.isNotEmpty(user.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), user.getUsername()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
    }


    public void saveUser(User user) {
        user.setPassword(passwordService.getDefaultPassword());
        userRepository.save(user);
    }

    public void removeUser(List<User> userList) {
        userRepository.deleteInBatch(userList);
    }

}
