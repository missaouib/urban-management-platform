package com.unicom.urban.management.service.attendance;

import com.unicom.urban.management.dao.attendance.AttendanceRepository;
import com.unicom.urban.management.dao.attendance.AttendanceSchemeRepository;
import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.pojo.dto.AttendanceDTO;
import com.unicom.urban.management.pojo.dto.AttendanceSchemeDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.entity.time.TimeScheme;
import com.unicom.urban.management.pojo.vo.AttendanceSchemeVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
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


    public void saveAndFlush(AttendanceSchemeDTO attendanceSchemeDTO){
        Dept dept = new Dept();
        dept.setId(attendanceSchemeDTO.getDeptId());
        AttendanceScheme attendanceScheme = AttendanceScheme.builder()
                .id(attendanceSchemeDTO.getId())
                .address(attendanceSchemeDTO.getAddress())
                .dept(dept)
                .x(attendanceSchemeDTO.getX())
                .y(attendanceSchemeDTO.getY())
                .sts("0")
                .radius(attendanceSchemeDTO.getRadius())
                .build();
        attendanceSchemeRepository.saveAndFlush(attendanceScheme);
    }


    public List<AttendanceSchemeVO> getAllByDept(){
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

    public Page<AttendanceSchemeVO> getAllByPage(AttendanceSchemeDTO attendanceSchemeDTO, Pageable pageable){
        Specification<AttendanceScheme> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if(StringUtils.isNotBlank(attendanceSchemeDTO.getDeptId())){
                list.add(criteriaBuilder.equal(root.get("dept").get("id").as(String.class), attendanceSchemeDTO.getDeptId()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        Page<AttendanceScheme> page = attendanceSchemeRepository.findAll(specification, pageable);
        List<AttendanceSchemeVO> attendanceSchemeVOS = new ArrayList<>();
        page.getContent().forEach(a->{
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
        return new PageImpl<>(attendanceSchemeVOS, page.getPageable(), page.getTotalElements());
    }
}
