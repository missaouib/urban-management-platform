package com.unicom.urban.management.web.project.activiti;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程定义
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


//    @Autowired
//    private ProcessDefinitionMapStruct processDefinitionMapStruct;


    @GetMapping("/processdef")
    public ModelAndView processDefinitions() {
        return new ModelAndView(SystemConstant.PAGE + "/processdef/processdef");
    }


    /**
     * 流程定义列表
     */
    @GetMapping("/processdef/search")
    public Map<String, Object> processDefinitionList() {

        Map<String, Object> map = new HashMap<>();

        List<HashMap<String, Object>> listMap = new ArrayList<>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().list();

        for (ProcessDefinition pd : list) {
            HashMap<String, Object> hashMap = new HashMap<>();
            //System.out.println("流程定义ID："+pd.getId());
            hashMap.put("processDefinitionID", pd.getId());
            hashMap.put("name", pd.getName());
            hashMap.put("key", pd.getKey());
            hashMap.put("resourceName", pd.getResourceName());
            hashMap.put("deploymentID", pd.getDeploymentId());
            hashMap.put("version", pd.getVersion());
            listMap.add(hashMap);
        }


        map.put("content", listMap);

        return map;
    }


    @PostMapping("/processdefXML")
    public void getProcessDefinition(HttpServletResponse response, @RequestParam("deploymentId") String deploymentId, @RequestParam("resourceName") String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/processdef/remove")
    public void delete(String ids) {
        repositoryService.deleteDeployment(ids, false);
    }


}
