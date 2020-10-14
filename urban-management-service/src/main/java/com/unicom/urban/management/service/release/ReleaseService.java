package com.unicom.urban.management.service.release;

import com.unicom.urban.management.dao.release.ReleaseRepository;
import com.unicom.urban.management.mapper.ReleaseMapper;
import com.unicom.urban.management.pojo.dto.ReleaseDTO;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.service.kv.KVService;
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
    @Autowired
    private KVService kvService;

    public void save(ReleaseDTO releaseDTO) {
        ReleaseMapper.INSTANCE.ReleaseDTOToRelease(releaseDTO);
        ReleaseMapper.INSTANCE.ReleaseDTOToRelease(releaseDTO);
        ReleaseMapper.INSTANCE.ReleaseDTOToRelease(releaseDTO);
        KV kv = kvService.findOneById("kvId");
    }

}
