package com.unicom.framework.web;

import com.unicom.constant.SystemConstant;
import com.unicom.framework.Result;
import com.unicom.framework.entity.LoginInfo;
import com.unicom.framework.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginInfoController {

    @Autowired
    private LoginInfoService loginInfoService;


    @GetMapping("/logininfo")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/logininfo/list");
    }


    @GetMapping("/logininfo/search")
    public Result search(LoginInfo loginInfo, @PageableDefault Pageable pageable) {
        Page<LoginInfo> loginInfoPage = loginInfoService.search(loginInfo, pageable);
        return Result.success(loginInfoPage);
    }


}
