package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.web.WebApplication;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    /**
     * 监督员上报测试
     */
    @Test
    @Transactional
    public void superviseReportingTest() {
        List<String> userIdList = Arrays.asList("受理员1", "受理员2", "受理员3", "受理员4");
        ProcessInstance processInstance = activitiService.superviseReporting("1", userIdList);

        Task task = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());

        // 信息收集 领取完成任务
        activitiService.claim(task.getId(), "受理员1");
        activitiService.complete(task.getId(), Arrays.asList("值班长1", "值班长2", "值班长3", "值班长4"), "3");


        // 值班长-立案 领取完成任务
        Task zhibanzhang = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.claim(zhibanzhang.getId(), "值班长1");
        activitiService.complete(zhibanzhang.getId(), Arrays.asList("派遣员1", "派遣员2", "派遣员3", "派遣员4"), "4");


        // 派遣员-派遣 派给专业部门
        Task paiqianyuan = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.claim(paiqianyuan.getId(), "派遣员1");
        activitiService.complete(paiqianyuan.getId(), Arrays.asList("专业部门1", "专业部门2", "专业部门3", "专业部门4"), "6");

        // 专业部门 领取完成任务
        Task zhuangyebumen = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.claim(zhuangyebumen.getId(), "专业部门1");
        activitiService.complete(zhuangyebumen.getId(), Arrays.asList("受理员1", "受理员2", "受理员3", "受理员4"), "7");

        // 受理员-核查 领取完成任务
        Task shouliyuanhecha = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.claim(shouliyuanhecha.getId(), "受理员1");
        activitiService.complete(shouliyuanhecha.getId(), Arrays.asList("监督员1", "监督员2", "监督员3", "监督员4"), "8");

        // 监督员-案件核查 完成任务
        Task jianduyuan = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.complete(jianduyuan.getId(), Arrays.asList("受理员1", "受理员2", "受理员3", "受理员4"), "9");

        // 受理员 领取完成任务
        Task shouliyuan = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.claim(shouliyuan.getId(), "受理员1");
        activitiService.complete(shouliyuan.getId(), Arrays.asList("值班长1", "值班长2", "值班长3", "值班长4"), "10");

        // 值班长 领取完成任务
        Task zhibanzhang2 = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.claim(zhibanzhang2.getId(), "值班长1");
        activitiService.complete(zhibanzhang2.getId(), null, null);

        // 流程结束
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();

        System.out.println("已完成的流程实例数量: " + count);


    }

    /**
     * 查询我的待办
     */
    @Test
    public void queryMyTask() {
        List<String> taskList = activitiService.queryTaskByAssignee("1");
    }

    /**
     * 受理员上报
     */
    @Test
    @Transactional
    public void acceptanceReporting() {
        List<String> userIdList = Arrays.asList("受理员1", "受理员2", "受理员3", "受理员4");
        ProcessInstance processInstance = activitiService.acceptanceReporting("1", userIdList);

        Task anjiandengji = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        // 受理员-案件登记 领取完成任务
        activitiService.claim(anjiandengji.getId(), "受理员1");
        activitiService.complete(anjiandengji.getId(), Arrays.asList("监督员1", "监督员2", "监督员3", "监督员4"), "100");

        // 监督员-信息核实 完成任务
        Task xinxiheshi = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.complete(xinxiheshi.getId(), Arrays.asList("受理员1", "受理员2", "受理员3", "受理员4"), "101");

        // 信息收集 领取完成任务
        Task xinxishouji = activitiService.getTaskByProcessInstanceId(processInstance.getProcessInstanceId());
        activitiService.claim(xinxishouji.getId(), "受理员1");
        activitiService.complete(xinxishouji.getId(), Arrays.asList("值班长1", "值班长2", "值班长3", "值班长4"), "3");

        System.out.println(xinxiheshi.getProcessInstanceId());

    }


}
