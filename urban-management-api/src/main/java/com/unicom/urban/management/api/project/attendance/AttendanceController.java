package com.unicom.urban.management.api.project.attendance;

import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.AttendanceDTO;
import com.unicom.urban.management.service.attendance.AttendanceSchemeService;
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
    @Autowired
    private AttendanceSchemeService attendanceSchemeService;

    /**
     * 判断上班打卡还是下班打卡
     * @return
     */
    @GetMapping("/isCommuting")
    public Result isCommuting(){
        return Result.success(attendanceService.isCommuting());
    }

    /**
     * 考勤打卡
     * @param attendanceDTO
     * @return
     */
    @PostMapping("/attendance")
    public Result attendance(AttendanceDTO attendanceDTO){
        attendanceService.save(attendanceDTO);
        return Result.success();
    }

    /**
     * 当前登陆人所在部门的考勤规范
     * @return
     */
    @GetMapping("/getAllByDetp")
    public Result getAllByDetp(){
        return Result.success(attendanceSchemeService.getAllByDept());
    }

}
