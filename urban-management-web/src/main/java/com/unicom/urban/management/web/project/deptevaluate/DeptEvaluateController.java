package com.unicom.urban.management.web.project.deptevaluate;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.vo.DeptEvaluate;
import com.unicom.urban.management.service.deptevaluate.DeptEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/11/18-10:50
 */
@RestController
@RequestMapping("/dept")
@ResponseResultBody
public class DeptEvaluateController {

    @Autowired
    private DeptEvaluateService evaluateService;


    @GetMapping("/evaluate")
    public List<DeptEvaluate> evaluates(){
        return evaluateService.deptEvaluates();
    }
}
