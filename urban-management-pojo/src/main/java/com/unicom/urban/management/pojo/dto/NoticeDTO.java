package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.entity.notice.NoticeType;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

/**
 * 通知公告类型
 *
 * @author jiangwen
 */
@Data
public class NoticeDTO {

    private String id;

    @NotBlank(message = "标题不能为空")
    private String title;

    private NoticeType noticeType;

    @NotBlank(message = "内容不能为空")
    @Max(value = 500, message = "内容长读不能超过500")
    private String content;

}
