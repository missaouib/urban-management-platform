package com.unicom.urban.management.dao.record;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Record;

import java.util.List;

/**
 * 编辑dao
 *
 * @author jiangwen
 */
public interface RecordRepository extends CustomizeRepository<Record, String> {

    /**
     * 根据发布id查询下面的元素
     *
     * @param id 主键
     * @return 元素
     */
    List<Record> findAllByPublish_Id(String id);

    /**
     * 根据发布id查询编辑中的元素
     *
     * @param id
     * @param sts
     * @return
     */
    List<Record> findAllByPublish_IdAndSts(String id, int sts);

}
