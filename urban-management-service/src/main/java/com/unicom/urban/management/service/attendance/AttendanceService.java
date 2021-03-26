package com.unicom.urban.management.service.attendance;

import com.unicom.urban.management.dao.attendance.AttendanceRepository;
import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.pojo.dto.AttendanceDTO;
import com.unicom.urban.management.pojo.entity.Attendance;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.entity.time.TimeScheme;
import com.unicom.urban.management.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-8:52
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class AttendanceService {
    private final static DateTimeFormatter DF;
    private final static DateTimeFormatter FMT;

    static {
        DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private TimePlanRepository timePlanRepository;
    @Autowired
    private DayRepository dayRepository;


    public void save(AttendanceDTO attendanceDTO) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        List<TimePlan> timePlans = timePlanRepository.findAllByStartTimeIsBeforeAndEndTimeIsAfterAndSts(localDate, localDate, TimePlan.Status.ENABLE);
        if (timePlans.size() == 0) {
            throw new RuntimeException("今日时间不在计时规范中");
        }
        User user = SecurityUtil.getUser().castToUser();
        /*判断是不是工作日*/
        String type;
        if (this.isWorkingDay(now)) {
            type = this.isWorking(timePlans.get(0),attendanceDTO.getAttendanceType());
        } else {
            type = "4";
        }
        Attendance attendance;
        /*判断如果是下班卡 看是不是已经打过下班卡了如果打过了就更新下班打卡数据*/
        if("1".equals(attendanceDTO.getAttendanceType())){
            Attendance attendances = attendanceRepository.findByUser_IdAndAttendanceDateIsAfterAndAttendanceType(user.getId(),LocalDateTime.of(LocalDate.now(), LocalTime.MAX),"1");
            if(attendances!=null){
                attendance = Attendance.builder()
                        .id(attendances.getId())
                        .attendanceType(attendanceDTO.getAttendanceType())
                        .user(user)
                        .attendanceDate(LocalDateTime.now())
                        .address(attendanceDTO.getAddress())
                        .x(attendanceDTO.getX())
                        .y(attendanceDTO.getY())
                        .type(type)
                        .build();
            }else{
                attendance = Attendance.builder()
                        .attendanceType(attendanceDTO.getAttendanceType())
                        .user(user)
                        .attendanceDate(LocalDateTime.now())
                        .address(attendanceDTO.getAddress())
                        .x(attendanceDTO.getX())
                        .y(attendanceDTO.getY())
                        .type(type)
                        .build();
            }
        }else{
            attendance = Attendance.builder()
                    .attendanceType(attendanceDTO.getAttendanceType())
                    .user(SecurityUtil.getUser().castToUser())
                    .attendanceDate(LocalDateTime.now())
                    .address(attendanceDTO.getAddress())
                    .x(attendanceDTO.getX())
                    .y(attendanceDTO.getY())
                    .type(type)
                    .build();
        }
        attendanceRepository.save(attendance);
    }


    private boolean isWorkingDay(LocalDateTime now) {
        LocalDate localDate = now.toLocalDate();
        Day day = dayRepository.getByCalendar(localDate).orElse(new Day());
        if (StringUtils.isBlank(day.getId())) {
            throw new RuntimeException("今日时间不在工作日计时规范中");
        }
        return day.isWorkDay();
    }

    private String isWorking(TimePlan timePlan, String attendanceType) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime localTime = now.toLocalTime();
        List<TimeScheme> timeSchemeList = timePlan.getTimeSchemeList();
        timeSchemeList.sort((a, b) -> b.getStartTime().compareTo(a.getStartTime()));
        /*上班*/
        if ("0".equals(attendanceType)) {
            if (localTime.isAfter(timeSchemeList.get(0).getStartTime())) {
                return "0";
            } else {
                return "10";
            }
        } else {
            /*下班*/
            if (localTime.isBefore(timeSchemeList.get(timeSchemeList.size()-1).getEndTime())) {
                return "0";
            } else {
                return "2";
            }
        }
    }


    public String isCommuting(){
        User user = SecurityUtil.getUser().castToUser();
        Attendance attendance = attendanceRepository.findByUser_IdAndAttendanceDateIsAfterAndAttendanceType(user.getId(),LocalDateTime.of(LocalDate.now(), LocalTime.MAX), "0");
        if(attendance == null){
            return "0";
        }else{
            return "1";
        }
    }
}
