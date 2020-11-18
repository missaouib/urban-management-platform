package com.unicom.urban.management.service.kv;

import com.unicom.urban.management.dao.kv.KVRepository;
import com.unicom.urban.management.pojo.entity.KV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/14-18:12
 */
@Service
public class KVService {

    @Autowired
    private KVRepository kvRepository;

    @Cacheable(value = "kv_id")
    public KV findOneById(String id) {
        return kvRepository.getOne(id);
    }

    @Cacheable(value = "kv_tableName_fieldName")
    public List<KV> findByTableNameAndFieldName(String tableName, String filedName) {
        return kvRepository.findByTableNameAndFieldName(tableName, filedName);
    }

    @Cacheable(value = "kv_tableName_fieldName_value")
    public List<KV> findByTableNameAndFieldNameAndValue(String tableName, String filedName, String value) {
        return kvRepository.findByTableNameAndFieldNameAndValue(tableName, filedName, value);
    }

}
