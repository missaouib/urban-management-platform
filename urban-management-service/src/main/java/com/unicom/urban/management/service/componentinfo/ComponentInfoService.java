package com.unicom.urban.management.service.componentinfo;

import com.unicom.urban.management.dao.componentinfo.ComponentInfoRepository;
import com.unicom.urban.management.pojo.entity.ComponentInfo;
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

    public ComponentInfo save(ComponentInfo componentInfo){
        ComponentInfo componentInfo1 =componentInfoRepository.save(componentInfo);
        componentInfoRepository.flush();
        return  componentInfo1;
    }

    public ComponentInfo findOne(String id){
        return componentInfoRepository.findById(id).orElse(new ComponentInfo());
    }


}
