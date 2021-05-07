package com.unicom.urban.management.dao.kv;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.KV;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/13-19:33
 */
public interface KVRepository extends CustomizeRepository<KV, String> {
    List<KV> findByTableNameAndFieldName(String tableName, String fieldName);

    /**
     * 通过多字段锁定唯一
     * 为了防止不必要的报错 此处就返回list
     *
     * @param tableName 表名
     * @param fieldName 字段名
     * @param value     值
     * @return 数据
     */
    List<KV> findByTableNameAndFieldNameAndValue(String tableName, String fieldName, String value);
}
