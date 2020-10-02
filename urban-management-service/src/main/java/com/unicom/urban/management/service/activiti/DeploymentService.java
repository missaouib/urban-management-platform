package com.unicom.urban.management.service.activiti;

import com.unicom.urban.management.mapper.DeploymentMapper;
import com.unicom.urban.management.pojo.vo.DeploymentVO;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeploymentService {


    @Autowired
    private RepositoryService repositoryService;

    public Page<DeploymentVO> search(Pageable pageable) {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().
                listPage(pageable.getPageNumber(), pageable.getPageSize());

        List<DeploymentVO> deploymentVOS = DeploymentMapper.INSTANCE.deploymentListToDeloymentVOList(deployments);

        return new PageImpl<>(deploymentVOS);
    }

    public void remove(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId);
    }


}
