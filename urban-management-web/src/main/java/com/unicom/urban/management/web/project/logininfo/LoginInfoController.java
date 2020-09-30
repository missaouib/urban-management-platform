package com.unicom.urban.management.web.project.logininfo;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.entity.LoginInfo;
import com.unicom.urban.management.service.logininfo.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> search(LoginInfo loginInfo, @PageableDefault Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        Page<LoginInfo> search = loginInfoService.search(loginInfo, pageable);
        map.put("content", search.getContent());
        map.put("totalElements", search.getTotalElements());
        return map;
    }


}
