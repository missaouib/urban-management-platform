package com.unicom.urban.management.dao.component;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Component;

/**
 * @author 顾志杰
 * @date 2020/10/13-19:33
 */
public interface ComponentRepository extends CustomizeRepository<Component, String> {
    Component findByRecord_Id(String recordId);
}
