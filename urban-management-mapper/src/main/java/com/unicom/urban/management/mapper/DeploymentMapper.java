package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.vo.DeploymentVO;
import org.activiti.engine.repository.Deployment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeploymentMapper {

    DeploymentMapper INSTANCE = Mappers.getMapper(DeploymentMapper.class);

    List<DeploymentVO> deploymentListToDeloymentVOList(List<Deployment> deploymentList);



    @Mapping(target = "deploymentTime", expression = "java(java.time.LocalDateTime.ofInstant( deployment.getDeploymentTime().toInstant(), java.time.ZoneId.of( \"GMT+8\" ) ))")
    DeploymentVO deploymentToDeploymentVO(Deployment deployment);

}
