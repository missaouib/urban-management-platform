package com.unicom.urban.management.web.framework.activiti;

//@Slf4j
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@WithMockUser(username = "admin", password = "123433356")
public class ActivitiTest {


//    @Test
//    public void testProcessEngines() {
//        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
//
//
//        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
//
//        TaskService taskService = defaultProcessEngine.getTaskService();
//
//        HistoryService historyService = defaultProcessEngine.getHistoryService();
//
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("userId", "111");
//        map.put("zhibanzhang", "222");
//        map.put("zhifarenyuan", "333");
//
//        ProcessInstance event = runtimeService.startProcessInstanceByKey("event", "001", map);
//
//        Task zhibanzhang = taskService.createTaskQuery().taskAssignee("222").singleResult();
//
//
//        taskService.complete(zhibanzhang.getId());
//
//
//        Task zhifarenyuan = taskService.createTaskQuery().taskAssignee("333").singleResult();
//
//        taskService.complete(zhifarenyuan.getId());
//
//
//        long count = historyService.createHistoricTaskInstanceQuery().count();
//
//        System.out.println("count = " + count);
//
//
//    }


}
