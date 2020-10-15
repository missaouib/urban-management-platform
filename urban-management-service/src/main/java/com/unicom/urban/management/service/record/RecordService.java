package com.unicom.urban.management.service.record;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.record.RecordRepository;
import com.unicom.urban.management.pojo.entity.Record;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.service.user.UserService;
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
    @Autowired
    private UserService userService;

    public void save(Record record) {
        User one = userService.findOne(SecurityUtil.getUserId());
        record.setUser(one);
        recordRepository.save(record);
    }

}
