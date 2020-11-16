package com.unicom.urban.management.service.component;

import cn.hutool.json.JSONObject;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.dao.component.ComponentRepository;
import com.unicom.urban.management.mapper.ComponentMapper;
import com.unicom.urban.management.pojo.RestReturn;
import com.unicom.urban.management.pojo.dto.ComponentDTO;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.ComponentVO;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.record.RecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @author 顾志杰
 * @date 2020/10/14-9:26
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ComponentService {

    private final ComponentRepository componentRepository;

    @Autowired
    private PublishService releaseService;

    private final RecordService recordService;

    private final EventTypeService eventTypeService;

    @Autowired
    private GisServiceProperties gisServiceProperties;

    @Autowired
    public ComponentService(ComponentRepository componentRepository, RecordService recordService, EventTypeService eventTypeService) {
        this.componentRepository = componentRepository;
        this.recordService = recordService;
        this.eventTypeService = eventTypeService;
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
            if (StringUtils.isNotBlank(component.getEventTypeId())) {
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("eventType").get("id"));
                List<String> type = eventTypeService.getComponentTypeIds(component.getEventTypeId());
                type.forEach(in::value);
                list.add(in);
            }
            if (StringUtils.isNotBlank(component.getObjId())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("objId").as(String.class), "%" + component.getObjId() + "%"));
            }
            if (StringUtils.isNotBlank(component.getObjName())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("objName").as(String.class), "%" + component.getObjName() + "%"));
            }
            if (StringUtils.isNotBlank(component.getMainDeptCode())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("mainDeptCode").as(String.class), "%" + component.getMainDeptCode() + "%"));
            }
            if (StringUtils.isNotBlank(component.getMainDeptName())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("mainDeptName").as(String.class), "%" + component.getMainDeptName() + "%"));
            }
            if (StringUtils.isNotBlank(component.getOwnershipDeptCode())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("ownershipDeptCode").as(String.class), "%" + component.getOwnershipDeptCode() + "%"));
            }
            if (StringUtils.isNotBlank(component.getOwnershipDeptName())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("ownershipDeptName").as(String.class), "%" + component.getOwnershipDeptName() + "%"));
            }
            if (StringUtils.isNotBlank(component.getMaintenanceDeptCode())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("maintenanceDeptCode").as(String.class), "%" + component.getMaintenanceDeptCode() + "%"));
            }
            if (StringUtils.isNotBlank(component.getMaintenanceDeptName())) {
                list.add(criteriaBuilder.like(root.get("componentInfo").get("maintenanceDeptName").as(String.class), "%" + component.getMaintenanceDeptName() + "%"));
            }
            if (StringUtils.isNotBlank(component.getBgid())) {
                list.add(criteriaBuilder.equal(root.get("componentInfo").get("bgid").get("id").as(String.class), component.getBgid()));
            }
            if (StringUtils.isNotBlank(component.getObjState())) {
                list.add(criteriaBuilder.equal(root.get("componentInfo").get("objState").get("id").as(String.class), component.getObjState()));
            }
            if (StringUtils.isNotBlank(component.getDataSource())) {
                list.add(criteriaBuilder.equal(root.get("componentInfo").get("dataSource").get("id").as(String.class), component.getDataSource()));
            }

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
     *
     * @param component 条件
     * @return T
     */
    public ComponentVO getComponent(ComponentDTO component) {
        Optional<Component> byId = componentRepository.findById(component.getComponentId());
        if (byId.isPresent()) {
            Component form = byId.orElse(new Component());
            return ComponentMapper.INSTANCE.componentToComponentVO(form);
        }
        throw new RuntimeException("部件不存在");
    }

    /**
     * 添加部件
     *
     * @param dto 参数
     */
    public void saveComponent(ComponentDTO dto) {

        Publish publish = new Publish();
        if (StringUtils.isNotBlank(dto.getPublish())) {
            publish.setId(dto.getPublish());
        } else {
            EventType eventType = eventTypeService.getEventType(dto.getEventTypeId());
            publish.setEventType(eventType);
            publish.setName(eventType.getName());
            publish = this.savePublish(publish);
        }
        Record record = this.saveRecord(dto.getCoordinate(), publish);

        Grid grid = new Grid();
        grid.setId(dto.getBgid());

        KV objState = KV.builder().id(dto.getObjState()).build();
        KV datasource = KV.builder().id(dto.getDataSource()).build();
        EventType eventType = EventType.builder().id(dto.getEventTypeId()).build();
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
                .objState(objState)
                .initialDate(dto.getInitialDate())
                .changeDate(dto.getChangeDate())
                .dataSource(datasource)
                .note(dto.getNote())
                .build();
        Component component = Component.builder()
                .componentInfo(componentInfo)
                .eventType(eventType)
                .publish(publish)
                .record(record)
                .build();
        componentRepository.save(component);

    }

    /**
     * 添加发布
     *
     * @param publish
     * @return
     */
    private Publish savePublish(Publish publish) {
        publish.setKv(KV.builder().id(KvConstant.KV_RELEASE_COMPONENT).build());
        return releaseService.save(publish);
    }

    /**
     * 添加位置
     *
     * @param coordinate
     * @param publish
     */
    private Record saveRecord(String coordinate, Publish publish) {
        Record record = new Record();
        record.setCoordinate(coordinate);
        record.setPublish(publish);
        return recordService.save(record);
    }

    public void saveComponent(List<ComponentDTO> dtos) {
        dtos.forEach(this::update);
    }

    public void update(ComponentDTO c) {
        Optional<Component> ifnull = componentRepository.findById(c.getComponentId());
        if (ifnull.isPresent()) {
            Component component = ifnull.get();
            KV objState = KV.builder().id(c.getObjState()).build();
            KV datasource = KV.builder().id(c.getDataSource()).build();
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
            component.getComponentInfo().setObjState(objState);
            component.getComponentInfo().setInitialDate(c.getInitialDate());
            component.getComponentInfo().setChangeDate(c.getChangeDate());
            component.getComponentInfo().setDataSource(datasource);
            componentRepository.saveAndFlush(component);
        }

    }

    /**
     * 通过编辑id（位置）查询部件
     *
     * @param recordId string
     * @return T
     */
    public ComponentVO getComponentByRecordId(String recordId) {
        Component byRecord_id = componentRepository.findByRecord_Id(recordId);
        return ComponentMapper.INSTANCE.componentToComponentVO(byRecord_id);
    }

    /**
     * 通过编辑id（位置）查询部件
     *
     * @param id string
     * @return T
     */
    public ComponentVO getComponentById(String id) {
        Component component = componentRepository.findById(id).orElse(new Component());
        return ComponentMapper.INSTANCE.componentToComponentVO(component);
    }


    public void deleteComponent(ComponentDTO dto) {

        Optional<Component> ifnull = componentRepository.findById(dto.getComponentId());
        if (StringUtils.isNotBlank(dto.getMongoId())) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("mongodbId", dto.getMongoId());
            Map<String, Object> layer = new HashMap<>(1);
            layer.put("id", dto.getLayerId());
            map.put("layer", layer);
            RestTemplateUtil.post(gisServiceProperties.getUrl() + "/elementAndAttribute/deleteElementAndAttribute", new JSONObject(map), RestReturn.class);
        }

        if (ifnull.isPresent()) {
            Component component = ifnull.get();
            recordService.delete(component.getRecord());
            componentRepository.delete(component);
        }
    }

    /**
     * 新增部件导入用
     *
     * @param component
     */
    public Component saveComponent4Import(Component component) {
        return componentRepository.saveAndFlush(component);
    }

    /**
     * 查询部件
     *
     * @param componentId 主键
     */
    public Component getOne(String componentId) {
        return componentRepository.getOne(componentId);
    }


    public List<Component> findAllByPublishIdAndRecordSts(String publishId) {
        return componentRepository.findAllByPublish_IdAndRecord_Sts(publishId, StsConstant.EDITING);
    }
}
