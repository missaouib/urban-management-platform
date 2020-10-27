package com.unicom.urban.management.web.framework.activiti;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.vo.ProcessDefinitionVO;
import com.unicom.urban.management.service.processdef.ProcessDefService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 流程定义管理
 *
 * @author liukai
 */
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


    @GetMapping("/process/{deploymentId}/{resourceName}")
    public void afd(@PathVariable String deploymentId, @PathVariable String resourceName, HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
//            response.setContentType("text/xml");
            response.setContentType("image/png");
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

}
