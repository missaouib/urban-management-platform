package com.unicom.urban.management.web.framework.activiti;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.pojo.vo.ProcessDefinitionVO;
import com.unicom.urban.management.service.processdef.ProcessDefService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程定义管理
 *
 * @author liukai
 */
@Slf4j
@RestController
@ResponseResultBody
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessDefService processDefService;


    @GetMapping("/processdef")
    public ModelAndView processDefinitions() {
        return new ModelAndView(SystemConstant.PAGE + "/processdef/processdef");
    }

    @GetMapping("/processdef/model")
    public ModelAndView model() {
        return new ModelAndView(SystemConstant.PAGE + "/processdef/model");
    }


    /**
     * 流程定义列表
     */
    @GetMapping("/processdef/search")
    public Page<ProcessDefinitionVO> processDefinitionList(@PageableDefault Pageable pageable) {
        return processDefService.search(pageable);
    }

    @PostMapping("/processdef/remove")
    public void delete(String ids) {
        repositoryService.deleteDeployment(ids, true);
    }


    /**
     * 获取流程定义xml格式
     */
    @GetMapping("/process/{deploymentId}/{resourceName}")
    public Map<String, Object> getProcessXML(@PathVariable String deploymentId, @PathVariable String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            String xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
            Map<String, Object> map = new HashMap<>(3);
            map.put("xml", xml);
            map.put("resourceName", resourceName);
            return map;
        } catch (Exception e) {
            log.error("读取工作流定义文件失败", e);
            throw new BusinessException("读取工作流定义文件失败");
        }

    }

}
