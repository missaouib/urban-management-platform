package com.unicom.urban.management.service.publish;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.dao.release.PublishRepository;
import com.unicom.urban.management.pojo.entity.Publish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public Publish save(Publish release) {
        release.setSts(StsConstant.UNRELEASED);
        return publishRepository.save(release);
    }

    public List<Publish> allByKvId() {
        return publishRepository.findAllByKv_Id(KvConstant.KV_RELEASE_GRID);
    }

}
