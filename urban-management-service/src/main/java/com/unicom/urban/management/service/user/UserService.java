package com.unicom.urban.management.service.user;

import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;


    public Page<User> search(User user, Pageable pageable) {
        return userRepository.findByUsername(user.getUsername(), pageable);
    }


    public void saveUser(User user) {
        user.setPassword(user.getUsername());
        userRepository.save(user);
    }
}
