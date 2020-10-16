package com.unicom.urban.management.service.record;

import com.unicom.urban.management.common.constant.StsConstant;
import com.unicom.urban.management.dao.record.RecordRepository;
import com.unicom.urban.management.mapper.RecordMapper;
import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 编辑service
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public Record save(Record record) {
        record.setSts(StsConstant.EDITING);
        return recordRepository.save(record);
    }

    public void update(Record record) {
        recordRepository.saveAndFlush(record);
    }

    public List<RecordVO> findAllByPublishId(String id) {
        List<Record> allByPublishId = recordRepository.findAllByPublish_Id(id);
        return RecordMapper.INSTANCE.RecordListToRecordVOList(allByPublishId);
    }


}
