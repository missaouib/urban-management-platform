package com.unicom.urban.management.service.user;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.mapper.UserMapper;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.entity.ChangePasswordDTO;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.password.PasswordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;


    public Page<UserVO> search(UserDTO userDTO, Pageable pageable) {
        Page<User> page = userRepository.findAll((Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(userDTO.getName())) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), userDTO.getName()));
            }
            if (StringUtils.isNotEmpty(userDTO.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), userDTO.getUsername()));
            }

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<UserVO> userVOList = UserMapper.INSTANCE.userListToUserVOList(page.getContent());

        return new PageImpl<>(userVOList, page.getPageable(), page.getTotalElements());
    }


    public void saveUser(UserDTO userDTO) {

        if (usernameAlreadyExists(userDTO.getUsername())) {
            throw new DataValidException("账号已经存在");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setMobileNumber(userDTO.getMobileNumber());
        initPassword(user);

        userRepository.save(user);
    }

    public void updateUser(User user) {

        User userFormDatabase = userRepository.getOne(user.getId());

        userFormDatabase.setName(user.getName());

        userFormDatabase.setMobileNumber(user.getMobileNumber());

        userRepository.save(userFormDatabase);

    }

    public void changePassword(ChangePasswordDTO changePasswordDTO) {

    }

    public User findById(String id) {
        return userRepository.getOne(id);
    }

    private void initPassword(User user) {
        user.setPassword(passwordService.getDefaultPassword());
    }

    public void removeUser(String ids) {

        if (checkUser(ids)) {

        }
        userRepository.deleteUserWithIds(Arrays.asList(ids.split(",")));
    }

    /**
     * 检查用户是否可以被删除
     */
    private boolean checkUser(String ids) {
        return true;
    }

    public boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

}
