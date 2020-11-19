package com.unicom.urban.management.web.project.supervise;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.service.supervise.SuperviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 顾志杰
 * @date 2020/11/19-14:31
 */
@RestController
@ResponseResultBody
@RequestMapping("/supervise")
public class SuperviseController {

    @Autowired
    private SuperviseService superviseService;

    @PostMapping("/supervise")
    public void supervise(){

    }
}
