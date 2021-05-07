package com.unicom.urban.management.dao.attendance;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.AttendanceScheme;

import java.util.List;

/**
 * 考勤
 *
 * @author 顾志杰
 * @date 2021/3/23-8:50
 */
public interface AttendanceSchemeRepository extends CustomizeRepository<AttendanceScheme, String> {

    List<AttendanceScheme> findAllByDept_IdAndSts(String deptId,String sts);

}
