package com.unicom.urban.management.service.time;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.mapper.TimeMapper;
import com.unicom.urban.management.pojo.dto.DayDTO;
import com.unicom.urban.management.pojo.dto.TimePlanDTO;
import com.unicom.urban.management.pojo.entity.time.Day;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.vo.DayVo;
import com.unicom.urban.management.pojo.vo.TimeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TimeService {

    @Autowired
    private TimePlanRepository timePlanRepository;

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
        if (TimePlan.Status.ENABLE.equals(status)) {
            List<TimePlan> timePlanList = timePlanRepository.findAll();
            for (TimePlan timePlan : timePlanList) {
                if (id.equals(timePlan.getId())) {
                    timePlan.setSts(TimePlan.Status.ENABLE);
                } else {
                    timePlan.setSts(TimePlan.Status.DISABLE);
                }
            }
        } else {
            Optional<TimePlan> option = timePlanRepository.findById(id);
            option.ifPresent(TimePlan::disable);
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDay(DayDTO dayDTO) {
        Optional<TimePlan> optional = timePlanRepository.findById(dayDTO.getTimePlanId());
        if (optional.isPresent()) {
            if (optional.get().hasDay(dayDTO.getCalendar())) {
                throw new DataValidException(dayDTO.getCalendar() + " 日期已经设置 不可再次设置");
            }
            Day day = new Day();
            day.setCalendar(dayDTO.getCalendar());
            day.setWorkDayMark(dayDTO.getWorkDayMark());
            day.setWork(dayDTO.getWork());
            optional.get().addDay(day);
        }
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

}
