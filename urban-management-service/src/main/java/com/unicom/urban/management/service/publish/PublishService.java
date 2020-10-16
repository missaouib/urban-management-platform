package com.unicom.urban.management.service.publish;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.dao.release.PublishRepository;
import com.unicom.urban.management.mapper.PublishMapper;
import com.unicom.urban.management.pojo.entity.Publish;
import com.unicom.urban.management.pojo.vo.PublishVO;
import com.unicom.urban.management.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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


}
