package com.unicom.demo;

import com.unicom.SystemConstant;
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


    private String prefix = "demo/modal";


    /**
     * 弹层组件
     */
    @GetMapping("/layer")
    public String layer() {
        return SystemConstant.asdf + prefix + "/layer";
    }

    /**
     * 模态窗口
     */
    @GetMapping("/modals")
    public String modals() {
        return SystemConstant.asdf + prefix + "/modals";
    }


}
