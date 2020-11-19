package com.unicom.urban.management.web.project.supervise;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.dto.ComponentDTO;
import com.unicom.urban.management.pojo.dto.SuperviseDTO;
import com.unicom.urban.management.pojo.entity.Supervise;
import com.unicom.urban.management.pojo.vo.SuperviseVO;
import com.unicom.urban.management.service.supervise.SuperviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public void supervise(@Valid SuperviseDTO dto){
        superviseService.save(dto);
    }

    @PostMapping("/reply")
    public void reply(@Valid SuperviseDTO dto){
        superviseService.reply(dto);
    }

    @GetMapping("/supervise")
    public SuperviseVO one(String eventId){
        return superviseService.superviseVO(eventId);
    }
}
