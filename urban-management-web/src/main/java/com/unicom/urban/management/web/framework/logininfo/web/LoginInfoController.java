package com.unicom.urban.management.web.framework.logininfo.web;

import com.unicom.urban.management.web.common.constant.SystemConstant;
import com.unicom.urban.management.web.framework.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.entity.LoginInfo;
import com.unicom.urban.management.web.framework.logininfo.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@ResponseResultBody
public class LoginInfoController {

    @Autowired
    private LoginInfoService loginInfoService;


    @GetMapping("/logininfo")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/logininfo/list");
    }


    @GetMapping("/logininfo/search")
    public Page<LoginInfo> search(LoginInfo loginInfo, @PageableDefault Pageable pageable) {
        return loginInfoService.search(loginInfo, pageable);
    }


}
