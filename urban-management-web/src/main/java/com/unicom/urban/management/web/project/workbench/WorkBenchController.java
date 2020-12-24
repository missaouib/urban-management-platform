package com.unicom.urban.management.web.project.workbench;

import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 工作台Controller
 *
 * @author liukai
 */
@Slf4j
@Controller
@RequestMapping("/workbench")
public class WorkBenchController {

    /**
     * 跳转到工作台页面
     */
    @GetMapping
    public String workBench() {
        return SystemConstant.PAGE + "/workbench";
    }


    /**
     * 将工作台的待办事件 转发到不同的Controller
     */
    @GetMapping("/dispatcher")
    public String dispatcher(String url, String id) {
        if (StringUtils.isEmpty(url)) {
            throw new BusinessException("url 不能为空");
        }
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException("id 不能为空");
        }
        return String.format("redirect:%s?eventId=%s", url, id);
    }

}
