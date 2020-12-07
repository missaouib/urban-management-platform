package com.unicom.urban.management.service.user;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.BadPasswordException;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.mapper.UserMapper;
import com.unicom.urban.management.pojo.dto.ChangePasswordDTO;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.dept.DeptService;
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
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PasswordService passwordService;

    public Page<UserVO> search(UserDTO userDTO, Pageable pageable) {
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(userDTO.getName())) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), userDTO.getName()));
            }
            if (StringUtils.isNotEmpty(userDTO.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), userDTO.getUsername()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        Page<User> page = userRepository.findAll(specification, pageable);

        List<UserVO> userVOList = UserMapper.INSTANCE.userListToUserVOList(page.getContent());

        return new PageImpl<>(userVOList, page.getPageable(), page.getTotalElements());
    }


    @Log(name = "新增管理-新增")
    public void saveUser(UserDTO userDTO) {

        if (usernameAlreadyExists(userDTO.getUsername())) {
            throw new DataValidException("账号已经存在");
        }

        persistUser(userDTO);

    }

    /**
     * 初始化密码
     */
    public void initialization(String userId){
        Optional<User> ifUser = userRepository.findById(userId);
        if(ifUser.isPresent()){
            User user = ifUser.get();
            this.initPassword(user);
            userRepository.saveAndFlush(user);
        }else{
            throw new RuntimeException("用户不存在");
        }
    }

    /**
     * 激活
     */
    public void activation(String userId,int sts){
        Optional<User> ifUser = userRepository.findById(userId);
        if(ifUser.isPresent()){
            User user = ifUser.get();
            user.setSts(sts);
            userRepository.saveAndFlush(user);
        }else{
            throw new RuntimeException("用户不存在");
        }
    }


    /**
     * 持久化user对象
     */
    private void persistUser(UserDTO userDTO) {
        String deptId = userDTO.getDeptId();
        Dept dept = new Dept();
        dept.setId(deptId);

        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getMobileNumber());
        user.setProfilePhotoUrl("http://www.baidu.com/");
//        user.setDept(dept);
        initPassword(user);

        userRepository.save(user);
    }

    @Log(name = "新增管理-修改")
    public void updateUser(User user) {

        User userFormDatabase = userRepository.getOne(user.getId());

        userFormDatabase.setName(user.getName());

        userFormDatabase.setPhone(user.getPhone());

        userRepository.save(userFormDatabase);

    }

    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findByUsername(SecurityUtil.getUsername());

        if (!passwordService.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BadPasswordException("密码错误");
        }

        if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfigNewPassword())) {
            throw new BadPasswordException("两次输入的密码不一致");
        }

        user.setPassword(passwordService.encode(changePasswordDTO.getNewPassword()));

        userRepository.save(user);

    }

    public UserVO findById(String id) {
        User user = userRepository.getOne(id);
        return UserMapper.INSTANCE.userToUserVO(user);
    }

    public User findOne(String id) {
        return userRepository.getOne(id);
    }

    private void initPassword(User user) {
        user.setPassword(passwordService.getDefaultPassword());
    }

    @Log(name = "新增管理-删除")
    public void removeUser(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));

        if (checkUser(idList)) {

        }

//        userRepository.deleteUserWithIds(idList);

        for (String id : idList) {
            userRepository.deleteById(id);
        }

    }

    /**
     * 检查用户是否可以被删除
     */
    private boolean checkUser(List<String> ids) {
        for (String id : ids) {
            if (isAdmin(id)) {
                throw new DataValidException("超级管理员角色不能删除");
            }

        }
        return true;
    }

    private boolean isAdmin(String id) {
        return SystemConstant.ADMIN_USER_ID.equals(id);
    }

    public boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 人员配置新增
     * 新增一个带有部门的默认人员
     *
     * @param deptId 部门id
     */
    public void saveUserByDeptId(String deptId) {
        User user = new User();
        user.setName("默认人员");
        initPassword(user);
        User save = userRepository.save(user);
        Dept dept = deptService.findOne(deptId);
        dept.getUserList().add(save);
        deptService.updateDeptForRoleSetup(dept);
    }

}
