package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 顾志杰
 * @date 2020/10/29-16:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO implements Serializable {

    private String starTime;

    private String endTime;

    private String opinions;

    private List<String> fileName;

    private String link;

    private String sign;

    private String user;

}
