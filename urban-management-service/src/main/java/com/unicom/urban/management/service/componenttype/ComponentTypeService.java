package com.unicom.urban.management.service.componenttype;

import com.unicom.urban.management.dao.componenttype.ComponentTypeRepository;
import com.unicom.urban.management.mapper.ComponentTypeMapper;
import com.unicom.urban.management.pojo.dto.ComponentTypeDTO;
import com.unicom.urban.management.pojo.entity.ComponentType;
import com.unicom.urban.management.pojo.vo.ComponentTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author 顾志杰
 * @date 2020/10/14-13:49
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ComponentTypeService {

    private final ComponentTypeRepository componentTypeRepository;

    @Autowired
    public ComponentTypeService(ComponentTypeRepository componentTypeRepository) {
        this.componentTypeRepository = componentTypeRepository;
    }

    /**
     * 查询所有部件分类
     * @return list
     */
    public List<ComponentTypeVO> getComponentTypeList(){
        List<ComponentType> fromList = componentTypeRepository.findAll();
       return ComponentTypeMapper.INSTANCE.componentTypeListToComponentTypeVOList(fromList);
    }

    public ComponentTypeVO getComponentType(ComponentTypeDTO componentTypeDTO){
        Optional<ComponentType> ifnull = componentTypeRepository.findById(componentTypeDTO.getId());
        if(ifnull.isPresent()){
            ComponentType componentType = ifnull.get();
            return ComponentTypeMapper.INSTANCE.componentTypeToComponentTypeVO(componentType);
        }
        throw new RuntimeException("没有该分类");
    }
}
