package com.unicom.urban.management.service.publish;

import cn.hutool.json.JSONObject;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.dao.release.PublishRepository;
import com.unicom.urban.management.mapper.PublishMapper;
import com.unicom.urban.management.pojo.RestReturn;
import com.unicom.urban.management.pojo.entity.Component;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Publish;
import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.vo.PublishVO;
import com.unicom.urban.management.service.component.ComponentService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.record.RecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布service
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class PublishService {

    private final String URL = "/layerAndElementAndAttribute/addLayer";

    @Autowired
    private PublishRepository publishRepository;
    @Autowired
    private RecordService recordService;
    @Autowired
    private GridService gridService;

    @Autowired
    private GisServiceProperties gisServiceProperties;

    @Autowired
    private ComponentService componentService;

    public Publish save(Publish release) {
        release.setSts(StsConstant.UNRELEASED);
        return publishRepository.save(release);
    }

    public List<Publish> allByKvId() {
        return publishRepository.findAllByKv_Id(KvConstant.KV_RELEASE_GRID);
    }

    public List<PublishVO> search() {
        List<Publish> publishList = publishRepository.findAll((Specification<Publish>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });
        return PublishMapper.INSTANCE.publishListToPublishVOList(publishList);
    }

    public List<PublishVO> searchTypeId(String typeId) {
        List<Publish> publishList = publishRepository.findAllByKv_IdAndEventType_id(KvConstant.KV_RELEASE_COMPONENT, typeId);
        return PublishMapper.INSTANCE.publishListToPublishVOList(publishList);
    }

    public Publish findOne(String publishId) {
        return publishRepository.getOne(publishId);
    }

    public void layerPublish(String publishId, String type) {
        String gridType = "网格";
        String partType = "部件";
        if (gridType.equals(type)) {
            List<Grid> gridList = gridService.findAllByPublishIdAndRecordSts(publishId);
            Publish publish = findOne(publishId);
            ResponseEntity<RestReturn> post = RestTemplateUtil.post(gisServiceProperties.getUrl() + URL, getGridJson(publish, gridList), RestReturn.class);
            Map<String, String> map = (Map<String, String>) post.getBody().getData();
            publish.setSts(StsConstant.RELEASE);
            String layerId = map.get("layerId");
            publish.setLayerId(StringUtils.isNotBlank(publish.getLayerId()) ? publish.getLayerId() : layerId);
            String url = map.get("url");
            publish.setUrl(StringUtils.isNotBlank(publish.getUrl()) ? publish.getUrl() : url);
            String workName = map.get("workName");
            publish.setUrl(StringUtils.isNotBlank(publish.getWorkName()) ? publish.getWorkName() : workName);
            String layerGroup = map.get("layerGroup");
            publish.setUrl(StringUtils.isNotBlank(publish.getLayerGroup()) ? publish.getLayerGroup() : layerGroup);
            publishRepository.saveAndFlush(publish);
            List<Record> recordList = new ArrayList<>();
            for (Grid grid : gridList) {
                grid.getRecord().setSts(StsConstant.RELEASE);
                recordList.add(grid.getRecord());
            }
            recordService.saveList(recordList);
        } else if (partType.equals(type)) {
            List<Component> components = componentService.findAllByPublishIdAndRecordSts(publishId);
            Publish publish = findOne(publishId);
            ResponseEntity<RestReturn> post = RestTemplateUtil.post(gisServiceProperties.getUrl() + URL, getComponentJson(publish, components), RestReturn.class);
            Map<String, String> map = (Map<String, String>) post.getBody().getData();
            publish.setSts(StsConstant.RELEASE);
            String layerId = map.get("layerId");
            publish.setLayerId(StringUtils.isNotBlank(publish.getLayerId()) ? publish.getLayerId() : layerId);
            String url = map.get("url");
            publish.setUrl(StringUtils.isNotBlank(publish.getUrl()) ? publish.getUrl() : url);
            String workName = map.get("workName");
            publish.setWorkName(StringUtils.isNotBlank(publish.getWorkName()) ? publish.getWorkName() : workName);
            String layerGroup = map.get("layerGroup");
            publish.setLayerGroup(StringUtils.isNotBlank(publish.getLayerGroup()) ? publish.getLayerGroup() : layerGroup);
            publishRepository.saveAndFlush(publish);
            components.forEach(c -> {
                Record record = c.getRecord();
                record.setSts(StsConstant.RELEASE);
                recordService.update(record);
            });

        } else {
            throw new DataValidException("");
        }
    }

    private JSONObject getGridJson(Publish publish, List<Grid> gridList) {
        int one = 1;
        Map<String, Object> layerSettingMap = new HashMap<>(one);
        layerSettingMap.put("id", KvConstant.LAYER_SETTING_GRID);
        Map<String, Object> epsgMap = new HashMap<>(one);
        epsgMap.put("id", KvConstant.E_PSG);
        Map<String, Object> scaleMap = new HashMap<>(one);
        scaleMap.put("id", KvConstant.SCALE);
        int five = 5;
        Map<String, Object> layerMap = new HashMap<>(five);
        layerMap.put("id", StringUtils.isNotBlank(publish.getLayerId()) ? publish.getLayerId() : "");
        layerMap.put("epsg", epsgMap);
        layerMap.put("layerSetting", layerSettingMap);
        layerMap.put("scale", scaleMap);
        layerMap.put("alias", publish.getName());

        List<Map<String, Object>> mapList = new ArrayList<>(gridList.size());
        for (Grid grid : gridList) {
            int three = 3;
            Map<String, Object> mapElementAndAttribute = new HashMap<>(three);
            mapElementAndAttribute.put("data", "objId=" + grid.getGridCode());
            mapElementAndAttribute.put("coordinate", grid.getRecord().getCoordinate());
            mapElementAndAttribute.put("name", grid.getGridName());
            mapList.add(mapElementAndAttribute);
        }

        int two = 2;
        Map<String, Object> map = new HashMap<>(two);
        map.put("layer", layerMap);
        map.put("elementAndAttributeList", mapList);

        return new JSONObject(map);
    }

    private JSONObject getComponentJson(Publish publish, List<Component> componentList) {
        int one = 1;
        Map<String, Object> layerSettingMap = new HashMap<>(one);
        layerSettingMap.put("id", KvConstant.LAYER_SETTING_COMPONENT);
        Map<String, Object> epsgMap = new HashMap<>(one);
        epsgMap.put("id", KvConstant.E_PSG);
        Map<String, Object> scaleMap = new HashMap<>(one);
        scaleMap.put("id", KvConstant.SCALE);
        int five = 5;
        Map<String, Object> layerMap = new HashMap<>(five);
        layerMap.put("id", StringUtils.isNotBlank(publish.getLayerId()) ? publish.getLayerId() : "");
        layerMap.put("epsg", epsgMap);
        layerMap.put("layerSetting", layerSettingMap);
        layerMap.put("scale", scaleMap);
        layerMap.put("alias", publish.getName());

        List<Map<String, Object>> mapList = new ArrayList<>(componentList.size());
        for (Component component : componentList) {
            int three = 3;
            Map<String, Object> mapElementAndAttribute = new HashMap<>(three);
            mapElementAndAttribute.put("data", "objId=" + component.getId());
            mapElementAndAttribute.put("coordinate", component.getRecord().getCoordinate());
            mapElementAndAttribute.put("name", component.getComponentInfo().getObjName());
            mapList.add(mapElementAndAttribute);
        }

        int two = 2;
        Map<String, Object> map = new HashMap<>(two);
        map.put("layer", layerMap);
        map.put("elementAndAttributeList", mapList);

        return new JSONObject(map);
    }

    public String getGridUrl() {
        Publish byKvId = publishRepository.findByKv_Id("28526efe-3db5-415b-8c7a-d0e3a49cab8f");
        if (byKvId != null && StringUtils.isNotBlank(byKvId.getUrl())) {
            return byKvId.getUrl();
        } else {
            throw new DataValidException("未查询到发布网格数据地址，请先发布网格");
        }
    }


    public boolean findByName(String name) {
        Publish publish = publishRepository.findByName(name);
        return publish != null;
    }
}
