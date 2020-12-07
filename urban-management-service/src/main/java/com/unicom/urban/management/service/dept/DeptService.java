package com.unicom.urban.management.service.dept;

import com.unicom.urban.management.dao.dept.DeptRepository;
import com.unicom.urban.management.pojo.dto.DeptDTO;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.grid.GridService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private GridService gridService;

    public List<DeptVO> getAll() {
        List<Dept> all = deptRepository.findAll();
        List<DeptVO> deptVOS = new ArrayList<>();
        all.forEach(d -> {
            DeptVO deptVO = DeptVO.builder()
                    .id(d.getId())
                    .deptName(d.getDeptName()).build();
            deptVOS.add(deptVO);
        });
        return deptVOS;
    }

    public Dept findOne(String deptId) {
        Optional<Dept> dept = deptRepository.findById(deptId);
        if (dept.isPresent()) {
            return dept.get();
        }
        throw new RuntimeException("部门不存在");
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
    public DeptVO findOneVO(String deptId) {
        Dept one = this.findOne(deptId);
        DeptVO deptVO = new DeptVO();
        BeanUtils.copyProperties(one, deptVO);
        if (one.getParent() != null) {
            deptVO.setParentId(one.getParent().getId());
            deptVO.setParentName(one.getParent().getDeptName());
        }
        if (one.getGrid() != null) {
            deptVO.setGridId(one.getGrid().getId());
            deptVO.setGridName(one.getGrid().getGridName());
        }

        return deptVO;
    }


    /**
     * 新增
     * @param deptDTO
     */
    public void save(DeptDTO deptDTO) {
        Dept dept = new Dept();
        BeanUtils.copyProperties(deptDTO, dept);
        Grid grid = gridService.findOne(deptDTO.getGridId());
        dept.setGrid(grid);
        if (StringUtils.isNotBlank(deptDTO.getParentId())) {
            Optional<Dept> ifParentDept = deptRepository.findById(deptDTO.getParentId());
            if (ifParentDept.isPresent()) {
                Dept parent = ifParentDept.get();
                dept.setParent(parent);
                if (deptDTO.getSort() == null) {
                    List<Dept> depts = deptRepository.findAllByParent_Id(parent.getId());
                    if (depts.size() != 0) {

                    } else {


                    }
                }
            } else {
                throw new RuntimeException("所属部门不存在");
            }
        }
        deptRepository.save(dept);
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
            deptVO.setParentId(dept.getParent().getId());
            List<User> userList = dept.getUserList();
            if (userList.size() > 0) {
                userList.forEach(user -> {
                    DeptVO userVO = new DeptVO();
                    userVO.setId(user.getId());
                    userVO.setDeptName(user.getName());
                    userVO.setParentId(dept.getId());
                    deptVOList.add(userVO);
                });
            }
            deptVOList.add(deptVO);
        });
        return deptVOList;
    }


    /**
     * 删除
     * @param id
     */
    public void del(String id) {
        Optional<Dept> dept = deptRepository.findById(id);
        if (dept.isPresent()) {
            Dept entity = dept.get();
            if (entity.getUserList().size() > 0) {
                throw new RuntimeException("部门中有人员，请删除后再执行操作");
            }
            if (entity.getRoleList().size() > 0) {
                throw new RuntimeException("部门中有角色，请删除后再执行操作");
            }
            List<Dept> depts = deptRepository.findAllByParent_Id(entity.getId());
            if (depts.size() > 0) {
                throw new RuntimeException("部门下有子部门，请删除后再执行操作");
            }

            deptRepository.delete(entity);
        } else {
            throw new RuntimeException("部门不存在");
        }
    }

    public void updateDeptForRoleSetup(Dept dept) {
        deptRepository.saveAndFlush(dept);
    }

}
