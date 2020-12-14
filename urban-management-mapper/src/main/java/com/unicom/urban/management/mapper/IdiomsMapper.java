package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Idioms;
import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.pojo.vo.IdiomsVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 惯用语mapper
 *
 * @author liubozhi
 */
@Mapper
public interface IdiomsMapper {

    IdiomsMapper INSTANCE = Mappers.getMapper(IdiomsMapper.class);

    /**
     * 转list
     *
     * @param idiomsList list
     * @return vo
     */
    List<IdiomsVO> idiomsListToIdiomsVOList(List<Idioms> idiomsList);

    /**
     * 转
     *
     * @param idioms 实体
     * @return vo
     */
    @Mapping(source = "user.name", target = "userName")
    IdiomsVO idiomsToIdiomsVO(Idioms idioms);
}
