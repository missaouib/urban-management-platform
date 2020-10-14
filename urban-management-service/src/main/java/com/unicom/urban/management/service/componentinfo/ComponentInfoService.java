package com.unicom.urban.management.service.componentinfo;

import com.unicom.urban.management.dao.componentinfo.ComponentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author 顾志杰
 * @date 2020/10/14-14:10
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ComponentInfoService {

    private final ComponentInfoRepository componentInfoRepository;

    @Autowired
    public ComponentInfoService(ComponentInfoRepository componentInfoRepository) {
        this.componentInfoRepository = componentInfoRepository;
    }


}
