package com.unicom.urban.management.web.framework.activiti;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.pojo.vo.DeploymentVO;
import com.unicom.urban.management.service.activiti.DeploymentService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程部署控制器
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class DeploymentController {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private DeploymentService deploymentService;

    /**
     * 跳转到流程部署页面
     */
    @GetMapping("/deployment")
    public ModelAndView deployment() {
        return new ModelAndView(SystemConstant.PAGE + "/deployment/deployment");
    }

    @GetMapping("/deployment/search")
    public Page<DeploymentVO> search(@PageableDefault Pageable pageable) {
        return deploymentService.search(pageable);
    }


    @PostMapping("/deployment/text")
    public void addDeploymentByString(@RequestParam String text) {
        Deployment deployment = repositoryService.createDeployment()
                .addString("CreateWithBPMNJS.bpmn", text)
                .name("不知道在哪显示的部署名称")
                .deploy();
    }

    /**
     * 上传流程定义文件
     */
    @PostMapping("/deployment/file")
    public void uploadProcessDefinitionAndDeployment(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try {
            String fileSuffix = FilenameUtils.getExtension(originalFilename);

            if (StringUtils.equalsIgnoreCase(fileSuffix, "zip")) {
                ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
                repositoryService.createDeployment().addZipInputStream(zipInputStream).name(originalFilename).deploy().getId();

            } else {
                repositoryService.createDeployment().addInputStream(originalFilename, file.getInputStream()).name(originalFilename).deploy().getId();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("上传流程文件出现异常", e);
        }
    }

    @GetMapping("/deployment/{deploymentId}")
    public List<String> asdf(@PathVariable String deploymentId) {
        List<String> deploymentResourceNames = repositoryService.getDeploymentResourceNames(deploymentId);
//        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).list();
//
//        return ProcessDefinitionMapper.INSTANCE.ProcessDefinitionListToProcessDefinitionVOList(list);

        for (String deploymentResourceName : deploymentResourceNames) {
            repositoryService.createProcessDefinitionQuery().processDefinitionResourceName(deploymentResourceName).list();

        }



        return deploymentResourceNames;
    }

    @PostMapping("/deployment/remove")
    public void remove(String ids) {
        deploymentService.remove(ids);
    }

}
