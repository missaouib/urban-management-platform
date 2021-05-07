package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 顾志杰
 * @date 2020/10/29-16:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsOneVO implements Serializable {

    /**
     * 处理意见
     */
    private String opinions;

    /**
     * 当前处理部门
     */
    private String deptName;
    /**
     * 当前处理人
     */
    private String userName;

}
