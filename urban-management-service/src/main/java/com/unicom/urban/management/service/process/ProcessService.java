package com.unicom.urban.management.service.process;

import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.ProcessConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.process.ProcessRepository;
import com.unicom.urban.management.pojo.entity.Process;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private StatisticsService statisticsService;

    public int findAllByNodeNameAndParentIsNotNull(String eventId) {
        Statistics byEventIdAndEndTimeIsNull = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        String taskName;
        if (byEventIdAndEndTimeIsNull != null) {
            taskName = byEventIdAndEndTimeIsNull.getTaskName();
        } else {
            taskName = EventConstant.CLOSE_TASK;
        }
        List<Process> processList = processRepository.findAllByNodeNameAndParentIsNotNull(taskName);
        int index;
        if (processList.size() == 1) {
            switch (processList.get(0).getParent().getNodeName()) {
                case ProcessConstant.INFORMATION_GATHERING:
                    index = 0;
                    break;
                case ProcessConstant.CASE_ESTABLISHMENT:
                    index = 1;
                    break;
                case ProcessConstant.MISSION_DISPATCH:
                    index = 2;
                    break;
                case ProcessConstant.TASK_PROCESSING:
                    index = 5;
                    break;
                case ProcessConstant.HANDLING_FEEDBACK:
                    index = 4;
                    break;
                case ProcessConstant.CHECK_CLOSE_CASE:
                    index = 3;
                    break;
                default:
                    throw new DataValidException("环节数据出现错误");
            }
        } else {
            throw new DataValidException("环节数据出现异常");
        }
        return index;
    }

    public List<Process> findAll(){
        return processRepository.findAll();
    }


}
