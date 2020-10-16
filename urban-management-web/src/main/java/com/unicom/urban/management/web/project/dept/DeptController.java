package com.unicom.urban.management.web.project.dept;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 部门管理
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
@RequestMapping("/dept")
public class DeptController {

    @GetMapping
    public ModelAndView dept() {
        return new ModelAndView(SystemConstant.PAGE + "/dept/dept");
    }


    /**
     * 查询部门树
     */
    @GetMapping("/selectdepttree")
    public ModelAndView selectDeptTree() {
        return new ModelAndView(SystemConstant.PAGE + "/dept/selectdepttree");
    }

}
