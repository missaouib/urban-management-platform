package com.unicom.urban.management.api.project.attendance;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.AttendanceDTO;
import com.unicom.urban.management.service.attendance.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-10:55
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/isCommuting")
    public Result isCommuting(){
        return Result.success(attendanceService.isCommuting());
    }

    @PostMapping("/attendance")
    public Result attendance(AttendanceDTO attendanceDTO){
        attendanceService.save(attendanceDTO);
        return Result.success();
    }
}
