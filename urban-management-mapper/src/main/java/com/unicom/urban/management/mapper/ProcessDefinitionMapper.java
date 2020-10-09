package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.vo.ProcessDefinitionVO;
import org.activiti.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProcessDefinitionMapper {

    ProcessDefinitionMapper INSTANCE = Mappers.getMapper(ProcessDefinitionMapper.class);

    List<ProcessDefinitionVO> ProcessDefinitionListToProcessDefinitionVOList(List<ProcessDefinition> processDefinitionList);

}
