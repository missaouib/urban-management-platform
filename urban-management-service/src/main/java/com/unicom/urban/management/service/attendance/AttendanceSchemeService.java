package com.unicom.urban.management.service.attendance;

import com.unicom.urban.management.dao.attendance.AttendanceRepository;
import com.unicom.urban.management.dao.attendance.AttendanceSchemeRepository;
import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.pojo.dto.AttendanceDTO;
import com.unicom.urban.management.pojo.dto.AttendanceSchemeDTO;
import com.unicom.urban.management.pojo.entity.Attendance;
import com.unicom.urban.management.pojo.entity.AttendanceScheme;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.entity.time.TimeScheme;
import com.unicom.urban.management.pojo.vo.AttendanceSchemeVO;
import com.unicom.urban.management.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-8:52
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class AttendanceSchemeService {
    private final static DateTimeFormatter DF;
    private final static DateTimeFormatter FMT;

    static {
        DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Autowired
    private AttendanceSchemeRepository attendanceSchemeRepository;


    public void save(AttendanceSchemeDTO attendanceSchemeDTO){
        Dept dept = new Dept();
        dept.setId(attendanceSchemeDTO.getDeptId());
        AttendanceScheme attendanceScheme = AttendanceScheme.builder()
                .address(attendanceSchemeDTO.getAddress())
                .dept(dept)
                .x(attendanceSchemeDTO.getX())
                .y(attendanceSchemeDTO.getY())
                .sts("0")
                .radius(attendanceSchemeDTO.getRadius())
                .build();
        attendanceSchemeRepository.save(attendanceScheme);
    }


    public List<AttendanceSchemeVO> getAllByDetp(){
        String deptId = SecurityUtil.getDeptId();
        List<AttendanceScheme> attendanceSchemes = attendanceSchemeRepository.findAllByDept_IdAndSts(deptId, "0");
        List<AttendanceSchemeVO> attendanceSchemeVOS = new ArrayList<>();
        attendanceSchemes.forEach(a->{
            AttendanceSchemeVO attendanceSchemeVO = AttendanceSchemeVO.builder()
                    .deptId(a.getDept().getId())
                    .deptName(a.getDept().getDeptName())
                    .x(a.getX())
                    .y(a.getY())
                    .id(a.getId())
                    .address(a.getAddress())
                    .radius(a.getRadius())
                    .build();
            attendanceSchemeVOS.add(attendanceSchemeVO);
        });
        return attendanceSchemeVOS;
    }
}
