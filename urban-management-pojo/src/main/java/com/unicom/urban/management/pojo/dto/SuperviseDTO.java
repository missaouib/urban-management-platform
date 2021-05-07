package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 顾志杰
 * @date 2020/11/19-13:39
 */
@Data
public class SuperviseDTO {

    @NotBlank(message = "事件不能为空")
    private String eventId;

    private String opinion;

    private String replayOpinion;

}
