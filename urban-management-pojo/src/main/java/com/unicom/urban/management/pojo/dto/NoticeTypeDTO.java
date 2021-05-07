package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 通知公告类型
 *
 * @author jiangwen
 */
@Data
public class NoticeTypeDTO {

    private String id;

    @NotBlank(message = "名称不能为空")
    private String name;

}
