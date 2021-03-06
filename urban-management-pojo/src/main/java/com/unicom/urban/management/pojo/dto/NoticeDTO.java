package com.unicom.urban.management.pojo.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

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

    private String noticeTypeId;

    @NotBlank(message = "内容不能为空")
    @Length(max = 500, message = "内容长读不能超过500")
    private String content;

}
