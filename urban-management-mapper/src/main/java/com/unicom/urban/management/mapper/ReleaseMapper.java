package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.dto.ReleaseDTO;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.entity.Release;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 发布mapper
 * 编辑mapper
 * 网格mapper
 *
 * @author jiangwen
 */
@Mapper
public interface ReleaseMapper {

    ReleaseMapper INSTANCE = Mappers.getMapper(ReleaseMapper.class);

    /**
     * dto转Release
     *
     * @param releaseDTO dto
     * @return Release
     */
    Release ReleaseDTOToRelease(ReleaseDTO releaseDTO);

    /**
     * dto转Record
     *
     * @param releaseDTO dto
     * @return Record
     */
    Record ReleaseDTOToRecord(ReleaseDTO releaseDTO);

    /**
     * dto转Grid
     *
     * @param releaseDTO dto
     * @return Grid
     */
    Grid ReleaseDTOToGrid(ReleaseDTO releaseDTO);

}
