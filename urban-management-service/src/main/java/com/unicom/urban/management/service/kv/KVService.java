package com.unicom.urban.management.service.kv;

import com.unicom.urban.management.dao.kv.KVRepository;
import com.unicom.urban.management.pojo.entity.KV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/14-18:12
 */
@Service
public class KVService {

    private final KVRepository kvRepository;

    @Autowired
    public KVService(KVRepository kvRepository) {
        this.kvRepository = kvRepository;
    }

    public List<KV> getKv(String tableName, String fieldName) {
        return kvRepository.findByTableNameAndFieldName(tableName, fieldName);
    }

    public KV findOneById(String KvId) {
        return kvRepository.getOne(KvId);
    }
}
