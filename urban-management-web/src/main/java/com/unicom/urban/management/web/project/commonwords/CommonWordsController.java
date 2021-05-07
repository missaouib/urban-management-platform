package com.unicom.urban.management.web.project.commonwords;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.pojo.vo.IdiomsVO;
import com.unicom.urban.management.service.idioms.IdiomsService;
import com.unicom.urban.management.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@ResponseResultBody
@RequestMapping("/commonWords")
public class CommonWordsController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private IdiomsService idiomsService;
    @GetMapping("/index")
    public ModelAndView role() {
        return new ModelAndView(SystemConstant.PAGE + "/commonWords/index");
    }

    @GetMapping("/list")
    public Page<IdiomsVO> list(IdiomsVO idiomsVO,@PageableDefault Pageable pageable){
        return idiomsService.search(idiomsVO,pageable);
    }
}
