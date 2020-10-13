package com.unicom.urban.management.service.processdef;

import com.unicom.urban.management.mapper.DeploymentMapper;
import com.unicom.urban.management.pojo.vo.ProcessDefinitionVO;
import org.activiti.engine.RepositoryService;
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

        List<ProcessDefinition> deployments = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().
                list();
//                listPage(pageable.getPageNumber(), pageable.getPageSize());

        List<ProcessDefinitionVO> processDefinitionVOList = DeploymentMapper.INSTANCE.processDefToprocessDefVO(deployments);

        return new PageImpl<>(processDefinitionVOList);
    }

}
