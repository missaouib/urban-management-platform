package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/12/14-15:16
 */
@Data
public class AreaDTO {

    private String id;

    private String pid;

    @NotBlank(message = "区域名称不能为空")
    private String gridName;

    @NotNull(message = "区域等级不能为空")
    private Integer level;

    private List<String> gridIds;
}
