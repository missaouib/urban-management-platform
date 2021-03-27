package com.unicom.urban.management.dao.notice;

import com.unicom.urban.management.dao.CustomizeRepository;
import com.unicom.urban.management.pojo.entity.notice.Notice;

/**
 * 通知详情
 *
 * @author jiangwen
 */
public interface NoticeRepository extends CustomizeRepository<Notice, String> {

    /**
     * 查询指定类型下是否有未删除的消息
     *
     * @param delete 删除
     * @param id 类型id
     * @return 是否有
     */
    Boolean existsAllByDeletedAndNoticeType_Id(String delete, String id);

}
