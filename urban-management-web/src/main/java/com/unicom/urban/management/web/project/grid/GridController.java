package com.unicom.urban.management.web.project.grid;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 网格管理
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class GridController {

    @GetMapping("/grid")
    public ModelAndView grid() {
        return new ModelAndView(SystemConstant.PAGE + "/grid/grid");
    }

    @GetMapping("/gridSave")
    public ModelAndView gridSave() {
        return new ModelAndView(SystemConstant.PAGE + "/grid/gridSave");
    }


}
