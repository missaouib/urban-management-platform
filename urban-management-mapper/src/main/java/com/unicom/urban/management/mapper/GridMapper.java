package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.GridDTO;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.vo.GridVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 网格mapper
 *
 * @author jiangwen
 */
@Mapper
public interface GridMapper {

    GridMapper INSTANCE = Mappers.getMapper(GridMapper.class);

    /**
     * 转list
     *
     * @param gridList list
     * @return vo
     */
    List<GridVO> gridListToGridVOList(List<Grid> gridList);

    /**
     * 转
     *
     * @param grid 实体
     * @return vo
     */
    @Mapping(source = "record.coordinate", target = "coordinate")
    GridVO gridToGridVO(Grid grid);

    /**
     * 转实体 保存修改
     *
     * @param gridDTO dto
     * @return 实体
     */
    Grid gridDTOToGrid(GridDTO gridDTO);

    /**
     * dto转Record
     *
     * @param gridDTO dto
     * @return Record
     */
    Record gridDTOToRecord(GridDTO gridDTO);

}
