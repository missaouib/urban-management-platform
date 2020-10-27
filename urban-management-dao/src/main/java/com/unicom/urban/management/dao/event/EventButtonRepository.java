package com.unicom.urban.management.dao.event;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.EventButton;

import java.util.List;

public interface EventButtonRepository extends CustomizeRepository<EventButton, String> {

    List<EventButton> findByTaskName(String taskName);

}
