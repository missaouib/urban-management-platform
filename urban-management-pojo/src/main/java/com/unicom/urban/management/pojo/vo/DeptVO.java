package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author 顾志杰
 * @date 2020/11/10-18:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeptVO {

    private String id;

    private String deptName;

    private String describes;

    private String deptAddress;

    private String deptPhone;

    private String parentId;

    private String parentName;

    private String gridId;

    private String gridName;

    private Integer sort;

    private String createTime;

    private String levelOrNot;

}
