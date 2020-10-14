package com.unicom.urban.management.dao.kv;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.ComponentType;
import com.unicom.urban.management.pojo.entity.KV;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/13-19:33
 */
public interface KVRepository extends CustomizeRepository<KV, String> {
    List<KV> findByTableNameAndFieldName(String tableName,String fieldName);
}
