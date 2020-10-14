package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Component;
import com.unicom.urban.management.pojo.entity.ComponentInfo;
import com.unicom.urban.management.pojo.vo.ComponentInfoVO;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/14-10:08
 */

@Mapper
public interface ComponentInfoMapper {
    ComponentInfoMapper INSTANCE = Mappers.getMapper(ComponentInfoMapper.class);

    /**
     * list转换
     * @param componentInfoList from
     * @return list
     */
    List<ComponentInfoVO> componentInfoListToComponentInfoVOList(List<ComponentInfo> componentInfoList);

    /**
     * 实体转换
     * @param componentInfo from
     * @return class
     */
    ComponentInfoVO componentInfoToComponentInfoVO(ComponentInfo componentInfo);
}
