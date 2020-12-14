package com.unicom.urban.management.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/12/14-15:16
 */
@Data
public class AreaDTO {

    private String id;

    private String pid;

    private String gridName;

    private Integer level;

    private List<String> gridIds;
}
