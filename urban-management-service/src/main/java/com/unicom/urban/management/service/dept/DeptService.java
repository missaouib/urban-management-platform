package com.unicom.urban.management.service.dept;

import com.unicom.urban.management.dao.dept.DeptRepository;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.DeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理service
 *
 * @author liukai
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DeptService {

    @Autowired
    private DeptRepository deptRepository;

    public List<DeptVO> getAll(){
        List<Dept> all = deptRepository.findAll();
        List<DeptVO> deptVOS = new ArrayList<>();
        all.forEach(d->{
            DeptVO deptVO = DeptVO.builder()
                    .id(d.getId())
                    .deptName(d.getDeptName()).build();
            deptVOS.add(deptVO);
        });
        return deptVOS;
    }

    public Dept findOne(String deptId) {
        return deptRepository.findById(deptId).orElse(new Dept());
    }

    /**
     * 角色配置结构树
     *
     * @return 树结构数据
     */
    public List<DeptVO> getAllAndRoleForTree() {
        List<Dept> deptList = deptRepository.findAll();
        List<DeptVO> deptVOList = new ArrayList<>();
        for (Dept dept : deptList) {
            DeptVO deptVO = new DeptVO(dept.getId(), dept.getDeptName(), dept.getParent() != null ? dept.getParent().getId() : "");
            List<Role> roleList = dept.getRoleList();
            if (roleList.size() > 0) {
                for (Role role : roleList) {
                    DeptVO roleVO = new DeptVO(role.getId(), role.getName(), dept.getId());
                    deptVOList.add(roleVO);
                }
            }
            deptVOList.add(deptVO);
        }
        return deptVOList;
    }

    public Integer getRoleSortByDeptId(String deptId) {
        Dept one = deptRepository.getOne(deptId);
        List<Role> roleList = one.getRoleList();
        if (roleList.size() > 0) {
            List<Integer> numList = roleList.stream().map(Role::getSort).collect(Collectors.toList());
            return Collections.max(numList) + 10;
        } else {
            return 10;
        }
    }

    /**
     * 人员配置结构树
     *
     * @return 树结构数据
     */
    public List<DeptVO> getAllAndUserForTree() {
        List<Dept> deptList = deptRepository.findAll();
        List<DeptVO> deptVOList = new ArrayList<>();
        deptList.forEach(dept -> {
            DeptVO deptVO = new DeptVO();
            deptVO.setId(dept.getId());
            deptVO.setDeptName(dept.getDeptName());
            deptVO.setPId(dept.getParent().getId());
            List<User> userList = dept.getUserList();
            if (userList.size() > 0) {
                userList.forEach(user -> {
                    DeptVO userVO = new DeptVO();
                    userVO.setId(user.getId());
                    userVO.setDeptName(user.getName());
                    userVO.setPId(dept.getId());
                    deptVOList.add(userVO);
                });
            }
            deptVOList.add(deptVO);
        });
        return deptVOList;
    }

    public void updateDeptForRoleSetup(Dept dept) {
        deptRepository.saveAndFlush(dept);
    }

}
