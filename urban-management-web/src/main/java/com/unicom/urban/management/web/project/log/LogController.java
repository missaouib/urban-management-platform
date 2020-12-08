package com.unicom.urban.management.web.project.log;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.LoginLogDTO;
import com.unicom.urban.management.pojo.vo.LoginLogVO;
import com.unicom.urban.management.pojo.vo.OperateLogVO;
import com.unicom.urban.management.service.log.LoginLogService;
import com.unicom.urban.management.service.log.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 日志控制器
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class LogController {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private OperateLogService operateLogService;


    @GetMapping("/loginlog")
    public ModelAndView loginLog() {
        return new ModelAndView(SystemConstant.PAGE + "/log/loginlog");
    }

    @GetMapping("/operatelog")
    public ModelAndView operateLog() {
        return new ModelAndView(SystemConstant.PAGE + "/log/operatelog");
    }


    @GetMapping("/loginlog/search")
    public Page<LoginLogVO> search(LoginLogDTO loginLogDTO, @PageableDefault Pageable pageable) {
        return loginLogService.search(loginLogDTO, pageable);
    }

    @GetMapping("/operatelog/search")
    public Page<OperateLogVO> search(String username, @PageableDefault Pageable pageable) {
        return operateLogService.search(username, pageable);
    }


}
