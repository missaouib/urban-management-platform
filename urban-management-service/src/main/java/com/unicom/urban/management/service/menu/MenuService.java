package com.unicom.urban.management.service.menu;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.menu.MenuRepository;
import com.unicom.urban.management.dao.menu.MenuTypeRepository;
import com.unicom.urban.management.dao.role.RoleRepository;
import com.unicom.urban.management.mapper.MenuMapper;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.entity.MenuType;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.vo.MenuVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@javax.transaction.Transactional(rollbackOn = Exception.class)
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuTypeRepository menuTypeRepository;

    public Page<MenuVO> search(MenuDTO menuDTO, Pageable pageable) {
        Page<Menu> page = menuRepository.findAll((Specification<Menu>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
//            if (StringUtils.isNotEmpty(menuDTO.getName())) {
//                list.add(criteriaBuilder.equal(root.get("name").as(String.class), menuDTO.getName()));
//            }
//            if (StringUtils.isNotEmpty(menuDTO.getUsername())) {
//                list.add(criteriaBuilder.equal(root.get("username").as(String.class), menuDTO.getUsername()));
//            }
            Join<Menu,Role> join = root.join("roleList", JoinType.LEFT);
            list.add(criteriaBuilder.equal(join.get("id").as(String.class),SecurityUtil.getRoleId().get(0)));

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<MenuVO> menuList = MenuMapper.INSTANCE.menuListToMenuVOList(page.getContent());

        return new PageImpl<>(menuList, page.getPageable(), page.getTotalElements());
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setName(menuDTO.getName());
        menu.setPath(menuDTO.getPath());

        if (StringUtils.isNotEmpty(menuDTO.getParentId())) {
            Menu parentMenu = new Menu();
            parentMenu.setId(menuDTO.getParentId());
            menu.setParent(parentMenu);
        }
        menuRepository.save(menu);
    }

    public List<MenuVO> getMenuByMenuType(String menuTypeId,String roleId) {
      List<Menu>  menuList = menuRepository.findMenuByMenuType_Id(menuTypeId);
      List<MenuVO> menuVOList = new ArrayList<>();
      for (Menu m : menuList){
          MenuVO vo = new MenuVO();
          boolean flag = false;
          if(CollectionUtils.isNotEmpty(m.getRoleList())) {
              if (StringUtils.isEmpty(roleId)){
                  flag = false;
              }else {
                  for (Role role : m.getRoleList()) {
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
              vo.setId(m.getId());
              vo.setParentId(m.getParent() == null ? "" : m.getParent().getId());
              vo.setName(m.getName());
              menuVOList.add(vo);
          }
      }
       return menuVOList;
    }
    public  List<MenuVO> getTree(){
        List<Menu>  menuList = menuRepository.findAll();
        return toVO(menuList);
    }
    private List<MenuVO> toVO(List<Menu> menuList){
        List<MenuVO> menuVOList = new ArrayList<>();
        for (Menu m : menuList){
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(m,vo);
            vo.setParentId(Optional.ofNullable(m.getParent()).map(Menu::getId).orElse(""));
            vo.setMenuTypeId(Optional.ofNullable(m.getMenuType()).map(MenuType::getId).orElse(""));
            menuVOList.add(vo);
        }
        return menuVOList;
    }

    public List<MenuVO> findAll() {
        List<Menu> menuList = null;
        List<MenuVO> menuVOList = new ArrayList<>();
        String roleId = SecurityUtil.getRoleId().get(0);
        if (SecurityUtil.getRoleId().contains("1")){
            menuList = menuRepository.findAll(Sort.by(Sort.Direction.ASC,"sort"));
        }else {
            Role role = roleRepository.findById(roleId).orElse(new Role());
            menuList = role.getMenuList();
        }
        for (Menu m : menuList){
            MenuVO vo = new MenuVO();
            vo.setId(m.getId());
            vo.setParentId(m.getParent() == null ? "" : m.getParent().getId());
            vo.setIcon(m.getIcon() == null ? "" : m.getIcon());
            vo.setName(m.getName());
            vo.setPath(m.getPath());
            menuVOList.add(vo);
        }
        return menuVOList;
    }



    public List<MenuType> menuType(){
        return menuTypeRepository.findAll();
    }
}
