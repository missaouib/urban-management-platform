package com.unicom.urban.management.service.release;

import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.dao.release.ReleaseRepository;
import com.unicom.urban.management.pojo.entity.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 发布service
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    public Release save(Release release) {
        release.setSts(StsConstant.EDITING);
        return releaseRepository.save(release);
    }

}
