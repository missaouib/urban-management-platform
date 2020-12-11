package com.unicom.urban.management.web.project.grid;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.grid.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 移动终端配置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/gridOwner")
public class GridOwnerController {

    @Autowired
    private GridService gridService;

    @GetMapping("/toList")
    public ModelAndView list() {
        return new ModelAndView(SystemConstant.PAGE + "/gridOwner/list");
    }

    @GetMapping("/toInsert")
    public ModelAndView add() {
        return new ModelAndView(SystemConstant.PAGE + "/gridOwner/add");
    }

}
