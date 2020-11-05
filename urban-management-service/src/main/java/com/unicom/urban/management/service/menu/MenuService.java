package com.unicom.urban.management.service.menu;

import com.unicom.urban.management.dao.menu.MenuRepository;
import com.unicom.urban.management.mapper.MenuMapper;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;


    public Page<MenuVO> search(MenuDTO menuDTO, Pageable pageable) {
        Page<Menu> page = menuRepository.findAll((Specification<Menu>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
//            if (StringUtils.isNotEmpty(menuDTO.getName())) {
//                list.add(criteriaBuilder.equal(root.get("name").as(String.class), menuDTO.getName()));
//            }
//            if (StringUtils.isNotEmpty(menuDTO.getUsername())) {
//                list.add(criteriaBuilder.equal(root.get("username").as(String.class), menuDTO.getUsername()));
//            }

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
        menuRepository.save(menu);
    }

}
