package com.unicom.urban.management.mapper;

import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.vo.RecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 编辑mapper
 *
 * @author jiangwen
 */
@Mapper
public interface RecordMapper {

    RecordMapper INSTANCE = Mappers.getMapper(RecordMapper.class);

    /**
     * 转list
     *
     * @param recordList list
     * @return vo
     */
    List<RecordVO> convertList(List<Record> recordList);

    /**
     * 转
     *
     * @param record 实体
     * @return vo
     */
    @Mapping(source = "publish.kv.value", target = "type")
    RecordVO convert(Record record);

}
