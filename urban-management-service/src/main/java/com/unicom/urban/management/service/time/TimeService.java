package com.unicom.urban.management.service.time;

import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.mapper.TimeMapper;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.vo.TimeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void save() {
        TimePlan timePlan = new TimePlan();
//        timePlan.setStartTime();
//        timePlan.setEndTime();
        timePlanRepository.save(timePlan);
    }

}
