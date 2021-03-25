package com.unicom.urban.management.web.project.attendancescheme;

import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.AttendanceDTO;
import com.unicom.urban.management.pojo.dto.AttendanceSchemeDTO;
import com.unicom.urban.management.service.attendance.AttendanceSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 顾志杰
 * @date 2021/3/25-19:01
 */
@RestController
@RequestMapping("/attendanceScheme")
public class AttendanceSchemeController {



    @Autowired
    private AttendanceSchemeService attendanceSchemeService;

    @GetMapping("/attendanceScheme")
    public Result attendanceScheme(AttendanceSchemeDTO attendanceSchemeDTO, @PageableDefault Pageable pageable){
        return Result.success(attendanceSchemeService.getAllByPage(attendanceSchemeDTO,pageable));
    }

    @PostMapping("/attendanceScheme")
    public Result attendanceScheme(AttendanceSchemeDTO attendanceSchemeDTO){
        attendanceSchemeService.saveAndFlush(attendanceSchemeDTO);
        return Result.success();
    }

    @PostMapping("/del")
    public Result del(String id){
        attendanceSchemeService.del(id);
        return Result.success();
    }

}
