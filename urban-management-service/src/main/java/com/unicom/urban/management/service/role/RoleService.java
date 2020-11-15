package com.unicom.urban.management.service.role;

import com.unicom.urban.management.dao.role.RoleRepository;
import com.unicom.urban.management.mapper.RoleMapper;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.vo.RoleVO;
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

}
