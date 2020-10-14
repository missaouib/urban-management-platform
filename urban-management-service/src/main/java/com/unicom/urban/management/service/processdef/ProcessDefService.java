package com.unicom.urban.management.service.processdef;

import com.unicom.urban.management.mapper.DeploymentMapper;
import com.unicom.urban.management.pojo.vo.ProcessDefinitionVO;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程定义管理Service
 *
 * @author liukai
 */
@Service
public class ProcessDefService {


    @Autowired
    private RepositoryService repositoryService;


    public Page<ProcessDefinitionVO> search(Pageable pageable) {

        List<Model> list = repositoryService.createModelQuery().list();

        List<ProcessDefinition> deployments = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionId().desc()
                .orderByProcessDefinitionVersion().desc()
                .list();


        List<ProcessDefinitionVO> processDefinitionVOList = DeploymentMapper.INSTANCE.processDefToprocessDefVO(deployments);


        for (ProcessDefinitionVO processDefinitionVO : processDefinitionVOList) {
            for (Model model : list) {
                if (processDefinitionVO.getDeploymentId().equals(model.getDeploymentId())) {
                    processDefinitionVO.setModelerId(model.getId());
                }
            }
        }

        return new PageImpl<>(processDefinitionVOList);
    }

}
