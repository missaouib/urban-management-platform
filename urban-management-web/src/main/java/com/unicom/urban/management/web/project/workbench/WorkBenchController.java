package com.unicom.urban.management.web.project.workbench;

import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 工作台Controller
 *
 * @author liukai
 */
@Controller
public class WorkBenchController {

    /**
     * 跳转到工作台页面
     */
    @GetMapping("/workbench")
    public String workBench() {
        return SystemConstant.PAGE + "/workbench";
    }

}
