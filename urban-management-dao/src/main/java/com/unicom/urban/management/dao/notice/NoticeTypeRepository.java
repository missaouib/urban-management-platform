package com.unicom.urban.management.dao.notice;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.notice.NoticeType;

import java.util.List;

/**
 * 通知公告类型
 *
 * @author jiangwen
 */
public interface NoticeTypeRepository extends CustomizeRepository<NoticeType, String> {

    /**
     * 查询未删除
     *
     * @param delete 删除
     * @return list
     */
    List<NoticeType> findAllByDeleted(String delete);

}
