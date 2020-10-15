package com.unicom.urban.management.service.component;

import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.component.ComponentRepository;
import com.unicom.urban.management.dao.record.RecordRepository;
import com.unicom.urban.management.mapper.ComponentMapper;
import com.unicom.urban.management.pojo.dto.ComponentDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import com.unicom.urban.management.service.record.RecordService;
import com.unicom.urban.management.service.release.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 顾志杰
 * @date 2020/10/14-9:26
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ComponentService {

    private final ComponentRepository componentRepository;

    private final ReleaseService releaseService;

    private final RecordService recordService;
    @Autowired
    public ComponentService(ComponentRepository componentRepository, ReleaseService releaseService, RecordService recordService) {
        this.componentRepository = componentRepository;
        this.releaseService = releaseService;
        this.recordService = recordService;
    }

    /**
     * 获取所有部件信息
     *
     * @param component 查询条件
     * @return list
     */
    public List<ComponentVO> getComponentList(ComponentDTO component) {
        List<Component> componentList = componentRepository.findAll((Specification<Component>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });
        return ComponentMapper.INSTANCE.componentListToComponentVOList(componentList);
    }

    /**
     * 获取所有部件信息（分页）
     *
     * @param component 查询条件
     * @param pageable  分页信息
     * @return page
     */
    public Page<ComponentVO> getComponentList(ComponentDTO component, Pageable pageable) {
        Page<Component> page = componentRepository.findAll((Specification<Component>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        List<ComponentVO> componentVOList = ComponentMapper.INSTANCE.componentListToComponentVOList(page.getContent());

        return new PageImpl<>(componentVOList, page.getPageable(), page.getTotalElements());
    }


    /**
     * 获取部件
     * @param component 条件
     * @return T
     */
    public ComponentVO getComponent(ComponentDTO component) {
        Optional<Component> byId = componentRepository.findById(component.getId());
        if (byId.isPresent()) {
            Component form = byId.orElse(new Component());
            return ComponentMapper.INSTANCE.componentToComponentVO(form);
        }
        throw new RuntimeException("部件不存在");
    }

    /**
     * 添加部件
     * @param dto 参数
     */
    public void saveComponent(ComponentDTO dto){
        Release release = this.saveRelease(dto.getReleaseName());
        this.saveRecord(dto.getCoordinate(),release);
        Grid grid = new Grid();
        grid.setId(dto.getBgid());

        KV kv = KV.builder().id(dto.getKvId()).build();
        ComponentType componentType = ComponentType.builder().id(dto.getComponentTypeId()).build();
        ComponentInfo componentInfo = ComponentInfo.builder()
                .objId(dto.getObjId())
                .objName(dto.getObjName())
                .mainDeptCode(dto.getMainDeptCode())
                .mainDeptName(dto.getMainDeptName())
                .ownershipDeptCode(dto.getOwnershipDeptCode())
                .ownershipDeptName(dto.getOwnershipDeptName())
                .maintenanceDeptCode(dto.getMaintenanceDeptCode())
                .maintenanceDeptName(dto.getMaintenanceDeptName())
                .bgid(grid)
                .objState(dto.getObjState())
                .initialDate(dto.getInitialDate())
                .changeDate(dto.getChangeDate())
                .dataSource(dto.getDataSource())
                .note(dto.getNote())
                .build();
        Component component = Component.builder()
                .componentInfo(componentInfo)
                .componentType(componentType)
                .sts(StsConstant.INUSE)
                .kv(kv)
                .release(release)
                .build();
        componentRepository.save(component);

    }

    private Release saveRelease(String releaseName){
        Release release = new Release();
        release.setReleaseName(releaseName);
        User user = new User();
        user.setId(SecurityUtil.getUserId());
        release.setUser(user);
        release.setKv(KV.builder().id("67369ef9-f2f9-4698-8c4e-f835a2af26b0").build());
        return releaseService.save(release);
    }

    private void saveRecord(String coordinate,Release release){
        Record record = new Record();
        record.setCoordinate(coordinate);
        record.setRelease(release);
        recordService.save(record);
    }

    public void saveComponent(List<ComponentDTO> dtos){
        dtos.forEach(c->{
            Optional<Component> ifnull = componentRepository.findById(c.getId());
            if(ifnull.isPresent()){
                Component component = ifnull.get();
                Grid grid = new Grid();
                grid.setId(c.getBgid());
                component.getComponentInfo().setObjId(c.getObjId());
                component.getComponentInfo().setObjName(c.getObjName());
                component.getComponentInfo().setMainDeptCode(c.getMainDeptCode());
                component.getComponentInfo().setMainDeptName(c.getMainDeptCode());
                component.getComponentInfo().setOwnershipDeptCode(c.getOwnershipDeptCode());
                component.getComponentInfo().setOwnershipDeptName(c.getOwnershipDeptName());
                component.getComponentInfo().setMaintenanceDeptCode(c.getMaintenanceDeptCode());
                component.getComponentInfo().setMaintenanceDeptName(c.getMaintenanceDeptName());
                component.getComponentInfo().setBgid(grid);
                component.getComponentInfo().setObjState(c.getObjState());
                component.getComponentInfo().setInitialDate(c.getInitialDate());
                component.getComponentInfo().setChangeDate(c.getChangeDate());
                component.getComponentInfo().setDataSource(c.getDataSource());
                componentRepository.saveAndFlush(component);
            }
        });
    }

}
