package com.unicom.urban.management.service.role;

import com.unicom.urban.management.dao.role.RoleRepository;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    public void save(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        roleRepository.save(role);
    }

    public Role findOne(RoleDTO roleDTO) {
        return roleRepository.getOne(roleDTO.getId());
    }

    public void update(RoleDTO roleDTO) {
        Role one = this.findOne(roleDTO);
        one.setName(roleDTO.getName());
        roleRepository.saveAndFlush(one);
    }

}
