package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.web.WebApplication;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = WebApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
@WithMockUser(username = "admin", password = "123433356")
public class ActivitiServiceImplTest {

    @Autowired
    private ActivitiService activitiService;

    /**
     * 监督员上报测试
     */
    @Test
    @Transactional
    public void superviseReportingTest() {
        List<String> userIdList = Arrays.asList("受理员1", "受理员2", "受理员3", "受理员4");
        ProcessInstance processInstance = activitiService.superviseReporting("1", userIdList);


        Task heshifankui = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());

        System.out.println(heshifankui.getId());
        System.out.println(heshifankui.getName());


        System.out.println(activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId()));
        activitiService.claim(heshifankui.getId(), "受理员1");
        System.out.println(activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId()));

        activitiService.complete(heshifankui.getId(), Arrays.asList("值班长1", "值班长2", "值班长3", "值班长4"), "3");

        System.out.println(activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId()));

    }

    /**
     * 受理员上报
     */
    @Test
    @Transactional
    public void acceptanceReporting() {
        List<String> userIdList = Arrays.asList("受理员1", "受理员2", "受理员3", "受理员4");
        activitiService.acceptanceReporting("1", userIdList);
    }


}
