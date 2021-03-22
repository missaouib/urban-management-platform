package com.unicom.urban.management.service.timeplan;

import com.unicom.urban.management.dao.time.DayRepository;
import com.unicom.urban.management.dao.time.TimePlanRepository;
import com.unicom.urban.management.pojo.dto.TimePlanDTO;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimePlanService {

    @Autowired
    private TimePlanRepository timePlanRepository;

    @Autowired
    private DayRepository calendarRepository;


    public void save(TimePlanDTO timePlanDTO) {
        TimePlan timePlan = new TimePlan();
        timePlan.setSts(timePlanDTO.getStatus());
        timePlanRepository.save(timePlan);
    }

}
