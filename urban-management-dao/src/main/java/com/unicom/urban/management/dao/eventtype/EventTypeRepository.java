package com.unicom.urban.management.dao.eventtype;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.EventType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/13-19:33
 */
public interface EventTypeRepository extends CustomizeRepository<EventType, String> {
    List<EventType> findAllByType(int type);

    List<EventType> findAllByLevelAndParent_Name(String level,String pName);

}
