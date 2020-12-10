package com.unicom.urban.management.service.user;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.BadPasswordException;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.mapper.UserMapper;
import com.unicom.urban.management.pojo.dto.ChangePasswordDTO;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.password.PasswordService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {
    private final static DateTimeFormatter df;
    private final static DateTimeFormatter fmt;

    static {
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

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
            if(userDTO.getSts()!=3){
               list.add(criteriaBuilder.equal(root.get("sts").as(Integer.class),userDTO.getSts()));
            }
            if(StringUtils.isNotBlank(userDTO.getDeptId())){
                list.add(criteriaBuilder.equal(root.get("dept").get("id").as(String.class),userDTO.getDeptId()));
            }
            if(StringUtils.isNotBlank(userDTO.getRoleId())){
                Join<Object, Object> roleList = root.join("roleList", JoinType.LEFT);
                list.add(criteriaBuilder.equal(roleList.get("id").as(String.class),userDTO.getRoleId()));
            }


            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        Page<User> page = userRepository.findAll(specification, pageable);

        List<User> content = page.getContent();
        List<UserVO> userVOList = UserMapper.INSTANCE.userListToUserVOList(content);
        userVOList.forEach(v -> {
            User user = content.stream().filter(c -> c.getId().equals(v.getId())).collect(Collectors.toList()).get(0);
            StringBuilder roles = new StringBuilder();
            for (Role role : user.getRoleList()) {
                roles.append(",").append(role.getName());
            }
            v.setRoles(roles.toString().length() > 0 ? roles.toString().substring(1) : "");
            if (user.getId().equals(SystemConstant.ADMIN_USER_ID)) {
                v.setSystem("1");
            }
            v.setSts(user.getSts());
        });
        return new PageImpl<>(userVOList, page.getPageable(), page.getTotalElements());
    }


    @Log(name = "用户管理-新增")
    public void saveUser(UserDTO userDTO) {

//        if (usernameAlreadyExists(userDTO.getUsername())) {
//            throw new DataValidException("账号已经存在");
//        }
//
//        persistUser(userDTO);

    }

    /**
     * 初始化密码
     */
    public void initialization(String userId) {
        Optional<User> ifUser = userRepository.findById(userId);
        if (ifUser.isPresent()) {
            User user = ifUser.get();
            this.initPassword(user);
            userRepository.saveAndFlush(user);
        } else {
            throw new RuntimeException("用户不存在");
        }
    }

    /**
     * 激活
     */
    public void activation(String userId) {
        Optional<User> ifUser = userRepository.findById(userId);
        if (ifUser.isPresent()) {
            User user = ifUser.get();
            user.setSts((null == user.getSts() || user.getSts() == 0) ? 1 : 0);
            userRepository.saveAndFlush(user);
        } else {
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

    @Log(name = "用户管理-修改")
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
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        if(Optional.ofNullable(user.getDept()).isPresent()){
            userVO.setDeptName(user.getDept().getDeptName());
            userVO.setDeptId(user.getDept().getId());
        }
        List<String> roleList = new ArrayList<>();
        user.getRoleList().forEach(r->roleList.add(r.getId()));
        userVO.setRoleIdList(roleList);
        if(Optional.ofNullable(user.getBirth()).isPresent()){
            userVO.setBirth(fmt.format(user.getBirth()));
        }

        return userVO;
    }

    public User findOne(String id) {
        return userRepository.getOne(id);
    }

    private void initPassword(User user) {
        user.setPassword(passwordService.getDefaultPassword());
    }

    @Log(name = "用户管理-删除")
    public void removeUser(String id) {
        List<String> idList = Collections.singletonList(id);
        checkUser(idList);
        userRepository.deleteById(id);
    }

    /**
     * 检查用户是否可以被删除
     */
    private void checkUser(List<String> ids) {
        for (String id : ids) {
            if (isAdmin(id)) {
                throw new DataValidException("超级管理员角色不能删除");
            }
        }
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
     * @param userDTO 数据
     */
    @Log(name = "人员管理-新增")
    public void saveUserByDeptId(UserDTO userDTO) {
        User user = new User();
        if (StringUtils.isNotBlank(userDTO.getSort())) {
            if (!this.ifSort(userDTO.getDeptId(), Integer.valueOf(userDTO.getSort()), null)) {
                throw new DataValidException("排序不能重复");
            } else {
                user.setSort(Integer.valueOf(userDTO.getSort()));
            }
        }
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getMobileNumber());
        user.setOfficePhone(userDTO.getOfficePhone());
        user.setPost(userDTO.getPost());
        initPassword(user);
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setSex(userDTO.getSex());
        if (StringUtils.isNotBlank(userDTO.getBirth())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birth = LocalDate.parse(userDTO.getBirth(), fmt);
            user.setBirth(birth);
        }
        user.setPhone(userDTO.getMobileNumber());
        user.setSts(0);
        user.setProfilePhotoUrl("http://www.baidu.com/");
        if (StringUtils.isNotBlank(userDTO.getDeptId())) {
            Dept dept = deptService.findOne(userDTO.getDeptId());
            user.setDept(dept);
        }
        if (userDTO.getRoleList().size() > 0) {
            List<Role> roleList = new ArrayList<>(userDTO.getRoleList().size());
            for (String s : userDTO.getRoleList().stream().distinct().collect(Collectors.toList())) {
                Role role = new Role(s);
                roleList.add(role);
            }
            user.setRoleList(roleList);
        }
        userRepository.save(user);
    }

    @Log(name = "人员管理-修改")
    public void updateUserByDeptId(UserDTO userDTO) {
        User user = findOne(userDTO.getId());
        if (StringUtils.isNotBlank(userDTO.getSort())) {
            if (!this.ifSort(userDTO.getDeptId(), Integer.valueOf(userDTO.getSort()), user.getId())) {
                throw new DataValidException("排序不能重复");
            } else {
                user.setSort(Integer.valueOf(userDTO.getSort()));
            }
        }
        user.setSex(userDTO.getSex());
        user.setName(userDTO.getName());
        if (StringUtils.isNotBlank(userDTO.getBirth())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birth = LocalDate.parse(userDTO.getBirth(), fmt);
            user.setBirth(birth);
        }
        user.setPhone(userDTO.getMobileNumber());
        if (StringUtils.isNotBlank(userDTO.getDeptId())) {
            Dept dept = deptService.findOne(userDTO.getDeptId());
            user.setDept(dept);
        }
        if (userDTO.getRoleList().size() > 0) {
            List<Role> roleList = new ArrayList<>(userDTO.getRoleList().size());
            for (String s : userDTO.getRoleList()) {
                Role role = new Role(s);
                roleList.add(role);
            }
            user.setRoleList(roleList);
        }
        user.setEmail(userDTO.getEmail());
        user.setOfficePhone(userDTO.getOfficePhone());
        user.setPost(userDTO.getPost());


        userRepository.saveAndFlush(user);
    }

    @Log(name = "人员管理-删除")
    public void deleteUserByDeptId(String id) {
        userRepository.deleteById(id);
    }

    private boolean ifSort(String deptId, Integer sort, String userId) {
        List<User> users = userRepository.findAllByDept_IdAndSort(deptId, sort);
        if (StringUtils.isNotBlank(userId)) {
            return users.size() == 0;
        } else {
            if (users.size() == 0) {
                return true;
            } else {
                return users.size() == 1 && userId.equals(users.get(0).getId());
            }
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
