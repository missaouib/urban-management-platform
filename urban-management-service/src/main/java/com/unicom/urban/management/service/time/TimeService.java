package com.unicom.urban.management.service.time;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.dao.time.TimeSchemeRepository;
import com.unicom.urban.management.mapper.TimeMapper;
import com.unicom.urban.management.pojo.dto.DayDTO;
import com.unicom.urban.management.pojo.dto.TimePlanDTO;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.entity.time.TimeScheme;
import com.unicom.urban.management.pojo.vo.DayVo;
import com.unicom.urban.management.pojo.vo.TimeSchemeVO;
import com.unicom.urban.management.pojo.vo.TimeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TimeService {

    @Autowired
    private TimePlanRepository timePlanRepository;

    @Autowired
    private TimeSchemeRepository timeSchemeRepository;

    @Autowired
    private DayRepository dayRepository;

    @Transactional(rollbackFor = Exception.class)
    public Page<TimeVO> search(Pageable pageable) {
        Page<TimePlan> page = timePlanRepository.findAll(pageable);
        List<TimeVO> timeList = TimeMapper.INSTANCE.convertList(page.getContent());
        return new PageImpl<>(timeList, page.getPageable(), page.getTotalElements());
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(TimePlanDTO timePlanDTO) {
        if (timePlanDTO.getEndTime().isBefore(timePlanDTO.getStartTime())) {
            throw new DataValidException("结束时间不可在开始时间之前");
        }
        TimePlan timePlan = new TimePlan();
        timePlan.setName(timePlanDTO.getName());
        timePlan.setStartTime(timePlanDTO.getStartTime());
        timePlan.setEndTime(timePlanDTO.getEndTime());
        timePlan.setSts(TimePlan.Status.DISABLE);
        timePlanRepository.save(timePlan);
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<DayVo> search(String timeId, Pageable pageable) {
        Page<Day> page = dayRepository.findByTimePlanOrderByCalendar(new TimePlan(timeId), pageable);
        List<DayVo> list = TimeMapper.INSTANCE.convertDayList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());

    }

    @Transactional(rollbackFor = Exception.class)
    public void activation(String id, TimePlan.Status status) {
        // 激活
        if (TimePlan.Status.ENABLE.equals(status)) {
//            TimePlan timePlan = timePlanRepository.findById(id).orElseThrow(() -> new DataValidException("数据不存在"));
//            List<LocalDate> between = LocalDateTimeUtil.between(timePlan.getStartTime(), timePlan.getEndTime());
//
//            List<Day> dayList = dayRepository.findByTimePlanOrderByCalendar(new TimePlan(id));
//
//            if (between.size() != dayList.size()) {
//
//                for (int i = 0; i < dayList.size(); i++) {
//
//                }
//
//            }
            timePlanRepository.updateStatus(TimePlan.Status.DISABLE);
            timePlanRepository.updateStatus(TimePlan.Status.ENABLE, id);
        } else {
            timePlanRepository.updateStatus(TimePlan.Status.DISABLE, id);
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDay(DayDTO dayDTO) {
        if (dayRepository.existsByCalendarAndTimePlan(dayDTO.getCalendar(), new TimePlan(dayDTO.getTimePlanId()))) {
            throw new DataValidException(dayDTO.getCalendar() + " 日期已经设置 不可再次设置");
        }

        TimePlan timePlan = timePlanRepository.findById(dayDTO.getTimePlanId()).orElseThrow(() -> new DataValidException("计时不存在"));

        if (dayDTO.getCalendar().isBefore(timePlan.getStartTime()) || dayDTO.getCalendar().isAfter(timePlan.getEndTime())) {
            throw new DataValidException("日期不可超出 " + timePlan.getStartTime() + " - " + timePlan.getEndTime() + "范围");
        }

        Day day = new Day();
        day.setTimePlan(new TimePlan(dayDTO.getTimePlanId()));
        day.setCalendar(dayDTO.getCalendar());
        day.setWorkDayMark(dayDTO.getWorkDayMark());
        day.setWork(dayDTO.getWork());
        dayRepository.save(day);

    }


    @Transactional(rollbackFor = Exception.class)
    public void updateDay(DayDTO dayDTO) {
        Optional<Day> optional = dayRepository.findById(dayDTO.getId());
        if (optional.isPresent()) {
            Day day = optional.get();
            day.setWork(dayDTO.getWork());
            day.setWorkDayMark(dayDTO.getWorkDayMark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<TimeSchemeVO> queryTimeScheme(String id) {
        Optional<TimePlan> optional = timePlanRepository.findById(id);
        if (optional.isPresent()) {
            List<TimeScheme> timeSchemeList = optional.get().getTimeSchemeList();
            return TimeMapper.INSTANCE.convertSchemeList(timeSchemeList);

        }
        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void setTime(String id, String time) {
        String[] timeStr = StringUtils.delimitedListToStringArray(time, ",");
        Optional<TimePlan> optional = timePlanRepository.findById(id);
        if (optional.isPresent()) {
            timeSchemeRepository.deleteByTimePlan(optional.get());
            for (String str : timeStr) {
                LocalTime startTime = LocalTime.parse(str);
                TimeScheme timeScheme = new TimeScheme();
                timeScheme.setStartTime(startTime);
                timeScheme.setEndTime(startTime.plusMinutes(30));
                timeScheme.setTimePlan(optional.get());
                optional.get().addTimeScheme(timeScheme);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(String id) {
        Optional<TimePlan> optional = timePlanRepository.findById(id);

        if (optional.isPresent()) {
            if (optional.get().isEnable()) {
                throw new DataValidException("启用中 不可被删除");
            }
            timePlanRepository.delete(optional.get());
        }

    }
}
