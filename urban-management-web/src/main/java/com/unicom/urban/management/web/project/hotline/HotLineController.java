package com.unicom.urban.management.web.project.hotline;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 热线上报
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class HotLineController {

    @GetMapping("/hotline")
    public ModelAndView hotline() {
        return new ModelAndView(SystemConstant.PAGE + "/hotline/hotline");
    }


}
