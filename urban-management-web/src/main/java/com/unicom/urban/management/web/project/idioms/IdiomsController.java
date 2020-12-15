package com.unicom.urban.management.web.project.idioms;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.IdiomsVO;
import com.unicom.urban.management.service.idioms.IdiomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 惯用语Controller
 * @author liubozhi
 */
@RestController
@ResponseResultBody
@RequestMapping("/idioms")
public class IdiomsController {
    @Autowired
    private IdiomsService idiomsService;

    @PostMapping("/saveIdioms")
    public Result saveIdioms(IdiomsVO idiomsVO){
        idiomsService.saveIdioms(idiomsVO);
        return Result.success();
    }
    @GetMapping("/getIdioms")
    public Result getIdiomsList(){
        List<String> list = idiomsService.findAllIdiomsValue();
        return Result.success(list);
    }
}
