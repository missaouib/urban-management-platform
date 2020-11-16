package com.unicom.urban.management.dao.eventfile;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.EventFile;

/**
 * 附件
 *
 * @author jiangwen
 */
public interface EventFileRepository extends CustomizeRepository<EventFile, String> {

    //void deleteByEvent_id(String eventId);
}
