package com.unicom.urban.management.service.menu;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.menu.MenuRepository;
import com.unicom.urban.management.dao.menu.MenuTypeRepository;
import com.unicom.urban.management.dao.role.RoleRepository;
import com.unicom.urban.management.mapper.MenuMapper;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.entity.MenuType;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.MenuVO;
import com.unicom.urban.management.service.user.UserService;
import com.unicom.urban.management.util.SecurityUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@javax.transaction.Transactional(rollbackOn = Exception.class)
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuTypeRepository menuTypeRepository;
    @Autowired
    private UserService userService;

    public Page<MenuVO> search(MenuDTO menuDTO, Pageable pageable) {
        Page<Menu> page = menuRepository.findAll((Specification<Menu>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
//            if (StringUtils.isNotEmpty(menuDTO.getName())) {
//                list.add(criteriaBuilder.equal(root.get("name").as(String.class), menuDTO.getName()));
//            }
            if (menuDTO.getPurpose() != null) {
                list.add(criteriaBuilder.equal(root.get("purpose").as(Integer.class), menuDTO.getPurpose()));
            }else{
                throw new DataValidException("功能列表code码错误");
            }
            Join<Menu,Role> join = root.join("roleList", JoinType.LEFT);
            list.add(criteriaBuilder.equal(join.get("id").as(String.class),SecurityUtil.getRoleId().get(0)));

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);

        List<MenuVO> menuList = MenuMapper.INSTANCE.menuListToMenuVOList(page.getContent());

        return new PageImpl<>(menuList, page.getPageable(), page.getTotalElements());
    }

    public List<MenuVO> searchAll(MenuDTO menuDTO) {
        List<Menu> listAll = menuRepository.findAll((Specification<Menu>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (menuDTO.getPurpose() != null) {
                list.add(criteriaBuilder.equal(root.get("purpose").as(Integer.class), menuDTO.getPurpose()));
            }
            Join<Menu,Role> join = root.join("roleList", JoinType.LEFT);
//            list.add(criteriaBuilder.equal(join.get("id").as(String.class),SecurityUtil.getRoleId().get(0)));
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(join.get("id"));
            User user = userService.findOne(SecurityUtil.getUserId());
            List<String> type = user.getRoleList().stream().map(Role::getId).distinct().collect(Collectors.toList());
            type.forEach(in::value);
            list.add(in);
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });

        List<MenuVO> menuVOList = MenuMapper.INSTANCE.menuListToMenuVOList(listAll);
        return menuVOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(MenuVO::getId))), ArrayList::new));
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(MenuDTO menuDTO) {
        if (existsMenuName(menuDTO.getParentId(), menuDTO.getName(), null)) {
            throw new DataValidException("菜单名重复");
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menu.setMenuType(new MenuType(menuDTO.getMenuTypeId()));
        menuRepository.findById(menuDTO.getParentId()).ifPresent(menu::setParent);
        if (menu.getParent() != null) {
            if (!menu.getParent().getPurpose().equals(menu.getPurpose())) {
                throw new DataValidException("应用类型与上级菜单不符");
            }
        }
        menuRepository.save(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(MenuDTO menuDTO) {
        if(existsMenuName(menuDTO.getParentId(), menuDTO.getName(), menuDTO.getId())) {
            throw new DataValidException("菜单名重复");
        }
        Optional<Menu> isMenu = menuRepository.findById(menuDTO.getId());
        if(!isMenu.isPresent()){
            throw new DataValidException("菜单不存在");
        }
        Menu menu = isMenu.get();
        menu.setName(menuDTO.getName());
        menu.setIcon(menuDTO.getIcon());
        menu.setSort(menuDTO.getSort());
        menuTypeRepository.findById(menuDTO.getMenuTypeId()).ifPresent(menu::setMenuType);
        if(menu.getParent()!=null){
           menu.setPurpose(menu.getParent().getPurpose());
        }
        menu.getChild().forEach(m->{
            if(!m.getPurpose().equals(menu.getPurpose())){
                m.setPurpose(menu.getPurpose());
                menuRepository.saveAndFlush(menu);
            }
        });
        menuRepository.saveAndFlush(menu);
    }

    private boolean existsMenuName(String pid, String name, String id) {
        if (StringUtils.isNotEmpty(id)) {
            return menuRepository.existsByParentAndNameAndIdNot(new Menu(pid), name, id);
        } else {
            return menuRepository.existsByParentAndName(new Menu(pid), name);
        }
    }

    public List<MenuVO> getMenuByMenuType(String menuTypeId,String roleId) {
      List<Menu>  menuList = menuRepository.findAllByMenuType_Id(menuTypeId);
      List<MenuVO> menuVOList = new ArrayList<>();
      for (Menu m : menuList){
          MenuVO vo = new MenuVO();
          boolean flag = false;
          if (StringUtils.isEmpty(roleId)) {
              flag = false;
          } else {
              for (Role role : m.getRoleList()) {
                  if (roleId.equals(role.getId())) {
                      flag = true;
                  }
                  continue;
              }
          }
          if (flag) {
              vo.setCheckbox(1);
          } else {
              vo.setCheckbox(0);
          }
          vo.setId(m.getId());
          vo.setParentId(m.getParent() == null ? "" : m.getParent().getId());
          vo.setName(m.getName());
          menuVOList.add(vo);
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
        List<Menu> menuList;
        List<MenuVO> menuVOList = new ArrayList<>();
        List<String> roleIdList = SecurityUtil.getRoleId();
        boolean flag = false;
        for (String roleId : roleIdList) {
            Role role = roleRepository.findById(roleId).orElse(new Role());
            menuList = role.getMenuList();
            for (Menu m : menuList) {
                if (CollectionUtils.isNotEmpty(menuVOList)) {
                    for (MenuVO menuVO : menuVOList) {
                        //一个用户多个角色去除重复菜单
                        if (menuVO.getId().equals(m.getId())) {
                            flag = false;
                            break;
                        }else {
                            flag = true;
                        }
                    }
                }else {
                    flag = true;
                }
                if (flag && m.getPurpose() == 1){
                    MenuVO vo = new MenuVO();
                    vo.setId(m.getId());
                    vo.setParentId(m.getParent() == null ? "" : m.getParent().getId());
                    vo.setIcon(m.getIcon() == null ? "" : m.getIcon());
                    vo.setName(m.getName());
                    vo.setPath(m.getPath());
                    menuVOList.add(vo);
                }
            }
        }
        return menuVOList;
    }

    public void del(MenuDTO menuDTO){
        Optional<Menu> ifmenu = menuRepository.findById(menuDTO.getId());
        if(ifmenu.isPresent()){
            Menu menu = ifmenu.get();
            if(menu.getChild().size()>0){
                throw new DataValidException("菜单下有子菜单不能删除");
            }
            menuRepository.delete(menu);
        }else{
            throw new DataValidException("菜单不存在");
        }
    }


    public List<MenuType> menuType(){
        return menuTypeRepository.findAll();
    }
}
