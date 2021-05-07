package com.unicom.urban.management.web.framework.common;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 通用请求Controller
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
@Slf4j
public class CommonController {


    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public Map<String, Object> uploadFile(MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<>(1);
        String uploadFile = fileUploadUtil.uploadFileToFastDFS(file);
        map.put("fileName", file.getOriginalFilename());
        map.put("url", uploadFile);
        return map;
    }

    @PostMapping("/common/uploads")
    public List<Map<String, Object>> uploadFiles(MultipartFile[] files) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> map = new HashMap<>(3);
            String url = fileUploadUtil.uploadFileToFastDFS(file);
            map.put("url", url);
            map.put("fileName", file.getOriginalFilename());
            list.add(map);
        }
        return list;
    }


    @GetMapping("/common/img/{id}")
    public void getImageInputStream(@PathVariable("id") String processInstanceId, HttpServletResponse response) throws IOException {
        //获得流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = StringUtils.EMPTY;
        if (processInstance == null) {
            //查询已经结束的流程实例
            HistoricProcessInstance processInstanceHistory = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (processInstanceHistory == null) {
            } else {
                processDefinitionId = processInstanceHistory.getProcessDefinitionId();
            }
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        String fontName = processEngineConfiguration.getActivityFontName();
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        //获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = Collections.EMPTY_LIST;
        if (processInstance != null) {
            currentActs = runtimeService.getActiveActivityIds(processInstance.getId());
        }


        // 获得历史活动记录实体（通过启动时间正序排序，不然有的线可以绘制不出来）
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        // 计算活动线
        List<String> highLightedFlows = this.getHighLightedFlows((ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId), historicActivityInstances);

        InputStream in = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(model, "png", currentActs, highLightedFlows, fontName, fontName, fontName, null, 1.0);

        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        // 4：将输入流中的数据读取出来，写到输出流中
        for (int b = -1; (b = in.read()) != -1; ) {
            out.write(b);
        }
        out.close();
        in.close();


//        return inputStream;

    }


    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {

        // 用以保存高亮的线flowId
        List<String> highFlows = new ArrayList<String>();
        // 对历史流程节点进行遍历
        for (int i = 0; i < historicActivityInstances.size(); i++) {
            // 得 到节点定义的详细信息
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());
            // 用以保存后需开始时间相同的节点
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();
            if ((i + 1) >= historicActivityInstances.size()) {
                break;
            }
            // 将后面第一个节点放在时间相同节点的集合里
            ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                // 后续第一个节点
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
                // 后续第二个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);
                // 如果第一个节点和第二个节点开始时间相同保存
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            // 取出节点的所有出去的线
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
            // 对所有的线进行遍历
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }


}
