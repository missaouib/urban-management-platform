package com.unicom.urban.management.pojo.vo;

import lombok.Data;

@Data
public class ProcessDefinitionVO {


    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private Integer version;

    private String resourceName;

    private String deploymentId;

    private String diagramResourceName;

    private Boolean startFormKey;

    private Boolean graphicalNotation;

    private Boolean uspended;

    private String tenantId;

    private String modelerId;

}
