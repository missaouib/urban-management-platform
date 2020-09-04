package com.unicom.project.user.service;

import com.unicom.project.user.dao.UserRepository;
import com.unicom.project.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        user.setPassword(user.getUsername());
        userRepository.save(user);
    }
}
