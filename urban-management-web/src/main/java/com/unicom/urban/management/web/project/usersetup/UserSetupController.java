package com.unicom.urban.management.web.project.usersetup;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 人员设置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/userSetup")
public class UserSetupController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/userSetup/index");
    }

    @GetMapping("/getAllAndUserForTree")
    public Result getAllAndUserForTree() {
        List<DeptVO> allAndRoleForTree = deptService.getAllAndUserForTree();
        return Result.success(allAndRoleForTree);
    }

}
