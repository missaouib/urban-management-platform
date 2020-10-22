package com.unicom.urban.management.dao.release;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Publish;

import java.util.List;

/**
 * 发布dao
 *
 * @author jiangwen
 */
public interface PublishRepository extends CustomizeRepository<Publish, String> {

    /**
     * 根据kvId查询 网格发布
     *
     * @param kvId id
     * @return 发布
     */
    List<Publish> findAllByKv_Id(String kvId);

    /**
     * 根据kvId 和 部件分类id查询 发布
     *
     * @param kvId            id
     * @param eventTypeId id
     * @return 发布
     */
    List<Publish> findAllByKv_IdAndEventType_id(String kvId, String eventTypeId);

}
