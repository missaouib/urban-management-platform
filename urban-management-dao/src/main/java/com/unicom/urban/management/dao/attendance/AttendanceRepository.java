package com.unicom.urban.management.dao.attendance;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Attendance;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-8:50
 */
public interface AttendanceRepository extends CustomizeRepository<Attendance, String> {
    Attendance findByUser_IdAndAttendanceDateIsAfterAndAttendanceType(String user, LocalDateTime localDateTime, String attendanceType);

}
