package com.unicom.urban.management.dao.process;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.Process;

import java.util.List;

public interface ProcessRepository extends CustomizeRepository<Process, String> {

    /**
     * 通过环节名称查询步骤
     *
     * @param nodeName 环节名称
     * @return list
     */
    List<Process> findAllByNodeNameAndParentIsNotNull(String nodeName);

}
