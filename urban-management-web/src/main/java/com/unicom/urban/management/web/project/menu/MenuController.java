package com.unicom.urban.management.web.project.menu;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.pojo.vo.MenuVO;
import com.unicom.urban.management.service.menu.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

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
    public ModelAndView menu(Model model) {
        model.addAttribute("menuType", menuService.menuType());
        return new ModelAndView(SystemConstant.PAGE + "/menu/menu");
    }

    @GetMapping("/search")
    public Page<MenuVO> search(MenuDTO menuDTO, @PageableDefault Pageable pageable) {
        return menuService.search(menuDTO, pageable);
    }

    @PostMapping("/menu")
    public void menu(@Valid MenuDTO menuDTO) {
        if(StringUtils.isBlank(menuDTO.getId())){
            menuService.save(menuDTO);
        }else{
            menuService.update(menuDTO);
        }

    }
    @GetMapping("/tree")
    public List<MenuVO> searchAll() {
        return menuService.getTree();
    }

    @GetMapping("/findAllMenu")
    public List<MenuVO> findAllMenu() {
        return menuService.findAll();
    }

    /**
     * 删除
     */
    @PostMapping("/del")
    public void del(MenuDTO deptDTO){
        menuService.del(deptDTO);
    }

}
