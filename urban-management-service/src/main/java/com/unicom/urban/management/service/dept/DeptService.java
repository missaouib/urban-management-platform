package com.unicom.urban.management.service.dept;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.AESUtil;
import com.unicom.urban.management.dao.dept.DeptRepository;
import com.unicom.urban.management.mapper.TreeMapper;
import com.unicom.urban.management.pojo.dto.DeptDTO;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.pojo.vo.TreeVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.role.RoleService;
import com.unicom.urban.management.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理service
 *
 * @author liukai
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DeptService {
    private final static DateTimeFormatter DF;
    private final static DateTimeFormatter FMT;

    static {
        DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Autowired
    private DeptRepository deptRepository;
    @Autowired
    private GridService gridService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    public List<DeptVO> getAll() {
        List<Dept> all = deptRepository.findAllByOrderBySortDesc();
        List<DeptVO> deptVOList = new ArrayList<>();
        all.forEach(d -> {
            DeptVO deptVO = DeptVO.builder()
                    .id(d.getId())
                    .parentId(d.getParent() != null ? d.getParent().getId() : null)
                    .deptName(d.getDeptName()).build();
            deptVOList.add(deptVO);
        });
        return deptVOList;
    }

    public Dept findOne(String deptId) {
        Optional<Dept> dept = deptRepository.findById(deptId);
        if (dept.isPresent()) {
            return dept.get();
        }
        throw new DataValidException("部门不存在");
    }

    /**
     * 角色配置结构树
     *
     * @return 树结构数据
     */
    public List<DeptVO> getAllAndRoleForTree() {
        List<Dept> deptList = deptRepository.findAll(Sort.by(Sort.Order.asc("sort")));
        return roleForTree(deptList);
    }

    /**
     * 专业部门角色配置结构树
     *
     * @return 树结构数据
     */
    public List<DeptVO> getDeptAndRoleForTree() {
        List<Dept> all = deptRepository.findAll(Sort.by(Sort.Order.asc("sort")));
        List<Dept> deptList = all.stream().filter(d->"2".equals(d.getType())).collect(Collectors.toList());
        return roleForTree(deptList);
    }

    private List<DeptVO> roleForTree(List<Dept> deptList){
        List<DeptVO> deptVOList = new ArrayList<>();
        for (Dept dept : deptList) {
            DeptVO deptVO = new DeptVO();
            deptVO.setId(dept.getId());
            deptVO.setIconSkin("treeDept");
            deptVO.setDeptName(dept.getDeptName());
            deptVO.setParentId(dept.getParent() != null ? dept.getParent().getId() : "");
            deptVO.setSort(dept.getSort() != null ? dept.getSort() : 0);
            List<Role> roleList = dept.getRoleList();
            if (roleList.size() > 0) {
                for (Role role : roleList) {
                    DeptVO roleVO = new DeptVO();
                    roleVO.setId(role.getId());
                    roleVO.setDeptName(role.getName());
                    roleVO.setIconSkin("treeRole");
                    roleVO.setDescribes(role.getDescribes());
                    roleVO.setCreateTime(DF.format(role.getCreateTime()));
                    roleVO.setParentId(dept.getId());
                    roleVO.setParentName(dept.getDeptName());
                    roleVO.setSort(role.getSort() != null ? role.getSort() : 0);
                    roleVO.setLevelOrNot("role");
                    deptVOList.add(roleVO);
                }
            }
            deptVOList.add(deptVO);
        }
         deptVOList.sort(Comparator.comparingInt(DeptVO::getSort));
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
        deptVO.setCreateTime(DF.format(one.getCreateTime()));

        return deptVO;
    }

    /**
     * 新增
     *
     * @param deptDTO 数据
     */
    public void save(DeptDTO deptDTO) {
        Dept dept = new Dept();
        Grid grid = gridService.findOne(deptDTO.getGridId());
        if(StringUtils.isNotBlank(deptDTO.getId())){
            Optional<Dept> ifDept = deptRepository.findById(deptDTO.getId());
            if(ifDept.isPresent()){
                dept = ifDept.get();
                dept.setDeptName(deptDTO.getDeptName());
                dept.setDeptPhone(deptDTO.getDeptPhone());
                dept.setDeptAddress(deptDTO.getDeptAddress());
                dept.setDescribes(deptDTO.getDescribes());
                dept.setType(deptDTO.getType());
            }
        }else{
            BeanUtils.copyProperties(deptDTO, dept);
        }
        dept.setGrid(grid);
        if(StringUtils.isNotBlank(deptDTO.getCdate())){
            dept.setCreateTime(LocalDateTime.parse(deptDTO.getCdate(), DF));
        }
        if (StringUtils.isNotBlank(deptDTO.getParentId())) {
            Optional<Dept> ifParentDept = deptRepository.findById(deptDTO.getParentId());
            if (ifParentDept.isPresent()) {
                Dept parent = ifParentDept.get();
                dept.setParent(parent);
                if (deptDTO.getSort() == null) {
                    List<Dept> deptList = deptRepository.findAllByParent_Id(parent.getId());
                    if (deptList.size() != 0) {
                        Integer sort = deptList.stream().map(Dept::getSort).max(Integer::compareTo).get();
                        dept.setSort(sort + 10);
                    } else {
                        dept.setSort(10);
                    }
                }else{
                    dept.setSort(deptDTO.getSort());
                }
                dept.setType(parent.getType());
            } else {
                throw new DataValidException("所属部门不存在");
            }
        }
        deptRepository.saveAndFlush(dept);
    }

    /**
     * 角色新增的排序字段
     *
     * @param deptId 部门id
     * @return 排序值
     */
    public Integer getRoleSortByDeptId(String deptId) {
        Dept one = deptRepository.getOne(deptId);
        List<Role> roleList = one.getRoleList();
        if (roleList.size() > 0) {
            List<Integer> numList = roleList.stream().map(Role::getSort).distinct().collect(Collectors.toList());
            Integer max = numList.stream().filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(0);
            return max + 10;
        } else {
            return 10;
        }
    }

    /**
     * 人员新增的排序字段
     *
     * @param deptId 部门id
     * @return 排序值
     */
    public Integer getUserSortByDeptId(String deptId) {
        if (StringUtils.isBlank(deptId)) {
            throw new DataValidException("请选择正确的部门");
        }
        Dept one = deptRepository.getOne(deptId);

        List<User> userList = one.getUserList();
        if (userList.size() > 0) {
            List<Integer> numList = userList.stream().map(User::getSort).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            Integer max;
            if (numList.size() == 0) {
                max = 0;
            } else {
                max = Collections.max(numList);
            }
            return (max != null ? max : 0) + 10;
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
            deptVO.setIconSkin("treeDept");
            deptVO.setDeptName(dept.getDeptName());
            deptVO.setParentId(dept.getParent() != null ? dept.getParent().getId() : "");
            List<User> userList = dept.getUserList();
            if (userList.size() > 0) {
                for (User user : userList) {
                    DeptVO userVO = new DeptVO();
                    userVO.setId(user.getId());
                    userVO.setDeptName(user.getName());
                    userVO.setIconSkin("treeUser");
                    userVO.setParentId(dept.getId());
                    userVO.setCreateTime(DF.format(user.getCreateTime()));
                    userVO.setSort(user.getSort());
                    userVO.setLevelOrNot("user");
                    UserVO newUserVO = new UserVO();
                    newUserVO.setPost(user.getPost());
                    newUserVO.setOfficePhone(user.getOfficePhone());
                    newUserVO.setEmail(user.getEmail());
                    newUserVO.setId(user.getId());
                    newUserVO.setName(user.getName());
                    newUserVO.setUsername(user.getUsername());
                    newUserVO.setSex(user.getSex());
                    newUserVO.setSts(user.getSts());
                    newUserVO.setPhone(StringUtils.isNotBlank(user.getPhone())? AESUtil.decrypt(user.getPhone()):"");
                    if (user.getBirth() != null) {
                        newUserVO.setBirth(user.getBirth().format(FMT));
                    }
                    if (user.getDept() != null) {
                        newUserVO.setDeptId(user.getDept().getId());
                        newUserVO.setDeptName(user.getDept().getDeptName());
                    }
                    if (user.getRoleList().size() > 0) {
                        List<String> roleIdList = new ArrayList<>(user.getRoleList().size());
                        List<String> roleNameList = new ArrayList<>(user.getRoleList().size());
                        for (Role role : user.getRoleList()) {
                            roleIdList.add(role.getId());
                            roleNameList.add(role.getName());
                        }
                        newUserVO.setRoleIdList(roleIdList);
                        newUserVO.setRoleNameList(roleNameList);
                    }
                    userVO.setUserVO(newUserVO);
                    deptVOList.add(userVO);
                }
            }
            deptVOList.add(deptVO);
        });
        return deptVOList;
    }


    /**
     * 删除
     *
     * @param id 主键
     */
    public void del(String id) {
        Optional<Dept> dept = deptRepository.findById(id);
        if (dept.isPresent()) {
            Dept entity = dept.get();
            List<UserVO> userByDept = userService.findUserByDept(id);
            if (userByDept.size() > 0) {
                throw new DataValidException("部门中有人员，请删除后再执行操作");
            }
            List<Role> roles = roleService.findByDept(entity);
            if (roles.size() > 0) {
                throw new DataValidException("部门中有角色，请删除后再执行操作");
            }
            List<Dept> depts = deptRepository.findAllByParent_Id(entity.getId());
            if (depts.size() > 0) {
                throw new DataValidException("部门下有子部门，请删除后再执行操作");
            }

            deptRepository.delete(entity);
        } else {
            throw new DataValidException("部门不存在");
        }
    }

    public void updateDeptForRoleSetup(Dept dept) {
        deptRepository.saveAndFlush(dept);
    }

    public List<TreeVO> searchTree() {
        List<Dept> deptList = deptRepository.findAllByOrderBySortDesc();
        return TreeMapper.INSTANCE.deptListToTreeVOList(deptList);
    }
    public List<Dept> findAllByGridId(String gridId){
        return deptRepository.findAllByGrid_id(gridId);
    }

}
