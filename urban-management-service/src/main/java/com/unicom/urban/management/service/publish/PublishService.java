package com.unicom.urban.management.service.publish;

import cn.hutool.json.JSONObject;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.dao.release.PublishRepository;
import com.unicom.urban.management.mapper.PublishMapper;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Publish;
import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.vo.PublishVO;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.record.RecordService;
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

    @Autowired
    private PublishRepository publishRepository;
    @Autowired
    private RecordService recordService;
    @Autowired
    private GridService gridService;

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
            list.add(criteriaBuilder.equal(root.get("sts").as(Integer.class), StsConstant.UNRELEASED));
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
        String url = "http://localhost:8099/api/layerAndElementAndAttribute/addLayer";
        String gridType = "网格";
        if (gridType.equals(type)) {
            List<Grid> gridList = gridService.findAllByPublishIdAndRecordSts(publishId);
            Publish publish = findOne(publishId);
            ResponseEntity<Map> post = RestTemplateUtil.post(url, getGridJson(publish, gridList), Map.class);
            System.out.println(post);
            publish.setSts(StsConstant.RELEASE);
            publish.setLayerId("layerId");
            publish.setUrl("url");
            publishRepository.saveAndFlush(publish);
            List<Record> recordList = new ArrayList<>();
            for (Grid grid : gridList) {
                grid.getRecord().setSts(StsConstant.RELEASE);
                recordList.add(grid.getRecord());
            }
            recordService.saveList(recordList);
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
        layerMap.put("id", publish.getLayerId());
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


}
