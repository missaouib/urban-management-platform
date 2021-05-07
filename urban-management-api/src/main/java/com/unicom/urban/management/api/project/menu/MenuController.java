package com.unicom.urban.management.api.project.menu;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.pojo.vo.MenuVO;
import com.unicom.urban.management.service.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询手机端菜单
     *
     * @param menuDTO 菜单配置码 手机端的话是2或3
     * @return 菜单
     */
    @GetMapping("/searchAll")
    public Result searchAll(MenuDTO menuDTO) {
        if (menuDTO.getPurpose() == null) {
            throw new DataValidException("功能列表code码错误");
        }
        List<MenuVO> searchAll = menuService.searchAll(menuDTO);
        return Result.success(searchAll);
    }

}
