package com.unicom.urban.management.dao.supervise;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Supervise;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/11/19-13:42
 */
public interface SuperviseRepository extends CustomizeRepository<Supervise, String> {
    List<Supervise> findAllByEvent_Id(String eventId);
}
