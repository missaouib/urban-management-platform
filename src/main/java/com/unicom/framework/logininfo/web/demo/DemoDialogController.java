package com.unicom.framework.logininfo.web.demo;

import com.unicom.constant.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 弹窗
 *
 * @author liukai
 */
@Controller
@RequestMapping("/demo/modal")
public class DemoDialogController {


    private final String prefix = "/demo/modal";


    /**
     * 弹层组件
     */
    @GetMapping("/layer")
    public String layer() {
        return SystemConstant.PAGE + prefix + "/layer";
    }

    /**
     * 模态窗口
     */
    @GetMapping("/modals")
    public String modals() {
        return SystemConstant.PAGE + prefix + "/modals";
    }


}
