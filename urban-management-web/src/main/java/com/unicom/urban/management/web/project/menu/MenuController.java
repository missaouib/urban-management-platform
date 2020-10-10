package com.unicom.urban.management.web.project.menu;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.pojo.vo.MenuVO;
import com.unicom.urban.management.service.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 菜单管理controller
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @GetMapping
    public ModelAndView menu() {
        return new ModelAndView(SystemConstant.PAGE + "/menu/menu");
    }

    @GetMapping("/search")
    public Page<MenuVO> search(MenuDTO menuDTO, @PageableDefault Pageable pageable) {
        return menuService.search(menuDTO, pageable);
    }

}
