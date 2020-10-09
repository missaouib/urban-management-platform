package com.unicom.urban.management.pojo.vo;

import lombok.Data;

@Data
public class ProcessDefinitionVO {


    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private int version;

    private String resourceName;

    private String deploymentId;

    private String diagramResourceName;

    private boolean startFormKey;

    private boolean graphicalNotation;

    private boolean uspended;

    private String tenantId;

}
