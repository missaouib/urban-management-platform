package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Publish;
import com.unicom.urban.management.pojo.vo.PublishVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 网格mapper
 *
 * @author jiangwen
 */
@Mapper
public interface PublishMapper {

    PublishMapper INSTANCE = Mappers.getMapper(PublishMapper.class);

    /**
     * 转list
     *
     * @param publishList list
     * @return vo
     */
    List<PublishVO> publishListToPublishVOList(List<Publish> publishList);

    /**
     * 转
     *
     * @param publish 实体
     * @return vo
     */
    PublishVO publishToPublishVO(Publish publish);

}
