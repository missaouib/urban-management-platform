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
        List<Publish> publishList = publishRepository.findAllByKv_IdAndComponentType_id(KvConstant.KV_RELEASE_COMPONENT, typeId);
        return PublishMapper.INSTANCE.publishListToPublishVOList(publishList);
    }

    public Publish findOne(String publishId) {
        return publishRepository.getOne(publishId);
    }

    public void layerPublish(String publishId, String type) {
        String url = "http://localhost:8099/api/layerAndElementAndAttribute/addLayer";
        String gridType = "网格";
        if (gridType.equals(type)) {

        }
        Map<String, Object> map = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        for (Grid grid : gridService.findAllByPublishIdAndRecordSts(publishId)) {
            ResponseEntity<Map> post = RestTemplateUtil.post(url, getGridJson(grid), Map.class);
            map = (Map<String, Object>) post;
            grid.getRecord().setSts(StsConstant.RELEASE);
            recordList.add(grid.getRecord());
        }
        recordService.saveList(recordList);
        Publish publish = findOne(publishId);
        publish.setSts(StsConstant.RELEASE);
        publish.setLayerId("layerId");
        publish.setUrl("url");
        publishRepository.saveAndFlush(publish);

    }

    private JSONObject getGridJson(Grid grid) {
        Map<String, Object> layerSettingMap = new HashMap<>();
        layerSettingMap.put("id", KvConstant.LAYER_SETTING_GRID);
        Map<String, Object> epsgMap = new HashMap<>();
        epsgMap.put("id", KvConstant.E_PSG);
        Map<String, Object> scaleMap = new HashMap<>();
        scaleMap.put("id", KvConstant.SCALE);
        Map<String, Object> layerMap = new HashMap<>();
        layerMap.put("id", grid.getPublish().getLayerId());
        layerMap.put("layerSetting", layerSettingMap);
        layerMap.put("epsg", epsgMap);
        layerMap.put("scale", scaleMap);
        Map<String, Object> map = new HashMap<>();
        map.put("data", "objId=" + grid.getGridCode());
        map.put("coordinate", grid.getRecord().getCoordinate());
        map.put("name", grid.getGridName());
        map.put("layer", layerMap);

        return new JSONObject(map);
    }


}
