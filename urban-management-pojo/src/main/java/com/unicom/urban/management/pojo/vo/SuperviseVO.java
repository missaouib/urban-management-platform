package com.unicom.urban.management.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 顾志杰
 * @date 2020/11/19-13:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuperviseVO {

    private String eventId;

    private String deptName;

    private String user;

    private String opinion;

    private String replayOpinion;
}
