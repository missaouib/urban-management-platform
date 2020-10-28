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
    List<EventCondition> findAllByEventTypeId_IdAndParentIsNull(String eventTypeId);
    List<EventCondition> findAllByParent_Id(String regionId);
}
