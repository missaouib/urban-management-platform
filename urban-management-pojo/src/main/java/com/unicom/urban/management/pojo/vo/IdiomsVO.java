package com.unicom.urban.management.pojo.vo;

import lombok.Data;
/**
 * 惯用语实体类
 *
 * @author liubozhi
 */
@Data
public class IdiomsVO {

    private String id;

    private String userId;
    /**
     * 惯用语内容
     */
    private String idiomsValue;
}
