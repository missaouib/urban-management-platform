package com.unicom.urban.management.service.role;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.dao.role.RoleRepository;
import com.unicom.urban.management.mapper.RoleMapper;
import com.unicom.urban.management.mapper.UserMapper;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.dto.RoleMenuDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.RoleVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.List;
import java.util.Map;

/**
 * 角色
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;
    public Page<RoleVO> search(RoleDTO roleDTO, Pageable pageable) {
        Page<Role> page = roleRepository.findAll((Specification<Role>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotEmpty(roleDTO.getName())) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), roleDTO.getName()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<RoleVO> roleVOList = RoleMapper.INSTANCE.roleListToRoleVOList(page.getContent());

        return new PageImpl<>(roleVOList, page.getPageable(), page.getTotalElements());
    }

    public void saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        roleRepository.saveAndFlush(role);
    }

    public RoleVO findById(String id) {
        Role role = roleRepository.getOne(id);
        return RoleMapper.INSTANCE.roleToRoleVO(role);
    }


    public Role findOne(String id) {
        return roleRepository.getOne(id);
    }

    public List<UserVO> findUserListForSupervision(String roleId, String gridId) {
        List<User> allUserList = roleRepository.getOne(roleId).getUserList();
        List<User> userList = new ArrayList<>();
        for (User user : allUserList) {
            if (user.getSts() == 0 && isGridOwnerOrNot(user, gridId)) {
                userList.add(user);
            }
        }
        return UserMapper.INSTANCE.userListToUserVOList(userList);
    }

    private boolean isGridOwnerOrNot(User user, String gridId) {
        for (Grid grid : user.getGridList()) {
            if (gridId.equals(grid.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 角色配置新增
     *
     * @param roleDTO 数据
     */
    @Log(name = "角色管理-新增")
    public void saveRoleByDeptId(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setDescribes(roleDTO.getDescribes());
        role.setSort(deptService.getRoleSortByDeptId(roleDTO.getDeptId()));
        Role saveRole = roleRepository.save(role);
        Dept dept = deptService.findOne(roleDTO.getDeptId());
        dept.getRoleList().add(saveRole);
        deptService.updateDeptForRoleSetup(dept);
    }

    /**
     * 角色配置修改
     *
     * @param roleDTO 数据
     */
    @Log(name = "角色管理-修改")
    public void updateRoleByDeptId(RoleDTO roleDTO) {
        Role one = roleRepository.getOne(roleDTO.getId());
        one.setName(roleDTO.getName());
        one.setDescribes(roleDTO.getDescribes());
        Dept dept = deptService.findOne(roleDTO.getDeptId());
        one.setDept(dept);
        roleRepository.saveAndFlush(one);
    }

    @Log(name = "角色管理-删除")
    public void deleteRoleById(String id) {
        roleRepository.deleteById(id);
    }

    /**
     * 保存角色和菜单关系
     * @param roleMenuDTO
     */
    public void saveRoleAndMenu(RoleMenuDTO roleMenuDTO) {
        Role one = this.findOne(roleMenuDTO.getId());
        List<String> menuIdList = roleMenuDTO.getMenuIdList();
        List<Menu> menuList = new ArrayList<>();
        if (CollectionUtils.isEmpty(menuIdList)){
            one.setMenuList(menuList);
        }else {
            for (String s : menuIdList) {
                Menu m = new Menu();
                m.setId(s);
                menuList.add(m);
            }
            one.setMenuList(menuList);
        }
        roleRepository.saveAndFlush(one);
    }

    public List<UserVO> findUserByRole(String roleId) {
        List<User> userList = userService.findAll();
        List<UserVO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userList)) {
            for (int i = 0; i < userList.size(); i++) {
                boolean flag = false;
                UserVO vo = new UserVO();
                if (CollectionUtils.isNotEmpty(userList.get(i).getRoleList())) {
                    for (Role role : userList.get(i).getRoleList()){
                        if (role.getId().equals(roleId)) {
                            flag = true;
                            break;
                        }
                    }
                }
                if (flag){
                    vo.setCheckbox(1);
                }else {
                    vo.setCheckbox(0);
                }
                vo.setId(userList.get(i).getId());
                vo.setName(userList.get(i).getName());
                list.add(vo);
            }
        }
        return list;
    }

    public void saveUserByRole(String roleId,List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            Role role = this.findOne(roleId);
            List<User> userList = new ArrayList<>();
            for (Map<String, Object> map : mapList) {
                String userId = map.get("id").toString();
                User user = new User();
                Integer checkbox = Integer.parseInt(map.get("checkbox").toString());
                if (checkbox == 1) {
                    user.setId(userId);
                }
//                else {
//                    user.setId(null);
//                }
                userList.add(user);
            }
            role.setUserList(userList);
            roleRepository.saveAndFlush(role);
        }
    }
}
