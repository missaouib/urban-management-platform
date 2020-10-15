package com.unicom.urban.management.service.record;

import com.unicom.urban.management.dao.record.RecordRepository;
import com.unicom.urban.management.pojo.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        return recordRepository.save(record);
    }

    public void update(Record record) {
        recordRepository.saveAndFlush(record);
    }

}
