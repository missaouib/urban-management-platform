package com.unicom.urban.management.web.project.loginlog;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.LoginLogDTO;
import com.unicom.urban.management.pojo.vo.LoginLogVO;
import com.unicom.urban.management.service.logininfo.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@ResponseResultBody
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;


    @GetMapping("/loginlog")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/loginlog/list");
    }


    @GetMapping("/loginlog/search")
    public Page<LoginLogVO> search(LoginLogDTO loginLogDTO, @PageableDefault Pageable pageable) {
        return loginLogService.search(loginLogDTO, pageable);
    }


}
