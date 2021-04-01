package com.unicom.urban.management.service.user;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.exception.BadPasswordException;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.AESUtil;
import com.unicom.urban.management.util.SecurityUtil;
import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.mapper.UserMapper;
import com.unicom.urban.management.pojo.Delete;
import com.unicom.urban.management.pojo.dto.ChangePasswordDTO;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.dto.UserIdListDTO;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.AddressBookVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.password.PasswordService;
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
    private final static DateTimeFormatter fmt;

    static {
        fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private GridService gridService;

    public Page<UserVO> search(UserDTO userDTO, Pageable pageable) {
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(userDTO.getName())) {
                list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + userDTO.getName() + "%"));
            }
            if (StringUtils.isNotEmpty(userDTO.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), userDTO.getUsername()));
            }
            if (userDTO.getSts() != 3) {
                list.add(criteriaBuilder.equal(root.get("sts").as(Integer.class), userDTO.getSts()));
            }
            if (StringUtils.isNotBlank(userDTO.getDeptId())) {
                list.add(criteriaBuilder.equal(root.get("dept").get("id").as(String.class), userDTO.getDeptId()));
            }
            if (StringUtils.isNotBlank(userDTO.getRoleId())) {
                Join<Object, Object> roleList = root.join("roleList", JoinType.LEFT);
                list.add(criteriaBuilder.equal(roleList.get("id").as(String.class), userDTO.getRoleId()));
            }


            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        Page<User> page = userRepository.findAll(specification, pageable);

        List<User> content = page.getContent();
        List<UserVO> userVOList = UserMapper.INSTANCE.convertList(content);
        userVOList.forEach(v -> {
            User user = content.stream().filter(c -> c.getId().equals(v.getId())).collect(Collectors.toList()).get(0);
            StringBuilder roles = new StringBuilder();
            for (Role role : user.getRoleList()) {
                roles.append(",").append(role.getName());
            }
            v.setRoles(roles.toString().length() > 0 ? roles.toString().substring(1) : "");
            if (user.isAdmin()) {
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
            throw new DataValidException("用户不存在");
        }
    }

    /**
     * 激活
     */
    public void activation(String userId) {
        Optional<User> ifUser = userRepository.findById(userId);
        if (ifUser.isPresent()) {
            User user = ifUser.get();
            user.activation();
        } else {
            throw new DataValidException("用户不存在");
        }
    }


    @Log(name = "用户管理-修改")
    public void updateUser(User user) {

        User userFormDatabase = userRepository.getOne(user.getId());

        userFormDatabase.setName(user.getName());

        userFormDatabase.setPhone(AESUtil.encrypt(user.getPhone()));

        userRepository.save(userFormDatabase);

    }

    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeleted(SecurityUtil.getUsername(), Delete.NORMAL);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!passwordService.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                throw new BadPasswordException("密码错误");
            }
            if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfigNewPassword())) {
                throw new BadPasswordException("两次输入的密码不一致");
            }
            user.setPassword(passwordService.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
        }


    }

    public UserVO findById(String id) {
        User user = userRepository.getOne(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        if (Optional.ofNullable(user.getDept()).isPresent()) {
            userVO.setDeptName(user.getDept().getDeptName());
            userVO.setDeptId(user.getDept().getId());
        }
        List<String> roleList = new ArrayList<>();
        user.getRoleList().forEach(r -> roleList.add(r.getId()));
        userVO.setRoleIdList(roleList);
        if (Optional.ofNullable(user.getBirth()).isPresent()) {
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
            User user = new User(id);
            if (user.isAdmin()) {
                throw new DataValidException("超级管理员角色不能删除");
            }
        }
    }

    public boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsernameAndDeleted(username, Delete.NORMAL);
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsernameAndDeleted(username, Delete.NORMAL);
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
        if (existsByUsername(userDTO.getUsername())) {
            throw new DataValidException("登录名不能重复");
        }
        user.setEmail(userDTO.getEmail());
        user.setPhone(AESUtil.encrypt(userDTO.getMobileNumber()));
        user.setOfficePhone(userDTO.getOfficePhone());
        user.setPost(userDTO.getPost());
        initPassword(user);
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setSex(userDTO.getSex());
        if (StringUtils.isNotBlank(userDTO.getBirth())) {
            LocalDate birth = LocalDate.parse(userDTO.getBirth(), fmt);
            user.setBirth(birth);
        }
        if (userDTO.getSts() == null) {
            user.setSts(User.DISABLED);
        } else {
            user.setSts(userDTO.getSts());
        }
        user.setProfilePhotoUrl("http://www.baidu.com/");
        if (StringUtils.isNotBlank(userDTO.getDeptId())) {
            Dept dept = new Dept(userDTO.getDeptId());
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
        if (userDTO.getSts() == null) {
            user.setSts(User.DISABLED);
        } else {
            user.setSts(userDTO.getSts());
        }
        user.setSex(userDTO.getSex());
        user.setName(userDTO.getName());
        if (StringUtils.isNotBlank(userDTO.getBirth())) {
            LocalDate birth = LocalDate.parse(userDTO.getBirth(), fmt);
            user.setBirth(birth);
        }
        user.setPhone(AESUtil.encrypt(userDTO.getMobileNumber()));
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
        List<User> users = userRepository.findAllByDept_IdAndSortAndDeleted(deptId, sort, Delete.NORMAL);
        if (StringUtils.isBlank(userId)) {
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

    public void saveBatchUser(UserIdListDTO userIdListDTO) {
        String deptId = userIdListDTO.getDeptId();
        List<Map<String, Object>> userIdList = userIdListDTO.getUserIdList();
        for (Map<String, Object> map : userIdList) {
            String userId = map.get("id").toString();
            Integer checkbox = Integer.valueOf(map.get("checkbox").toString());
            User user = this.findOne(userId);
            Dept dept = new Dept();
            if (checkbox == 1) {
                dept.setId(deptId);
                user.setDept(dept);
            } else {
                user.setDept(null);
            }

            userRepository.saveAndFlush(user);
        }
    }

    public List<UserVO> findUserByDept(String deptId) {
        List<User> userList = this.findAll();
        List<UserVO> list = new ArrayList<>();
        for (User user : userList) {
            UserVO vo = new UserVO();
            if (user.getDept() != null && user.getDept().getId().equals(deptId)) {
                vo.setCheckbox(1);
            } else {
                vo.setCheckbox(0);
            }
            vo.setId(user.getId());
            vo.setName(user.getName());
            list.add(vo);

        }
        return list;
    }

    public List<UserVO> getSupervisorUserList(String gridId) {
        List<User> userList = this.findAll();
        List<UserVO> list = new ArrayList<>();
        for (User user : userList) {
            if (user.getRoleList() != null) {
                for (Role role : user.getRoleList()) {
                    if (KvConstant.SUPERVISOR_ROLE.equals(role.getId())) {
                        UserVO userVO = new UserVO();
                        userVO.setId(user.getId());
                        userVO.setName(user.getName());
                        if (user.getGridList() != null) {
                            if (listIndexOfObject(user.getGridList(), gridId)) {
                                userVO.setSts(1);
                            } else {
                                userVO.setSts(0);
                            }
                        }
                        list.add(userVO);
                    }
                }
            }
        }
        return list;
    }

    private boolean listIndexOfObject(List<Grid> gridList, String id) {
        for (Grid grid : gridList) {
            if (grid.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 手机端 获取通讯录
     *
     * @return 通讯录
     */
    public List<AddressBookVO> getAddressBook() {
        List<User> userList = findAll();
        List<AddressBookVO> addressBookVOList = new ArrayList<>(userList.size());
        for (User user : userList) {
            AddressBookVO addressBookVO = new AddressBookVO();
            addressBookVO.setId(user.getId());
            addressBookVO.setName(user.getName());
            addressBookVO.setPhone(AESUtil.decrypt(user.getPhone()));
            addressBookVOList.add(addressBookVO);
        }
        return addressBookVOList;
    }


    public UserVO getUser(String id) {
        User user = userRepository.findById(id).orElse(new User());
        return new UserVO(user);
    }

    /**
     * 获取个角色人数
     */
    public Map<String, Object> getUserCount() {
        List<User> all = userRepository.findAll();
        Map<String, Object> map = new HashMap<>();
        // 专业部门
        long professionalDepartments = this.roleCount(KvConstant.PROFESSIONAL_DEPARTMENTS_ROLE, all);
        // 监督员
        long supervisor = this.roleCount(KvConstant.SUPERVISOR_ROLE, all);
        // 值班长
        long shiftLeader = this.roleCount(KvConstant.SHIFT_LEADER_ROLE, all);
        // 派遣员
        long dispatcher = this.roleCount(KvConstant.DISPATCHER_ROLE, all);
        // 受理员
        long receptionist = this.roleCount(KvConstant.RECEPTIONIST_ROLE, all);
        map.put("professionalDepartments", professionalDepartments);
        map.put("supervisor", supervisor);
        map.put("shiftLeader", shiftLeader);
        map.put("dispatcher", dispatcher);
        map.put("receptionist", receptionist);
        return map;
    }

    private long roleCount(String role, List<User> users) {
        return users.stream().filter(u -> u.getRoleList().stream().anyMatch(r -> r.getId().equals(role))).count();
    }

}
