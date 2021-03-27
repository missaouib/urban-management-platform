package com.unicom.urban.management.pojo.vo;

import com.unicom.urban.management.pojo.entity.notice.NoticeType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告类型
 *
 * @author jiangwen
 */
@Data
public class NoticeVO {

    private String id;

    private String title;

    private NoticeType noticeType;

    private String content;

    private String createUserName;

    private LocalDateTime createTime;

}
