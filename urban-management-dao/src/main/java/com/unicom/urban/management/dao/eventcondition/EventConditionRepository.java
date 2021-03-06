package com.unicom.urban.management.dao.eventcondition;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.EventCondition;

import java.util.List;

/**
 * 立案条件
 *
 * @author liubozhi
 */
public interface EventConditionRepository extends CustomizeRepository<EventCondition, String> {
    List<EventCondition> findAllByEventTypeId_IdAndTypeAndParentIsNull(String eventTypeId, int type);

    List<EventCondition> findAllByParent_Id(String regionId);

    /**
     * 通过eventTypeId查询大分类(例：一类区域)
     *
     * @param eventTypeId id
     * @return 数据
     */
    List<EventCondition> findAllByEventType_IdAndRegionIsNotNull(String eventTypeId);

}
