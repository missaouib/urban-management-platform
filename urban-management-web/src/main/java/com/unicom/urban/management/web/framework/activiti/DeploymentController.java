package com.unicom.urban.management.web.framework.activiti;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.exception.BusinessException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * 部署控制器
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
public class DeploymentController {


    @Autowired
    private RepositoryService repositoryService;


    @PostMapping(value = "/deployment/text")
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
                repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy().getId();

            } else {
                repositoryService.createDeployment().addInputStream(originalFilename, file.getInputStream()).deploy().getId();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("上传流程文件出现异常");
        }
    }

}
