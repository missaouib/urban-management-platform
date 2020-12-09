package com.unicom.urban.management.service.role;

import com.unicom.urban.management.common.annotations.Log;
import com.unicom.urban.management.dao.role.RoleRepository;
import com.unicom.urban.management.mapper.RoleMapper;
import com.unicom.urban.management.mapper.UserMapper;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.dto.RoleMenuDTO;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.RoleVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.dept.DeptService;
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

//    public void update(RoleDTO roleDTO) {
//        Role one = this.findOne(roleDTO);
//        one.setName(roleDTO.getName());
//        roleRepository.saveAndFlush(one);
//        throw new BusinessException("saveAndFlush");
//    }

    public List<UserVO> findUserListByRoleId(String id) {
        List<User> userList = roleRepository.getOne(id).getUserList();
        return UserMapper.INSTANCE.userListToUserVOList(userList);
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
        String[] menuIdList = roleMenuDTO.getMenuIdList();
        List<Menu> menuList = new ArrayList<>();
        if (menuIdList != null && menuIdList.length>0) {
            for (String s : menuIdList) {
                Menu m = new Menu();
                m.setId(s);
                menuList.add(m);
            }
        }
        one.setMenuList(menuList);
        roleRepository.saveAndFlush(one);
    }
}
