package com.unicom.urban.management.service.event;

import com.unicom.urban.management.service.activiti.ActivitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用工作流
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class WorkService {

    @Autowired
    private ActivitiService activitiService;

    public void reportEvent(String eventId) {
        /*TODO 查询所有有受理员角色的人*/
        List<String> userList = new ArrayList<>();
        userList.add("userId");
        activitiService.reportEvent(eventId, userList);
    }

}
